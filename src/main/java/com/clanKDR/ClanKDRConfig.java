package com.clanKDR;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("clanKDR")
public interface ClanKDRConfig extends Config {
	@ConfigItem(
			keyName = "showValue",
			name = "Show value",
			description = "Configures if running total value should be displayed",
			position = 1
	)
	default boolean showValue() {
		return false;
	}

	@ConfigItem(
			keyName = "excludeLowLoot",
			name = "Exclude kills below value",
			description = "Configures if kills below certain value shouldn't be counted",
			position = 2
	)
	default boolean excludeLowLoot() {
		return false;
	}

	@Range(
			min = 1
	)
	@ConfigItem(
			keyName = "lootThreshold",
			name = "Minimum kill value",
			description = "Lowest value to count as a kill",
			position = 3
	)
	default int minimumValue()
	{
		return 1;
	}
}
