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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.keesmand.magnetcommand.util.Magnet.InjectStack;
import static net.minecraft.block.Block.getDroppedStacks;

@Mixin(Block.class)
public class BlockMixin {
	@Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
	private static void onDropStacks(BlockState state, World world, BlockPos pos, @Nullable BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfo ci) {
		if (!(world instanceof ServerWorld)) return;
		MagnetCommandConfig config = MagnetCommandMod.CONFIG;
		if (config == null) return;

		if (entity instanceof PlayerEntity player) {
			MagnetMode mode = MagnetModeData.getMagnetMode((IEntityDataSaver) player);
			if (mode != MagnetMode.OnBreak) return;

			getDroppedStacks(state, (ServerWorld)world, pos, blockEntity, entity, stack)
					.forEach(dropStack -> InjectStack(world,
							config.dropLocation == DropMode.Block ? pos : player.getBlockPos(),
							player, dropStack));

			ci.cancel();
		}
	}
}
