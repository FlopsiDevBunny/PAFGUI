package de.simonsator.partyandfriendsgui.api.events.creation.menu;

import com.google.gson.JsonObject;
import de.simonsator.partyandfriendsgui.api.menu.MainMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MainMenuCreationEvent extends FriendsMenuCreationEvent {
	private final Class<? extends MainMenu> CALLER;

	public MainMenuCreationEvent(Player pPlayer, Inventory pInventory, JsonObject pInformation, Class<? extends MainMenu> pCaller) {
		super(pPlayer, pInventory, pInformation);
		CALLER = pCaller;
	}

	public Class<? extends MainMenu> getCaller() {
		return CALLER;
	}
}
