package de.simonsator.partyandfriendsgui.commands;

import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyGUICommand extends PartyFriendCommand {
	public PartyGUICommand(List<String> aliases) {
		super(aliases);
	}

	@Override
	protected void onCommand(Player pPlayer, String command, String[] args) {
		PartyFriendsAPI.openPartyInventory(pPlayer, 0);
	}

}
