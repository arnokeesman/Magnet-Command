package dev.keesmand.magnetcommand;

import dev.keesmand.magnetcommand.commands.MagnetCommand;
import dev.keesmand.magnetcommand.config.MagnetCommandConfig;
import dev.keesmand.magnetcommand.config.MagnetCommandConfigManager;
import eu.pb4.playerdata.api.PlayerDataApi;
import eu.pb4.playerdata.api.storage.NbtDataStorage;
import eu.pb4.playerdata.api.storage.PlayerDataStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MagnetCommandMod implements ModInitializer {
    public static final String MOD_CONTAINER_ID = "magnet-command";
    public static final ModMetadata MOD_METADATA = FabricLoader.getInstance().getModContainer(MOD_CONTAINER_ID).map(ModContainer::getMetadata).orElse(null);
    public static final String MOD_ID = MOD_METADATA != null ? MOD_METADATA.getId() : "magnet-command | I guess, couldn't find it";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final String logPrefix = "[" + MOD_METADATA.getName() + "] ";
    public static MagnetCommandConfig CONFIG;
    public static Map<BlockPos, ServerPlayer> BLOCKS_BROKEN_BY;
    public static final PlayerDataStorage<CompoundTag> DATA_STORAGE = new NbtDataStorage("magnet_command");

    static {
        PlayerDataApi.register(DATA_STORAGE);
    }

    public static void log(String message) {
        LOGGER.info(logPrefix + message);
    }

    public static void error(String message) {
        LOGGER.error(logPrefix + message);
    }

    @Override
    public void onInitialize() {
        log("loading...");
        CONFIG = MagnetCommandConfigManager.load();
        if (CONFIG == null) {
            log("disabling mod...");
            return;
        }
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(MagnetCommand.register()));
        BLOCKS_BROKEN_BY = new HashMap<>();

        log("loaded!");
    }
}
