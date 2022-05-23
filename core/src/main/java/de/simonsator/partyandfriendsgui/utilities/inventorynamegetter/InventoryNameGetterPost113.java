package de.simonsator.partyandfriendsgui.utilities.inventorynamegetter;

import org.bukkit.event.inventory.InventoryEvent;

public class InventoryNameGetterPost113 extends InventoryNameGetter {
	@Override
	public String getName(InventoryEvent pEvent) {
		try {
			return pEvent.getView().getTitle();
		} catch (IllegalStateException ignored) {
			return "";
		}
	}
}
