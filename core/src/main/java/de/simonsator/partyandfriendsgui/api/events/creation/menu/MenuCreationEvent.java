package de.simonsator.partyandfriendsgui.api.events.creation.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

/**
 * @author simonbrungs
 * @version 1.0.0 04.12.16
 */
public abstract class MenuCreationEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final Player PLAYER;
	private final Inventory INVENTORY;

	protected MenuCreationEvent(Inventory inv, Player player) {
		this.INVENTORY = inv;
		PLAYER = player;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getPlayer() {
		return PLAYER;
	}

	public Inventory getInventory() {
		return INVENTORY;
	}

}
