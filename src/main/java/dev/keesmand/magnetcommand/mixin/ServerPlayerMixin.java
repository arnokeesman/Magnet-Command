package dev.keesmand.magnetcommand.mixin;

import dev.keesmand.magnetcommand.MagnetCommandMod;
import dev.keesmand.magnetcommand.config.MagnetCommandConfig;
import dev.keesmand.magnetcommand.enums.MagnetMode;
import dev.keesmand.magnetcommand.enums.MoveMode;
import dev.keesmand.magnetcommand.util.Magnet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static dev.keesmand.magnetcommand.util.Magnet.PullItem;
import static dev.keesmand.magnetcommand.util.Magnet.TeleportItem;
import static dev.keesmand.magnetcommand.util.MagnetModeData.getMagnetMode;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @SuppressWarnings("UnreachableCode")
    @Inject(method = "tick", at = @At("HEAD"))
    private void magnetRangeMode(CallbackInfo callbackInfo) {
        // well this sure is a nice trick, thanks u/tom_the_geek
        // https://www.reddit.com/r/fabricmc/comments/nw3rs8/comment/h17daen
        ServerPlayer player = (ServerPlayer) (Object) this;

        MagnetCommandConfig config = MagnetCommandMod.CONFIG;
        if (config == null) return;
        if (!config.rangeEnabled) return;
        MagnetMode mode = getMagnetMode(player);
        if (mode != MagnetMode.Range) return;

        double range = config.range;

        // get items within range
        Vec3 playerPos = player.position();
        AABB box = new AABB(
                playerPos.x + range, playerPos.y + range, playerPos.z + range,
                playerPos.x - range, playerPos.y - range, playerPos.z - range);
        List<ItemEntity> items = player.level().getEntities(EntityType.ITEM, box, Magnet::TestItemEntity);

        items.forEach(item -> {
            if (config.moveMode == MoveMode.Pull) PullItem(playerPos, item, range);
            else if (config.moveMode == MoveMode.Teleport) TeleportItem(playerPos, item);
        });
    }
}
