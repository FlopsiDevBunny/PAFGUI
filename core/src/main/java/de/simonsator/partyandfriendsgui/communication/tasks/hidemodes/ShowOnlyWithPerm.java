package de.simonsator.partyandfriendsgui.communication.tasks.hidemodes;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.hide.HideMode;
import de.simonsator.partyandfriendsgui.api.hide.contexts.HideContext;
import de.simonsator.partyandfriendsgui.nmsdepending.hider.Hider;
import org.bukkit.entity.Player;

public class ShowOnlyWithPerm extends HideMode {
	public ShowOnlyWithPerm(Hider pHider) {
		super(3, pHider);
	}

	@Override
	public void executeHide(Player pPlayer1, Player pPlayer2, HideContext pContext) {
		if (pPlayer2.hasPermission(Main.getInstance().getConfig().getString("General.PermissionNoHide"))) {
			HIDER.showPlayer(pPlayer1, pPlayer2);
		} else {
			HIDER.hidePlayer(pPlayer1, pPlayer2);
		}
	}
}
