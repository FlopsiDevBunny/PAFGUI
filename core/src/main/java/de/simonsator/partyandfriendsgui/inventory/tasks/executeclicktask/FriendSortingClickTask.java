package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import de.simonsator.partyandfriendsgui.utilities.inventorynamegetter.InventoryNameGetter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FriendSortingClickTask extends InventoryTask {
	private final String PAGE_PREFIX;
	private final String PAGE_POSTFIX;
	private final boolean[] enabledSortModes = new boolean[4];
	private final int CHANGE;

	public FriendSortingClickTask() {
		PAGE_PREFIX = LanguageManager.getInstance().getText(TextIdentifier.FRIENDS_MENU);
		PAGE_POSTFIX = LanguageManager.getInstance().getText(TextIdentifier.POST_FRIEND_PAGE);
		enabledSortModes[0] = true;
		enabledSortModes[1] = ItemManager.getInstance().FRIEND_SORT_BY_ALPHABETIC != null;
		enabledSortModes[2] = ItemManager.getInstance().FRIEND_SORT_BY_REVERSE_ALPHABETIC != null;
		enabledSortModes[3] = ItemManager.getInstance().FRIEND_SORT_BY_FRIENDSHIP_DURATION != null;
		int change = 0;
		if (Main.getInstance().getConfig().getBoolean("Inventories.FriendMenu.StartsWithOne"))
			change = 1;
		CHANGE = change;
	}

	@Override
	public void execute(InventoryClickEvent pEvent) {
		String num = InventoryNameGetter.getInstance().getName(pEvent).substring(PAGE_PREFIX.length());
		num = num.substring(0, num.length() - PAGE_POSTFIX.length());
		if (Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch"))
			pEvent.getWhoClicked().closeInventory();
		pEvent.setCancelled(true);
		int currentSortMode = 0;
		if (AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_SORT_BY_ALPHABETIC, pEvent.getCurrentItem(), false))
			currentSortMode = 1;
		if (AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_SORT_BY_REVERSE_ALPHABETIC, pEvent.getCurrentItem(), false))
			currentSortMode = 2;
		if (AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_SORT_BY_FRIENDSHIP_DURATION, pEvent.getCurrentItem(), false))
			currentSortMode = 3;
		openMenu((Player) pEvent.getWhoClicked(), Integer.parseInt(num) - CHANGE, getNewSortMode(currentSortMode));
	}

	private int getNewSortMode(int pCurrent) {
		pCurrent++;
		if (pCurrent == enabledSortModes.length)
			return 0;
		if (enabledSortModes[pCurrent])
			return pCurrent;
		return getNewSortMode(pCurrent);
	}

	private void openMenu(Player pPlayer, int pPage, int pNewSortMode) {
		PartyFriendsAPI.openMainInventory(pPlayer, pPage, pNewSortMode);
	}

	@Override
	public boolean isApplicable(InventoryClickEvent pEvent) {
		return AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_SORT_BY_LAST_ONLINE, pEvent.getCurrentItem(), false) ||
				AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_SORT_BY_ALPHABETIC, pEvent.getCurrentItem(), false) ||
				AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_SORT_BY_FRIENDSHIP_DURATION, pEvent.getCurrentItem(), false) ||
				AdvancedItem.itemsAreEqual(ItemManager.getInstance().FRIEND_SORT_BY_REVERSE_ALPHABETIC, pEvent.getCurrentItem(), false);
	}
}
