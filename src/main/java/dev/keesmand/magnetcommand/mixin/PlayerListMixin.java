package dev.keesmand.magnetcommand.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.keesmand.magnetcommand.enums.MagnetMode;
import dev.keesmand.magnetcommand.util.MagnetModeData;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(
            method = "respawn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V")
    )
    void handlePlayerRespawn(ServerPlayer oldPlayer, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir, @Local(name = "player") ServerPlayer newPlayer) {
        MagnetMode mode = MagnetModeData.getMagnetMode(oldPlayer);
        MagnetModeData.setMagnetMode(newPlayer, mode);
    }
}
