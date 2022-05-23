package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OpenPartyMenuClickTask extends InventoryTask {

	@Override
	public void execute(InventoryClickEvent pEvent) {
		if (Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch"))
			pEvent.getWhoClicked().closeInventory();
		PartyFriendsAPI.openPartyInventory((Player) pEvent.getWhoClicked(), 0);
	}

	@Override
	public boolean isApplicable(InventoryClickEvent pEvent) {
		return AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().OPEN_PARTY_GUI_ITEM, true);
	}
}
