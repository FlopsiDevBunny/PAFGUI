package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.api.menu.MenuManager;
import de.simonsator.partyandfriendsgui.listener.ItemsManager;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

/**
 * @author Simonsator
 * @version 1.0.0 on 07.08.16.
 */
public class EveryMenuTasks extends InventoryAssignmentTask {
	private final ItemsManager ITEMS_MANAGER;

	public EveryMenuTasks(ItemsManager pItemsManager) {
		super("EveryMenuTasks");
		ITEMS_MANAGER = pItemsManager;
	}

	@Override
	public void executeTask(InventoryClickEvent pEvent) {
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().BACK_ITEM, false)) {
			pEvent.setCancelled(true);
			if (Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch"))
				pEvent.getWhoClicked().closeInventory();
			MenuManager.getInstance().openMenuFor((Player) pEvent.getWhoClicked());
		}
		if (ItemsManager.isHideItem(pEvent.getCurrentItem())
				|| ITEMS_MANAGER.isFriendHead(pEvent.getCurrentItem())) {
			pEvent.setCancelled(true);
			((Player) pEvent.getWhoClicked()).updateInventory();
		}
	}

	@Override
	public boolean isApplicable(String pName) {
		return true;
	}

	@Override
	public void cancel(InventoryInteractEvent pEvent) {

	}
}
