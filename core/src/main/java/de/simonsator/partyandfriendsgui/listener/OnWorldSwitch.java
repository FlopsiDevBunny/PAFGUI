package de.simonsator.partyandfriendsgui.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class OnWorldSwitch implements Listener {
	private final ItemsManager ITEMS_MANAGER;

	public OnWorldSwitch(ItemsManager pItemsManager) {
		ITEMS_MANAGER = pItemsManager;
	}

	@EventHandler
	public void onWorldSwitch(PlayerChangedWorldEvent pEvent) {
		ITEMS_MANAGER.setItems(pEvent.getPlayer(), ITEMS_MANAGER.getHead(pEvent.getPlayer()));
	}
}
