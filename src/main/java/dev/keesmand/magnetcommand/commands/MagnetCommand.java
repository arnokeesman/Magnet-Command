package dev.keesmand.magnetcommand.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.keesmand.magnetcommand.MagnetCommandMod;
import dev.keesmand.magnetcommand.config.MagnetCommandConfig;
import dev.keesmand.magnetcommand.enums.MagnetMode;
import dev.keesmand.magnetcommand.util.MagnetModeData;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static dev.keesmand.magnetcommand.MagnetCommandMod.MOD_METADATA;

public class MagnetCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        LiteralArgumentBuilder<CommandSourceStack> node = Commands.literal("magnet")
                .then(Commands.literal("info")
                        .executes(ctx -> provideInfo(ctx.getSource())));

        MagnetCommandConfig config = MagnetCommandMod.CONFIG;

        if (config.rangeEnabled || config.onBreakEnabled) {
            node.then(Commands.literal("Off").executes(ctx -> setMode(ctx.getSource(), MagnetMode.Off)));
        }

        if (config.rangeEnabled) {
            node.then(Commands.literal("Range")
                    .requires(ctx -> Permissions.check(ctx, "magnet.mode.range", config.permissionLevel))
                    .executes(ctx -> setMode(ctx.getSource(), MagnetMode.Range)));
        }

        if (config.onBreakEnabled) {
            node.then(Commands.literal("OnBreak")
                    .requires(ctx -> Permissions.check(ctx, "magnet.mode.break", config.permissionLevel))
                    .executes(ctx -> setMode(ctx.getSource(), MagnetMode.OnBreak)));
        }

        return node;
    }

    public static int setMode(CommandSourceStack source, MagnetMode mode) {
        if (!source.isPlayer()) {
            source.sendSuccess(() -> Component.literal("This command can only be used by players"), false);
            return 0;
        }
        MagnetModeData.setMagnetMode(source.getPlayer(), mode);
        source.sendSuccess(() -> Component.literal("Set magnet mode to " + mode.name()), false);

        return 0;
    }

    public static int provideInfo(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal(MOD_METADATA.getName() + " " + MOD_METADATA.getVersion().getFriendlyString()), false);
        return 0;
    }
}
