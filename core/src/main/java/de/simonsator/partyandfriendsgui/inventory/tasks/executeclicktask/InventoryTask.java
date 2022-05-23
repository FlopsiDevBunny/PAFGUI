package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Simonsator
 * @version 1.0.0 on 12.07.16.
 */
public abstract class InventoryTask {
	public abstract void execute(InventoryClickEvent pEvent);

	public abstract boolean isApplicable(InventoryClickEvent pEvent);
}
