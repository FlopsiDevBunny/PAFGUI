package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask;

import de.simonsator.partyandfriendsgui.Main;
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

import java.util.List;

/**
 * @author 00pfl
 * @version 1.0.0 on 06.08.2016
 */
public abstract class OpenFriendDetailView extends InventoryTask implements OwnExecuteCommandBlockMenu {
	private final String IDENTIFIER;
	private final String PLAYER_COLOR;
	private final List<OwnExecuteCommandContainer> ownExecuteCommandContainerList;

	public OpenFriendDetailView(String pIdentifier, String pPlayerColor) {
		IDENTIFIER = pIdentifier;
		PLAYER_COLOR = pPlayerColor;
		ownExecuteCommandContainerList = createExecuteCommandContainerList(Main.getInstance().getConfig(), "Inventories.FriendDetailView.Own");
	}

	@Override
	public void execute(InventoryClickEvent pEvent) {
		String handOver = shortUp(pEvent.getCurrentItem().getItemMeta().getDisplayName());
		if (handOver.length() < 33) {
			pEvent.setCancelled(true);
			if (Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch"))
				pEvent.getWhoClicked().closeInventory();
			Inventory inv = createInventory(handOver, (Player) pEvent.getWhoClicked());
			for (int i = 0; i < inv.getSize(); i++)
				if (inv.getItem(i) == null)
					inv.setItem(i, ItemManager.getInstance().FRIEND_DETAIL_VIEW_PLACEHOLDER);
			pEvent.getWhoClicked().openInventory(inv);
		} else sendErrorMessage((Player) pEvent.getWhoClicked());
	}

	abstract String shortUp(String pHandOver);

	protected Inventory createStandardInventory(String pInventoryName) {
		Inventory inv = Bukkit.createInventory(null, Main.getInstance().getConfig().getInt("Inventories.FriendDetailView.Size"), pInventoryName);
		inv.setItem(Main.getInstance().getConfig().getInt("Inventories.FriendDetailView.DeleteItem.Place"), ItemManager.getInstance().FRIEND_DELETE);
		if (Main.getInstance().getConfig().getBoolean("Inventories.FriendDetailView.BackItem.Use"))
			inv.setItem(Main.getInstance().getConfig().getInt("Inventories.FriendDetailView.BackItem.Place"), ItemManager.getInstance().BACK_ITEM);
		for (OwnExecuteCommandContainer ownExecuteCommandContainer : ownExecuteCommandContainerList)
			inv.setItem(ownExecuteCommandContainer.PLACE, ownExecuteCommandContainer.ITEM);
		return inv;
	}

	private void sendErrorMessage(Player pPlayer) {
		pPlayer.sendMessage(LanguageManager.getInstance().getText(TextIdentifier.NAME_TOO_LONG_FRIENDS));
		pPlayer.closeInventory();
	}

	protected abstract Inventory createInventory(String pHandOver, Player pPlayer);

	@Override
	public boolean isApplicable(InventoryClickEvent pEvent) {
		String displayName = pEvent.getCurrentItem().getItemMeta().getDisplayName();
		return displayName.startsWith(PLAYER_COLOR) && displayName.contains(IDENTIFIER) && displayName.endsWith(")");
	}

	@Override
	public boolean conditionForAdding(Configuration pConfig, String pKey) {
		return (!pConfig.getBoolean(pKey + "OnlyOnline") || this instanceof OpenOnlineFriendMenu);
	}
}
