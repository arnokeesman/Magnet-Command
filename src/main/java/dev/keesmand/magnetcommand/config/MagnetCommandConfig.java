package dev.keesmand.magnetcommand.config;

import dev.keesmand.magnetcommand.enums.DropMode;
import dev.keesmand.magnetcommand.enums.MoveMode;

public class MagnetCommandConfig {
	public final int permissionLevel;
	public final boolean rangeEnabled;
	public final int range;
	public final MoveMode moveMode;
	public final boolean onBreakEnabled;
	public final DropMode dropLocation;

	public MagnetCommandConfig(int permissionLevel, boolean rangeEnabled, int range, MoveMode moveMode, boolean onBreakEnabled, DropMode dropLocation) {
		this.permissionLevel = permissionLevel;
		this.rangeEnabled = rangeEnabled;
		this.range = range;
		this.moveMode = moveMode;
		this.onBreakEnabled = onBreakEnabled;
		this.dropLocation = dropLocation;
	}
}
