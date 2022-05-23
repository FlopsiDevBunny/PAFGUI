package de.simonsator.partyandfriendsgui.api.menu;

import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.InventoryTask;

public interface MainMenuClickProcessor {
	void registerTask(InventoryTask pTask);
}
