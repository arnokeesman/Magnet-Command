package dev.keesmand.magnetcommand.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.keesmand.magnetcommand.MagnetCommandMod;
import dev.keesmand.magnetcommand.enums.DropMode;
import dev.keesmand.magnetcommand.enums.MoveMode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class MagnetCommandConfigManager {
	static final String configFile = "config/magnet-command.json";

	public static void save(MagnetCommandConfig config) {
		JsonObject jsonRoot = new JsonObject();
		jsonRoot.addProperty("CONFIG_VERSION", 1);
		jsonRoot.addProperty("permissionLevel", config.permissionLevel);

		JsonObject modes = new JsonObject();
		jsonRoot.add("modes", modes);

		JsonObject rangeMode = new JsonObject();
		modes.add("Range", rangeMode);
		rangeMode.addProperty("enabled", config.rangeEnabled);
		rangeMode.addProperty("range", config.range);
		rangeMode.addProperty("_c1", "moveMode can be either Pull or Teleport");
		rangeMode.addProperty("moveMode", config.moveMode.name());

		JsonObject onBreakMode = new JsonObject();
		modes.add("OnBreak", onBreakMode);
		onBreakMode.addProperty("enabled", config.onBreakEnabled);
		onBreakMode.addProperty("_c1", "drop location (for when inventory is full), either Block or Player");
		onBreakMode.addProperty("dropLocation", config.dropLocation.name());
		onBreakMode.addProperty("includeContainerItems", config.includeContainerItems);


		try (Writer writer = new FileWriter(configFile)) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(jsonRoot, writer);
		} catch (IOException e) {
			MagnetCommandMod.log("failed to save config: " + e);
		}
	}

	public static MagnetCommandConfig load() {
		File file = new File(configFile);
		if (!file.exists()) {
			MagnetCommandMod.log("No config found, generating");
			MagnetCommandConfig config = generateDefault();
			save(config);
			return config;
		}

		try {
			JsonObject jsonConfig = new Gson().fromJson(Files.readString(Path.of(configFile)), JsonObject.class);
			int configVersion = jsonConfig.get("CONFIG_VERSION").getAsInt();
			if (configVersion != 1) throw new IllegalArgumentException("unknown config version");

			int permissionLevel = jsonConfig.get("permissionLevel").getAsInt();

			JsonObject modesObject = jsonConfig.get("modes").getAsJsonObject();
			JsonObject rangeModeObject = modesObject.get("Range").getAsJsonObject();
			boolean rangeEnabled = rangeModeObject.get("enabled").getAsBoolean();
			int range = rangeModeObject.get("range").getAsInt();
			String moveModeString = rangeModeObject.has("moveMode")
					? rangeModeObject.get("moveMode").getAsString()
					: rangeModeObject.get("mode").getAsString();
			if (!isValidEnumValue(MoveMode.class, moveModeString))
				throw new IllegalArgumentException("unknown moveMode");
			MoveMode moveMode = MoveMode.valueOf(moveModeString);

			JsonObject onBreakModeObject = modesObject.get("OnBreak").getAsJsonObject();
			boolean onBreakEnabled = onBreakModeObject.get("enabled").getAsBoolean();
			String dropModeString = onBreakModeObject.get("dropLocation").getAsString();
			if (!isValidEnumValue(DropMode.class, dropModeString))
				throw new IllegalArgumentException("unknown dropLocation value");
			DropMode dropMode = DropMode.valueOf(dropModeString);
			boolean includeContainerItems = onBreakModeObject.has("includeContainerItems")
					? onBreakModeObject.get("includeContainerItems").getAsBoolean()
					: true;

			MagnetCommandConfig config = new MagnetCommandConfig(permissionLevel, rangeEnabled, range, moveMode, onBreakEnabled, dropMode, includeContainerItems);
			save(config);
			return config;

		} catch (Exception e) {
			String message;
			if (e instanceof JsonParseException) message = "JSON is config is invalid\ntry opening with a text editor like https://vscode.dev to see mistakes\nyou can also delete the file to generate a new one on next startup";
			else message = e.getMessage();
			MagnetCommandMod.error(message);
			return null;
		}
	}

	static MagnetCommandConfig generateDefault() {
		return new MagnetCommandConfig(2, true, 3, MoveMode.Pull, true, DropMode.Block, true);
	}

	private static <E extends Enum<E>> boolean isValidEnumValue(Class<E> enumType, String value) {
		try {
			Enum.valueOf(enumType, value);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
