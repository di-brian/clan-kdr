package com.clanKDR;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

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
}
