package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FriendsChangePage extends ChangePage {
	public FriendsChangePage(int pChange, ItemStack pApplicableItem) {
		super(pChange, pApplicableItem, LanguageManager.getInstance().getText(TextIdentifier.FRIENDS_MENU), LanguageManager.getInstance().getText(TextIdentifier.POST_FRIEND_PAGE), Main.getInstance().getConfig().getBoolean("Inventories.FriendMenu.StartsWithOne"));
	}

	@Override
	protected void openMenu(Player pPlayer, int pPage) {
		PartyFriendsAPI.openMainInventory(pPlayer, pPage);
	}
}
