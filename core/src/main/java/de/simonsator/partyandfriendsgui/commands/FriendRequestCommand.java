package de.simonsator.partyandfriendsgui.commands;

import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import org.bukkit.entity.Player;

import java.util.List;

public class FriendRequestCommand extends PartyFriendCommand {
	public FriendRequestCommand(List<String> aliases) {
		super(aliases);
	}

	@Override
	protected void onCommand(Player pPlayer, String command, String[] args) {
		PartyFriendsAPI.openFriendRequestsMenu(pPlayer);
	}
}
