package de.simonsator.partyandfriendsgui.listener;

import de.simonsator.partyandfriendsgui.manager.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHandItem implements Listener {
	private final ItemsManager ITEMS_MANAGER;

	public PlayerSwapHandItem(ItemsManager pItemManager) {
		ITEMS_MANAGER = pItemManager;
	}


	@EventHandler
	public void onSwitch(PlayerSwapHandItemsEvent pEvent) {
		if (pEvent.getMainHandItem() != null)
			if (pEvent.getOffHandItem().equals(ItemManager.getInstance().HIDE_ITEM)
					|| ITEMS_MANAGER.isFriendHead(pEvent.getOffHandItem()))
				pEvent.setCancelled(true);
	}

}
