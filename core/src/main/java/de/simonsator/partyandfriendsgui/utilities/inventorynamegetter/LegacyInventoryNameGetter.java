package de.simonsator.partyandfriendsgui.utilities.inventorynamegetter;

import org.bukkit.event.inventory.InventoryEvent;

public class LegacyInventoryNameGetter extends InventoryNameGetter {
	@Override
	public String getName(InventoryEvent pEvent) {
		return pEvent.getInventory().getName();
	}
}
