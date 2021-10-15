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

import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.data.*;
import com.nukkitx.protocol.bedrock.data.command.CommandPermission;
import com.nukkitx.protocol.bedrock.data.entity.EntityData;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlag;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlags;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import com.nukkitx.protocol.bedrock.packet.*;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.WaterdogPE;
import dev.waterdog.waterdogpe.network.upstream.AbstractUpstreamHandler;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import io.netty.util.internal.ResourcesUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.loper.waterdog.monoprotector.nbt.PaletteManager;
import me.loper.waterdog.monoprotector.registry.Registries;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerConnectionHandler extends AbstractUpstreamHandler {

    public static final int SUNRISE_TIME = 23000;

    private final long mapId = ThreadLocalRandom.current().nextLong();
    private static final int[] colors = new int[128 * 128];
    private static byte[] imageInBytes;

    public void init() {
        if (null != imageInBytes && 0 == imageInBytes.length) {
            return;
        }

        try {
            BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
            URL resource = getClass().getClassLoader().getResource("dimas.png");
            BufferedImage originalImage = ImageIO.read(resource);

            Graphics2D graphics = image.createGraphics();
            graphics.drawImage(originalImage, 0, 0, 128, 128, null);
            graphics.dispose();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            imageInBytes = stream.toByteArray();

            int id = 0;
            for (int y = 0; y < image.getWidth(); y++) {
                for (int x = 0; x < image.getHeight(); x++) {
                    colors[id++] = (int) toABGR(image.getRGB(x, y));
                }
            }
            image.flush();
            originalImage.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public PlayerConnectionHandler(ProxiedPlayer player) {
        super(player);
    }

    @Override
    public boolean handle(TextPacket packet) {
        this.player.getProxy().getLogger().info("Player sends message: " + packet.getMessage());
        return true;
    }

    @Override
    public boolean handle(CommandRequestPacket packet) {
        this.player.getProxy().getLogger().info("Player sends command: " + packet.getCommand());
        return true;
    }

    @Override
    public boolean handle(MovePlayerPacket packet) {
        return true;
    }

    public void spawn() {
        try {
            this.player.sendPacket(createStartGamePacket());
            this.player.sendPacket(createBiomeDefinitionListPacket());
            this.player.sendPacket(createAvailableEntityIdentifiersPacket());
            this.player.sendPacket(createCreativeContentPacket());
            this.player.sendPacket(createAdventureSettingsPacket());
            this.player.sendPacket(createUpdateAttributesPacket());
            this.player.sendPacket(createSetEntityDataPacket());
            this.player.sendPacket(createEmptyChunkPacket());
            this.player.sendPacket(createPlayStatusSpawnPacket());

            this.player.sendPacket(createSetTimePacket());
        } catch (IllegalStateException ignored) {
        }
    }

    private BedrockPacket createSetEntityDataPacket() {
        SetEntityDataPacket packet = new SetEntityDataPacket();
        packet.setRuntimeEntityId(1);

        EntityFlags flags = new EntityFlags();
        flags.setFlag(EntityFlag.NO_AI, true);
        flags.setFlag(EntityFlag.HAS_GRAVITY, false);
        flags.setFlag(EntityFlag.HAS_COLLISION, false);

        packet.getMetadata().put(EntityData.FLAGS, flags);

        return packet;
    }

    private BedrockPacket createUpdateAttributesPacket() {
        UpdateAttributesPacket packet = new UpdateAttributesPacket();
        List<AttributeData> attributes = new ObjectArrayList<>();

        attributes.add(new AttributeData("minecraft:health", 0.00f, 20.00f, 20.00f));
        attributes.add(new AttributeData("minecraft:movement", 0.00f, 0.00f, -1000.00f));
        attributes.add(new AttributeData("minecraft:player.hunger", 0.00f, 20.00f, 20.00f));
        attributes.add(new AttributeData("minecraft:player.level", 0.00f, 24791.00f, 256.00f));
        attributes.add(new AttributeData("minecraft:player.experience", 0.00f, 1.00f, 1.00f));

        packet.setAttributes(attributes);
        packet.setRuntimeEntityId(1);

        return packet;
    }

    private BedrockPacket createAdventureSettingsPacket() {
        AdventureSettingsPacket packet = new AdventureSettingsPacket();

        packet.setUniqueEntityId(1);
        packet.setPlayerPermission(PlayerPermission.VISITOR);
        packet.setCommandPermission(CommandPermission.NORMAL);

        packet.getSettings().clear();
        packet.getSettings().add(AdventureSetting.WORLD_IMMUTABLE);
        packet.getSettings().add(AdventureSetting.TELEPORT);

        return packet;
    }

    private BedrockPacket createAvailableEntityIdentifiersPacket() {
        AvailableEntityIdentifiersPacket packet = new AvailableEntityIdentifiersPacket();
        packet.setIdentifiers(Registries.ENTITY_IDENTIFIERS.get());

        return packet;
    }

    private BedrockPacket createEmptyChunkPacket() {
        LevelChunkPacket packet = new LevelChunkPacket();

        packet.setChunkX(0);
        packet.setChunkZ(0);
        packet.setSubChunksLength(0);
        packet.setData(PaletteManager.EMPTY_LEVEL_CHUNK_DATA);
        packet.setCachingEnabled(false);

        return packet;
    }

    private PlayStatusPacket createPlayStatusSpawnPacket() {
        PlayStatusPacket packet = new PlayStatusPacket();
        packet.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);

        return packet;
    }

    private BiomeDefinitionListPacket createBiomeDefinitionListPacket() {
        BiomeDefinitionListPacket packet = new BiomeDefinitionListPacket();
        packet.setDefinitions(Registries.BIOMES_NBT.get());

        return packet;
    }

    private CreativeContentPacket createCreativeContentPacket() {
        CreativeContentPacket packet = new CreativeContentPacket();
        packet.setContents(new ItemData[0]);

        return packet;
    }

    private SetTimePacket createSetTimePacket() {
        SetTimePacket packet = new SetTimePacket();

        packet.setTime(SUNRISE_TIME * 256); // day is 256 - sunrise

        return packet;
    }

    private StartGamePacket createStartGamePacket() {
        StartGamePacket packet = new StartGamePacket();

        packet.setUniqueEntityId(1);
        packet.setRuntimeEntityId(1);
        packet.setPlayerGameType(GameType.ADVENTURE);
        packet.setPlayerPosition(Vector3f.from(0f, 66f, 0f));
        packet.setRotation(Vector2f.ONE);

        packet.setSeed(-1);
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
        packet.getGamerules().add(new GameRuleData<>("showcoordinates", true));
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
        packet.setLevelName("MonoProtector");
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

    @Override
    public boolean handle(MapInfoRequestPacket packet) {
        this.player.sendPacket(createMapDataPacket(packet.getUniqueMapId(), new int[128 * 128]));
        this.player.sendPacket(createMapDataPacket(packet.getUniqueMapId(), colors));

//        this.player.getProxy().getScheduler().scheduleAsync(() -> {
//            int part = 8;
//
//            int tinySize = colors.length / part;
//            int[] tinyColors = new int[128 * 128];
//
//            for (int i = 0; i < part; i++) {
//                int position = i == 0 ? 0 : tinySize * i;
//
//                System.arraycopy(colors, position, tinyColors, position, tinySize);
//
//                ProxyServer.getInstance().getLogger().info("position: " + position + "; iteration: " + i);
//
//                this.player.sendPacket(createMapDataPacket(packet.getUniqueMapId(), tinyColors));
//            }
//        });

        return true;
    }

    public BedrockPacketCodec getCodec() {
        return player.getProtocol().getCodec();
    }

    public void sendCaptcha() {
        InventoryContentPacket packet = new InventoryContentPacket();

        NbtMapBuilder nbtMapBuilder = NbtMap.builder();
        nbtMapBuilder.put("Colors", imageInBytes);
        nbtMapBuilder.put("map_uuid", this.mapId);
        NbtMap nbtMap = nbtMapBuilder.build();

        packet.setContainerId(ContainerId.INVENTORY);

        ItemData itemMap = ItemData.builder()
                .usingNetId(true)
                .tag(nbtMap)
                .netId(82)
                .id(420)
                .count(1)
                .build();

        ArrayList<ItemData> items = new ArrayList<>();

        for (int i = 0; i < 36; i++) {
            items.add(ItemData.builder().id(0).netId(0).usingNetId(false).build());
        }

        items.set(0, itemMap);
        packet.setContents(items);

        this.player.sendPacket(packet);
    }

    public static long toABGR(int argb) {
        long result = argb & 0xFF00FF00L;
        result |= (argb << 16) & 0x00FF0000L; // B to R
        result |= (argb >>> 16) & 0xFFL; // R to B
        return result & 0xFFFFFFFFL;
    }

    private BedrockPacket createMapDataPacket(long uniqueMapId, int[] colors) {
        ClientboundMapItemDataPacket packet = new ClientboundMapItemDataPacket();

        packet.setUniqueMapId(uniqueMapId);

        packet.setHeight(128);
        packet.setWidth(128);
        packet.setXOffset(0);
        packet.setYOffset(0);
        packet.setScale(0);

        MapTrackedObject mapTrackedObject = new MapTrackedObject(1);
        packet.getTrackedObjects().add(mapTrackedObject);

        packet.setColors(colors);

        return packet;
    }
}
