package de.simonsator.partyandfriendsgui.api.events.creation.menu;

import de.simonsator.partyandfriendsgui.api.menu.MainMenu;
import de.simonsator.partyandfriendsgui.api.menu.MenuBarItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class MenuBarCreationEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final MainMenu MENU;
	private final Player PLAYER;
	private final List<MenuBarItem> menuBarItems;
	private final Inventory inv;

	public MenuBarCreationEvent(List<MenuBarItem> pDefaultItems, MainMenu menu, Player player, Inventory pInv) {
		menuBarItems = pDefaultItems;
		MENU = menu;
		PLAYER = player;
		inv = pInv;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public void addMenuBarItem(MenuBarItem pItem) {
		menuBarItems.add(pItem);
	}

	public MainMenu getMenu() {
		return MENU;
	}

	public Player getPlayer() {
		return PLAYER;
	}

	public Inventory getInventory() {
		return inv;
	}

	public List<MenuBarItem> getMenuBarItems() {
		return menuBarItems;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
