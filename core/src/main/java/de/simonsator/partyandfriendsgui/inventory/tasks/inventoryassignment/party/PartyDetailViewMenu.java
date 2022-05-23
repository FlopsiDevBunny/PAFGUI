package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment.party;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment.OwnExecuteCommandTask;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import de.simonsator.partyandfriendsgui.utilities.inventorynamegetter.InventoryNameGetter;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import static de.simonsator.partyandfriendsgui.api.PartyFriendsAPI.kickPlayer;
import static de.simonsator.partyandfriendsgui.api.PartyFriendsAPI.makeLeader;

public class PartyDetailViewMenu extends OwnExecuteCommandTask {
	private final String IDENTIFIER_LONG;
	private final String IDENTIFIER_SHORT;

	public PartyDetailViewMenu() {
		super("PlayerDetailViewMenu", "Inventories.PartyGUI.PartyMemberDetailView.Own");
		String identifierTemp1 = LanguageManager.getInstance().getText(TextIdentifier.PARTY_MEMBER_DETAIL_VIEW_PREFIX) + Main.getInstance().getColor(5);
		String identifierTemp2 = LanguageManager.getInstance().getText(TextIdentifier.PARTY_MEMBER_DETAIL_VIEW_SHORT_PREFIX) + Main.getInstance().getColor(5);
		if (identifierTemp1.length() >= identifierTemp2.length()) {
			IDENTIFIER_LONG = identifierTemp1;
			IDENTIFIER_SHORT = identifierTemp2;
		} else {
			IDENTIFIER_LONG = identifierTemp2;
			IDENTIFIER_SHORT = identifierTemp1;
		}
	}

	@Override
	public void executeTask(InventoryClickEvent pEvent) {
		super.executeTask(pEvent);
		pEvent.setCancelled(true);
		String inventoryName = InventoryNameGetter.getInstance().getName(pEvent);
		String playerName;
		if (inventoryName.startsWith(IDENTIFIER_LONG))
			playerName = inventoryName.replace(IDENTIFIER_LONG, "");
		else
			playerName = inventoryName.replace(IDENTIFIER_SHORT, "");
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().KICK_FROM_PARTY_ITEM, false))
			kickPlayer((Player) pEvent.getWhoClicked(), playerName);
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().MAKE_NEW_PARTY_LEADER_ITEM, false))
			makeLeader((Player) pEvent.getWhoClicked(), playerName);
		checkForOwnExecuteCommand(pEvent, playerName, 0);
		if (!AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().PARTY_DETAIL_VIEW_PLACEHOLDER, false) && !(AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().BACK_ITEM, false) && !Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch"))) {
			pEvent.getWhoClicked().closeInventory();
		}
	}

	@Override
	public boolean isApplicable(String pName) {
		return pName.startsWith(IDENTIFIER_LONG) || pName.startsWith(IDENTIFIER_SHORT);
	}

	@Override
	public boolean conditionForAdding(Configuration pConfig, String pKey) {
		return true;
	}
}
