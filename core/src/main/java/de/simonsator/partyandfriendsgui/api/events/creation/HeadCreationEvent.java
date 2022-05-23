package de.simonsator.partyandfriendsgui.api.events.creation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.simonsator.partyandfriendsgui.api.menu.MainMenu;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * @author simonbrungs
 * @version 1.0.0 02.12.16
 */
public class HeadCreationEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final boolean IS_ONLINE;
	private final JsonObject INFORMATION;
	private final ItemStack HEAD;
	private final MainMenu MENU;

	public HeadCreationEvent(ItemStack pHead, boolean pIsOnline, JsonObject pInformation, MainMenu pMenu) {
		this.HEAD = pHead;
		IS_ONLINE = pIsOnline;
		INFORMATION = pInformation;
		MENU = pMenu;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public boolean isOnline() {
		return IS_ONLINE;
	}

	public ItemStack getHead() {
		return HEAD;
	}

	public JsonElement get(String memberName) {
		return INFORMATION.get(memberName);
	}

	public MainMenu getMenu() {
		return MENU;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
