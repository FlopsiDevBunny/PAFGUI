package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.inventory.PAFClickManager;
import de.simonsator.partyandfriendsgui.listener.InformAboutInitError;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import de.simonsator.partyandfriendsgui.utilities.OwnExecuteCommandContainer;
import de.simonsator.partyandfriendsgui.utilities.inventorynamegetter.InventoryNameGetter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

import static de.simonsator.partyandfriendsgui.api.PartyFriendsAPI.deleteFriend;

public class ConfirmFriendDeleteMenu extends OwnExecuteCommandTask {
	private final static String INVENTORY_NAME_ERROR_MESSAGE = "§cThe config value set under \"Inventories.ConfirmFriendDeleteMenu.Name\" is not valid. It has to contain the text %PLAYER_NAME%";
	private final int INVENTORY_SIZE = Main.getInstance().getConfig().getInt("Inventories.ConfirmFriendDeleteMenu.Size");
	private final String INVENTORY_NAME_START;
	private final String INVENTORY_NAME_END;
	private final List<OwnExecuteCommandContainer> ownExecuteCommandContainerListCreate;

	public ConfirmFriendDeleteMenu() {
		super("ConfirmFriendDeleteMenu", "Inventories.ConfirmFriendDeleteMenu.Own");
		String[] tempInvName = LanguageManager.getInstance().getText(TextIdentifier.CONFIRM_FRIEND_DELETE_MENU_NAME).split("%PLAYER_NAME%", 2);
		if (tempInvName.length == 2) {
			INVENTORY_NAME_START = tempInvName[0];
			INVENTORY_NAME_END = tempInvName[1];
			PAFClickManager.getInstance().registerTask(this);
		} else {
			INVENTORY_NAME_START = null;
			INVENTORY_NAME_END = null;
			new InformAboutInitError(INVENTORY_NAME_ERROR_MESSAGE);
		}
		ownExecuteCommandContainerListCreate = createExecuteCommandContainerList(Main.getInstance().getConfig(), "Inventories.ConfirmFriendDeleteMenu.Own");
	}

	@Override
	public void executeTask(InventoryClickEvent pEvent) {
		pEvent.setCancelled(true);
		Player player = (Player) pEvent.getWhoClicked();
		String playerName = getPlayerName(pEvent);
		if (AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_DELETE_CONFIRM_ACCEPT_ITEM, pEvent.getCurrentItem(), false)) {
			deleteFriend(player, playerName);
			pEvent.getWhoClicked().closeInventory();
		}
		if (AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_DELETE_CONFIRM_DECLINE_ITEM, pEvent.getCurrentItem(), false))
			pEvent.getWhoClicked().closeInventory();
		checkForOwnExecuteCommand(pEvent, playerName, 0);
	}

	private String getPlayerName(InventoryClickEvent pEvent) {
		return InventoryNameGetter.getInstance().getName(pEvent).replaceFirst(INVENTORY_NAME_START, "").replaceFirst(INVENTORY_NAME_END, "");
	}

	@Override
	public boolean isApplicable(String pName) {
		return pName.startsWith(INVENTORY_NAME_START) && pName.endsWith(INVENTORY_NAME_END);
	}

	@Override
	public boolean isApplicable(InventoryClickEvent pEvent) {
		return super.isApplicable(pEvent) && pEvent.getInventory().getSize() == INVENTORY_SIZE &&
				AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_DELETE_CONFIRM_ACCEPT_ITEM, pEvent.getInventory().getItem(Main.getInstance().getConfig().getInt("Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmAccept.Place")), false);
	}

	@Override
	public boolean conditionForAdding(Configuration pConfig, String pKey) {
		return true;
	}

	public void openMenu(Player pPlayer, String pPlayerName) {
		if (INVENTORY_NAME_START == null) {
			pPlayer.sendMessage(INVENTORY_NAME_ERROR_MESSAGE);
			return;
		}
		Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE, INVENTORY_NAME_START + pPlayerName + INVENTORY_NAME_END);
		inv.setItem(Main.getInstance().getConfig().getInt("Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmAccept.Place"), ItemManager.getInstance().FRIEND_DELETE_CONFIRM_ACCEPT_ITEM);
		inv.setItem(Main.getInstance().getConfig().getInt("Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmDecline.Place"), ItemManager.getInstance().FRIEND_DELETE_CONFIRM_DECLINE_ITEM);
		for (OwnExecuteCommandContainer ownExecuteCommandContainer : ownExecuteCommandContainerListCreate)
			inv.setItem(ownExecuteCommandContainer.PLACE, ownExecuteCommandContainer.ITEM);
		for (int i = 0; i < inv.getSize(); i++)
			if (inv.getItem(i) == null)
				inv.setItem(i, ItemManager.getInstance().FRIEND_DELETE_CONFIRM_MENU_PLACEHOLDER);
		pPlayer.openInventory(inv);
	}

}
