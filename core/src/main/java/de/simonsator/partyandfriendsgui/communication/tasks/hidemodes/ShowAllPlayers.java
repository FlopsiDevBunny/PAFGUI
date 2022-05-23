package de.simonsator.partyandfriendsgui.communication.tasks.hidemodes;

import de.simonsator.partyandfriendsgui.api.hide.HideMode;
import de.simonsator.partyandfriendsgui.api.hide.contexts.HideContext;
import de.simonsator.partyandfriendsgui.nmsdepending.hider.Hider;
import org.bukkit.entity.Player;

public class ShowAllPlayers extends HideMode {
	public ShowAllPlayers(Hider pHider) {
		super(0, pHider);
	}

	@Override
	public void removeFromThisHideMode(Player pPlayer) {
	}

	@Override
	public void addToThisHideMode(Player pPlayer) {
	}

	@Override
	public void executeHide(Player pPlayer1, Player pPlayer2, HideContext pContext) {
		HIDER.showPlayer(pPlayer1, pPlayer2);
	}

	@Override
	public void onPlayerJoin(Player pJoined, HideContext pContext) {

	}
}
