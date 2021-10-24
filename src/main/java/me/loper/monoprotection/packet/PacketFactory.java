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

package me.loper.monoprotection.packet;

import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.data.*;
import com.nukkitx.protocol.bedrock.data.command.CommandPermission;
import com.nukkitx.protocol.bedrock.data.entity.EntityData;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlag;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlags;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import com.nukkitx.protocol.bedrock.packet.*;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import me.loper.monoprotection.captcha.Captcha;
import me.loper.monoprotection.palette.ChunkPalette;
import me.loper.monoprotection.registry.Registries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacketFactory {

    public static final int SUNRISE_TIME = 23000;

    public static BedrockPacket createEmptyWorldSetEntityDataPacket(int entityId) {
        SetEntityDataPacket packet = new SetEntityDataPacket();
        packet.setRuntimeEntityId(entityId);

        EntityFlags flags = new EntityFlags();
        flags.setFlag(EntityFlag.NO_AI, true);
        flags.setFlag(EntityFlag.HAS_GRAVITY, false);
        flags.setFlag(EntityFlag.HAS_COLLISION, false);

        packet.getMetadata().put(EntityData.FLAGS, flags);

        return packet;
    }

    public static BedrockPacket createNormalSetEntityDataPacket(int entityId) {
        SetEntityDataPacket packet = new SetEntityDataPacket();
        packet.setRuntimeEntityId(entityId);

        EntityFlags flags = new EntityFlags();
        flags.setFlag(EntityFlag.NO_AI, false);
        flags.setFlag(EntityFlag.HAS_GRAVITY, true);
        flags.setFlag(EntityFlag.HAS_COLLISION, true);

        packet.getMetadata().put(EntityData.FLAGS, flags);

        return packet;
    }

    public static BedrockPacket createUpdateAttributesPacket(int runtimeEntityId) {
        UpdateAttributesPacket packet = new UpdateAttributesPacket();
        List<AttributeData> attributes = new ObjectArrayList<>();

        attributes.add(new AttributeData("minecraft:health", 0.00f, 20.00f, 20.00f));
        attributes.add(new AttributeData("minecraft:movement", 0.00f, 0.00f, -1000.00f));
        attributes.add(new AttributeData("minecraft:player.hunger", 0.00f, 20.00f, 20.00f));
        attributes.add(new AttributeData("minecraft:player.level", 0.00f, 24791.00f, 256.00f));
        attributes.add(new AttributeData("minecraft:player.experience", 0.00f, 1.00f, 1.00f));

        packet.setAttributes(attributes);
        packet.setRuntimeEntityId(runtimeEntityId);

        return packet;
    }

    public static BedrockPacket createAdventureSettingsPacket(int entityId) {
        AdventureSettingsPacket packet = new AdventureSettingsPacket();

        packet.setUniqueEntityId(entityId);
        packet.setPlayerPermission(PlayerPermission.VISITOR);
        packet.setCommandPermission(CommandPermission.NORMAL);

        packet.getSettings().clear();
        packet.getSettings().add(AdventureSetting.WORLD_IMMUTABLE);
        packet.getSettings().add(AdventureSetting.TELEPORT);

        return packet;
    }

    public static BedrockPacket createAvailableEntityIdentifiersPacket() {
        AvailableEntityIdentifiersPacket packet = new AvailableEntityIdentifiersPacket();
        packet.setIdentifiers(Registries.ENTITY_IDENTIFIERS.get());

        return packet;
    }

    public static BedrockPacket createEmptyChunkPacket() {
        LevelChunkPacket packet = new LevelChunkPacket();

        packet.setChunkX(0);
        packet.setChunkZ(0);
        packet.setSubChunksLength(0);
        packet.setData(ChunkPalette.EMPTY_LEVEL_CHUNK_DATA);
        packet.setCachingEnabled(false);

        return packet;
    }

    public static PlayStatusPacket createPlayStatusSpawnPacket() {
        PlayStatusPacket packet = new PlayStatusPacket();
        packet.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);

        return packet;
    }

    public static PlayStatusPacket createLoginSuccessPacket() {
        PlayStatusPacket packet = new PlayStatusPacket();
        packet.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);

        return packet;
    }

    public static BiomeDefinitionListPacket createBiomeDefinitionListPacket() {
        BiomeDefinitionListPacket packet = new BiomeDefinitionListPacket();
        packet.setDefinitions(Registries.BIOMES_NBT.get());

        return packet;
    }

    public static CreativeContentPacket createCreativeContentPacket() {
        CreativeContentPacket packet = new CreativeContentPacket();
        packet.setContents(new ItemData[0]);

        return packet;
    }

    public static SetTimePacket createSetTimePacket() {
        SetTimePacket packet = new SetTimePacket();

        packet.setTime(SUNRISE_TIME * 256); // day is 256 - sunrise

        return packet;
    }

    public static StartGamePacket createStartGamePacket(int entityId) {
        StartGamePacket packet = new StartGamePacket();

        packet.setUniqueEntityId(entityId);
        packet.setRuntimeEntityId(entityId);
        packet.setPlayerGameType(GameType.ADVENTURE);
        packet.setPlayerPosition(Vector3f.from(0f, 66f, 0f));
        packet.setRotation(Vector2f.ONE);

        packet.setSeed(0);
        packet.setDimensionId(0);
        packet.setGeneratorId(2);
        packet.setXblBroadcastMode(GamePublishSetting.NO_MULTI_PLAY);
        packet.setLevelGameType(GameType.ADVENTURE);
        packet.setDifficulty(0);
        packet.setDefaultSpawn(Vector3i.ZERO);
        packet.setAchievementsDisabled(true);
        packet.setCurrentTick(-1);
        packet.setEduEditionOffers(0);
        packet.setEduFeaturesEnabled(false);
        packet.setRainLevel(0);
        packet.setLightningLevel(0);
        packet.setMultiplayerGame(true);
        packet.setBroadcastingToLan(true);
        packet.setPlatformBroadcastMode(GamePublishSetting.PUBLIC);
        packet.setXblBroadcastMode(GamePublishSetting.PUBLIC);
        packet.setCommandsEnabled(true);
        packet.setTexturePacksRequired(false);
        packet.setBonusChestEnabled(false);
        packet.setStartingWithMap(false);
        packet.setTrustingPlayers(true);
        packet.setDefaultPlayerPermission(PlayerPermission.VISITOR);
        packet.setServerChunkTickRange(4);
        packet.setBehaviorPackLocked(false);
        packet.setResourcePackLocked(false);
        packet.setFromLockedWorldTemplate(false);
        packet.setUsingMsaGamertagsOnly(false);
        packet.setFromWorldTemplate(false);
        packet.setWorldTemplateOptionLocked(false);

        packet.setLevelId("");
        packet.setLevelName("MonoProtection");
        packet.setPremiumWorldTemplateId("");
        packet.setCurrentTick(0);
        packet.setEnchantmentSeed(0);
        packet.setMultiplayerCorrelationId("");
        packet.setItemEntries(Collections.emptyList());
        packet.setItemEntries(Collections.emptyList());
        packet.setInventoriesServerAuthoritative(true);
        packet.setServerEngine("");

        SyncedPlayerMovementSettings settings = new SyncedPlayerMovementSettings();
        settings.setMovementMode(AuthoritativeMovementMode.CLIENT);
        settings.setServerAuthoritativeBlockBreaking(true);
        settings.setRewindHistorySize(0);

        packet.setPlayerMovementSettings(settings);

        packet.setVanillaVersion("*");

        return packet;
    }

    public static ItemData createMapItem(Captcha captcha, long mapId) {
        NbtMapBuilder nbtMapBuilder = NbtMap.builder();
        nbtMapBuilder.put("Colors", captcha.getImageBytes());
        nbtMapBuilder.put("map_uuid", mapId);
        NbtMap nbtMap = nbtMapBuilder.build();

        return ItemData.builder()
                .usingNetId(true)
                .tag(nbtMap)
                .netId(82)
                .id(420)
                .count(1)
                .build();
    }

    private InventorySlotPacket createInventorySlotPacket(ItemData item, int slot) {
        InventorySlotPacket packet = new InventorySlotPacket();

        packet.setContainerId(ContainerId.INVENTORY);
        packet.setItem(item);
        packet.setSlot(slot);

        return packet;
    }

    public static InventoryContentPacket createInventoryContentPacket(List<ItemData> items) {
        InventoryContentPacket packet = new InventoryContentPacket();
        packet.setContainerId(ContainerId.INVENTORY);

        packet.setContents(items);

        return packet;
    }

    public static ArrayList<ItemData> createItems() {
        ArrayList<ItemData> items = new ArrayList<>();

        for (int i = 0; i < 36; i++) {
            items.add(ItemData.builder().id(0).netId(0).usingNetId(false).build());
        }
        return items;
    }

    public static BedrockPacket createMapDataPacket(long uniqueMapId, int[] colors) {
        ClientboundMapItemDataPacket packet = new ClientboundMapItemDataPacket();

        packet.setUniqueMapId(uniqueMapId);

        packet.setHeight(128);
        packet.setWidth(128);
        packet.setXOffset(0);
        packet.setYOffset(0);
        packet.setScale(0);

        packet.setColors(colors);

        return packet;
    }
}
