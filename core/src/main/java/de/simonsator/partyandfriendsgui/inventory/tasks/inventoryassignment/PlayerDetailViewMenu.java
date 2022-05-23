package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.utilities.inventorynamegetter.InventoryNameGetter;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.StringTokenizer;


/**
 * @author Simonsator
 * @version 1.0.0 on 07.08.16.
 */
public abstract class PlayerDetailViewMenu extends OwnExecuteCommandTask {
	private final String IDENTIFIER;
	private final ConfirmFriendDeleteMenu CONFIRM_FRIEND_DELETE_MENU;

	public PlayerDetailViewMenu(String pIdentifier, ConfirmFriendDeleteMenu pConfirmFriendDeleteMenu) {
		super("PlayerDetailViewMenu", "Inventories.FriendDetailView.Own");
		IDENTIFIER = pIdentifier;
		CONFIRM_FRIEND_DELETE_MENU = pConfirmFriendDeleteMenu;
	}

	@Override
	public void executeTask(InventoryClickEvent pEvent) {
		StringTokenizer st = new StringTokenizer(InventoryNameGetter.getInstance().getName(pEvent), " (");
		if (!st.hasMoreTokens())
			return;
		String playerNameWithColor = st.nextToken();
		String playerName = playerNameWithColor.substring(2, playerNameWithColor.length() - 2);
		executeFurtherTasks(playerName, pEvent);
		checkForOwnExecuteCommand(pEvent, playerName, 0);
		super.executeTask(pEvent);
		pEvent.setCancelled(true);
		if (!AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().FRIEND_DETAIL_VIEW_PLACEHOLDER, false) && !(AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().BACK_ITEM, false) && !Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch"))) {
			pEvent.getWhoClicked().closeInventory();
		}
		if (AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_DELETE, pEvent.getCurrentItem(), false)) {
			deleteFriend((Player) pEvent.getWhoClicked(), playerName);
		}
	}

	private void deleteFriend(Player pPlayer, String pPlayerName) {
		if (CONFIRM_FRIEND_DELETE_MENU == null)
			PartyFriendsAPI.deleteFriend(pPlayer, pPlayerName);
		else
			CONFIRM_FRIEND_DELETE_MENU.openMenu(pPlayer, pPlayerName);
	}

	protected abstract void executeFurtherTasks(String pPlayerName, InventoryClickEvent pEvent);

	@Override
	public boolean isApplicable(String pName) {
		return pName.contains(IDENTIFIER);
	}

	@Override
	public boolean conditionForAdding(Configuration pConfig, String pKey) {
		return (!pConfig.getBoolean(pKey + "OnlyOnline") || this instanceof OnlinePlayerDetailViewMenu);
	}
}
