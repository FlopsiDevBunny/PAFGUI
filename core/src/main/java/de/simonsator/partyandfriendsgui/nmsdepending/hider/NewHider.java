package de.simonsator.partyandfriendsgui.nmsdepending.hider;

import de.simonsator.partyandfriendsgui.Main;
import org.bukkit.entity.Player;

public class NewHider extends Hider {
	@Override
	public void hidePlayer(Player pPlayer1, Player pPlayer2) {
		pPlayer1.hidePlayer(Main.getInstance(), pPlayer2);
	}

	@Override
	public void showPlayer(Player pPlayer1, Player pPlayer2) {
		pPlayer1.showPlayer(Main.getInstance(), pPlayer2);
	}
}
