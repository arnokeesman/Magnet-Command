package dev.keesmand.magnetcommand.mixin;

import dev.keesmand.magnetcommand.enums.MagnetMode;
import dev.keesmand.magnetcommand.util.IEntityDataSaver;
import dev.keesmand.magnetcommand.util.MagnetModeData;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Inject(
			method = "respawnPlayer",
			at = @At(value = "INVOKE", target="Lnet/minecraft/server/network/ServerPlayerEntity;copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	void handlePlayerRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir, BlockPos blockPos, float f, boolean bl, ServerWorld serverWorld, Optional optional, ServerWorld serverWorld2, ServerPlayerEntity newPlayer) {
		MagnetMode mode = MagnetModeData.getMagnetMode((IEntityDataSaver) oldPlayer);
		MagnetModeData.setMagnetMode((IEntityDataSaver) newPlayer, mode);
	}
}
