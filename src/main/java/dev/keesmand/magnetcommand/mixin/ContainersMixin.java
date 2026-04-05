package dev.keesmand.magnetcommand.mixin;

import dev.keesmand.magnetcommand.MagnetCommandMod;
import dev.keesmand.magnetcommand.config.MagnetCommandConfig;
import dev.keesmand.magnetcommand.enums.DropMode;
import dev.keesmand.magnetcommand.enums.MagnetMode;
import dev.keesmand.magnetcommand.util.MagnetModeData;
import net.minecraft.world.Container;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.keesmand.magnetcommand.util.Magnet.InjectStack;

@Mixin(Containers.class)
public class ContainersMixin {
    @Inject(method = "dropContents(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/Container;)V", at = @At("HEAD"), cancellable = true)
    private static void inject(Level level, BlockPos pos, Container container, CallbackInfo ci) {
        MagnetCommandConfig config = MagnetCommandMod.CONFIG;
        if (config == null || !config.includeContainerItems) return;

        ServerPlayer player = MagnetCommandMod.BLOCKS_BROKEN_BY.getOrDefault(pos, null);
        if (player == null) return;

        if (MagnetModeData.getMagnetMode(player) != MagnetMode.OnBreak) return;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            InjectStack(level,
                    config.dropLocation == DropMode.Block ? pos : player.blockPosition(),
                    player, container.getItem(i));
        }

        ci.cancel();
    }

}
