package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.utilities.inventorynamegetter.InventoryNameGetter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import static de.simonsator.partyandfriendsgui.api.PartyFriendsAPI.acceptFriendRequest;
import static de.simonsator.partyandfriendsgui.api.PartyFriendsAPI.declineFriendRequest;

/**
 * @author Simonsator
 * @version 1.0.0 on 07.08.16.
 */
public class FriendRequestAcceptMenu extends InventoryAssignmentTask {
	private final int INVENTORY_SIZE = 9;

	public FriendRequestAcceptMenu() {
		super("FriendRequestMenu");
	}


	@Override
	public void executeTask(InventoryClickEvent pEvent) {
		if (pEvent.getInventory().getSize() != INVENTORY_SIZE)
			return;
		Player player = (Player) pEvent.getWhoClicked();
		if (AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_REQUEST_ACCEPT_MENU_ACCEPT, pEvent.getCurrentItem(), false))
			acceptFriendRequest(player, getPlayerName(pEvent));
		if (AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_REQUEST_ACCEPT_MENU_DENY, pEvent.getCurrentItem(), false))
			declineFriendRequest(player, getPlayerName(pEvent));
		pEvent.setCancelled(true);
		if (!AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().FRIEND_ACCEPT_MENU_PLACEHOLDER, false) && !(AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().BACK_ITEM, false) && !Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch")))
			pEvent.getWhoClicked().closeInventory();
	}

	private String getPlayerName(InventoryClickEvent pEvent) {
		return InventoryNameGetter.getInstance().getName(pEvent).substring(2);
	}

	@Override
	public boolean isApplicable(String pName) {
		return pName.startsWith(Main.getInstance().getColor(4));
	}

	@Override
	public boolean isApplicable(InventoryClickEvent pEvent) {
		return super.isApplicable(pEvent) && pEvent.getInventory().getSize() == INVENTORY_SIZE && pEvent.getInventory().contains(ItemManager.getInstance().FRIEND_REQUEST_ACCEPT_MENU_ACCEPT.getType()) && pEvent.getInventory().contains(ItemManager.getInstance().FRIEND_REQUEST_ACCEPT_MENU_DENY.getType());
	}
}
