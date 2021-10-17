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

package me.loper.monoprotection.upstream.network;

import com.nukkitx.network.SessionConnection;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import com.nukkitx.protocol.bedrock.packet.*;
import dev.waterdog.waterdogpe.network.rewrite.types.RewriteData;
import dev.waterdog.waterdogpe.network.upstream.AbstractUpstreamHandler;
import dev.waterdog.waterdogpe.packs.PackManager;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.EventLoop;
import me.loper.monoprotection.MonoProtection;
import me.loper.monoprotection.captcha.Captcha;
import me.loper.monoprotection.packet.PacketFactory;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerConnectionHandler extends AbstractUpstreamHandler {

    private final long mapId = ThreadLocalRandom.current().nextLong();
    private final MonoProtection monoProtection;

    private Captcha captcha;

    public PlayerConnectionHandler(ProxiedPlayer player, MonoProtection monoProtection) {
        super(player);

        this.monoProtection = monoProtection;

        resetCaptcha();
    }

    private Captcha resetCaptcha() {
        return this.captcha = this.monoProtection.getCaptchaStore().getCaptcha();
    }

    @Override
    public boolean handle(TextPacket packet) {
        this.player.getProxy().getLogger().info(
                "Player sends message: " + packet.getMessage()
                + "; Captcha answear: " + captcha.getAnswear()
                + "; Equals: " + captcha.isValid(packet.getMessage()));

        if (!this.captcha.isValid(packet.getMessage())) {
            this.player.sendMessage("Вы ввели неккоректное значение капчи. Попробуйще пожалуйста еще раз");
            this.sendNewCaptcha();
            return true;
        }

        this.success();

        return true;
    }

    private void success() {
        this.monoProtection.passedProtection(this.player);

        this.player.initialConnect();
    }

    @Override
    public boolean handle(CommandRequestPacket packet) {
        this.player.getProxy().getLogger().info("Player sends command: " + packet.getCommand());
        return true;
    }

    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        PackManager packManager = this.player.getProxy().getPackManager();

        switch (packet.getStatus()) {
            case REFUSED:
                this.player.disconnect("disconnectionScreen.noReason");
                break;
            case HAVE_ALL_PACKS:
                this.player.getUpstream().sendPacket(packManager.getStackPacket());
                break;
            case COMPLETED:
                if (!this.player.hasUpstreamBridge()) {
                    this.spawn();
                }
                break;
        }

        return this.cancel();
    }

    @Override
    public boolean handle(MovePlayerPacket packet) {
        return true;
    }

    public void spawn() {
        int entityId = ThreadLocalRandom.current().nextInt(10000, 15000);

        RewriteData rewriteData = this.player.getRewriteData();
        rewriteData.setEntityId(entityId);
        rewriteData.setDimension(0);

        try {
            this.player.sendPacketImmediately(PacketFactory.createStartGamePacket(entityId));
            this.player.sendPacketImmediately(PacketFactory.createBiomeDefinitionListPacket());
            this.player.sendPacketImmediately(PacketFactory.createAvailableEntityIdentifiersPacket());
            this.player.sendPacketImmediately(PacketFactory.createCreativeContentPacket());
            this.player.sendPacketImmediately(PacketFactory.createAdventureSettingsPacket(entityId));
            this.player.sendPacketImmediately(PacketFactory.createUpdateAttributesPacket(entityId));
            this.player.sendPacketImmediately(PacketFactory.createEmptyWorldSetEntityDataPacket(entityId));
            this.player.sendPacketImmediately(PacketFactory.createEmptyChunkPacket());
            this.player.sendPacketImmediately(PacketFactory.createPlayStatusSpawnPacket());

            ArrayList<ItemData> items = PacketFactory.createItems();
            items.set(0, PacketFactory.createMapItem(this.captcha, this.mapId));

            this.player.sendPacketImmediately(PacketFactory.createInventoryContentPacket(items));
            this.player.sendPacketImmediately(PacketFactory.createSetTimePacket());
        } catch (IllegalStateException ignored) {
        }

        this.player.getProxy().getScheduler().scheduleDelayed(this::success, 20 * 10);
    }

    @Override
    public boolean handle(MapInfoRequestPacket packet) {
        this.player.sendPacketImmediately(PacketFactory.createMapDataPacket(packet.getUniqueMapId(), new int[128 * 128]));
        this.player.sendPacketImmediately(PacketFactory.createMapDataPacket(packet.getUniqueMapId(), this.captcha.getColors()));

        return true;
    }

    public BedrockPacketCodec getCodec() {
        return player.getProtocol().getCodec();
    }

    public void sendNewCaptcha() {
        this.player.sendPacketImmediately(PacketFactory.createMapDataPacket(this.mapId, resetCaptcha().getColors()));
    }
}
