package de.simonsator.partyandfriendsgui.communication.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.api.events.creation.HeadCreationEvent;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.MainMenuCreationEvent;
import de.simonsator.partyandfriendsgui.api.menu.MainMenu;
import de.simonsator.partyandfriendsgui.api.menu.MenuManager;
import de.simonsator.partyandfriendsgui.communication.BungeecordCommunication;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

import static de.simonsator.partyandfriendsgui.manager.TextIdentifier.POST_FRIEND_PAGE;

/**
 * @author Simonsator
 * @version 1.0.0 on 06.07.2016
 */
public class OpenFriendMenu extends CommunicationTask implements MainMenu {
	private final int MIN_SIZE = Main.getInstance().getConfig().getInt("Design.FriendsMenu.Minimum-Lines") * 9;
	private final int PLUS_PAGE;
	private final String ONLINE_SUFFIX;
	private final String OFFLINE_SUFFIX;

	public OpenFriendMenu(String pOnlineSuffix, String pOfflineSuffix) {
		super("OpenMainMenu");
		if (Main.getInstance().getConfig().getBoolean("Inventories.FriendMenu.StartsWithOne"))
			PLUS_PAGE = 1;
		else PLUS_PAGE = 0;
		ONLINE_SUFFIX = pOnlineSuffix;
		OFFLINE_SUFFIX = " (" + pOfflineSuffix + ")";
	}

	@Override
	public void executeTask(Player pPlayer, JsonObject pJObj) {
		MenuManager.getInstance().setLastOpened(pPlayer, this);
		JsonArray friends = pJObj.get("friends").getAsJsonArray();
		final int friendsCount = friends.size();
		int in = friendsCount + 1;
		int toSet = (in / 9 + 1) * 9 + 18;
		if (friendsCount == 0) toSet = 9;
		else if (friendsCount >= 27)
			toSet = 45;
		if (toSet < MIN_SIZE)
			toSet = MIN_SIZE;
		int page = pJObj.get("page").getAsInt();
		Inventory inv = Bukkit.createInventory(null, toSet,
				LanguageManager.getInstance().getText(TextIdentifier.FRIENDS_MENU) + (page + PLUS_PAGE) + LanguageManager.getInstance().getText(POST_FRIEND_PAGE));
		int start = page * 27;
		int i;
		for (i = start; i < friendsCount && i - start < 27; i++) {
			JsonObject friend = friends.get(i).getAsJsonObject();
			ItemStack friendHead;
			if (friend.get("isOnline").getAsBoolean())
				friendHead = createOnlineHead(friend);
			else friendHead = createOfflineHead(friend);
			inv.addItem(friendHead);
		}
		if (ItemManager.getInstance().NO_FRIENDS != null &&
				friendsCount == 0) {
			inv.setItem(0, ItemManager.getInstance().NO_FRIENDS);
		}
		createMenuBar(pPlayer, inv, page != 0, start + i - start < friendsCount);
		if (ItemManager.getInstance().FRIEND_SORT_BY_LAST_ONLINE != null) {
			ItemStack sortItemToSet;
			switch (pJObj.get("sorting").getAsInt()) {
				case 1:
					sortItemToSet = ItemManager.getInstance().FRIEND_SORT_BY_ALPHABETIC;
					break;
				case 2:
					sortItemToSet = ItemManager.getInstance().FRIEND_SORT_BY_REVERSE_ALPHABETIC;
					break;
				case 3:
					sortItemToSet = ItemManager.getInstance().FRIEND_SORT_BY_FRIENDSHIP_DURATION;
					break;
				default:
					sortItemToSet = null;
					break;
			}
			if (sortItemToSet == null)
				sortItemToSet = ItemManager.getInstance().FRIEND_SORT_BY_LAST_ONLINE;
			inv.setItem(toSet - Main.getInstance().getConfig().getInt("Inventories.FriendMenu.Sorting.BackwardsMenubarPlace"), sortItemToSet);
		}
		Bukkit.getServer().getPluginManager().callEvent(new MainMenuCreationEvent(pPlayer, inv, pJObj, getClass()));
		if (ItemManager.getInstance().FRIEND_MENU_PLACEHOLDER != null)
			for (int k = 0; k < toSet - 9; k++)
				if (inv.getItem(k) == null)
					inv.setItem(k, ItemManager.getInstance().FRIEND_MENU_PLACEHOLDER);
		pPlayer.openInventory(inv);
	}

	private ItemStack createOfflineHead(JsonObject pFriend) {
		String playerName = pFriend.get("playerName").getAsString();
		ItemStack item;
		if (Main.getInstance().getConfig().getBoolean("General.UseSkeletonSkullForOfflinePlayer")) {
			item = new ItemStack(ItemManager.getInstance().SKELETON_SKULL_MATERIAL, 1, (short) 0);
		} else {
			item = new ItemStack(ItemManager.getInstance().PLAYER_HEAD_MATERIAL, 1, (short) 3);
			SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
			BungeecordCommunication.getInstance().SET_HEAD_OWNER.setHeadOwner(skullMeta, playerName, pFriend.get("playerUUID"));
			item.setItemMeta(skullMeta);
		}
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Main.getInstance().getColor(2) + playerName + Main.getInstance().getColor(3) + OFFLINE_SUFFIX);
		if (Main.getInstance().getConfig().getBoolean("Design.Lore.ShowLastOnlineTime")) {
			ArrayList<String> list = new ArrayList<>();
			list.add(LanguageManager.getInstance().getText(TextIdentifier.LAST_SEEN).replace("[LAST_SEEN]", pFriend.get("lastOnline").getAsString()));
			meta.setLore(list);
		}
		item.setItemMeta(meta);
		callEvent(false, item, pFriend);
		return item;
	}

	private ItemStack createOnlineHead(JsonObject pFriend) {
		ItemStack item = new ItemStack(ItemManager.getInstance().PLAYER_HEAD_MATERIAL, 1, (short) 3);
		ItemMeta meta = item.getItemMeta();
		String playerName = pFriend.getAsJsonObject().get("playerName").getAsString();
		meta.setDisplayName(Main.getInstance().getColor(0) + playerName + Main.getInstance().getColor(1)
				+ " (" + ONLINE_SUFFIX + ")");
		if (Main.getInstance().getConfig().getBoolean("Inventories.FriendMenu.OnlineFriends.ShowServer"))
			if (Main.getInstance().getConfig().getBoolean("Design.Lore.ShowServerInLore")) {
				List<String> lore = new ArrayList<>();
				lore.add(LanguageManager.getInstance().getText(TextIdentifier.ONLINE_ON).replace("[SERVER]", pFriend.getAsJsonObject().get("serverName").getAsString()));
				meta.setLore(lore);
			} else {
				meta.setDisplayName(Main.getInstance().getColor(0) + playerName + Main.getInstance().getColor(1)
						+ " (" + ONLINE_SUFFIX + " " + pFriend.getAsJsonObject().get("serverName").getAsString() + ")");
			}
		item.setItemMeta(meta);
		if (Main.getInstance().getConfig().getBoolean("General.SkinHeadDownload")) {
			SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
			BungeecordCommunication.getInstance().SET_HEAD_OWNER.setHeadOwner(skullMeta, playerName, pFriend.get("playerUUID"));
			item.setItemMeta(skullMeta);
		}
		callEvent(true, item, pFriend);
		return item;
	}

	private void callEvent(boolean isOnline, ItemStack pHead, JsonObject pInformation) {
		Bukkit.getServer().getPluginManager().callEvent(new HeadCreationEvent(pHead, isOnline, pInformation, this));
	}

	@Override
	public void openMenu(Player pPlayer) {
		PartyFriendsAPI.openMainInventory(pPlayer, 0);
	}
}
