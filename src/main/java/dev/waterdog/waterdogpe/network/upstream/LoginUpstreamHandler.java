/*
 * Copyright 2021 WaterdogTEAM
 * Licensed under the GNU General Public License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.waterdog.waterdogpe.network.upstream;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.WaterdogPE;
import dev.waterdog.waterdogpe.event.defaults.PlayerPreLoginEvent;
import dev.waterdog.waterdogpe.network.protocol.ProtocolConstants;
import dev.waterdog.waterdogpe.network.protocol.ProtocolVersion;
import dev.waterdog.waterdogpe.network.session.LoginData;
import dev.waterdog.waterdogpe.player.HandshakeEntry;
import dev.waterdog.waterdogpe.player.HandshakeUtils;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.utils.types.ProxyListenerInterface;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

/**
 * The Pipeline Handler handling the login handshake part of the initial connect. Will be replaced after success.
 */
public class LoginUpstreamHandler implements BedrockPacketHandler {

    private final ProxyServer proxy;
    private final BedrockServerSession session;

    public LoginUpstreamHandler(ProxyServer proxy, BedrockServerSession session) {
        this.proxy = proxy;
        this.session = session;
    }

    private void onLoginFailed(boolean xboxAuth, Throwable throwable, String disconnectReason) {
        String message = this.proxy.getProxyListener().onLoginFailed(this.session.getAddress(), xboxAuth, throwable, disconnectReason);
        this.session.disconnect(message);
    }

    @Override
    public boolean handle(LoginPacket packet) {
        ProxyListenerInterface listener = this.proxy.getProxyListener();
        if (!listener.onLoginAttempt(this.session.getAddress())) {
            this.proxy.getLogger().debug("[" + this.session.getAddress() + "] <-> Login denied");
            this.session.disconnect("Login denied");
            return true;
        }

        int protocolVersion = packet.getProtocolVersion();
        ProtocolVersion protocol = ProtocolConstants.get(protocolVersion);
        this.session.setPacketCodec(protocol == null ? ProtocolConstants.getLatestProtocol().getCodec() : protocol.getCodec());

        if (protocol == null) {
            PlayStatusPacket status = new PlayStatusPacket();
            status.setStatus((protocolVersion > WaterdogPE.version().latestProtocolVersion() ?
                    PlayStatusPacket.Status.LOGIN_FAILED_SERVER_OLD :
                    PlayStatusPacket.Status.LOGIN_FAILED_CLIENT_OLD));
            this.session.sendPacketImmediately(status);
            this.session.disconnect();

            listener.onIncorrectVersionLogin(protocolVersion, this.session.getAddress());
            this.proxy.getLogger().alert("[" + this.session.getAddress() + "] <-> Upstream has disconnected due to incompatible protocol (protocol=" + protocolVersion + ")");
            return true;
        }

        boolean xboxAuth = false;
        this.session.setLogging(WaterdogPE.version().debug());

        try {
            JsonObject certJson = (JsonObject) JsonParser.parseReader(new InputStreamReader(new ByteArrayInputStream(packet.getChainData().toByteArray())));
            if (!certJson.has("chain") || !certJson.getAsJsonObject().get("chain").isJsonArray()) {
                throw new IllegalStateException("Certificate data is not valid");
            }
            JsonArray certChain = certJson.getAsJsonArray("chain");

            boolean strictAuth = this.proxy.getConfiguration().isOnlineMode();
            HandshakeEntry handshakeEntry = HandshakeUtils.processHandshake(this.session, packet, certChain, protocol, strictAuth);

            if (!(xboxAuth = handshakeEntry.isXboxAuthed()) && strictAuth) {
                this.onLoginFailed(false, null, "disconnectionScreen.notAuthenticated");
                this.proxy.getLogger().info("[" + this.session.getAddress() + "|" + handshakeEntry.getDisplayName() + "] <-> Upstream has disconnected due to failed XBOX authentication!");
                return true;
            }

            this.proxy.getLogger().info("[" + this.session.getAddress() + "|" + handshakeEntry.getDisplayName() + "] <-> Upstream has connected (protocol=" + protocolVersion + ")");
            LoginData loginData = handshakeEntry.buildData(this.session, this.proxy);

            PlayerPreLoginEvent loginEvent = new PlayerPreLoginEvent(ProxiedPlayer.class, loginData, this.session.getAddress());
            this.proxy.getEventManager().callEvent(loginEvent);
            if (loginEvent.isCancelled()) {
                this.session.disconnect(loginEvent.getCancelReason());
                return true;
            }

            ProxiedPlayer player = loginEvent.getBaseClass().getConstructor(ProxyServer.class, BedrockServerSession.class, LoginData.class).newInstance(this.proxy, this.session, loginData);
            if (!this.proxy.getPlayerManager().registerPlayer(player)) {
                return true;
            }

            PlayStatusPacket status = new PlayStatusPacket();
            status.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
            this.session.sendPacket(status);


            if (this.proxy.getMonoProtection().isProtectionPassed(player)) {
                player.initPlayer();

                return true;
            }

            this.proxy.getMonoProtection().connect(player);
        } catch (Exception e) {
            this.onLoginFailed(xboxAuth, e, "Login failed: " + e.getMessage());
            this.proxy.getLogger().error("[" + this.session.getAddress() + "] Unable to complete login", e);
        }
        return true;
    }

}
