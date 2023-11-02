package dev.keesmand.magnetcommand.mixin;

import dev.keesmand.magnetcommand.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistantData;

    @Override
    public NbtCompound getPersistantData() {
        if (persistantData == null) persistantData = new NbtCompound();
        return persistantData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable ci) {
        if (persistantData == null) return;
        nbt.put("magnet", persistantData);
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo ci) {
        if (!nbt.contains("magnet", NbtElement.COMPOUND_TYPE)) return;
        persistantData = nbt.getCompound("magnet");
    }
}
