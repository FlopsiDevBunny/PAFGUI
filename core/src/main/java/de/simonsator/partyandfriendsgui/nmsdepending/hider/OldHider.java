package de.simonsator.partyandfriendsgui.nmsdepending.hider;

import org.bukkit.entity.Player;

public class OldHider extends Hider {

	@Override
	public void hidePlayer(Player pPlayer1, Player pPlayer2) {
		pPlayer1.hidePlayer(pPlayer2);
	}

	@Override
	public void showPlayer(Player pPlayer1, Player pPlayer2) {
		pPlayer1.showPlayer(pPlayer2);
	}
}
