package de.simonsator.partyandfriendsgui.api.events.creation.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

/**
 * @author simonbrungs
 * @version 1.0.0 18.12.16
 */
public class FriendDetailViewCreationEvent extends MenuCreationEvent {
	private static final HandlerList handlers = new HandlerList();
	private final boolean IS_ONLINE;

	public FriendDetailViewCreationEvent(Inventory inv, Player player, boolean pIsOnline) {
		super(inv, player);
		IS_ONLINE = pIsOnline;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public boolean isOnline() {
		return IS_ONLINE;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
