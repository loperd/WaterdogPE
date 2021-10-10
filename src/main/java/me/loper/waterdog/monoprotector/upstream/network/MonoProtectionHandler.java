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

package me.loper.waterdog.monoprotector.upstream.network;

import com.nukkitx.protocol.bedrock.packet.*;
import dev.waterdog.waterdogpe.network.upstream.AbstractUpstreamHandler;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

public class MonoProtectionHandler extends AbstractUpstreamHandler {

    public MonoProtectionHandler(ProxiedPlayer player) {
        super(player);
    }

    @Override
    public boolean handle(TextPacket packet) {
        this.player.getProxy().getLogger().info("Player sends message: " + packet.getMessage());
        return true;
    }

    @Override
    public boolean handle(LoginPacket packet) {
        this.player.getProxy().getLogger().info("Player sends login packet.");
        return true;
    }

    @Override
    public boolean handle(CommandRequestPacket packet) {
        this.player.getProxy().getLogger().info("Player sends command: " + packet.getCommand());
        return true;
    }

    @Override
    public boolean handle(InventorySlotPacket packet) {
        this.player.getProxy().getLogger().info("Player request inventory slot: " + packet.getSlot());
        return true;
    }

    @Override
    public boolean handle(InventoryContentPacket packet) {
        this.player.getProxy().getLogger().info("Player sends inventory content.");
        return true;
    }

    @Override
    public boolean handle(MovePlayerPacket packet) {
        this.player.getProxy().getLogger().info("Move player packet: " + packet.getPosition().toString());
        return true;
    }
}
