package de.simonsator.partyandfriendsgui.api.menu;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.MenuBarCreationEvent;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.LinkedList;

public interface MainMenu {
	default void createMenuBar(Player pPlayer, Inventory pInventory, boolean pHasPreviousPage, boolean pHasNextPage) {
		createMenuBar(pPlayer, pInventory);
		int invSize = pInventory.getSize();
		if (pHasPreviousPage)
			pInventory.setItem(invSize - Main.getInstance().getConfig().getInt("Inventories.MainMenuMenuBar.PreviousPageButton.Place"),
					ItemManager.getInstance().PREVIOUS_PAGE_ITEM);
		if (pHasNextPage)
			pInventory.setItem(invSize - Main.getInstance().getConfig().getInt("Inventories.MainMenuMenuBar.NextPageButton.Place"),
					ItemManager.getInstance().NEXT_PAGE_ITEM);
	}

	default void createMenuBar(Player pPlayer, Inventory pInventory) {
		int invSize = pInventory.getSize();
		MenuBarCreationEvent event = new MenuBarCreationEvent(new LinkedList<>(), this, pPlayer, pInventory);
		Bukkit.getServer().getPluginManager().callEvent(event);
		for (MenuBarItem menuBarItem : event.getMenuBarItems())
			pInventory.setItem(invSize - menuBarItem.getPlace(), menuBarItem.getItem());
		for (int i = invSize - 9; i < invSize; i++)
			if (pInventory.getItem(i) == null)
				pInventory.setItem(i, ItemManager.getInstance().MENU_BAR_PLACEHOLDER_ITEM);
	}

	void openMenu(Player pPlayer);
}
