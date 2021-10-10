package me.loper.waterdog.monoprotector;

import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.data.*;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import com.nukkitx.protocol.bedrock.packet.*;
import dev.waterdog.waterdogpe.WaterdogPE;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.loper.waterdog.monoprotector.registry.Registries;
import me.loper.waterdog.monoprotector.upstream.network.MonoProtectionHandler;
import me.loper.waterdog.monoprotector.upstream.utils.PaletteManager;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonoProtection {

    private final Map<String, ProxiedPlayer> players = new ConcurrentHashMap<>();

    private static InputStream getFileInputStream(String path) {
        InputStream is = WaterdogPE.class.getClassLoader().getResourceAsStream(path);

        if (is == null) {
            throw new AssertionError("Could not find " + path);
        }

        return is;
    }

    public void connect(ProxiedPlayer player) {
        player.getUpstream().setPacketHandler(new MonoProtectionHandler(player));
        player.getUpstream().setPacketCodec(player.getProtocol().getCodec());

        if (null != this.players.putIfAbsent(player.getName().toLowerCase(), player)) {
            player.disconnect("§cPlayer with this name already play on the server.");
            return;
        }

        player.getProxy().getMonoProtection().spawnPlayer(player);
    }

    public void spawnPlayer(ProxiedPlayer player) {
        try {
            player.sendPacket(createStartGamePacket(player));
            player.sendPacket(createBiomeDefinitionListPacket());
            player.sendPacket(createAvailableEntityIdentifiersPacket());
            player.sendPacket(createCreativeContentPacket());
            player.sendPacket(createAdventureSettingsPacket());
            player.sendPacket(createEmptyChunkPacket());
            player.sendPacket(createPlayStatusSpawnPacket());
            player.sendPacket(createUpdateAttributesPacket());

            player.sendPacket(createSetTimePacket());
        } catch (IllegalStateException ignored) {
        }
    }

    private BedrockPacket createUpdateAttributesPacket() {
        UpdateAttributesPacket packet = new UpdateAttributesPacket();
        List<AttributeData> attributes = new ObjectArrayList<>();

        attributes.add(new AttributeData("minecraft:health", 0.00f, 20.00f, 20.00f));
        attributes.add(new AttributeData("minecraft:movement", 0.00f, 0.00f, 0.00f));
        attributes.add(new AttributeData("minecraft:player.hunger", 0.00f, 20.00f, 20.00f));
        attributes.add(new AttributeData("minecraft:player.level", 0.00f, 24791.00f, 256.00f));
        attributes.add(new AttributeData("minecraft:player.experience", 0.00f, 1.00f, 0.00f));

        packet.setAttributes(attributes);

        return packet;
    }

    private BedrockPacket createAdventureSettingsPacket() {
        AdventureSettingsPacket packet = new AdventureSettingsPacket();

        packet.setUniqueEntityId(1);
        packet.setPlayerPermission(PlayerPermission.VISITOR);

        packet.getSettings().clear();
        packet.getSettings().add(AdventureSetting.WORLD_IMMUTABLE);
        packet.getSettings().add(AdventureSetting.MAY_FLY);
        packet.getSettings().add(AdventureSetting.FLYING);

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

    private ResourcePacksInfoPacket createResourcePacksInfoPacket() {
        ResourcePacksInfoPacket packet = new ResourcePacksInfoPacket();

        packet.setForcedToAccept(false);
        packet.setScriptingEnabled(false);
        packet.setForcingServerPacksEnabled(false);

        return packet;
    }

    private ResourcePackStackPacket createResourcePackStackPacket() {
        ResourcePackStackPacket packet = new ResourcePackStackPacket();

        packet.setGameVersion("");
        packet.setForcedToAccept(false);
        packet.setExperimentsPreviouslyToggled(false);

        return packet;
    }

    private CreativeContentPacket createCreativeContentPacket() {
        CreativeContentPacket packet = new CreativeContentPacket();
        packet.setContents(new ItemData[0]);

        return packet;
    }

    private SetTimePacket createSetTimePacket() {
        SetTimePacket packet = new SetTimePacket();

        packet.setTime(23000);

        return packet;
    }

    private StartGamePacket createStartGamePacket(ProxiedPlayer player) {
        StartGamePacket packet = new StartGamePacket();

        packet.setUniqueEntityId(1);
        packet.setRuntimeEntityId(1);
        packet.setPlayerGameType(GameType.ADVENTURE);
        packet.setPlayerPosition(Vector3f.from(0, 150 + 2, 0));
        packet.setRotation(Vector2f.ONE);

        packet.setSeed(-1);
        packet.setDimensionId(2);
        packet.setGeneratorId(1);
        packet.setLevelGameType(GameType.CREATIVE);
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
        settings.setMovementMode(AuthoritativeMovementMode.SERVER);
        settings.setServerAuthoritativeBlockBreaking(false);
        settings.setRewindHistorySize(0);

        packet.setPlayerMovementSettings(settings);

        packet.setVanillaVersion("*");

        return packet;
    }

    public void removePlayer(ProxiedPlayer player) {
        if (null != player) {
            this.players.remove(player.getName().toLowerCase());
        }
    }
}
