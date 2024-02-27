package dev.keesmand.magnetcommand.util;

import dev.keesmand.magnetcommand.MagnetCommandMod;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static net.minecraft.block.Block.dropStack;

/* implementation stolen from https://github.com/maxvar/mcf-magnets/blob/8c4bb0126f43589931ffa256e31a0bd8c4231d82/src/main/kotlin/ru/maxvar/mcf/magnets/Magnet.kt under WTFPL
 * thanks for this code maxvar!
 */

public class Magnet {
    public static void PullItem(Vec3d playerPos, ItemEntity item, double pullStrength) {
        Vec3d itemPos = item.getPos();
        pullStrength *= MagnetCommandMod.CONFIG.pullStrengthMultiplier;
        item.addVelocity(
                force(playerPos.x - itemPos.x, pullStrength),
                force(playerPos.y - itemPos.y, pullStrength),
                force(playerPos.z - itemPos.z, pullStrength)
        );
    }

    public static void TeleportItem(Vec3d playerPos, ItemEntity item) {
//		if (playerPos.distanceTo(item.getPos()) < 0.5) return;
        item.setPosition(playerPos);
    }

    private static double force(double distance, double strength) {
        if (Math.abs(distance) > strength) return distance * .01;
        if (Math.abs(distance) > (strength / 2)) return distance * .05;
        return distance * .1;
    }

    public static boolean TestItemEntity(ItemEntity item) {
        if (MagnetCommandMod.CONFIG.skipCanPickUpCheck) return true;
        return !item.cannotPickup();
    }

    public static void InjectStack(World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
        if (!player.getInventory().insertStack(stack))
            dropStack(world, pos, stack);
    }
}
