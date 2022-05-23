package de.simonsator.partyandfriendsgui.api.menu;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class MenuBarItem extends Event {
	private static HandlerList handlers;
	private final int PLACE;
	private final ItemStack ITEM;

	/**
	 * @param pPlace The place calculated from behind
	 * @param pItem  The item to set
	 */
	public MenuBarItem(int pPlace, ItemStack pItem) {
		PLACE = pPlace;
		ITEM = pItem;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public int getPlace() {
		return PLACE;
	}

	public ItemStack getItem() {
		return ITEM;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
