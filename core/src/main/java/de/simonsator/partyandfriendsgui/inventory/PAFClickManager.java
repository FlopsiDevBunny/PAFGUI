package de.simonsator.partyandfriendsgui.inventory;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.inventory.ownitems.MainMenuBarOwnItems;
import de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment.*;
import de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment.party.PartyDetailViewMenu;
import de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment.party.PartyMenu;
import de.simonsator.partyandfriendsgui.listener.ItemsManager;
import de.simonsator.partyandfriendsgui.utilities.inventorynamegetter.InventoryNameGetter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.ArrayList;


/**
 * The OnClick listener
 *
 * @author Simonsator
 * @version 1.0.0
 */
public class PAFClickManager implements Listener {
	private static PAFClickManager instance;
	private final ArrayList<InventoryAssignmentTask> tasks = new ArrayList<>();

	public PAFClickManager(ItemsManager pItemsManager, String pOnlineSuffix, String pOfflineSuffix) {
		instance = this;
		tasks.add(new FriendRequestAcceptMenu());
		tasks.add(new FriendsMenu(pOnlineSuffix, pOfflineSuffix));
		if (Main.getInstance().getConfig().getBoolean("Inventories.PartyGUI.Enabled")) {
			tasks.add(new PartyMenu());
			tasks.add(new PartyDetailViewMenu());
		}
		tasks.add(new HideMenu());
		tasks.add(new FriendRequestMenu());
		tasks.add(new SettingsMenu());
		tasks.add(new EveryMenuTasks(pItemsManager));
		ConfirmFriendDeleteMenu confirmFriendDeleteMenu = null;
		if (Main.getInstance().getConfig().getBoolean("Inventories.ConfirmFriendDeleteMenu.Use")) {
			confirmFriendDeleteMenu = new ConfirmFriendDeleteMenu();
		}
		tasks.add(new OnlinePlayerDetailViewMenu(pOnlineSuffix, confirmFriendDeleteMenu));
		tasks.add(new OfflinePlayerDetailViewMenu(pOfflineSuffix, confirmFriendDeleteMenu));
		tasks.add(new EveryMenuTasks(pItemsManager));
		new MainMenuBarOwnItems();
	}

	public static PAFClickManager getInstance() {
		return instance;
	}

	@EventHandler
	public void executeTask(InventoryClickEvent pEvent) {
		for (InventoryAssignmentTask task : tasks) {
			if (task.isApplicable(pEvent)) {
				task.cancel(pEvent);
				if (pEvent.getCurrentItem() != null && pEvent.getCurrentItem().hasItemMeta() &&
						pEvent.getCurrentItem().getItemMeta().hasDisplayName()) {
					task.executeTask(pEvent);
					if (pEvent.isShiftClick()) {
						((Player) pEvent.getWhoClicked()).updateInventory();
					}
				}
			}
		}
	}

	@EventHandler
	public void onMove(InventoryDragEvent pEvent) {
		for (InventoryAssignmentTask task : tasks) {
			if (task.isApplicable(InventoryNameGetter.getInstance().getName(pEvent))) {
				task.cancel(pEvent);
			}
		}
	}

	public void registerTask(InventoryAssignmentTask pTask) {
		tasks.add(pTask);
	}

	public InventoryAssignmentTask getTask(Class<? extends InventoryAssignmentTask> pClass) {
		for (InventoryAssignmentTask task : tasks)
			if (task.getClass() == pClass)
				return task;
		return null;
	}
}
