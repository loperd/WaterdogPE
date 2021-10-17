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

package me.loper.monoprotection;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.logger.Logger;
import dev.waterdog.waterdogpe.network.session.SessionInjections;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.loper.monoprotection.captcha.CaptchaGenerator;
import me.loper.monoprotection.captcha.CaptchaStore;
import me.loper.monoprotection.upstream.network.PlayerConnectionHandler;

import java.awt.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MonoProtection {

    public static final int MAX_CAPTCHAS = 111;

    private final Map<String, InetSocketAddress> protectionPassedPlayers = new ConcurrentHashMap<>();
    private final Map<String, PlayerConnectionHandler> connections = new ConcurrentHashMap<>();

    private final CaptchaStore captchaStore = new CaptchaStore();
    private final CaptchaGenerator generator;
    private final CaptchaGenerator.Worker worker;

    public MonoProtection() {
        List<Font> fonts = new ObjectArrayList<>();
        fonts.add(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
        fonts.add(new Font(Font.SERIF, Font.PLAIN, 50));
        fonts.add(new Font(Font.MONOSPACED, Font.BOLD, 50));

        this.worker = new CaptchaGenerator.Worker(this);
        this.generator = new CaptchaGenerator(fonts);

        generateCaptcha();
    }

    private void generateCaptcha() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < MAX_CAPTCHAS; i++) {
            executor.execute(this.worker);
        }

        long start = System.currentTimeMillis();
        ThreadPoolExecutor ex = (ThreadPoolExecutor) executor;

        while (ex.getActiveCount() != 0) {
            int alreadyGenerated = MAX_CAPTCHAS - ex.getQueue().size() - ex.getActiveCount();
            ProxyServer.getInstance().getLogger().info(String.format(
                    "[MonoProtection] Генерирую капчу [%s/%s]",
                    alreadyGenerated, MAX_CAPTCHAS
            ));
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ex1) {
                ProxyServer.getInstance().getLogger().info("[MonoProtection] Не могу сгенерировать капчу.", ex1);
                System.exit(0);
                return;
            }
        }
        executor.shutdownNow();
        System.gc();
        ProxyServer.getInstance().getLogger().info(String.format("[MonoProtection] Капча сгенерированна за %s мс", System.currentTimeMillis() - start));
    }

    public void connect(ProxiedPlayer player) {
        SessionInjections.injectUpstreamSettings(player.getUpstream(), player);

        PlayerConnectionHandler connection = new PlayerConnectionHandler(player, this);

        if (null != this.connections.putIfAbsent(player.getName().toLowerCase(), connection)) {
            player.disconnect("§cPlayer with this name already play on the server.");
            return;
        }

        BedrockServerSession session = player.getUpstream();

        session.setPacketHandler(connection);
        session.setPacketCodec(connection.getCodec());
        session.addDisconnectHandler(reason -> this.removePlayer(player));
        session.sendPacket(player.getProxy().getPackManager().getPacksInfoPacket());
    }

    public void removePlayer(ProxiedPlayer player) {
        if (null != player) {
            this.connections.remove(player.getName().toLowerCase());
        }
    }

    public CaptchaGenerator getGenerator() {
        return generator;
    }

    public CaptchaStore getCaptchaStore() {
        return this.captchaStore;
    }

    public Logger getLogger() {
        return ProxyServer.getInstance().getLogger();
    }

    public boolean isProtectionPassed(ProxiedPlayer player) {
        if (!this.protectionPassedPlayers.containsKey(player.getName())) {
            return false;
        }

        InetSocketAddress socket = this.protectionPassedPlayers.get(player.getName());

        return socket.getHostString().equals(player.getAddress().getHostString())
                && player.getAddress().getPort() == socket.getPort();
    }

    public void passedProtection(ProxiedPlayer player) {
        this.protectionPassedPlayers.put(player.getName().toLowerCase(), player.getAddress());
    }
}
