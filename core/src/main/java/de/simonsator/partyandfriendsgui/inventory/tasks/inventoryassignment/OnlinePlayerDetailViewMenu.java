package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import static de.simonsator.partyandfriendsgui.api.PartyFriendsAPI.inviteIntoParty;
import static de.simonsator.partyandfriendsgui.api.PartyFriendsAPI.jumpTo;

/**
 * @author Simonsator
 * @version 1.0.0 on 07.08.16.
 */
public class OnlinePlayerDetailViewMenu extends PlayerDetailViewMenu {
	private final String ONLINE_SUFFIX;

	public OnlinePlayerDetailViewMenu(String pOnlineSuffix, ConfirmFriendDeleteMenu confirmFriendDeleteMenu) {
		super("(On", confirmFriendDeleteMenu);
		ONLINE_SUFFIX = "(" + pOnlineSuffix + ")";
	}

	@Override
	protected void executeFurtherTasks(String pPlayerName, InventoryClickEvent pEvent) {
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().FRIEND_INVITE_PARTY, false))
			inviteIntoParty((Player) pEvent.getWhoClicked(), pPlayerName);
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().JUMP_TO_FRIEND, false))
			jumpTo((Player) pEvent.getWhoClicked(), pPlayerName);
	}

	@Override
	public boolean isApplicable(String pName) {
		return pName.startsWith(Main.getInstance().getColor(0)) && (pName.endsWith(ONLINE_SUFFIX) || pName.endsWith("(On)"));
	}

}