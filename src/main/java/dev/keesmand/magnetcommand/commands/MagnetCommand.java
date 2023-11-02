package dev.keesmand.magnetcommand.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.keesmand.magnetcommand.MagnetCommandMod;
import dev.keesmand.magnetcommand.config.MagnetCommandConfig;
import dev.keesmand.magnetcommand.enums.MagnetMode;
import dev.keesmand.magnetcommand.util.IEntityDataSaver;
import dev.keesmand.magnetcommand.util.MagnetModeData;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static dev.keesmand.magnetcommand.MagnetCommandMod.MOD_METADATA;

public class MagnetCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        LiteralArgumentBuilder<ServerCommandSource> node = CommandManager.literal("magnet")
                .then(CommandManager.literal("info")
                        .executes(ctx -> provideInfo(ctx.getSource())));

        MagnetCommandConfig config = MagnetCommandMod.CONFIG;

        if (config.rangeEnabled || config.onBreakEnabled) {
            node.then(CommandManager.literal("Off").executes(ctx -> setMode(ctx.getSource(), MagnetMode.Off)));
        }

        if (config.rangeEnabled) {
            node.then(CommandManager.literal("Range")
                    .requires(ctx -> Permissions.check(ctx, "magnet.mode.range", config.permissionLevel))
                    .executes(ctx -> setMode(ctx.getSource(), MagnetMode.Range)));
        }

        if (config.onBreakEnabled) {
            node.then(CommandManager.literal("OnBreak")
                    .requires(ctx -> Permissions.check(ctx, "magnet.mode.break", config.permissionLevel))
                    .executes(ctx -> setMode(ctx.getSource(), MagnetMode.OnBreak)));
        }

        return node;
    }

    public static int setMode(ServerCommandSource source, MagnetMode mode) {
        if (!source.isExecutedByPlayer()) {
            source.sendFeedback(Text.literal("This command can only be used by players"), false);
            return 0;
        }
        MagnetModeData.setMagnetMode((IEntityDataSaver) source.getPlayer(), mode);
        source.sendFeedback(Text.literal("Set magnet mode to " + mode.name()), false);

        return 0;
    }

    public static int provideInfo(ServerCommandSource source) {
        source.sendFeedback(Text.literal(MOD_METADATA.getName() + " " + MOD_METADATA.getVersion().getFriendlyString()), false);
        return 0;
    }
}
