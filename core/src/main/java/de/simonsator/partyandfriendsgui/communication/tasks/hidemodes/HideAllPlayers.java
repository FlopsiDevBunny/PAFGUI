package de.simonsator.partyandfriendsgui.communication.tasks.hidemodes;

import de.simonsator.partyandfriendsgui.api.hide.HideMode;
import de.simonsator.partyandfriendsgui.api.hide.contexts.HideContext;
import de.simonsator.partyandfriendsgui.nmsdepending.hider.Hider;
import org.bukkit.entity.Player;

public class HideAllPlayers extends HideMode {
	public HideAllPlayers(Hider pHider) {
		super(4, pHider);
	}

	@Override
	public void executeHide(Player pPlayer1, Player pPlayer2, HideContext pContext) {
		HIDER.hidePlayer(pPlayer1, pPlayer2);
	}
}
