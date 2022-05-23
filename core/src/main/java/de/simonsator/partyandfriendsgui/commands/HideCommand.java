package de.simonsator.partyandfriendsgui.commands;

import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import org.bukkit.entity.Player;

import java.util.List;

import static de.simonsator.partyandfriendsgui.api.PartyFriendsAPI.setHideMode;

/**
 * @author simonbrungs
 * @version 1.0.0 30.09.16
 */
public class HideCommand extends PartyFriendCommand {
	public HideCommand(List<String> aliases) {
		super(aliases);
	}

	@Override
	protected void onCommand(Player pPlayer, String command, String[] args) {
		if (args.length != 0) {
			try {
				int value = Integer.parseInt(args[0]);
				if (value < 6 && value > -1) {
					setHideMode(pPlayer, value);
					return;
				}
			} catch (NumberFormatException ignored) {
			}
		}
		PartyFriendsAPI.openHideInventory(pPlayer);
	}
}
