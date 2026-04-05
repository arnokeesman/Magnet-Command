package dev.keesmand.magnetcommand.util;

import dev.keesmand.magnetcommand.MagnetCommandMod;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import static net.minecraft.world.level.block.Block.popResource;

/* implementation stolen from https://github.com/maxvar/mcf-magnets/blob/8c4bb0126f43589931ffa256e31a0bd8c4231d82/src/main/kotlin/ru/maxvar/mcf/magnets/Magnet.kt under WTFPL
 * thanks for this code maxvar!
 */

public class Magnet {
    public static void PullItem(Vec3 playerPos, ItemEntity item, double pullStrength) {
        Vec3 itemPos = item.position();
        pullStrength *= MagnetCommandMod.CONFIG.pullStrengthMultiplier;
        item.push(
                force(playerPos.x - itemPos.x, pullStrength),
                force(playerPos.y - itemPos.y, pullStrength),
                force(playerPos.z - itemPos.z, pullStrength)
        );
    }

    public static void TeleportItem(Vec3 playerPos, ItemEntity item) {
//		if (playerPos.distanceTo(item.getPos()) < 0.5) return;
        item.setPos(playerPos);
    }

    private static double force(double distance, double strength) {
        if (Math.abs(distance) > strength) return distance * .01;
        if (Math.abs(distance) > (strength / 2)) return distance * .05;
        return distance * .1;
    }

    public static boolean TestItemEntity(ItemEntity item) {
        if (MagnetCommandMod.CONFIG.skipCanPickUpCheck) return true;
        return !item.hasPickUpDelay();
    }

    public static void InjectStack(Level world, BlockPos pos, Player player, ItemStack stack) {
        if (!player.getInventory().add(stack))
            popResource(world, pos, stack);
    }
}
