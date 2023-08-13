package dev.keesmand.magnetcommand.util;

import dev.keesmand.magnetcommand.MagnetCommandMod;
import dev.keesmand.magnetcommand.config.MagnetCommandConfig;
import dev.keesmand.magnetcommand.enums.MagnetMode;
import dev.keesmand.magnetcommand.enums.MoveMode;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

import static dev.keesmand.magnetcommand.util.MagnetModeData.getMagnetMode;

/* implementation stolen from https://github.com/maxvar/mcf-magnets/blob/8c4bb0126f43589931ffa256e31a0bd8c4231d82/src/main/kotlin/ru/maxvar/mcf/magnets/Magnet.kt under WTFPL
 * thanks for this code maxvar!
 */

public class Magnet {
	public static void ApplyMagnetEffect(PlayerEntity player) {
		MagnetCommandConfig config = MagnetCommandMod.CONFIG;
		if (config == null) return;
		if (!config.rangeEnabled) return;
		MagnetMode mode = getMagnetMode((IEntityDataSaver) player);
		if (mode != MagnetMode.Range) return;

		double range = config.range;

		// get items within range
		Vec3d playerPos = player.getPos();
		Box box = new Box(
				playerPos.x+range, playerPos.y+range, playerPos.z+range,
				playerPos.x-range, playerPos.y-range, playerPos.z-range);
		List<ItemEntity> items = player.getWorld().getEntitiesByType(EntityType.ITEM, box, Magnet::testItem);

		items.forEach(item -> {
			if (config.moveMode == MoveMode.Pull) PullItem(playerPos, item, range);
			else if (config.moveMode == MoveMode.Teleport) TeleportItem(playerPos, item);
		});
	}

	public static void PullItem(Vec3d playerPos, ItemEntity item, double pullStrength) {
			Vec3d itemPos = item.getPos();
			item.addVelocity(
				force(playerPos.x-itemPos.x, pullStrength),
				force(playerPos.y-itemPos.y, pullStrength),
				force(playerPos.z-itemPos.z, pullStrength)
			);
	}

	public static void TeleportItem(Vec3d playerPos, ItemEntity item) {
//		if (playerPos.distanceTo(item.getPos()) < 0.5) return;
		item.setPosition(playerPos);
	}

	private static double force(double distance, double strength) {
		if (Math.abs(distance) > strength) return distance*.01;
		if (Math.abs(distance) > (strength/2)) return distance*.05;
		return distance*.1;
	}

	private static boolean testItem(ItemEntity item) {
		return !item.cannotPickup();
	}
}
