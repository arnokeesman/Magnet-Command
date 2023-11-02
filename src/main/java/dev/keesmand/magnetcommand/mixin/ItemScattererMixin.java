package dev.keesmand.magnetcommand.mixin;

import dev.keesmand.magnetcommand.MagnetCommandMod;
import dev.keesmand.magnetcommand.config.MagnetCommandConfig;
import dev.keesmand.magnetcommand.enums.DropMode;
import dev.keesmand.magnetcommand.enums.MagnetMode;
import dev.keesmand.magnetcommand.util.IEntityDataSaver;
import dev.keesmand.magnetcommand.util.MagnetModeData;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.keesmand.magnetcommand.util.Magnet.InjectStack;

@Mixin(ItemScatterer.class)
public class ItemScattererMixin {
    @Inject(method = "spawn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/inventory/Inventory;)V", at = @At("HEAD"), cancellable = true)
    private static void inject(World world, BlockPos pos, Inventory inventory, CallbackInfo ci) {
        MagnetCommandConfig config = MagnetCommandMod.CONFIG;
        if (config == null || !config.includeContainerItems) return;

        ServerPlayerEntity player = MagnetCommandMod.BLOCKS_BROKEN_BY.getOrDefault(pos, null);
        if (player == null) return;

        if (MagnetModeData.getMagnetMode((IEntityDataSaver) player) != MagnetMode.OnBreak) return;

        for (int i = 0; i < inventory.size(); ++i) {
            InjectStack(world,
                    config.dropLocation == DropMode.Block ? pos : player.getBlockPos(),
                    player, inventory.getStack(i));
            inventory.removeStack(i);
        }

        ci.cancel();
    }

}
