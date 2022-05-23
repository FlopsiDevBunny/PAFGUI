package de.simonsator.partyandfriendsgui.api.events.creation.menu;

import com.google.gson.JsonObject;
import de.simonsator.datastructures.ItemPackage;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

import java.util.Stack;

/**
 * @author simonbrungs
 * @version 1.0.0 20.12.16
 */
public class SettingsMenuCreationEvent extends MenuCreationEvent {
	private static final HandlerList handlers = new HandlerList();
	private final JsonObject DATA;
	private final Stack<ItemPackage> itemPackages;

	public SettingsMenuCreationEvent(Inventory inv, Player player, Stack<ItemPackage> pItemPackages, JsonObject pData) {
		super(inv, player);
		DATA = pData;
		itemPackages = pItemPackages;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public void addItemPackage(ItemPackage pItemPackage) {
		itemPackages.push(pItemPackage);
	}

	public JsonObject getData() {
		return DATA;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
