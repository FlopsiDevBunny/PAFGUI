package de.simonsator.partyandfriendsgui.inventory.ownitems;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.MenuBarCreationEvent;
import de.simonsator.partyandfriendsgui.api.menu.MainMenuClickProcessor;
import de.simonsator.partyandfriendsgui.api.menu.MenuBarItem;
import de.simonsator.partyandfriendsgui.api.menu.MenuManager;
import de.simonsator.partyandfriendsgui.inventory.PAFClickManager;
import de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment.InventoryAssignmentTask;
import de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment.OwnExecuteCommandTask;
import de.simonsator.partyandfriendsgui.utilities.OwnExecuteCommandContainer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class MainMenuBarOwnItems extends OwnExecuteCommandTask implements Listener {
	private final List<OwnExecuteCommandContainer> ownExecuteCommandContainerList = createExecuteCommandContainerList(Main.getInstance().getConfig(), "Inventories.MainMenuMenuBar.Own");

	public MainMenuBarOwnItems() {
		super("MainMenuBarOwnItems", "Inventories.MainMenuMenuBar.Own", Main.getInstance().getConfig());
		PAFClickManager.getInstance().registerTask(this);
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}

	@Override
	public void executeTask(InventoryClickEvent pEvent) {
		checkForOwnExecuteCommand(pEvent, null, pEvent.getInventory().getSize() - 9);
	}

	@Override
	public boolean isApplicable(String pName) {
		for (MainMenuClickProcessor processor : MenuManager.getInstance().getMainMenuClickProcessors())
			if (processor instanceof InventoryAssignmentTask)
				if (((InventoryAssignmentTask) processor).isApplicable(pName))
					return true;
		return false;
	}

	@EventHandler
	public void onMenuBarCreation(MenuBarCreationEvent pEvent) {
		for (OwnExecuteCommandContainer ownExecuteCommandContainerItem : ownExecuteCommandContainerList) {
			pEvent.addMenuBarItem(new MenuBarItem(9 - ownExecuteCommandContainerItem.PLACE, ownExecuteCommandContainerItem.ITEM));
		}
	}

	@Override
	public boolean conditionForAdding(Configuration pConfig, String pKey) {
		return true;
	}
}
