package de.simonsator.partyandfriendsgui.commands;

import de.simonsator.partyandfriendsgui.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Simonsator
 * @version 1.0.0 30.09.16
 */
public abstract class PartyFriendCommand extends BukkitCommand {
	public PartyFriendCommand(List<String> aliases) {
		super(aliases.get(0), "", "", aliases);
	}

	@Override
	public boolean execute(CommandSender commandSender, String command, String[] args) {
		if (commandSender instanceof Player)
			onCommand((Player) commandSender, command, args);
		else {
			Main.getInstance().loadConfig();
			System.out.println("Config reloaded!");
		}
		return true;
	}

	protected abstract void onCommand(Player pPlayer, String command, String[] args);
}
