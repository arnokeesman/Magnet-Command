package dev.keesmand.magnetcommand.mixin;

import dev.keesmand.magnetcommand.MagnetCommandMod;
import dev.keesmand.magnetcommand.config.MagnetCommandConfig;
import dev.keesmand.magnetcommand.enums.DropMode;
import dev.keesmand.magnetcommand.enums.MagnetMode;
import dev.keesmand.magnetcommand.util.IEntityDataSaver;
import dev.keesmand.magnetcommand.util.MagnetModeData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static dev.keesmand.magnetcommand.util.Magnet.InjectStack;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
	@Shadow @Final protected ServerPlayerEntity player;

	@Shadow protected ServerWorld world;

	@Inject(
			method = "tryBreakBlock",
			at = @At(value = "INVOKE", target="Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	void injectInventoryContents(BlockPos pos, CallbackInfoReturnable<Boolean> cir, BlockState blockState, BlockEntity blockEntity, Block block) {
		if (!(blockEntity instanceof Inventory inventory)) return;
		MagnetCommandConfig config = MagnetCommandMod.CONFIG;
		if (config == null || !config.includeContainerItems) return;

		if (MagnetModeData.getMagnetMode((IEntityDataSaver)this.player) != MagnetMode.OnBreak) return;

		for(int i = 0; i < inventory.size(); ++i) {
			InjectStack(world,
					config.dropLocation == DropMode.Block ? pos : player.getBlockPos(),
					player, inventory.getStack(i));
			inventory.removeStack(i);
		}
	}
}
