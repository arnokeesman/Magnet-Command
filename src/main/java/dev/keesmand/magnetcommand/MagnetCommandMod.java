package dev.keesmand.magnetcommand;

import dev.keesmand.magnetcommand.commands.MagnetCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MagnetCommandMod implements ModInitializer {
	public static final String MOD_CONTAINER_ID = "magnet-command";
	public static final ModMetadata MOD_METADATA = FabricLoader.getInstance().getModContainer(MOD_CONTAINER_ID).map(ModContainer::getMetadata).orElse(null);
	public static final String MOD_ID = MOD_METADATA != null ? MOD_METADATA.getId() : "magnet-command | I guess, couldn't find it";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		log("loading...");
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(MagnetCommand.register()));
		log("loaded!");
	}

	private static final String logPrefix = "["+MOD_METADATA.getName()+"] ";
	public static void log(String message) {
		LOGGER.info(logPrefix+message);
	}
}
