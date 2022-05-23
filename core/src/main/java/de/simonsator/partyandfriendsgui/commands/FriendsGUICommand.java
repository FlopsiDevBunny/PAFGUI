package de.simonsator.partyandfriendsgui.commands;

import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Simonsator
 * @version 1.0.0 30.09.16
 */
public class FriendsGUICommand extends PartyFriendCommand {
	public FriendsGUICommand(List<String> aliases) {
		super(aliases);
	}

	@Override
	protected void onCommand(Player pPlayer, String command, String[] args) {
		PartyFriendsAPI.openMainInventory(pPlayer, 0);
	}
}
