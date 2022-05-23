package de.simonsator.partyandfriendsgui.communication;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.datarequest.party.PartyDataRequestCallbackAPI;
import de.simonsator.partyandfriendsgui.communication.tasks.*;
import de.simonsator.partyandfriendsgui.communication.tasks.party.OpenPartyMenu;
import de.simonsator.partyandfriendsgui.utilities.SetHeadOwner;
import de.simonsator.partyandfriendsgui.utilities.SetHeadOwner112;
import de.simonsator.partyandfriendsgui.utilities.SetHeadOwner113;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Simonsator
 * @version 1.0.0 on 06.07.2016
 */
public class BungeecordCommunication implements PluginMessageListener {
	public static final String CHANNEL = "paf:gui";
	public static final int PROTOCOL_VERSION = 5;
	private static BungeecordCommunication instance;
	public final SetHeadOwner SET_HEAD_OWNER;
	private final Gson GSON = new Gson();
	private final ArrayList<CommunicationTask> tasks = new ArrayList<>();

	public BungeecordCommunication(String pOnlineSuffix, String pOfflineSuffix) {
		instance = this;
		tasks.add(new OpenFriendMenu(pOnlineSuffix, pOfflineSuffix));
		tasks.add(new HidePlayers());
		tasks.add(new OpenHideMenu());
		tasks.add(new OpenFriendRequestMenu());
		tasks.add(new OpenSettingsMenu());
		if (Main.getInstance().getConfig().getBoolean("Inventories.PartyGUI.Enabled")) {
			tasks.add(new OpenPartyMenu());
		}
		tasks.add(new PartyDataRequestCallbackAPI());
		if (Bukkit.getVersion().contains("1.7") ||
				Bukkit.getVersion().contains("1.8") ||
				Bukkit.getVersion().contains("1.9") ||
				Bukkit.getVersion().contains("1.10") ||
				Bukkit.getVersion().contains("1.11") ||
				Bukkit.getVersion().contains("1.12"))
			SET_HEAD_OWNER = new SetHeadOwner112();
		else SET_HEAD_OWNER = new SetHeadOwner113();
	}

	public static BungeecordCommunication getInstance() {
		return instance;
	}

	/**
	 * Receive message from the BungeeCord
	 *
	 * @param channel The channel
	 * @param player  The player
	 * @param bytes   The byte code
	 */
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
		if (!channel.equals(CHANNEL))
			return;
		ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
		DataInputStream in = new DataInputStream(stream);
		try {
			executeTask(GSON.fromJson(in.readUTF(), JsonObject.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Decides what should be done
	 *
	 * @param pReceived The Json which was send by the BungeeCord.
	 */
	private void executeTask(JsonObject pReceived) {
		Player player = Bukkit.getPlayer(pReceived.get("receiverName").getAsString());
		if (player == null)
			return;
		if (protocolCheckFailed(pReceived.get("protocolVersion"), player))
			return;
		for (CommunicationTask task : tasks)
			if (task.isApplicable(pReceived.get("task").getAsString()))
				task.executeTask(player, pReceived);
	}

	private boolean protocolCheckFailed(JsonElement protocolVersion, Player player) {
		if (protocolVersion == null) {
			player.sendMessage("§cYou need to update Party and Friends on the BungeeCord.");
			return true;
		} else {
			int bungeeProtocolVersion = protocolVersion.getAsInt();
			if (bungeeProtocolVersion < PROTOCOL_VERSION) {
				player.sendMessage("§cYou need to update Party and Friends on the BungeeCord.");
				return true;
			} else if (bungeeProtocolVersion > PROTOCOL_VERSION) {
				player.sendMessage("§cYou need to update Party and Friends on the Spigot Server.");
				return true;
			}
		}
		return false;
	}

	public void registerTask(CommunicationTask pTask) {
		tasks.add(pTask);
	}

	public CommunicationTask getTask(Class<? extends CommunicationTask> pClass) {
		for (CommunicationTask task : tasks)
			if (task.getClass().equals(pClass))
				return task;
		return null;
	}
}
