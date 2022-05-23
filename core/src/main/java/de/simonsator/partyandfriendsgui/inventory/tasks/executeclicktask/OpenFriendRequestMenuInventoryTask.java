package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import static de.simonsator.partyandfriendsgui.api.PartyFriendsAPI.openFriendRequestsMenu;

/**
 * @author Simonsator
 * @version 1.0.0 on 12.07.16.
 */
public class OpenFriendRequestMenuInventoryTask extends InventoryTask {

	@Override
	public void execute(InventoryClickEvent pEvent) {
		Player player = (Player) pEvent.getWhoClicked();
		if (Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch"))
			player.closeInventory();
		player.updateInventory();
		openFriendRequestsMenu(player);

	}

	@Override
	public boolean isApplicable(InventoryClickEvent pEvent) {
		return AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().FRIEND_REQUEST_ITEM, true);
	}
}
