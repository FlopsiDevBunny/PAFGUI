package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask;

import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LeavePartyClickTask extends InventoryTask {
	@Override
	public void execute(InventoryClickEvent pEvent) {
		PartyFriendsAPI.leaveParty((Player) pEvent.getWhoClicked());
	}

	@Override
	public boolean isApplicable(InventoryClickEvent pEvent) {
		return AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().LEAVE_PARTY_ITEM, false);
	}
}
