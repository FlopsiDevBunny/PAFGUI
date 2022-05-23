package de.simonsator.partyandfriendsgui.manager;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Simonsator
 * @version 1.0.0 on 09.07.2016
 */
public class LanguageManager {
	private static LanguageManager instance;
	private final Language LANGUAGE;
	private final FileConfiguration CONFIG;

	public LanguageManager(FileConfiguration pConfig) {
		CONFIG = pConfig;
		if (pConfig.getBoolean("General.UseOwnLanguageFile"))
			LANGUAGE = Language.OWN;
		else if (pConfig.getString("General.Language").equalsIgnoreCase("english"))
			LANGUAGE = Language.ENGLISH;
		else
			LANGUAGE = Language.GERMAN;
		instance = this;
	}

	public static LanguageManager getInstance() {
		return instance;
	}

	public String getText(TextIdentifier pIdentifier) {
		String toSend = null;
		switch (pIdentifier) {
			case NAME_TOO_LONG_FRIENDS:
				return "§8[§5§lFriends§8]§r §7The name of the player is too long. Please use the command &6/friend §7instead of the GUI";
			case NAME_TOO_LONG_PARTY:
				return "§8[§5§lFriends§8]§r §7The name of the player is too long. Please use the command &6/party §7instead of the GUI";
			case FRIENDS_MENU:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5§lFriends §7Page ";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendMenu.Name");
						break;
					case GERMAN:
						toSend = "§5§lFreunde §7Seite ";
						break;
				}
				break;
			case POST_FRIEND_PAGE:
				if (LANGUAGE == Language.OWN)
					toSend = CONFIG.getString("Inventories.FriendMenu.PastPage");
				else toSend = "";
				break;
			case HIDE_PLAYERS_MENU:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§6Hide Players";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.HideInventory.Name");
						break;
					case GERMAN:
						toSend = "§6Verstecke Spieler";
						break;
				}
				break;
			case FRIEND_REQUEST_MENU:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5§lFriend requests";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendRequestMenu.Name");
						break;
					case GERMAN:
						toSend = "§5§lFreundschaftsanfragen";
						break;
				}
				break;
			case SETTINGS_MENU:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§7Settings";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.SettingsMenu.Name");
						break;
					case GERMAN:
						toSend = "§7§lEinstellungen";
						break;
				}
				break;
			case HEAD_NAME:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5Profile";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.ToolBar.ProfileItem.Text");
						break;
					case GERMAN:
						toSend = "§5Profil";
						break;
				}
				break;
			case BACK_ITEM_NAME:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5Back%LINE_BREAK%§7Click on this item to%LINE_BREAK%§7return to the main menu.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.EveryMenu.BackItem.Text");
						break;
					case GERMAN:
						toSend = "§5Zurück";
						break;
				}
				break;
			case SETTINGS_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§7Settings%LINE_BREAK%§7Opens a menu where you can%LINE_BREAK%§7change your settings.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.MainMenuMenuBar.SettingsItem.Text");
						break;
					case GERMAN:
						toSend = "§7Einstellungen%LINE_BREAK%§7Öffnet ein Menü wo du%LINE_BREAK%§7deine Einstellungen ändern kannst.";
						break;
				}
				break;
			case FRIENDSHIP_INVITATION_NAME:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5Friend requests%LINE_BREAK%§7Opens a menu where you can%LINE_BREAK%§7view and accept or decline friend%LINE_BREAK%§7requests.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.MainMenuMenuBar.FriendRequestItem.Text");
						break;
					case GERMAN:
						toSend = "§5Freundschaftsanfragen%LINE_BREAK%§7Öffnet ein Menü wo du dir Freundschafts-%LINE_BREAK%§7anfragen ansehen und akzeptieren%LINE_BREAK%§7oder ablehnen kannst.";
						break;
				}
				break;
			case ACCEPT_FRIEND_REQUEST_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aAccept friend request%LINE_BREAK%§7Click on this item%LINE_BREAK%§7to accept the friend request.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendAcceptMenu.AcceptItem.Text");
						break;
					case GERMAN:
						toSend = "§aDie Freundschaftsanfrage akzeptieren";
						break;
				}
				break;
			case DENY_FRIEND_REQUEST_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§cDecline the friend request%LINE_BREAK%§7Click on this item%LINE_BREAK%§7to decline the friend request.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendAcceptMenu.DenyItem.Text");
						break;
					case GERMAN:
						toSend = "§cDie Freundschaftsanfrage ablehnen";
						break;
				}
				break;
			case HIDE_PLAYERS_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§6Hide Players";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.ToolBar.HideItem.Text");
						break;
					case GERMAN:
						toSend = "§6Verstecke Spieler";
						break;
				}
				break;
			case DELETE_FRIEND_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§cRemove this friend%LINE_BREAK%§7If you click on this item your friend will%LINE_BREAK%§7be removed from your friend list and you from his.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendDetailView.DeleteItem.Text");
						break;
					case GERMAN:
						toSend = "§cEntferne diesen Freund";
						break;
				}
				break;
			case INVITE_INTO_PARTY_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5Invite this player into your party%LINE_BREAK%§7If you click on this item a%LINE_BREAK%§7party invite will be send to your friend.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendDetailView.InviteIntoParty.Text");
						break;
					case GERMAN:
						toSend = "§5Lade diesen Spieler in deine Party ein";
						break;
				}
				break;
			case JUMP_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§6Jump to this Player%LINE_BREAK%§7If you click on this item you will%LINE_BREAK%§7join the server of your friend.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendDetailView.JumpTo.Text");
						break;
					case GERMAN:
						toSend = "§6Springe zu diesem Spieler";
						break;
				}
				break;
			case CONFIRM_FRIEND_DELETE_MENU_NAME:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§cRemove §7%PLAYER_NAME%";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.ConfirmFriendDeleteMenu.Name");
						break;
					case GERMAN:
						toSend = "§cEntferne §7%PLAYER_NAME%";
						break;
				}
				break;
			case FRIEND_DELETE_CONFIRM_ACCEPT:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§cConfirm to delete this friend from your friend list.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmAccept.Text");
						break;
					case GERMAN:
						toSend = "§cBestätige, dass du diesen Freund von deiner Freundesliste entfernen möchtest.";
						break;
				}
				break;
			case FRIEND_DELETE_CONFIRM_DECLINE:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aDo not delete this friend from your friend list.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmDecline.Text");
						break;
					case GERMAN:
						toSend = "§aEntferne diesen Freund nicht von deiner Freundesliste.";
						break;
				}
				break;
			case FRIEND_DELETE_CONFIRM_PLACEHOLDER:
				switch (LANGUAGE) {
					case OWN:
						toSend = CONFIG.getString("Inventories.ConfirmFriendDeleteMenu.PlaceHolderItem.Text");
						break;
					default:
						toSend = " ";
						break;
				}
				break;
			case SHOW_ALL_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aShow all players";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.HideInventory.HideModeItems.ShowAll.Text");
						break;
					case GERMAN:
						toSend = "§aZeige alle Spieler";
						break;
				}
				break;
			case SHOW_FRIENDS_AND_PEOPLE_WITH_PERMISSION_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§eShow only friends and people of the server team";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.HideInventory.HideModeItems.ShowOnlyFriendsAndPeopleOfTheServerTeam.Text");
						break;
					case GERMAN:
						toSend = "§eZeige nur Freunde und Spieler vom Server Team";
						break;
				}
				break;
			case SHOW_ONLY_YOUR_FRIENDS_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§6Show only friends";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.HideInventory.HideModeItems.ShowOnlyFriends.Text");
						break;
					case GERMAN:
						toSend = "§6Zeige nur Freunde";
						break;
				}
				break;
			case SHOW_ONLY_PEOPLE_FROM_THE_SERVER_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5Show only people of the server team";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.HideInventory.HideModeItems.ShowOnlyPeopleOfTheServerTeamItem.Text");
						break;
					case GERMAN:
						toSend = "§5Zeige nur Spieler vom Server Team";
						break;
				}
				break;
			case SHOW_NOBODY_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§cHide All Players";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.HideInventory.HideModeItems.HideAll.Text");
						break;
					case GERMAN:
						toSend = "§4Verstecke alle Spieler";
						break;
				}
				break;
			case SHOW_PARTY_MEMBERS:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§dShow only party members";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.HideInventory.HideModeItems.ShowOnlyPartyMembers.Text");
						break;
					case GERMAN:
						toSend = "§dZeige nur Spieler die in deiner Party sind";
						break;
				}
				break;
			case HIDE_INVENTORY_PLACE_HOLDER:
				switch (LANGUAGE) {
					case OWN:
						toSend = CONFIG.getString("Inventories.HideInventory.PlaceHolderItem.Text");
						break;
					default:
						toSend = " ";
						break;
				}
				break;
			case FRIEND_DETAIL_VIEW_PLACE_HOLDER:
				switch (LANGUAGE) {
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendDetailView.PlaceHolderItem.Text");
						break;
					default:
						toSend = " ";
						break;
				}
				break;
			case FRIEND_ACCEPT_MENU_PLACEHOLDER:
				switch (LANGUAGE) {
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendAcceptMenu.PlaceHolderItem.Text");
						break;
					default:
						toSend = " ";
						break;
				}
				break;
			case YOU_RECEIVE_FRIEND_REQUESTS_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aYou will now receive friend requests";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.YouReceiveFriendRequests") + "§a";
						break;
					case GERMAN:
						toSend = "§aDu erhälst Freundschaftsanfragen";
						break;
				}
				break;
			case YOU_RECEIVE_NO_FRIEND_REQUESTS_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§cYou will not receive friend requests";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.YouReceiveNoFriendRequests") + "§b";
						break;
					case GERMAN:
						toSend = "§cDu erhälst keine Freundschaftsanfragen";
						break;
				}
				break;
			case YOU_RECEIVE_PARTY_INVITATIONS_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aYou will receive party invitations from everyone";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.YouReceivePartyInvitations") + "§c";
						break;
					case GERMAN:
						toSend = "§aDu erhälst Party Einladungen von jedem";
						break;
				}
				break;
			case YOU_RECEIVE_NO_PARTY_INVITATIONS_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§cYou will only receive party invitations from your friends";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.YouReceivePartyInvitationsOnlyFromFriends") + "§d";
						break;
					case GERMAN:
						toSend = "§cDu erhälst Party Einladungen nur von Freunden";
						break;
				}
				break;
			case YOUR_STATUS_WILL_BE_SHOWN_ONLINE_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aYour online status will be shown";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.YourStatusWillBeShownOnline") + "§e";
						break;
					case GERMAN:
						toSend = "§aDein Online Status wird angezeigt";
						break;
				}
				break;
			case YOUR_STATUS_WILL_BE_SHOWN_OFFLINE_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§cYour online status will not be shown";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.YourStatusWillBeShownOffline") + "§f";
						break;
					case GERMAN:
						toSend = "§cDein Online Status wird nicht angezeigt";
						break;
				}
				break;
			case YOU_RECEIVE_ONLINE_STATUS_NOTIFICATION_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aYou will receive a notification when a friend of yours goes online/offline";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.YouReceiveOnlineStatusNotifcation") + "§0";
						break;
					case GERMAN:
						toSend = "§aDu erhälst eine Benachrichtigung, wenn ein Freund Online/Offline geht";
						break;
				}
				break;
			case YOU_RECEIVE_NO_ONLINE_STATUS_NOTIFICATION_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§cYou will not receive a notification when a friend of yours goes online/offline";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.YouDoNotReceiveOnlineStatusNotifcation") + "§1";
						break;
					case GERMAN:
						toSend = "§cDu erhälst keine Benachrichtigung mehr, wenn ein Freund Online/Offline geht";
						break;
				}
				break;

			case JUMP_ALLOWED_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§cNo one can jump to you";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.NoJump") + "§2";
						break;
					case GERMAN:
						toSend = "§cFreunde können nicht zu dir springen";
						break;
				}
				break;
			case NO_JUMP_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aFriends can jump to you";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.JumpAllowed") + "§3";
						break;
					case GERMAN:
						toSend = "§aFreunde können zu dir springen";
						break;
				}
				break;
			case YOU_RECEIVE_MESSAGES_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aYou receive messages";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.YouReceiveMessages") + "§4";
						break;
					case GERMAN:
						toSend = "§aDu erhälst Nachrichten";
						break;
				}
				break;
			case YOU_RECEIVE_NO_MESSAGES_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§cYou will not receive messages";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.YouWontReceiveMessages") + "§5";
						break;
					case GERMAN:
						toSend = "§cDu erhälst keine Nachrichten";
						break;
				}
				break;
			case WANT_RECEIVE_FRIEND_REQUESTS_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5Do you want to receive friend requests?";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.SettingsMenu.FriendRequestSetting.TopItem.Text");
						break;
					case GERMAN:
						toSend = "§5Möchtest du Freundschaftsanfragen erhalten?";
						break;
				}
				break;
			case WANT_RECEIVE_PARTY_INVITATIONS_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5Do you want to receive party invitations from everybody?";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.SettingsMenu.PartyInviteSetting.TopItem.Text");
						break;
					case GERMAN:
						toSend = "§5Möchtest du Party Einladungen von jedem Spieler erhalten?";
						break;
				}
				break;
			case WANT_TO_BE_SHOWN_AS_ONLINE_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aDo you want your friends to see that you are online?";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.SettingsMenu.ShowOnlineSetting.TopItem.Text");
						break;
					case GERMAN:
						toSend = "§aMöchtest du, dass deine Freunde sehen, dass du Online bist?";
						break;
				}
				break;
			case WANT_TO_RECEIVE_FRIEND_STATUS_NOTIFICATION:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aDo you want to receive a notification if a friend goes offline/online?";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.SettingsMenu.NotifyOnlineStatusChange.TopItem.Text");
						break;
					case GERMAN:
						toSend = "§aMöchtest du benachrichtigt werden, wenn ein Freund Online/Offline geht?";
						break;
				}
				break;
			case SHOULD_FRIENDS_BE_ABLE_TO_JUMP_TO_YOU_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§6Should your friends be allowed to jump to you?";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.SettingsMenu.FriendJumpSetting.TopItem.Text");
						break;
					case GERMAN:
						toSend = "§6Sollen Freunde zu dir springen dürfen?";
						break;
				}
				break;
			case WANT_RECEIVE_MESSAGES_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aDo you want to receive messages?";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.SettingsMenu.ReceiveFriendMSG.TopItem.Text");
						break;
					case GERMAN:
						toSend = "§aMöchtest du von Freunden angeschrieben werden können?";
						break;
				}
				break;
			case PREVIOUS_PAGE_BUTTON_NAME_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aPrevious";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.MainMenuMenuBar.PreviousPageButton.Text");
						break;
					case GERMAN:
						toSend = "§aZurück";
						break;
				}
				break;
			case NEXT_PAGE_BUTTON_ITEM:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§aNext";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.MainMenuMenuBar.NextPageButton.Text");
						break;
					case GERMAN:
						toSend = "§aVor";
						break;
				}
				break;
			case FRIEND_SORT_BY_LAST_ONLINE:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§dCurrently sorting by §blast online";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendMenu.Sorting.SortByLastOnline.Text");
						break;
					case GERMAN:
						toSend = "§dMomentan wird nach §bzuletzt online §dsortiert";
						break;
				}
				break;
			case FRIEND_SORT_BY_ALPHABETIC:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§dCurrently sorting by §3ascending alphabetic order";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendMenu.Sorting.SortByAlphabeticAscending.Text");
						break;
					case GERMAN:
						toSend = "§dMomentan wird §3alphabetisch aufsteigend §dsortiert";
						break;
				}
				break;
			case FRIEND_SORT_BY_REVERSE_ALPHABETIC:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§dCurrently sorting by §cdescending alphabetic order.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendMenu.Sorting.SortByAlphabeticDescending.Text");
						break;
					case GERMAN:
						toSend = "§dMomentan wird §calphabetisch absteigend §dsortiert";
						break;
				}
				break;
			case FRIEND_SORT_BY_FRIENDSHIP_DURATION:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§dCurrently sorting by §5friendship duration.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendMenu.Sorting.SortByFriendshipDuration.Text");
						break;
					case GERMAN:
						toSend = "§dMomentan wird nach §5Freundschafts Dauer §dsortiert";
						break;
				}
				break;
			case LAST_SEEN:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§7Last seen at [LAST_SEEN]";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendMenu.OfflineFriends.LastSeen");
						break;
					case GERMAN:
						toSend = "§7Zuletzt online am [LAST_SEEN]";
						break;
				}
				break;
			case ONLINE_ON:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§7Online on §e[SERVER]";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendMenu.OnlineFriends.OnlineOn");
						break;
					case GERMAN:
						toSend = "§7Online auf §e[SERVER]";
						break;
				}
				break;
			case GUI_COOLDOWN:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§8[§5§lFriends§8] §cPlease don't use this item so often.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.ToolBar.ProfileItem.Cooldown.Message");
						break;
					case GERMAN:
						toSend = "§8[§5§lFriends§8] §cBitte nutze das Item nicht so oft.";
						break;
				}
				break;
			case HIDE_COOLDOWN:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§8[§5§lFriends§8] §cPlease don't use this item so often.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.ToolBar.HideItem.Cooldown.Message");
						break;
					case GERMAN:
						toSend = "§8[§5§lFriends§8] §cBitte nutze das Item nicht so oft.";
						break;
				}
				break;
			case PARTY_MENU_NAME:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5§lYour Party §7Page ";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.PartyGUI.MainMenu.Name");
						break;
					case GERMAN:
						toSend = "§5§lDeine Party §7Seite ";
						break;
				}
				break;
			case PAST_PARTY_PAGE:
				if (LANGUAGE == Language.OWN)
					toSend = CONFIG.getString("Inventories.PartyGUI.MainMenu.PastPage");
				else toSend = "";
				break;
			case HOW_TO_CREATE_A_PARTY:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§4You are not in a party. Invite people into a party by using /party invite";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.PartyGUI.MainMenu.HowToCreateAPartyItem.Text");
						break;
					case GERMAN:
						toSend = "§4Du bist in keiner Party. Nutze /party invite um Leute in eine Party einzuladen.";
						break;
				}
				break;
			case PARTY_MENU_PLACEHOLDER:
				switch (LANGUAGE) {
					case OWN:
						toSend = CONFIG.getString("Inventories.PartyGUI.MainMenu.PlaceHolderItem.Text");
						break;
					default:
						toSend = " ";
						break;
				}
				break;
			case LORE_IS_LEADER:
				switch (LANGUAGE) {
					case ENGLISH:
					case GERMAN:
						toSend = "§7Party §5Leader";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.PartyGUI.MainMenu.Leader.Lore");
						break;
				}
				break;
			case LORE_IS_MEMBER:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§7Party §6Member";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.PartyGUI.MainMenu.PartyMembers.Lore");
						break;
					case GERMAN:
						toSend = "§7Party §6Mitglied";
						break;
				}
				break;
			case OPEN_PARTY_MENU:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5Open party menu";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.MainMenuMenuBar.PartyItem.Text");
						break;
					case GERMAN:
						toSend = "§5Öffne Party Menü";
						break;
				}
				break;
			case OPEN_FRIEND_MENU:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§eOpen friend menu";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.MainMenuMenuBar.FriendItem.Text");
						break;
					case GERMAN:
						toSend = "§eÖffne Freunde Menü";
						break;
				}
				break;
			case KICK_FROM_PARTY:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§cKick this player from the party";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.PartyGUI.PartyMemberDetailView.KickItem.Text");
						break;
					case GERMAN:
						toSend = "§cKicke diesen Spieler aus der Party";
						break;
				}
				break;
			case MAKE_NEW_PARTY_LEADER:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§eMakes this player the new leader of this party";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.PartyGUI.PartyMemberDetailView.MakeLeader.Text");
						break;
					case GERMAN:
						toSend = "§eMacht diesen Spieler zum neuen Leader der Party";
						break;
				}
				break;
			case PARTY_MEMBER_DETAIL_VIEW_PREFIX:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5Party §6Member: ";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.PartyGUI.PartyMemberDetailView.Prefix");
						break;
					case GERMAN:
						toSend = "§5Party §6Mitglied: ";
						break;
				}
				break;
			case PARTY_MEMBER_DETAIL_VIEW_SHORT_PREFIX:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§6Member: ";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.PartyGUI.PartyMemberDetailView.ShortPrefix");
						break;
					case GERMAN:
						toSend = "§6Mitglied: ";
						break;
				}
				break;
			case PARTY_DETAIL_VIEW_PLACE_HOLDER:
				switch (LANGUAGE) {
					case ENGLISH:
					case GERMAN:
						toSend = " ";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.PartyGUI.PartyMemberDetailView.PlaceHolderItem.Text");
						break;
				}
				break;
			case MENU_BAR_PLACEHOLDER:
				switch (LANGUAGE) {
					case OWN:
						toSend = CONFIG.getString("Inventories.MainMenuMenuBar.PlaceHolderItem.Text");
						break;
					default:
						toSend = " ";
						break;
				}
				break;
			case LEAVE_PARTY:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§4Leave your party.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.PartyGUI.MainMenu.LeaveParty.Text");
						break;
					case GERMAN:
						toSend = "§4Verlasse deine Party.";
						break;
				}
				break;
			case NO_PENDING_FRIEND_REQUEST:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§5You do not have any pending friend requests.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendRequestMenu.NoPendingFriendRequestsItem.Text");
						break;
					case GERMAN:
						toSend = "§5Du hast keine Freundschaftsanfragen.";
						break;
				}
				break;
			case NO_FRIENDS:
				switch (LANGUAGE) {
					case ENGLISH:
						toSend = "§4You have not added any friends as yet.%LINE_BREAK%§4Add friends by using §5/friend add§4.";
						break;
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendMenu.NoFriendsItem.Text");
						break;
					case GERMAN:
						toSend = "§4Du hast bis jetzt noch keine Freunde hinzugefügt.%LINE_BREAK%§4Füge Freunde hinzu mit §5/friend add§4.";
						break;
				}
				break;
			case FRIEND_MENU_PLACEHOLDER:
				switch (LANGUAGE) {
					case OWN:
						toSend = CONFIG.getString("Inventories.FriendMenu.PlaceHolderItem.Text");
						break;
					default:
						toSend = " ";
						break;
				}
				break;
			case FRIEND_HEAD_ONLINE_SUFFIX:
				switch (LANGUAGE) {
					case ENGLISH:
					case GERMAN:
						toSend = "Online";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.Experimental.FriendHeadOnlineSuffix");
						break;
				}
				break;
			case FRIEND_HEAD_OFFLINE_SUFFIX:
				switch (LANGUAGE) {
					case ENGLISH:
					case GERMAN:
						toSend = "Offline";
						break;
					case OWN:
						toSend = CONFIG.getString("Messages.Experimental.FriendHeadOfflineSuffix");
						break;
				}
				break;
			default:
				break;
		}
		return StringEscapeUtils.unescapeJava(toSend);
	}

	private enum Language {
		GERMAN, ENGLISH, OWN
	}
}
