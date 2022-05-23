package de.simonsator.partyandfriendsgui.api.hide;

import de.simonsator.partyandfriendsgui.api.hide.contexts.HideContext;
import de.simonsator.partyandfriendsgui.nmsdepending.hider.Hider;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class HideMode {
	public final int HIDE_MODE_ID;
	protected final Hider HIDER;
	private final List<Player> inThisHideMode = new ArrayList<>();
	private final HideContext CONTEXT_CREATOR;

	protected HideMode(int pHideModeId, Hider pHider) {
		this(pHideModeId, pHider, null);
	}

	protected HideMode(int pHideModeId, Hider pHider, HideContext pRequiredContextType) {
		HIDE_MODE_ID = pHideModeId;
		HIDER = pHider;
		CONTEXT_CREATOR = pRequiredContextType;
	}

	public abstract void executeHide(Player pPlayer1, Player pPlayer2, HideContext pContext);

	public HideContext getContextCreator() {
		return CONTEXT_CREATOR;
	}

	public String getContextType() {
		if (CONTEXT_CREATOR == null)
			return "NONE";
		return CONTEXT_CREATOR.CONTEXT_TYPE;
	}


	public void onPlayerJoin(Player pJoined, HideContext pContext) {
		for (Player players : inThisHideMode) {
			if (pJoined.equals(players))
				continue;
			executeHide(players, pJoined, pContext);
		}
	}

	public void removeFromThisHideMode(Player pPlayer) {
		inThisHideMode.remove(pPlayer);
	}

	public void addToThisHideMode(Player pPlayer) {
		inThisHideMode.add(pPlayer);
	}

	protected List<Player> getInThisHideMode() {
		return inThisHideMode;
	}

	public void updateInformation(HideContext pContext) {

	}
}
