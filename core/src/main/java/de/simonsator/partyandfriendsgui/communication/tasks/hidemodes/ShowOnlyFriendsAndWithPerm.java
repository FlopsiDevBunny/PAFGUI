package de.simonsator.partyandfriendsgui.communication.tasks.hidemodes;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.hide.contexts.FriendHideContext;
import de.simonsator.partyandfriendsgui.api.hide.contexts.HideContext;
import de.simonsator.partyandfriendsgui.nmsdepending.hider.Hider;
import org.bukkit.entity.Player;

public class ShowOnlyFriendsAndWithPerm extends ShowOnlyFriends {
	public ShowOnlyFriendsAndWithPerm(Hider pHider) {
		super(2, pHider);
	}

	@Override
	public void executeHide(Player pPlayer1, Player pPlayer2, HideContext pContext) {
		FriendHideContext friendHideContext = (FriendHideContext) pContext;
		assert friendHideContext.FRIENDS != null;
		if (friendHideContext.FRIENDS.contains(pPlayer2.getName())
				|| pPlayer2.hasPermission(Main.getInstance().getConfig().getString("General.PermissionNoHide")))
			HIDER.showPlayer(pPlayer1, pPlayer2);
		else
			HIDER.hidePlayer(pPlayer1, pPlayer2);
	}

	@Override
	public void onPlayerJoin(Player pJoined, HideContext pContext) {
		FriendHideContext friendHideContext = (FriendHideContext) pContext;
		for (Player players : getInThisHideMode()) {
			if (pJoined.equals(players))
				continue;
			boolean hasPerm = pJoined.hasPermission(Main.getInstance().getConfig().getString("General.PermissionNoHide"));
			assert friendHideContext.FRIENDS != null;
			if (friendHideContext.FRIENDS.contains(players.getName())
					|| hasPerm)
				HIDER.showPlayer(players, pJoined);
			else
				HIDER.hidePlayer(players, pJoined);
		}
	}

	@Override
	public void updateInformation(HideContext pContext) {
		FriendHideContext friendHideContext = (FriendHideContext) pContext;
		assert friendHideContext.UPDATE_TYPE != null;
		if (friendHideContext.UPDATE_TYPE.equals("FriendAdded")) {
			updateFriendAddedInfo(friendHideContext);
		} else if (friendHideContext.UPDATE_TYPE.equals("FriendRemoved")) {
			updateFriendInfo(friendHideContext, new RunnableHider() {
				@Override
				public void run(Player friend1, Player friend2) {
					if (!friend2.hasPermission(Main.getInstance().getConfig().getString("General.PermissionNoHide")))
						HIDER.hidePlayer(friend1, friend2);
				}
			}, new RunnableHider() {
				@Override
				public void run(Player friend1, Player friend2) {
					if (!friend1.hasPermission(Main.getInstance().getConfig().getString("General.PermissionNoHide")))
						HIDER.hidePlayer(friend2, friend1);
				}
			});
		}
	}
}