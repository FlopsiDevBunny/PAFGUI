package de.simonsator.partyandfriendsgui.communication.tasks;

import com.google.gson.JsonObject;
import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.menu.OwnExecuteCommandBlockMenu;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import de.simonsator.partyandfriendsgui.nmsdepending.GlowEffect;
import de.simonsator.partyandfriendsgui.nmsdepending.NewGlowEffect;
import de.simonsator.partyandfriendsgui.nmsdepending.NoGlowEffect;
import de.simonsator.partyandfriendsgui.utilities.OwnExecuteCommandContainer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Simonsator
 * @version 1.0.0 on 07.07.2016
 */
public class OpenHideMenu extends CommunicationTask implements OwnExecuteCommandBlockMenu {
	private final GlowEffect gloweffect;
	private final int SIZE;
	private final List<OwnExecuteCommandContainer> ownExecuteCommandContainerList;

	public OpenHideMenu() {
		super("OpenHideMenu");
		if (Bukkit.getBukkitVersion().contains("1.7") || !Main.getInstance().getConfig().getBoolean("Inventories.HideInventory.GlowIfSelectedHideMode")) {
			gloweffect = new NoGlowEffect();
		} else
			gloweffect = new NewGlowEffect();
		SIZE = Main.getInstance().getConfig().getInt("Inventories.HideInventory.Size");
		ownExecuteCommandContainerList = createExecuteCommandContainerList(Main.getInstance().getConfig(), "Inventories.HideInventory.Own");
	}

	@Override
	public void executeTask(Player pPlayer, JsonObject pJObj) {
		Inventory inv = Bukkit.createInventory(null, SIZE, LanguageManager.getInstance().getText(TextIdentifier.HIDE_PLAYERS_MENU));
		int currentlySelectedHideMode = pJObj.get("hideMode").getAsInt();
		addToInventory(inv, "Inventories.HideInventory.HideModeItems.ShowAll.Place", ItemManager.getInstance().HIDE_SHOW_ALL_ITEM, 0 == currentlySelectedHideMode);
		addToInventory(inv, "Inventories.HideInventory.HideModeItems.ShowOnlyFriends.Place", ItemManager.getInstance().HIDE_SHOW_ONLY_YOUR_FRIENDS_ITEM, 1 == currentlySelectedHideMode);
		addToInventory(inv, "Inventories.HideInventory.HideModeItems.ShowOnlyFriendsAndPeopleOfTheServerTeam.Place", ItemManager.getInstance().HIDE_SHOW_FRIENDS_AND_PEOPLE_WITH_PERMISSION_ITEM, 2 == currentlySelectedHideMode);
		addToInventory(inv, "Inventories.HideInventory.HideModeItems.ShowOnlyPeopleOfTheServerTeamItem.Place", ItemManager.getInstance().HIDE_SHOW_ONLY_PEOPLE_FROM_THE_SERVER_ITEM, 3 == currentlySelectedHideMode);
		addToInventory(inv, "Inventories.HideInventory.HideModeItems.HideAll.Place", ItemManager.getInstance().HIDE_ALL_ITEM, 4 == currentlySelectedHideMode);
		addToInventory(inv, "Inventories.HideInventory.HideModeItems.ShowOnlyPartyMembers.Place", ItemManager.getInstance().HIDE_SHOW_ONLY_PARTY_MEMBERS_ITEM, 5 == currentlySelectedHideMode);
		for (OwnExecuteCommandContainer ownExecuteCommandContainer : ownExecuteCommandContainerList)
			inv.setItem(ownExecuteCommandContainer.PLACE, ownExecuteCommandContainer.ITEM);
		if (ItemManager.getInstance().HIDE_INVENTORY_PLACEHOLDER != null)
			for (int i = 0; i < SIZE; i++)
				if (inv.getItem(i) == null)
					inv.setItem(i, ItemManager.getInstance().HIDE_INVENTORY_PLACEHOLDER);
		pPlayer.openInventory(inv);
	}

	private void addToInventory(Inventory pInventory, String pItemConfigPath, ItemStack pItem, boolean pShouldGlow) {
		if (pItem == null)
			return;
		if (pShouldGlow)
			pItem = gloweffect.addGlow(pItem.clone());
		pInventory.setItem(Main.getInstance().getConfig().getInt(pItemConfigPath), pItem);
	}

	@Override
	public boolean conditionForAdding(Configuration pConfig, String pKey) {
		return true;
	}
}
