package de.simonsator.partyandfriendsgui.api;

import com.google.gson.JsonObject;
import de.simonsator.datastructures.ItemPackage;
import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.communication.BungeecordCommunication;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class PartyFriendsAPI {
	private static final ArrayList<ItemPackage> CUSTOM_SETTINGS = new ArrayList<>();

	/**
	 * Adds a setting to the Party and Friends GUI settings interface
	 *
	 * @param pSetting The setting
	 * @see de.simonsator.datastructures.ItemPackage
	 */
	public static void addCustomSetting(ItemPackage pSetting) {
		CUSTOM_SETTINGS.add(pSetting);
		Collections.sort(CUSTOM_SETTINGS);
	}

	/**
	 * @return Returns the settings which will be added into the settings
	 * interface of the Party and Friends GUI settings
	 */
	public static ArrayList<ItemPackage> getCustomSettings() {
		return CUSTOM_SETTINGS;
	}

	/**
	 * Opens the main inventory
	 *
	 * @param pPlayer For whom the main inventory should be open
	 * @param pPage   The page that should be opened
	 */
	public static void openMainInventory(Player pPlayer, int pPage) {
		JsonObject jobj = new JsonObject();
		jobj.addProperty("page", pPage);
		jobj.addProperty("task", "OpenMainMenu");
		sendMessage(jobj, pPlayer);
	}

	public static void openMainInventory(Player pPlayer, int pPage, int pNewSortMode) {
		JsonObject jobj = new JsonObject();
		jobj.addProperty("page", pPage);
		jobj.addProperty("changeSorting", pNewSortMode);
		jobj.addProperty("task", "OpenMainMenu");
		sendMessage(jobj, pPlayer);
	}

	/**
	 * Opens the party inventory
	 *
	 * @param pPlayer For whom the party inventory should be open
	 * @param pPage   The page that should be opened
	 */
	public static void openPartyInventory(Player pPlayer, int pPage) {
		JsonObject jobj = new JsonObject();
		jobj.addProperty("page", pPage);
		jobj.addProperty("task", "OpenPartyMenu");
		sendMessage(jobj, pPlayer);
	}

	/**
	 * Opens the hide inventory
	 *
	 * @param pPlayer For whom the hide inventory should be open
	 */
	public static void openHideInventory(Player pPlayer) {
		JsonObject jobj = new JsonObject();
		jobj.addProperty("task", "OpenHideMenu");
		sendMessage(jobj, pPlayer);
	}

	/**
	 * Opens the friend request inventory
	 *
	 * @param pPlayer For whom the friend request inventory should be open
	 */
	public static void openFriendRequestsMenu(Player pPlayer) {
		JsonObject jobj = new JsonObject();
		jobj.addProperty("task", "OpenFriendRequestMenu");
		sendMessage(jobj, pPlayer);
	}

	/**
	 * Opens the settings inventory
	 *
	 * @param pPlayer For whom the settings inventory should be open
	 */
	public static void openSettingsInventory(Player pPlayer) {
		JsonObject jobj = new JsonObject();
		jobj.addProperty("task", "OpenSettingsMenu");
		sendMessage(jobj, pPlayer);
	}

	/**
	 * Sets the hide mode of a player.
	 *
	 * @param pPlayer The player for whom the hide mode should be set.
	 * @param worth   Possible worth:
	 *                <ul>
	 *                <li>0 Shows all Players</li>
	 *                <li>1 Show only Friends and people of the server team</li>
	 *                <li>2 Show only friends</li>
	 *                <li>3 Show only people of the server team</li>
	 *                <li>4 Hide all players</li>
	 *                <li>5 Show only party members</li>
	 *                </ul>
	 */
	public static void setHideMode(Player pPlayer, int worth) {
		JsonObject jobj = new JsonObject();
		jobj.addProperty("newHideMode", worth);
		jobj.addProperty("task", "SetHideSetting");
		sendMessage(jobj, pPlayer);
	}

	/**
	 * Reloads the hide mode for a specific player
	 *
	 * @param pPlayer The player for whom the hide mode should be reloaded
	 * @param pJoin   If he just joined
	 */
	public static void loadHideMode(Player pPlayer, boolean pJoin) {
		JsonObject jobj = new JsonObject();
		jobj.addProperty("task", "HidePlayers");
		jobj.addProperty("join", pJoin);
		sendMessage(jobj, pPlayer);
	}

	/**
	 * Sends a player a friend request
	 *
	 * @param pRequester The sender of the friend request
	 * @param pRequested The receiver
	 */
	@Deprecated
	public static void sendFriendRequest(Player pRequester, String pRequested) {
		sendOrAcceptFriendRequest(pRequester, pRequested);
	}

	public static void sendOrAcceptFriendRequest(Player pRequester, String pRequested) {
		JsonObject toSend = new JsonObject();
		toSend.addProperty("task", "SendOrAcceptFriendRequest");
		toSend.addProperty("receiver", pRequested);
		sendMessage(toSend, pRequester);
	}

	/**
	 * Accepts a friend request (runs through the /friend accept command)
	 *
	 * @param pPlayer The player who accepts the friend request
	 * @param pFriend The friend request who should be accept
	 */
	public static void acceptFriendRequest(Player pPlayer, String pFriend) {
		JsonObject toSend = new JsonObject();
		toSend.addProperty("task", "AcceptFriendRequest");
		toSend.addProperty("friendName", pFriend);
		sendMessage(toSend, pPlayer);
	}

	/**
	 * Declines a friend request (runs through the /friend deny command)
	 *
	 * @param pPlayer The player who is denying the friend request
	 * @param toDeny  The rejected friend request
	 */
	public static void declineFriendRequest(Player pPlayer, String toDeny) {
		JsonObject toSend = new JsonObject();
		toSend.addProperty("task", "DeclineFriendRequest");
		toSend.addProperty("toDeny", toDeny);
		sendMessage(toSend, pPlayer);
	}

	/**
	 * Removes a friend
	 *
	 * @param pPlayer The player who removes the friend
	 * @param pFriend The friend who should be removed
	 */
	public static void deleteFriend(Player pPlayer, String pFriend) {
		JsonObject toSend = new JsonObject();
		toSend.addProperty("task", "RemoveFriend");
		toSend.addProperty("toRemove", pFriend);
		sendMessage(toSend, pPlayer);
	}

	/**
	 * The player who should jump to a friend (runs through the /friend jump
	 * command)
	 *
	 * @param pPlayer The player who jumps to the friend
	 * @param pFriend The friend who should be jump to
	 */
	public static void jumpTo(Player pPlayer, String pFriend) {
		JsonObject toSend = new JsonObject();
		toSend.addProperty("task", "JumpTo");
		toSend.addProperty("playerToJumpTo", pFriend);
		sendMessage(toSend, pPlayer);
	}

	public static void kickPlayer(Player pSender, String pToKick) {
		JsonObject toSend = new JsonObject();
		toSend.addProperty("task", "KickFromParty");
		toSend.addProperty("toKick", pToKick);
		sendMessage(toSend, pSender);
	}

	public static void makeLeader(Player pSender, String pNewLeader) {
		JsonObject toSend = new JsonObject();
		toSend.addProperty("task", "MakePartyLeader");
		toSend.addProperty("toPromote", pNewLeader);
		sendMessage(toSend, pSender);
	}

	public static void leaveParty(Player pPlayer) {
		JsonObject toSend = new JsonObject();
		toSend.addProperty("task", "LeaveParty");
		sendMessage(toSend, pPlayer);
	}

	/**
	 * Invites a player into a party
	 *
	 * @param pInviter The inviter
	 * @param pInvited The player who should be invited into the party
	 */
	public static void inviteIntoParty(Player pInviter, String pInvited) {
		JsonObject toSend = new JsonObject();
		toSend.addProperty("task", "InviteIntoParty");
		toSend.addProperty("toInvite", pInvited);
		sendMessage(toSend, pInviter);
	}

	public static void changeSetting(Player pPlayer, String pSettingName) {
		JsonObject toSend = new JsonObject();
		toSend.addProperty("task", "ChangeSetting");
		toSend.addProperty("setting", pSettingName);
		sendMessage(toSend, pPlayer);
	}

	/**
	 * Changes the friend request setting
	 *
	 * @param pPlayer The player for whom the setting should be changed
	 */
	public static void changeSettingFriendRequests(Player pPlayer) {
		changeSetting(pPlayer, "friendrequests");
	}

	/**
	 * Changes the setting who can invite the player into a party
	 *
	 * @param pPlayer The player for whom the setting should be changed
	 */
	public static void changeSettingParty(Player pPlayer) {
		changeSetting(pPlayer, "party");
	}

	/**
	 * Changes the show always as offline setting
	 *
	 * @param pPlayer The player for whom the setting should be changed
	 */
	public static void changeSettingOffline(Player pPlayer) {
		changeSetting(pPlayer, "offline");
	}

	/**
	 * Changes the jump setting
	 *
	 * @param pPlayer The player for whom the setting should be changed
	 */
	public static void changeSettingJump(Player pPlayer) {
		changeSetting(pPlayer, "jump");
	}

	/**
	 * Changes the receive messages setting
	 *
	 * @param pPlayer The player for whom the setting should be changed
	 */
	public static void changeMessageSetting(Player pPlayer) {
		changeSetting(pPlayer, "messages");
	}

	/**
	 * Changes the receive friend went offline/online notification setting
	 *
	 * @param pPlayer The player for whom the setting should be changed
	 */
	public static void changeStatusNotifySetting(Player pPlayer) {
		changeSetting(pPlayer, "notifyonline");
	}

	/**
	 * Executes a BungeeCord command
	 *
	 * @param pCommand The command (inclusive the arguments) which should be executed
	 * @param pPlayer  The player who will be executing the command
	 */
	public static void executeBungeeCordCommand(String pCommand, Player pPlayer) {
		JsonObject toSend = new JsonObject();
		toSend.addProperty("task", "ExecuteNonPAFCommand");
		toSend.addProperty("command", pCommand);
		sendMessage(toSend, pPlayer);
	}

	/**
	 * Sends a message to the Bungeecord server
	 *
	 * @param message The string which should be send
	 * @param p       The player, from the server who should receive the message
	 */
	private static void sendMessage(String message, Player p) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(stream);
		try {
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.sendPluginMessage(Main.getInstance(), BungeecordCommunication.CHANNEL, stream.toByteArray());
	}

	public static void sendMessage(JsonObject pJObj, Player pPlayer) {
		pJObj.addProperty("senderName", pPlayer.getName());
		pJObj.addProperty("protocolVersion", BungeecordCommunication.PROTOCOL_VERSION);
		sendMessage(pJObj.toString(), pPlayer);
	}

}
