package dev.keesmand.magnetcommand;

import dev.keesmand.magnetcommand.commands.MagnetCommand;
import dev.keesmand.magnetcommand.config.MagnetCommandConfig;
import dev.keesmand.magnetcommand.config.MagnetCommandConfigManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MagnetCommandMod implements ModInitializer {
	public static final String MOD_CONTAINER_ID = "magnet-command";
	public static final ModMetadata MOD_METADATA = FabricLoader.getInstance().getModContainer(MOD_CONTAINER_ID).map(ModContainer::getMetadata).orElse(null);
	public static final String MOD_ID = MOD_METADATA != null ? MOD_METADATA.getId() : "magnet-command | I guess, couldn't find it";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static MagnetCommandConfig CONFIG;
    public static Map<BlockPos, ServerPlayerEntity> BLOCKS_BROKEN_BY;

	@Override
	public void onInitialize() {
		log("loading...");
		CONFIG = MagnetCommandConfigManager.load();
		if (CONFIG == null) {
			log("disabling mod...");
			return;
		}
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(MagnetCommand.register()));
        BLOCKS_BROKEN_BY = new HashMap<>();

		log("loaded!");
	}

	private static final String logPrefix = "["+MOD_METADATA.getName()+"] ";
	public static void log(String message) {
		LOGGER.info(logPrefix+message);
	}
	public static void error(String message) {
		LOGGER.error(logPrefix+message);
	}
}
