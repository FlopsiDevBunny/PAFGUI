package de.simonsator.partyandfriendsgui.communication.tasks.hidemodes;

import de.simonsator.partyandfriendsgui.api.hide.HideMode;
import de.simonsator.partyandfriendsgui.api.hide.contexts.FriendHideContext;
import de.simonsator.partyandfriendsgui.api.hide.contexts.HideContext;
import de.simonsator.partyandfriendsgui.nmsdepending.hider.Hider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ShowOnlyFriends extends HideMode {
	public ShowOnlyFriends(Hider pHider) {
		this(1, pHider);
	}

	ShowOnlyFriends(int pHideMode, Hider pHider) {
		super(pHideMode, pHider, new FriendHideContext());
	}


	@Override
	public void executeHide(Player pPlayer1, Player pPlayer2, HideContext pContext) {
		FriendHideContext friendHideContext = (FriendHideContext) pContext;
		assert friendHideContext.FRIENDS != null;
		if (friendHideContext.FRIENDS.contains(pPlayer2.getName()))
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
			assert friendHideContext.FRIENDS != null;
			if (friendHideContext.FRIENDS.contains(players.getName()))
				HIDER.showPlayer(players, pJoined);
			else
				HIDER.hidePlayer(players, pJoined);
		}
	}

	protected void updateFriendAddedInfo(FriendHideContext pContext) {
		updateFriendInfo(pContext, new RunnableHider() {
			@Override
			public void run(Player friend1, Player friend2) {
				HIDER.showPlayer(friend1, friend2);
			}
		}, new RunnableHider() {
			@Override
			public void run(Player friend1, Player friend2) {
				HIDER.showPlayer(friend2, friend1);
			}
		});
	}

	protected void updateFriendInfo(FriendHideContext pContext, RunnableHider pFirstCase, RunnableHider pSecondCase) {
		Player friend1 = Bukkit.getPlayer(pContext.FRIEND1);
		Player friend2 = Bukkit.getPlayer(pContext.FRIEND2);
		if (friend1 == null || friend2 == null)
			return;
		if (getInThisHideMode().contains(friend1)) {
			pFirstCase.run(friend1, friend2);
		}
		if (getInThisHideMode().contains(friend2)) {
			pSecondCase.run(friend1, friend2);
		}
	}


	@Override
	public void updateInformation(HideContext pContext) {
		FriendHideContext friendHideContext = (FriendHideContext) pContext;
		if (friendHideContext.UPDATE_TYPE == null) {
			return;
		}
		if (friendHideContext.UPDATE_TYPE.equals("FriendAdded")) {
			updateFriendAddedInfo(friendHideContext);
		} else if (friendHideContext.UPDATE_TYPE.equals("FriendRemoved")) {
			updateFriendInfo(friendHideContext, new RunnableHider() {
				@Override
				public void run(Player friend1, Player friend2) {
					HIDER.hidePlayer(friend1, friend2);
				}
			}, new RunnableHider() {
				@Override
				public void run(Player friend1, Player friend2) {
					HIDER.hidePlayer(friend2, friend1);
				}
			});
		}
	}

	abstract static class RunnableHider {
		public abstract void run(Player friend1, Player friend2);
	}
}
