package de.simonsator.partyandfriendsgui.api.menu;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.InventoryTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuManager implements Listener {
	private static MenuManager instance;
	private final Map<Player, MainMenu> lastOpened = new HashMap<>();
	private final List<MainMenu> mainMenus = new ArrayList<>();
	private final List<MainMenuClickProcessor> mainMenuClickProcessors = new ArrayList<>();
	private final List<InventoryTask> defaultInventoryTasks = new ArrayList<>();
	private final boolean alwaysOpenFriendMenu = Main.getInstance().getConfig().getBoolean("Inventories.ToolBar.ProfileItem.AlwaysOpenFriendMenu");

	public MenuManager() {
		instance = this;
	}

	public static MenuManager getInstance() {
		return instance;
	}

	public void openMenuFor(Player pPlayer) {
		MainMenu lastOpenedMenu = getLastOpenedMenu(pPlayer);
		if (lastOpenedMenu == null)
			PartyFriendsAPI.openMainInventory(pPlayer, 0);
		else lastOpenedMenu.openMenu(pPlayer);
	}

	public void setLastOpened(Player pPlayer, MainMenu pMenu) {
		if (!alwaysOpenFriendMenu)
			lastOpened.put(pPlayer, pMenu);
	}

	@EventHandler
	public void removeFromList(PlayerQuitEvent pEvent) {
		lastOpened.remove(pEvent.getPlayer());
	}

	public void registerInventoryTask(InventoryTask pTask) {
		for (MainMenuClickProcessor menuClickProcessor : mainMenuClickProcessors)
			menuClickProcessor.registerTask(pTask);
		defaultInventoryTasks.add(pTask);
	}

	public void registerInventoryTask(InventoryTask pTask, Class<? extends MainMenuClickProcessor> pMenuClass) {
		for (MainMenuClickProcessor menuClickProcessor : mainMenuClickProcessors)
			if (menuClickProcessor.getClass().equals(pMenuClass))
				menuClickProcessor.registerTask(pTask);
	}

	public void registerMenu(MainMenu pMenu) {
		mainMenus.add(pMenu);
	}

	public void registerMenuClickProcessor(MainMenuClickProcessor pMenu) {
		mainMenuClickProcessors.add(pMenu);
		for (InventoryTask task : defaultInventoryTasks)
			pMenu.registerTask(task);
	}

	public List<MainMenuClickProcessor> getMainMenuClickProcessors() {
		return mainMenuClickProcessors;
	}

	public MainMenu getLastOpenedMenu(Player pPlayer) {
		return lastOpened.get(pPlayer);
	}

	public void resetLastOpenedMenu(Player pPlayer) {
		lastOpened.remove(pPlayer);
	}
}
