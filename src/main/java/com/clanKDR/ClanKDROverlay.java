package com.clanKDR;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.text.DecimalFormat;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

public class ClanKDROverlay extends OverlayPanel {
	private final ClanKDRPlugin plugin;

	private final ClanKDRConfig config;
	@Inject
	private Client client;

	private final DecimalFormat decFormat = new DecimalFormat("###,###");

	@Inject
	private ClanKDROverlay(ClanKDRPlugin plugin, ClanKDRConfig config) {
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.BOTTOM_RIGHT);
		setPriority(OverlayPriority.LOW);
		panelComponent.setWrap(true);
		panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);
		getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Clan KDR overlay"));
	}

	@Override
	public Dimension render(Graphics2D graphics) {

		if (client.getClanChannel() != null) {

			panelComponent.getChildren().add(TitleComponent.builder().text(client.getClanChannel().getName()).build());
			panelComponent.getChildren().add(TitleComponent.builder().text("Kills: " + plugin.getClanKills() + " | "
					+ "Deaths: " + plugin.getClanDeaths()).build());
			if (config.showValue()) {
				panelComponent.getChildren().add(TitleComponent.builder().text("Kills Value: " + decFormat.format(plugin.getClanKillsTotalValue())).build());
				panelComponent.getChildren().add(TitleComponent.builder().text("Deaths Value: " + decFormat.format(plugin.getClanDeathsTotalValue())).build());
			}
		}
		return super.render(graphics);
	}
}
