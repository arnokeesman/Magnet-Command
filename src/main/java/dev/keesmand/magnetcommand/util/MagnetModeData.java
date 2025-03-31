package dev.keesmand.magnetcommand.util;

import dev.keesmand.magnetcommand.enums.MagnetMode;
import net.minecraft.nbt.NbtCompound;

public class MagnetModeData {
    public static int setMagnetMode(IEntityDataSaver player, MagnetMode mode) {
        NbtCompound nbt = player.getPersistantData();
        int modeInt = mode.ordinal();
        nbt.putInt("mode", modeInt);
        return modeInt;
    }

    public static MagnetMode getMagnetMode(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistantData();
        int modeInt = nbt.getInt("mode", MagnetMode.Off.ordinal());
        MagnetMode mode = MagnetMode.values()[modeInt];
        return mode;
    }
}
