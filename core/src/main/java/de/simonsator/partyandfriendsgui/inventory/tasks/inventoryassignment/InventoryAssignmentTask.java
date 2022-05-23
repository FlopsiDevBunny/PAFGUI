package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment;

import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.InventoryTask;
import de.simonsator.partyandfriendsgui.utilities.inventorynamegetter.InventoryNameGetter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.util.ArrayList;

/**
 * @author Simonsator
 * @version 1.0.0 on 12.07.16.
 */
public abstract class InventoryAssignmentTask {
	protected final ArrayList<InventoryTask> tasks = new ArrayList<>();
	private final String NAME;

	public InventoryAssignmentTask(String pName) {
		NAME = pName;
	}

	public void executeTask(InventoryClickEvent pEvent) {
		for (InventoryTask task : tasks)
			if (task.isApplicable(pEvent)) {
				task.execute(pEvent);
				break;
			}
	}

	public void cancel(InventoryInteractEvent pEvent) {
		pEvent.setCancelled(true);
	}

	public boolean isApplicable(String pName) {
		return NAME.equals(pName);
	}

	public boolean isApplicable(InventoryClickEvent pEvent) {
		return isApplicable(InventoryNameGetter.getInstance().getName(pEvent));
	}

	public void addTask(InventoryTask pTask) {
		tasks.add(pTask);
	}
}
