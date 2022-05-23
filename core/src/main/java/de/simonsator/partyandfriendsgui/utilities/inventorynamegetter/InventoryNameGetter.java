package de.simonsator.partyandfriendsgui.utilities.inventorynamegetter;

import org.bukkit.event.inventory.InventoryEvent;

public abstract class InventoryNameGetter {
	private static InventoryNameGetter instance;

	public InventoryNameGetter() {
		instance = this;
	}

	public static InventoryNameGetter getInstance() {
		return instance;
	}

	public abstract String getName(InventoryEvent pEvent);
}
