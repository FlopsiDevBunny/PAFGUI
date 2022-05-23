package de.simonsator.partyandfriendsgui.communication.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.simonsator.datastructures.ItemPackage;
import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.MainMenuCreationEvent;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.SettingsMenuCreationEvent;
import de.simonsator.partyandfriendsgui.api.menu.MainMenu;
import de.simonsator.partyandfriendsgui.api.menu.MenuManager;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author 00pfl
 * @version 1.0.0 on 07.07.2016
 */
public class OpenSettingsMenu extends CommunicationTask implements MainMenu {
	private final int FIRST_PLACE;
	private final int INVENTORY_SIZE;
	private final Map<Integer, SettingsData> SETTINGS = new HashMap<>();

	public OpenSettingsMenu() {
		super("OpenSettingsMenu");
		FIRST_PLACE = Main.getInstance().getConfig().getInt("Inventories.SettingsMenu.FirstItemPlace");
		INVENTORY_SIZE = Main.getInstance().getConfig().getInt("Inventories.SettingsMenu.Size");
		addToSettings(new SettingsData(0, ItemManager.getInstance().WANT_RECEIVE_FRIEND_REQUESTS_ITEM, new ItemStack[]{Main.getInstance().blockOutput(1, 6),
				Main.getInstance().blockOutput(0, 6)}, Main.getInstance().getConfig().getInt("Inventories.SettingsMenu.FriendRequestSetting.Priority"),
				Main.getInstance().getConfig().getString("Inventories.SettingsMenu.FriendRequestSetting.Permission")));
		addToSettings(new SettingsData(1, ItemManager.getInstance().WANT_RECEIVE_PARTY_INVITATIONS_ITEM, new ItemStack[]{Main.getInstance().blockOutput(2, 6),
				Main.getInstance().blockOutput(3, 6)}, Main.getInstance().getConfig().getInt("Inventories.SettingsMenu.PartyInviteSetting.Priority"),
				Main.getInstance().getConfig().getString("Inventories.SettingsMenu.PartyInviteSetting.Permission")));
		addToSettings(new SettingsData(2, ItemManager.getInstance().WANT_RECEIVE_MESSAGES_ITEM, new ItemStack[]{Main.getInstance().blockOutput(8, 6),
				Main.getInstance().blockOutput(9, 6)}, Main.getInstance().getConfig().getInt("Inventories.SettingsMenu.ReceiveFriendMSG.Priority"),
				Main.getInstance().getConfig().getString("Inventories.SettingsMenu.ReceiveFriendMSG.Permission")));
		addToSettings(new SettingsData(3, ItemManager.getInstance().WANT_TO_BE_SHOWN_AS_ONLINE_ITEM, new ItemStack[]{Main.getInstance().blockOutput(4, 6),
				Main.getInstance().blockOutput(5, 6)}, Main.getInstance().getConfig().getInt("Inventories.SettingsMenu.ShowOnlineSetting.Priority"),
				Main.getInstance().getConfig().getString("Inventories.SettingsMenu.ShowOnlineSetting.Permission")));
		addToSettings(new SettingsData(4, ItemManager.getInstance().SHOULD_FRIENDS_BE_ABLE_TO_JUMP_TO_YOU_ITEM, new ItemStack[]{Main.getInstance().blockOutput(6, 6),
				Main.getInstance().blockOutput(7, 6)}, Main.getInstance().getConfig().getInt("Inventories.SettingsMenu.FriendJumpSetting.Priority"),
				Main.getInstance().getConfig().getString("Inventories.SettingsMenu.FriendJumpSetting.Permission")));
		addToSettings(new SettingsData(101, ItemManager.getInstance().WANT_TO_RECEIVE_FRIEND_STATUS_NOTIFICATION, new ItemStack[]{Main.getInstance().blockOutput(10, 6),
				Main.getInstance().blockOutput(11, 6)}, Main.getInstance().getConfig().getInt("Inventories.SettingsMenu.NotifyOnlineStatusChange.Priority"),
				Main.getInstance().getConfig().getString("Inventories.SettingsMenu.NotifyOnlineStatusChange.Permission")));
	}

	@Override
	public void executeTask(Player pPlayer, JsonObject pJObj) {
		MenuManager.getInstance().setLastOpened(pPlayer, this);
		Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE, LanguageManager.getInstance().getText(TextIdentifier.SETTINGS_MENU));
		JsonArray settings = pJObj.get("settings").getAsJsonArray();
		Stack<ItemPackage> items = new Stack<>();
		for (JsonElement element : settings) {
			JsonObject settingsObject = element.getAsJsonObject();
			SettingsData settingsData = SETTINGS.get(settingsObject.get("id").getAsInt());
			if (settingsData != null)
				if (settingsData.PERMISSION == null || settingsData.PERMISSION.equals("") || pPlayer.hasPermission(settingsData.PERMISSION))
					items.push(new ItemPackage(settingsData.TOP_ITEM, settingsData.LOWER_ITEMS[settingsObject.getAsJsonObject().get("worth").getAsInt()], settingsData.PRIORITY));
		}
		Bukkit.getServer().getPluginManager().callEvent(new SettingsMenuCreationEvent(inv, pPlayer, items, pJObj));
		for (ItemPackage currentSetting : PartyFriendsAPI.getCustomSettings())
			items.push(currentSetting);
		Collections.sort(items);
		int addedObjectsCount = items.size();
		int firstPlace = (int) (4.5 - items.size() / 2);
		for (int i = 0; i < addedObjectsCount; i++) {
			ItemPackage momItemPackage = items.pop();
			if (momItemPackage != null) {
				inv.setItem(firstPlace + i + FIRST_PLACE, momItemPackage.getTopItem());
				inv.setItem(firstPlace + FIRST_PLACE + 9 + i, momItemPackage.getLowerItem());
			}
		}
		for (int i = 0; i < INVENTORY_SIZE; i++)
			if (inv.getItem(i) == null)
				inv.setItem(i, ItemManager.getInstance().SETTINGS_MENU_PLACEHOLDER);
		createMenuBar(pPlayer, inv);
		Bukkit.getServer().getPluginManager().callEvent(new MainMenuCreationEvent(pPlayer, inv, pJObj, getClass()));
		if (Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch"))
			pPlayer.closeInventory();
		pPlayer.openInventory(inv);
	}

	private void addToSettings(SettingsData pSetting) {
		if (pSetting.TOP_ITEM != null)
			SETTINGS.put(pSetting.ID, pSetting);
	}

	@Override
	public void openMenu(Player pPlayer) {
		PartyFriendsAPI.openSettingsInventory(pPlayer);
	}

	private static class SettingsData {
		public final int ID;
		public final int PRIORITY;
		public final ItemStack TOP_ITEM;
		public final ItemStack[] LOWER_ITEMS;
		public final String PERMISSION;

		private SettingsData(int id, ItemStack topItem, ItemStack[] lowerItems, int pPriority, String permission) {
			ID = id;
			TOP_ITEM = topItem;
			LOWER_ITEMS = lowerItems;
			PRIORITY = pPriority;
			PERMISSION = permission;
		}
	}
}
