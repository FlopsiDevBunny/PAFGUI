package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import static de.simonsator.partyandfriendsgui.api.PartyFriendsAPI.openSettingsInventory;

/**
 * @author Simonsator
 * @version 1.0.0 on 12.07.16.
 */
public class OpenSettingsInventory extends InventoryTask {
	@Override
	public void execute(InventoryClickEvent pEvent) {
		if (Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch"))
			pEvent.getWhoClicked().closeInventory();
		openSettingsInventory((Player) pEvent.getWhoClicked());
	}

	@Override
	public boolean isApplicable(InventoryClickEvent pEvent) {
		return AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().SETTINGS_ITEM, true);
	}
}
