package de.simonsator.partyandfriendsgui.listener;

import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * The OnEntityDamageListener
 *
 * @author Simonsator
 * @version 1.0.0
 */
public class OnEntityDamage implements Listener {
	private final ItemsManager ITEMS_MANAGER;

	public OnEntityDamage(ItemsManager itemsManager) {
		ITEMS_MANAGER = itemsManager;
	}

	/**
	 * The OnEntityDamageListener
	 *
	 * @param e The event
	 */
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player damager = ((Player) e.getDamager());
			if (ITEMS_MANAGER.isFriendHead(damager.getItemInHand())) {
				Player requested = (Player) e.getEntity();
				PartyFriendsAPI.sendOrAcceptFriendRequest(damager, requested.getName());
				e.setCancelled(true);
			}
		}
	}
}
