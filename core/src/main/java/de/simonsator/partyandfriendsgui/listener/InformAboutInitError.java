package de.simonsator.partyandfriendsgui.listener;

import de.simonsator.partyandfriendsgui.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Simonsator
 * @version 1.0.0 08.09.17
 */
public class InformAboutInitError implements Listener {
	private final String ERROR_MESSAGE;

	public InformAboutInitError(String pErrorMessage) {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
		ERROR_MESSAGE = pErrorMessage;
	}

	@EventHandler
	public void informAboutInitError(PlayerJoinEvent pEvent) {
		pEvent.getPlayer().sendMessage(ERROR_MESSAGE);
		pEvent.getPlayer().sendMessage("If the error cannot be resolved please contact the developer Simonsator via Skype (live:00pflaume), PM him (https://www.spigotmc.org/conversations/add?to=simonsator ) or write an email to him (support@simonsator.de). Please don't forget to send him the latest.log file. Also please don't write a bad review without giving him 24 hours time to fix the problem.");
	}
}
