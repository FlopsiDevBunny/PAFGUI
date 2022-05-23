package de.simonsator.partyandfriendsgui.api.datarequest.party;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.api.datarequest.DataRequestPlayerInfo;
import de.simonsator.partyandfriendsgui.communication.tasks.CommunicationTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PartyDataRequestCallbackAPI extends CommunicationTask {
	private static PartyDataRequestCallbackAPI instance;
	private final Map<Integer, PartyDataCallBackRunnable> partyRunnableAssignment = new HashMap<>();
	private int idCounter = 0;

	public PartyDataRequestCallbackAPI() {
		super("FetchPartyData");
		instance = this;
	}

	/**
	 * @return Returns an instance of this class
	 */
	public static PartyDataRequestCallbackAPI getInstance() {
		return instance;
	}

	/**
	 * @param pPlayer   The player for whom the party data should be received
	 * @param pRunnable This runnable will be executed when the spigot server receives the party data from the bungeecord
	 * @return An id which can be used to identify the request later on
	 */
	public int fetchPartyData(Player pPlayer, PartyDataCallBackRunnable pRunnable) {
		idCounter++;
		partyRunnableAssignment.put(idCounter, pRunnable);
		JsonObject jobj = new JsonObject();
		jobj.addProperty("task", "FetchPartyData");
		jobj.addProperty("id", idCounter);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			if (partyRunnableAssignment.remove(idCounter) != null) {
				pRunnable.onTimeout(idCounter);
			}
		}, 40);
		PartyFriendsAPI.sendMessage(jobj, pPlayer);
		return idCounter;
	}

	@Override
	public void executeTask(Player pPlayer, JsonObject pJObj) {
		int id = pJObj.get("id").getAsInt();
		PartyDataCallBackRunnable runnable = partyRunnableAssignment.remove(id);
		if (runnable != null) {
			PartyData partyData;
			if (pJObj.get("isInParty").getAsBoolean()) {
				JsonObject leaderObject = pJObj.get("leader").getAsJsonObject();
				JsonArray partyMembersObject = pJObj.get("partyMembers").getAsJsonArray();
				List<DataRequestPlayerInfo> partyMembers = new ArrayList<>(partyMembersObject.size());
				for (JsonElement jsonElement : partyMembersObject) {
					JsonObject jsonObject = jsonElement.getAsJsonObject();
					partyMembers.add(new DataRequestPlayerInfo(jsonObject.get("playerName").getAsString(),
							UUID.fromString(jsonObject.getAsJsonObject().get("playerUUID").getAsString()), jsonObject.get("serverName").getAsString()));
				}
				DataRequestPlayerInfo leaderInfo = new DataRequestPlayerInfo(leaderObject.get("playerName").getAsString(), UUID.fromString(leaderObject.get("playerUUID").getAsString()), leaderObject.get("serverName").getAsString());
				JsonObject propertiesObject = pJObj.get("partyProperties").getAsJsonObject();
				PartyDataProperties partyDataProperties = new PartyDataProperties(propertiesObject.get("isPublic").getAsBoolean());
				partyData = new PartyData(partyMembers, leaderInfo, partyDataProperties);
			} else partyData = new PartyData();
			runnable.onCallback(pPlayer, partyData, id);
		}
	}
}
