package de.simonsator.partyandfriendsgui.manager;

import de.simonsator.partyandfriendsgui.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;

/**
 * @author simonbrungs
 * @version 1.0.0 06.01.17
 */
public class ItemManager {
	private static ItemManager instance;
	public final boolean USE_ITEM_META_DATA;
	public final ItemStack NO_PENDING_FRIEND_REQUESTS_ITEM;
	public final ItemStack OPEN_PARTY_GUI_ITEM;
	public final ItemStack HOW_TO_CREATE_A_PARTY;
	public final ItemStack PARTY_MENU_PLACEHOLDER;
	public final ItemStack PARTY_DETAIL_VIEW_PLACEHOLDER;
	public final ItemStack FRIEND_ACCEPT_MENU_PLACEHOLDER;
	public final ItemStack MENU_BAR_PLACEHOLDER_ITEM;
	public final ItemStack FRIEND_DETAIL_VIEW_PLACEHOLDER;
	public final ItemStack HIDE_INVENTORY_PLACEHOLDER;
	public final ItemStack FRIEND_DELETE;
	public final ItemStack FRIEND_INVITE_PARTY;
	public final ItemStack JUMP_TO_FRIEND;
	public final ItemStack FRIEND_DELETE_CONFIRM_ACCEPT_ITEM;
	public final ItemStack FRIEND_DELETE_CONFIRM_DECLINE_ITEM;
	public final ItemStack FRIEND_DELETE_CONFIRM_MENU_PLACEHOLDER;
	public final ItemStack SETTINGS_MENU_PLACEHOLDER;
	public final ItemStack WANT_RECEIVE_FRIEND_REQUESTS_ITEM;
	public final ItemStack WANT_RECEIVE_PARTY_INVITATIONS_ITEM;
	public final ItemStack WANT_TO_BE_SHOWN_AS_ONLINE_ITEM;
	public final ItemStack WANT_TO_RECEIVE_FRIEND_STATUS_NOTIFICATION;
	public final ItemStack SHOULD_FRIENDS_BE_ABLE_TO_JUMP_TO_YOU_ITEM;
	public final ItemStack WANT_RECEIVE_MESSAGES_ITEM;
	public final ItemStack BACK_ITEM;
	public final ItemStack HIDE_ITEM;
	public final ItemStack SETTINGS_ITEM;
	public final ItemStack FRIEND_REQUEST_ACCEPT_MENU_ACCEPT;
	public final ItemStack FRIEND_REQUEST_ACCEPT_MENU_DENY;
	public final ItemStack OPEN_FRIEND_GUI_ITEM;
	public final ItemStack KICK_FROM_PARTY_ITEM;
	public final ItemStack MAKE_NEW_PARTY_LEADER_ITEM;
	public final ItemStack FRIEND_REQUEST_ITEM;
	public final ItemStack LEAVE_PARTY_ITEM;
	public final ItemStack NO_FRIENDS;
	public final ItemStack FRIEND_MENU_PLACEHOLDER;
	public final ItemStack PREVIOUS_PAGE_ITEM;
	public final ItemStack NEXT_PAGE_ITEM;
	public final ItemStack FRIEND_SORT_BY_LAST_ONLINE;
	public final ItemStack FRIEND_SORT_BY_ALPHABETIC;
	public final ItemStack FRIEND_SORT_BY_REVERSE_ALPHABETIC;
	public final ItemStack FRIEND_SORT_BY_FRIENDSHIP_DURATION;
	public final Material PLAYER_HEAD_MATERIAL;
	public final Material SKELETON_SKULL_MATERIAL;
	public final Material STAINED_CLAY_MATERIAL;
	public final ItemStack RED_TERRACOTTA;
	public final ItemStack GREEN_TERRACOTTA;
	public final ItemStack ORANGE_TERRACOTTA;
	public final ItemStack YELLOW_TERRACOTTA;
	public final ItemStack PURPLE_TERRACOTTA;
	public final ItemStack HIDE_SHOW_ALL_ITEM;
	public final ItemStack HIDE_SHOW_ONLY_YOUR_FRIENDS_ITEM;
	public final ItemStack HIDE_SHOW_FRIENDS_AND_PEOPLE_WITH_PERMISSION_ITEM;
	public final ItemStack HIDE_SHOW_ONLY_PEOPLE_FROM_THE_SERVER_ITEM;
	public final ItemStack HIDE_ALL_ITEM;
	public final ItemStack HIDE_SHOW_ONLY_PARTY_MEMBERS_ITEM;
	public final ItemStack PLAYER_HEAD;

	public ItemManager(Configuration pConfig) {
		instance = this;
		USE_ITEM_META_DATA = Bukkit.getVersion().contains("1.7") || Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12");
		if (USE_ITEM_META_DATA) {
			PLAYER_HEAD_MATERIAL = Material.valueOf("SKULL_ITEM");
			SKELETON_SKULL_MATERIAL = Material.valueOf("SKULL_ITEM");
			STAINED_CLAY_MATERIAL = Material.valueOf("STAINED_CLAY");
			PLAYER_HEAD = new ItemStack(ItemManager.getInstance().PLAYER_HEAD_MATERIAL, 1, (short) 3);
			RED_TERRACOTTA = new ItemStack(STAINED_CLAY_MATERIAL, 1, (short) 14);
			GREEN_TERRACOTTA = new ItemStack(STAINED_CLAY_MATERIAL, 1, (short) 5);
			ORANGE_TERRACOTTA = new ItemStack(STAINED_CLAY_MATERIAL, 1, (short) 1);
			YELLOW_TERRACOTTA = new ItemStack(STAINED_CLAY_MATERIAL, 1, (short) 4);
			PURPLE_TERRACOTTA = new ItemStack(STAINED_CLAY_MATERIAL, 1, (short) 10);
		} else {
			PLAYER_HEAD_MATERIAL = Material.valueOf("PLAYER_HEAD");
			SKELETON_SKULL_MATERIAL = Material.valueOf("SKELETON_SKULL");
			STAINED_CLAY_MATERIAL = Material.valueOf("TERRACOTTA");
			PLAYER_HEAD = new ItemStack(PLAYER_HEAD_MATERIAL);
			RED_TERRACOTTA = new ItemStack(Material.valueOf("RED_TERRACOTTA"));
			GREEN_TERRACOTTA = new ItemStack(Material.valueOf("GREEN_TERRACOTTA"));
			ORANGE_TERRACOTTA = new ItemStack(Material.valueOf("ORANGE_TERRACOTTA"));
			YELLOW_TERRACOTTA = new ItemStack(Material.valueOf("YELLOW_TERRACOTTA"));
			PURPLE_TERRACOTTA = new ItemStack(Material.valueOf("PURPLE_TERRACOTTA"));
		}
		ItemManagerSetupHelper setupHelper = new ItemManagerSetupHelper(pConfig, PLAYER_HEAD);
		String itemPart = "Inventories.EveryMenu.BackItem.";
		BACK_ITEM = setupHelper.getItemStack(TextIdentifier.BACK_ITEM_NAME, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		itemPart = "Inventories.HideInventory.PlaceHolderItem.";
		if (pConfig.getBoolean(itemPart + "Use"))
			HIDE_INVENTORY_PLACEHOLDER = setupHelper.getItemStack(TextIdentifier.HIDE_INVENTORY_PLACE_HOLDER, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else HIDE_INVENTORY_PLACEHOLDER = null;
		itemPart = "Inventories.HideInventory.HideModeItems.ShowAll.";
		HIDE_SHOW_ALL_ITEM = setupHelper.getItemStack(TextIdentifier.SHOW_ALL_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		itemPart = "Inventories.HideInventory.HideModeItems.HideAll.";
		if (pConfig.getBoolean(itemPart + "Use"))
			HIDE_ALL_ITEM = setupHelper.getItemStack(TextIdentifier.SHOW_NOBODY_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else HIDE_ALL_ITEM = null;
		itemPart = "Inventories.HideInventory.HideModeItems.ShowOnlyPartyMembers.";
		if (pConfig.getBoolean(itemPart + "Use"))
			HIDE_SHOW_ONLY_PARTY_MEMBERS_ITEM = setupHelper.getItemStack(TextIdentifier.SHOW_PARTY_MEMBERS, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else HIDE_SHOW_ONLY_PARTY_MEMBERS_ITEM = null;
		itemPart = "Inventories.HideInventory.HideModeItems.ShowOnlyFriendsAndPeopleOfTheServerTeam.";
		if (pConfig.getBoolean(itemPart + "Use"))
			HIDE_SHOW_FRIENDS_AND_PEOPLE_WITH_PERMISSION_ITEM = setupHelper.getItemStack(TextIdentifier.SHOW_FRIENDS_AND_PEOPLE_WITH_PERMISSION_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else HIDE_SHOW_FRIENDS_AND_PEOPLE_WITH_PERMISSION_ITEM = null;
		itemPart = "Inventories.HideInventory.HideModeItems.ShowOnlyFriends.";
		if (pConfig.getBoolean(itemPart + "Use"))
			HIDE_SHOW_ONLY_YOUR_FRIENDS_ITEM = setupHelper.getItemStack(TextIdentifier.SHOW_ONLY_YOUR_FRIENDS_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else HIDE_SHOW_ONLY_YOUR_FRIENDS_ITEM = null;
		itemPart = "Inventories.HideInventory.HideModeItems.ShowOnlyPeopleOfTheServerTeamItem.";
		if (pConfig.getBoolean(itemPart + "Use"))
			HIDE_SHOW_ONLY_PEOPLE_FROM_THE_SERVER_ITEM = setupHelper.getItemStack(TextIdentifier.SHOW_ONLY_PEOPLE_FROM_THE_SERVER_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else HIDE_SHOW_ONLY_PEOPLE_FROM_THE_SERVER_ITEM = null;
		itemPart = "Inventories.FriendDetailView.PlaceHolderItem.";
		if (pConfig.getBoolean(itemPart + "Use"))
			FRIEND_DETAIL_VIEW_PLACEHOLDER = setupHelper.getItemStack(TextIdentifier.FRIEND_DETAIL_VIEW_PLACE_HOLDER, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else FRIEND_DETAIL_VIEW_PLACEHOLDER = null;
		itemPart = "Inventories.FriendDetailView.DeleteItem.";
		FRIEND_DELETE = setupHelper.getItemStack(TextIdentifier.DELETE_FRIEND_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		itemPart = "Inventories.FriendDetailView.InviteIntoParty.";
		if (pConfig.getBoolean(itemPart + "Use"))
			FRIEND_INVITE_PARTY = setupHelper.getItemStack(TextIdentifier.INVITE_INTO_PARTY_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else FRIEND_INVITE_PARTY = null;
		itemPart = "Inventories.FriendDetailView.JumpTo.";
		if (pConfig.getBoolean(itemPart + "Use"))
			JUMP_TO_FRIEND = setupHelper.getItemStack(TextIdentifier.JUMP_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else JUMP_TO_FRIEND = null;
		if (pConfig.getBoolean("Inventories.ConfirmFriendDeleteMenu.Use")) {
			itemPart = "Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmAccept.";
			FRIEND_DELETE_CONFIRM_ACCEPT_ITEM = setupHelper.getItemStack(TextIdentifier.FRIEND_DELETE_CONFIRM_ACCEPT, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			itemPart = "Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmDecline.";
			if (pConfig.getBoolean(itemPart + "Use"))
				FRIEND_DELETE_CONFIRM_DECLINE_ITEM = setupHelper.getItemStack(TextIdentifier.FRIEND_DELETE_CONFIRM_DECLINE, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			else FRIEND_DELETE_CONFIRM_DECLINE_ITEM = null;
			itemPart = "Inventories.ConfirmFriendDeleteMenu.PlaceHolderItem.";
			if (pConfig.getBoolean(itemPart + "Use"))
				FRIEND_DELETE_CONFIRM_MENU_PLACEHOLDER = setupHelper.getItemStack(TextIdentifier.FRIEND_DELETE_CONFIRM_PLACEHOLDER, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			else FRIEND_DELETE_CONFIRM_MENU_PLACEHOLDER = null;

		} else {
			FRIEND_DELETE_CONFIRM_ACCEPT_ITEM = null;
			FRIEND_DELETE_CONFIRM_DECLINE_ITEM = null;
			FRIEND_DELETE_CONFIRM_MENU_PLACEHOLDER = null;
		}
		itemPart = "Inventories.FriendAcceptMenu.PlaceHolderItem.";
		if (pConfig.getBoolean(itemPart + "Use"))
			FRIEND_ACCEPT_MENU_PLACEHOLDER = setupHelper.getItemStack(TextIdentifier.FRIEND_ACCEPT_MENU_PLACEHOLDER, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else FRIEND_ACCEPT_MENU_PLACEHOLDER = null;
		itemPart = "Inventories.MainMenuMenuBar.PlaceHolderItem.";
		if (pConfig.getBoolean(itemPart + "Use"))
			MENU_BAR_PLACEHOLDER_ITEM = setupHelper.getItemStack(TextIdentifier.MENU_BAR_PLACEHOLDER, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else MENU_BAR_PLACEHOLDER_ITEM = null;
		itemPart = "Inventories.SettingsMenu.PlaceHolderItem.";
		if (pConfig.getBoolean(itemPart + "Use"))
			SETTINGS_MENU_PLACEHOLDER = setupHelper.getItemStack(TextIdentifier.FRIEND_ACCEPT_MENU_PLACEHOLDER, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else SETTINGS_MENU_PLACEHOLDER = null;
		itemPart = "Inventories.ToolBar.HideItem.";
		if (pConfig.getBoolean(itemPart + "Use"))
			HIDE_ITEM = setupHelper.getItemStack(TextIdentifier.HIDE_PLAYERS_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else HIDE_ITEM = null;

		itemPart = "Inventories.SettingsMenu.FriendRequestSetting.TopItem.";
		if (pConfig.getBoolean("Inventories.SettingsMenu.FriendRequestSetting.Activated"))
			WANT_RECEIVE_FRIEND_REQUESTS_ITEM = setupHelper.getItemStack(TextIdentifier.WANT_RECEIVE_FRIEND_REQUESTS_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else WANT_RECEIVE_FRIEND_REQUESTS_ITEM = null;
		itemPart = "Inventories.SettingsMenu.PartyInviteSetting.TopItem.";
		if (pConfig.getBoolean("Inventories.SettingsMenu.PartyInviteSetting.Activated"))
			WANT_RECEIVE_PARTY_INVITATIONS_ITEM = setupHelper.getItemStack(TextIdentifier.WANT_RECEIVE_PARTY_INVITATIONS_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else WANT_RECEIVE_PARTY_INVITATIONS_ITEM = null;
		itemPart = "Inventories.SettingsMenu.ReceiveFriendMSG.TopItem.";
		if (pConfig.getBoolean("Inventories.SettingsMenu.ReceiveFriendMSG.Activated"))
			WANT_RECEIVE_MESSAGES_ITEM = setupHelper.getItemStack(TextIdentifier.WANT_RECEIVE_MESSAGES_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else WANT_RECEIVE_MESSAGES_ITEM = null;
		itemPart = "Inventories.SettingsMenu.ShowOnlineSetting.TopItem.";
		if (pConfig.getBoolean("Inventories.SettingsMenu.ShowOnlineSetting.Activated"))
			WANT_TO_BE_SHOWN_AS_ONLINE_ITEM = setupHelper.getItemStack(TextIdentifier.WANT_TO_BE_SHOWN_AS_ONLINE_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else WANT_TO_BE_SHOWN_AS_ONLINE_ITEM = null;
		itemPart = "Inventories.SettingsMenu.NotifyOnlineStatusChange.TopItem.";
		if (pConfig.getBoolean("Inventories.SettingsMenu.NotifyOnlineStatusChange.Activated"))
			WANT_TO_RECEIVE_FRIEND_STATUS_NOTIFICATION = setupHelper.getItemStack(TextIdentifier.WANT_TO_RECEIVE_FRIEND_STATUS_NOTIFICATION, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else WANT_TO_RECEIVE_FRIEND_STATUS_NOTIFICATION = null;
		itemPart = "Inventories.SettingsMenu.FriendJumpSetting.TopItem.";
		if (pConfig.getBoolean("Inventories.SettingsMenu.FriendJumpSetting.Activated"))
			SHOULD_FRIENDS_BE_ABLE_TO_JUMP_TO_YOU_ITEM = setupHelper.getItemStack(TextIdentifier.SHOULD_FRIENDS_BE_ABLE_TO_JUMP_TO_YOU_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else SHOULD_FRIENDS_BE_ABLE_TO_JUMP_TO_YOU_ITEM = null;
		itemPart = "Inventories.MainMenuMenuBar.SettingsItem.";
		if (pConfig.getBoolean("Inventories.MainMenuMenuBar.SettingsItem.Use"))
			SETTINGS_ITEM = setupHelper.getItemStack(TextIdentifier.SETTINGS_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else SETTINGS_ITEM = null;
		itemPart = "Inventories.FriendAcceptMenu.AcceptItem.";
		FRIEND_REQUEST_ACCEPT_MENU_ACCEPT = setupHelper.getItemStack(TextIdentifier.ACCEPT_FRIEND_REQUEST_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		itemPart = "Inventories.FriendAcceptMenu.DenyItem.";
		FRIEND_REQUEST_ACCEPT_MENU_DENY = setupHelper.getItemStack(TextIdentifier.DENY_FRIEND_REQUEST_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		itemPart = "Inventories.MainMenuMenuBar.FriendItem.";
		if (pConfig.getBoolean(itemPart + "Use")) {
			OPEN_FRIEND_GUI_ITEM = setupHelper.getItemStack(TextIdentifier.OPEN_FRIEND_MENU, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		} else OPEN_FRIEND_GUI_ITEM = null;
		itemPart = "Inventories.FriendMenu.NoFriendsItem.";
		if (pConfig.getBoolean(itemPart + "Use")) {
			NO_FRIENDS = setupHelper.getItemStack(TextIdentifier.NO_FRIENDS, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		} else NO_FRIENDS = null;
		itemPart = "Inventories.FriendMenu.PlaceHolderItem.";
		if (pConfig.getBoolean(itemPart + "Use"))
			FRIEND_MENU_PLACEHOLDER = setupHelper.getItemStack(TextIdentifier.FRIEND_MENU_PLACEHOLDER, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		else FRIEND_MENU_PLACEHOLDER = null;
		itemPart = "Inventories.MainMenuMenuBar.FriendRequestItem.";
		if (pConfig.getBoolean(itemPart + "Use")) {
			FRIEND_REQUEST_ITEM = setupHelper.getItemStack(TextIdentifier.FRIENDSHIP_INVITATION_NAME, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		} else FRIEND_REQUEST_ITEM = null;
		itemPart = "Inventories.MainMenuMenuBar.NextPageButton.";
		NEXT_PAGE_ITEM = setupHelper.getItemStack(TextIdentifier.NEXT_PAGE_BUTTON_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		if (Main.getInstance().getConfig().getBoolean("Inventories.FriendMenu.Sorting.Enabled")) {
			itemPart = "Inventories.FriendMenu.Sorting.SortByLastOnline.";
			FRIEND_SORT_BY_LAST_ONLINE = setupHelper.getItemStack(TextIdentifier.FRIEND_SORT_BY_LAST_ONLINE, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			itemPart = "Inventories.FriendMenu.Sorting.SortByAlphabeticAscending.";
			if (pConfig.getBoolean(itemPart + "Use"))
				FRIEND_SORT_BY_ALPHABETIC = setupHelper.getItemStack(TextIdentifier.FRIEND_SORT_BY_ALPHABETIC, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			else FRIEND_SORT_BY_ALPHABETIC = null;
			itemPart = "Inventories.FriendMenu.Sorting.SortByAlphabeticDescending.";
			if (pConfig.getBoolean(itemPart + "Use"))
				FRIEND_SORT_BY_REVERSE_ALPHABETIC = setupHelper.getItemStack(TextIdentifier.FRIEND_SORT_BY_REVERSE_ALPHABETIC, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			else FRIEND_SORT_BY_REVERSE_ALPHABETIC = null;
			itemPart = "Inventories.FriendMenu.Sorting.SortByFriendshipDuration.";
			if (pConfig.getBoolean(itemPart + "Use"))
				FRIEND_SORT_BY_FRIENDSHIP_DURATION = setupHelper.getItemStack(TextIdentifier.FRIEND_SORT_BY_FRIENDSHIP_DURATION, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			else FRIEND_SORT_BY_FRIENDSHIP_DURATION = null;
		} else {
			FRIEND_SORT_BY_LAST_ONLINE = null;
			FRIEND_SORT_BY_ALPHABETIC = null;
			FRIEND_SORT_BY_REVERSE_ALPHABETIC = null;
			FRIEND_SORT_BY_FRIENDSHIP_DURATION = null;
		}
		itemPart = "Inventories.MainMenuMenuBar.PreviousPageButton.";
		PREVIOUS_PAGE_ITEM = setupHelper.getItemStack(TextIdentifier.PREVIOUS_PAGE_BUTTON_NAME_ITEM, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		itemPart = "Inventories.FriendRequestMenu.NoPendingFriendRequestsItem.";
		if (pConfig.getBoolean(itemPart + "Use")) {
			NO_PENDING_FRIEND_REQUESTS_ITEM = setupHelper.getItemStack(TextIdentifier.NO_PENDING_FRIEND_REQUEST, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		} else NO_PENDING_FRIEND_REQUESTS_ITEM = null;
		if (pConfig.getBoolean("Inventories.PartyGUI.Enabled")) {
			itemPart = "Inventories.PartyGUI.MainMenu.HowToCreateAPartyItem.";
			HOW_TO_CREATE_A_PARTY = setupHelper.getItemStack(TextIdentifier.HOW_TO_CREATE_A_PARTY, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			itemPart = "Inventories.PartyGUI.MainMenu.PlaceHolderItem.";
			if (pConfig.getBoolean(itemPart + "Use"))
				PARTY_MENU_PLACEHOLDER = setupHelper.getItemStack(TextIdentifier.PARTY_MENU_PLACEHOLDER, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			else PARTY_MENU_PLACEHOLDER = null;
			itemPart = "Inventories.MainMenuMenuBar.PartyItem.";
			if (pConfig.getBoolean(itemPart + "Use")) {
				OPEN_PARTY_GUI_ITEM = setupHelper.getItemStack(TextIdentifier.OPEN_PARTY_MENU, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			} else OPEN_PARTY_GUI_ITEM = null;
			itemPart = "Inventories.PartyGUI.PartyMemberDetailView.KickItem.";
			if (pConfig.getBoolean(itemPart + "Use")) {
				KICK_FROM_PARTY_ITEM = setupHelper.getItemStack(TextIdentifier.KICK_FROM_PARTY, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			} else KICK_FROM_PARTY_ITEM = null;
			itemPart = "Inventories.PartyGUI.PartyMemberDetailView.MakeLeader.";
			if (pConfig.getBoolean(itemPart + "Use")) {
				MAKE_NEW_PARTY_LEADER_ITEM = setupHelper.getItemStack(TextIdentifier.MAKE_NEW_PARTY_LEADER, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			} else MAKE_NEW_PARTY_LEADER_ITEM = null;
			itemPart = "Inventories.PartyGUI.PartyMemberDetailView.PlaceHolderItem.";
			if (pConfig.getBoolean(itemPart + "Use"))
				PARTY_DETAIL_VIEW_PLACEHOLDER = setupHelper.getItemStack(TextIdentifier.PARTY_DETAIL_VIEW_PLACE_HOLDER, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			else PARTY_DETAIL_VIEW_PLACEHOLDER = null;
			itemPart = "Inventories.PartyGUI.MainMenu.LeaveParty.";
			if (pConfig.getBoolean(itemPart + "Use"))
				LEAVE_PARTY_ITEM = setupHelper.getItemStack(TextIdentifier.LEAVE_PARTY, itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
			else LEAVE_PARTY_ITEM = null;
		} else {
			HOW_TO_CREATE_A_PARTY = null;
			PARTY_MENU_PLACEHOLDER = null;
			OPEN_PARTY_GUI_ITEM = null;
			KICK_FROM_PARTY_ITEM = null;
			MAKE_NEW_PARTY_LEADER_ITEM = null;
			PARTY_DETAIL_VIEW_PLACEHOLDER = null;
			LEAVE_PARTY_ITEM = null;
		}
	}

	public static ItemManager getInstance() {
		return instance;
	}


}
