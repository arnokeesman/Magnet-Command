package dev.keesmand.magnetcommand.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.keesmand.magnetcommand.enums.MagnetMode;
import dev.keesmand.magnetcommand.util.IEntityDataSaver;
import dev.keesmand.magnetcommand.util.MagnetModeData;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;

import static dev.keesmand.magnetcommand.MagnetCommandMod.MOD_METADATA;

public class MagnetCommand {
	public static LiteralArgumentBuilder<ServerCommandSource> register() {
		return CommandManager.literal("magnet")
			.then(CommandManager.literal("OnBreak")
				.requires(ctx -> Permissions.check(ctx, "magnet.mode.break", 2))
				.executes(ctx -> setMode(ctx.getSource(), MagnetMode.OnBreak)))
			.then(CommandManager.literal("Range")
				.requires(ctx -> Permissions.check(ctx, "magnet.mode.range", 2))
				.executes(ctx -> setMode(ctx.getSource(), MagnetMode.Range)))
			.then(CommandManager.literal("Off")
				.executes(ctx -> setMode(ctx.getSource(), MagnetMode.Off)))
			.then(CommandManager.literal("info")
				.executes(ctx -> provideInfo(ctx.getSource())));
	}

	public static int setMode(ServerCommandSource source, MagnetMode mode) throws CommandSyntaxException {
		MagnetModeData.setMagnetMode((IEntityDataSaver) source.getPlayer(), mode);
		source.sendFeedback(new LiteralText("Set magnet mode to "+mode.name()), false);

		return 0;
	}
	public static int provideInfo(ServerCommandSource source) throws CommandSyntaxException {
		source.sendFeedback(new LiteralText(MOD_METADATA.getName()+" "+MOD_METADATA.getVersion().getFriendlyString()), false);
		return 0;
	}
}
