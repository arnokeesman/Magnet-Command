package dev.keesmand.magnetcommand.mixin;

import dev.keesmand.magnetcommand.MagnetCommandMod;
import dev.keesmand.magnetcommand.config.MagnetCommandConfig;
import dev.keesmand.magnetcommand.enums.DropMode;
import dev.keesmand.magnetcommand.enums.MagnetMode;
import dev.keesmand.magnetcommand.util.MagnetModeData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

import static dev.keesmand.magnetcommand.util.Magnet.InjectStack;
import static net.minecraft.world.level.block.Block.getDrops;

@Mixin(Block.class)
public class BlockMixin {
    @Redirect(
            method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;")
    )
    private static List<ItemStack> onDropStacks(BlockState state, ServerLevel world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack tool) {
        List<ItemStack> droppedStacks = getDrops(state, world, pos, blockEntity, entity, tool);

        MagnetCommandConfig config = MagnetCommandMod.CONFIG;
        if (config == null) return droppedStacks;

        if (entity instanceof ServerPlayer player) {
            MagnetMode mode = MagnetModeData.getMagnetMode(player);
            if (mode != MagnetMode.OnBreak) return droppedStacks;

            droppedStacks.forEach(dropStack -> InjectStack(world,
                    config.dropLocation == DropMode.Block ? pos : player.blockPosition(),
                    player, dropStack));

            // return an empty list as we've already given out the drops
            return new ArrayList<>();
        }

        return droppedStacks;
    }
}
