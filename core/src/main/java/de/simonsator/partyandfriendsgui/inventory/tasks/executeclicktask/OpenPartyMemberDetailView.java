package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.PartyDetailViewCreationEvent;
import de.simonsator.partyandfriendsgui.api.menu.OwnExecuteCommandBlockMenu;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import de.simonsator.partyandfriendsgui.utilities.OwnExecuteCommandContainer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OpenPartyMemberDetailView extends InventoryTask implements OwnExecuteCommandBlockMenu {
	private final List<OwnExecuteCommandContainer> ownExecuteCommandContainerList;

	public OpenPartyMemberDetailView() {
		this.ownExecuteCommandContainerList = createExecuteCommandContainerList(Main.getInstance().getConfig(), "Inventories.PartyGUI.PartyMemberDetailView.Own");
	}

	@Override
	public void execute(InventoryClickEvent pEvent) {
		if (Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch"))
			pEvent.getWhoClicked().closeInventory();
		openPartyMemberViewGUI((Player) pEvent.getWhoClicked(), pEvent.getCurrentItem());
	}

	@Override
	public boolean isApplicable(InventoryClickEvent pEvent) {
		return pEvent.getCurrentItem().getType().equals(ItemManager.getInstance().PLAYER_HEAD_MATERIAL) && pEvent.getInventory().getItem(4).getItemMeta().getDisplayName().endsWith(pEvent.getWhoClicked().getName()) &&
				!pEvent.getCurrentItem().getItemMeta().getDisplayName().endsWith(pEvent.getWhoClicked().getName()) && pEvent.getSlot() < pEvent.getInventory().getSize() - 9;
	}

	private void openPartyMemberViewGUI(Player pPlayer, ItemStack pPartyMemberHead) {
		String invName = LanguageManager.getInstance().getText(TextIdentifier.PARTY_MEMBER_DETAIL_VIEW_PREFIX) + pPartyMemberHead.getItemMeta().getDisplayName();
		if (invName.length() > 32) {
			invName = LanguageManager.getInstance().getText(TextIdentifier.PARTY_MEMBER_DETAIL_VIEW_SHORT_PREFIX) + pPartyMemberHead.getItemMeta().getDisplayName();
		}
		if (invName.length() < 33) {
			Inventory inv = createStandardInventory(invName);
			for (OwnExecuteCommandContainer ownExecuteCommandContainer : ownExecuteCommandContainerList)
				inv.setItem(ownExecuteCommandContainer.PLACE, ownExecuteCommandContainer.ITEM);
			for (int i = 0; i < inv.getSize(); i++)
				if (inv.getItem(i) == null)
					inv.setItem(i, ItemManager.getInstance().PARTY_DETAIL_VIEW_PLACEHOLDER);
			Bukkit.getServer().getPluginManager().callEvent(new PartyDetailViewCreationEvent(inv, pPlayer));
			pPlayer.openInventory(inv);
		} else sendErrorMessage(pPlayer);
	}

	private void sendErrorMessage(Player pPlayer) {
		pPlayer.sendMessage(LanguageManager.getInstance().getText(TextIdentifier.NAME_TOO_LONG_PARTY));
		pPlayer.closeInventory();
	}

	private Inventory createStandardInventory(String pInventoryName) {
		Inventory inv = Bukkit.createInventory(null, Main.getInstance().getConfig().getInt("Inventories.PartyGUI.PartyMemberDetailView.Size"), pInventoryName);
		if (ItemManager.getInstance().KICK_FROM_PARTY_ITEM != null)
			inv.setItem(Main.getInstance().getConfig().getInt("Inventories.PartyGUI.PartyMemberDetailView.KickItem.Place"), ItemManager.getInstance().KICK_FROM_PARTY_ITEM);
		if (ItemManager.getInstance().MAKE_NEW_PARTY_LEADER_ITEM != null)
			inv.setItem(Main.getInstance().getConfig().getInt("Inventories.PartyGUI.PartyMemberDetailView.MakeLeader.Place"), ItemManager.getInstance().MAKE_NEW_PARTY_LEADER_ITEM);
		if (Main.getInstance().getConfig().getBoolean("Inventories.PartyGUI.PartyMemberDetailView.BackItem.Use"))
			inv.setItem(Main.getInstance().getConfig().getInt("Inventories.PartyGUI.PartyMemberDetailView.BackItem.Place"), ItemManager.getInstance().BACK_ITEM);
		return inv;
	}

	@Override
	public boolean conditionForAdding(Configuration pConfig, String pKey) {
		return true;
	}
}
