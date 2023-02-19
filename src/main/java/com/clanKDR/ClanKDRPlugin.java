package com.clanKDR;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.clan.ClanMember;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
		name = "Clan KDR"
)
public class ClanKDRPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private ClanKDRConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ClanKDROverlay overlay;
	@Getter(AccessLevel.PACKAGE)
	private int clanKills = 0, clanDeaths = 0, clanKillsTotalValue = 0, clanDeathsTotalValue = 0;

	@Provides
	ClanKDRConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ClanKDRConfig.class);
	}

	@Override
	protected void startUp() {
		this.overlayManager.add(this.overlay);
	}

	@Override
	protected void shutDown() {
		this.overlayManager.remove(this.overlay);
		resetKDR();
	}

	private void resetKDR() {
		clanKills = 0;
		clanDeaths = 0;
		clanKillsTotalValue = 0;
		clanDeathsTotalValue = 0;
	}

	@Subscribe
	public void onChatMessage(ChatMessage event) {
		if (event.getType() != ChatMessageType.CLAN_MESSAGE) {
			return;
		}

		if (event.getMessage().contains("has defeated")) {
			if(config.excludeFriendlyFire()){
				String playerKilled = StringUtils.substringBetween(event.getMessage(), "has defeated ", " and received");
				ClanSettings clanSettings = this.client.getClanSettings();
				for (ClanMember clanMember : clanSettings.getMembers()) {
					if(Text.toJagexName(clanMember.getName()).equalsIgnoreCase(Text.toJagexName(playerKilled))){
						return;
					}
				}
			}
			String killValue = StringUtils.substringBetween(event.getMessage(), "(", ")");
			if (config.excludeLowLoot()){
				if(killValue == null){
					return;
				}
				int value = getKillValue(killValue);
				if(value <= config.minimumValue()){
					return;
				}
			}
			clanKills++;
			if (killValue != null) {
				clanKillsTotalValue += getKillValue(killValue);
			}
		}

		if (event.getMessage().contains("has been defeated")) {
			if(config.excludeFriendlyFire()){
				String playerKiller = StringUtils.substringBetween(event.getMessage(), "has been defeated by ", " in The Wilderness");
				ClanSettings clanSettings = this.client.getClanSettings();
				for (ClanMember clanMember : clanSettings.getMembers()) {
					if(Text.toJagexName(clanMember.getName()).equalsIgnoreCase(Text.toJagexName(playerKiller))){
						return;
					}
				}
			}
			String deathValue = StringUtils.substringBetween(event.getMessage(), "(", ")");
			if (deathValue != null) {
				clanDeathsTotalValue += getKillValue(deathValue);
				clanDeaths++;
			} else {
				if(!config.excludeZeroValueDeaths()){
					clanDeaths++;
				}
			}
		}
	}

	private int getKillValue(String killMsg){
		return Integer.parseInt(killMsg.replace(",", "").replace("coins", "").replace(" ", ""));
	}
}
