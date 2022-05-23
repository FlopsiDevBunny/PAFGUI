package de.simonsator.partyandfriendsgui.api.events.creation.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class PartyDetailViewCreationEvent extends MenuCreationEvent {
	private static final HandlerList handlers = new HandlerList();

	public PartyDetailViewCreationEvent(Inventory inv, Player player) {
		super(inv, player);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
