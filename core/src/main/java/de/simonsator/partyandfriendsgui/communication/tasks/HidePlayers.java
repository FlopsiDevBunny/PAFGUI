package de.simonsator.partyandfriendsgui.communication.tasks;

import com.google.gson.JsonObject;
import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.hide.HideMode;
import de.simonsator.partyandfriendsgui.api.hide.contexts.HideContext;
import de.simonsator.partyandfriendsgui.communication.tasks.hidemodes.*;
import de.simonsator.partyandfriendsgui.nmsdepending.hider.Hider;
import de.simonsator.partyandfriendsgui.nmsdepending.hider.NewHider;
import de.simonsator.partyandfriendsgui.nmsdepending.hider.OldHider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * @author Simonsator
 * @version 1.0.0 on 07.07.2016
 */
public class HidePlayers extends CommunicationTask implements Listener {
	private final Hider HIDER;
	private final Map<Integer, HideMode> hideModes = new HashMap<>();
	private final List<Player> vanished = new ArrayList<>();
	private final List<HideContext> HIDE_CONTEXT_CREATORS = new ArrayList<>();

	public HidePlayers() {
		super("HidePlayers");
		Hider hider;
		try {
			Player.class.getMethod("hidePlayer", Plugin.class, Player.class);
			hider = new NewHider();
		} catch (NoSuchMethodException | SecurityException e) {
			hider = new OldHider();
		}
		HIDER = hider;
		HideMode hideMode = new ShowOnlyFriends(HIDER);
		addHideMode(hideMode);
		hideMode = new ShowOnlyFriendsAndWithPerm(HIDER);
		addHideMode(hideMode);
		hideMode = new ShowOnlyWithPerm(HIDER);
		addHideMode(hideMode);
		hideMode = new HideAllPlayers(HIDER);
		addHideMode(hideMode);
		hideMode = new ShowAllPlayers(HIDER);
		addHideMode(hideMode);
		hideMode = new ShowOnlyPartyMembers(HIDER);
		addHideMode(hideMode);
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}

	public void addHideMode(HideMode pHideMode) {
		hideModes.put(pHideMode.HIDE_MODE_ID, pHideMode);
		HideContext hideContextCreator = pHideMode.getContextCreator();
		if (hideContextCreator != null) {
			for (HideContext hideContext : HIDE_CONTEXT_CREATORS) {
				if (hideContextCreator.getClass().equals(hideContext.getClass()))
					return;
			}
			HIDE_CONTEXT_CREATORS.add(hideContextCreator);
			Collections.sort(HIDE_CONTEXT_CREATORS);
		}
	}

	@Override
	public void executeTask(Player pPlayer, JsonObject pJObj) {
		Map<String, HideContext> contextMap = getContextMap(pJObj);
		if (pJObj.getAsJsonPrimitive("isUpdate").getAsBoolean()) {
			updateInfo(pPlayer, contextMap);
			return;
		}
		int hideModeId = pJObj.get("hideMode").getAsInt();
		HideMode hideMode = hideModes.get(hideModeId);
		if (hideMode == null) {
			System.out.println("Error hiding player. Hide mode not found.");
			return;
		}
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (players.equals(pPlayer))
				continue;
			hideMode.executeHide(pPlayer, players, contextMap.get(hideMode.getContextType()));
		}
		removeFromList(pPlayer);
		hideMode.addToThisHideMode(pPlayer);
		if (pJObj.get("join").getAsBoolean()) {
			for (HideMode hideModeI : hideModes.values())
				hideModeI.onPlayerJoin(pPlayer, contextMap.get(hideModeI.getContextType()));
		}
		vanishVanishedPlayers(pPlayer);
	}

	private void updateInfo(Player pPlayer, Map<String, HideContext> contextMap) {
		for (HideMode hideModeI : hideModes.values())
			hideModeI.updateInformation(contextMap.get(hideModeI.getContextType()));
		vanishVanishedPlayers(pPlayer);
	}

	private void vanishVanishedPlayers(Player pPlayer) {
		for (Player players : vanished)
			HIDER.hidePlayer(pPlayer, players);
	}

	private Map<String, HideContext> getContextMap(JsonObject pJObj) {
		Map<String, HideContext> contextMap = new HashMap<>();
		for (HideContext hideContextCreators : HIDE_CONTEXT_CREATORS) {
			HideContext hideContext = hideContextCreators.toHideContext(pJObj, contextMap);
			contextMap.put(hideContext.CONTEXT_TYPE, hideContext);
		}
		return contextMap;
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent pEvent) {
		removeFromList(pEvent.getPlayer());
		removeVanished(pEvent.getPlayer());
	}

	private void removeFromList(Player pPlayer) {
		for (HideMode hideMode : hideModes.values())
			hideMode.removeFromThisHideMode(pPlayer);
	}

	public void addVanished(Player pPlayer) {
		vanished.add(pPlayer);
	}

	public void removeVanished(Player pPlayer) {
		vanished.remove(pPlayer);
	}
}
