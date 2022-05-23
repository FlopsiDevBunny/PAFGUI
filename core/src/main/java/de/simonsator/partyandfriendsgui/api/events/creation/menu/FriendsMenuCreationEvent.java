package de.simonsator.partyandfriendsgui.api.events.creation.menu;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

/**
 * Please use the class MainMenuCreationEvent instead.
 *
 * @author simonbrungs
 * @version 1.0.0 03.12.16
 */
@Deprecated
public class FriendsMenuCreationEvent extends MenuCreationEvent {
	private static final HandlerList handlers = new HandlerList();
	private final JsonObject INFORMATION;

	public FriendsMenuCreationEvent(Player pPlayer, Inventory pInventory, JsonObject pInformation) {
		super(pInventory, pPlayer);
		INFORMATION = pInformation;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public JsonObject getInformation() {
		return INFORMATION;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
