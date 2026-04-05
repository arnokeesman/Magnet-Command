package dev.keesmand.magnetcommand.util;

import dev.keesmand.magnetcommand.MagnetCommandMod;
import dev.keesmand.magnetcommand.enums.MagnetMode;
import eu.pb4.playerdata.api.PlayerDataApi;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class MagnetModeData {
    public static int setMagnetMode(ServerPlayer player, MagnetMode mode) {
        int modeInt = mode.ordinal();
        CompoundTag nbt = PlayerDataApi.getCustomDataFor(player, MagnetCommandMod.DATA_STORAGE);
        if (nbt == null) nbt = new CompoundTag();
        nbt.putInt("mode", modeInt);
        PlayerDataApi.setCustomDataFor(player, MagnetCommandMod.DATA_STORAGE, nbt);
        return modeInt;
    }

    public static MagnetMode getMagnetMode(ServerPlayer player) {
        CompoundTag nbt = PlayerDataApi.getCustomDataFor(player, MagnetCommandMod.DATA_STORAGE);
        if (nbt == null) return MagnetMode.Off;

        int modeInt = nbt.getIntOr("mode", 0);
        return MagnetMode.values()[modeInt];
    }
}
