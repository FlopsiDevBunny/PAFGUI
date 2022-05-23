package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.party;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.ChangePage;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PartyChangePage extends ChangePage {
	public PartyChangePage(int pChange, ItemStack pApplicableItem) {
		super(pChange, pApplicableItem, LanguageManager.getInstance().getText(TextIdentifier.PARTY_MENU_NAME), LanguageManager.getInstance().getText(TextIdentifier.PAST_PARTY_PAGE), Main.getInstance().getConfig().getBoolean("Inventories.PartyGUI.MainMenu.StartsWithOne"));
	}

	@Override
	protected void openMenu(Player pPlayer, int pPage) {
		PartyFriendsAPI.openPartyInventory(pPlayer, pPage);
	}
}
