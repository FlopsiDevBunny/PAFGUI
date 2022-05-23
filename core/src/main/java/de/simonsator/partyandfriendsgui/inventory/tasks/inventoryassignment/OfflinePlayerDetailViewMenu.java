package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment;

import de.simonsator.partyandfriendsgui.Main;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Simonsator
 * @version 1.0.0 on 07.08.16.
 */
public class OfflinePlayerDetailViewMenu extends PlayerDetailViewMenu {
	private final String OFFLINE_SUFFIX;

	public OfflinePlayerDetailViewMenu(String pOfflineSuffix, ConfirmFriendDeleteMenu confirmFriendDeleteMenu) {
		super("(Off", confirmFriendDeleteMenu);
		OFFLINE_SUFFIX = " (" + pOfflineSuffix + ")";
	}

	@Override
	protected void executeFurtherTasks(String pPlayerName, InventoryClickEvent pEvent) {

	}

	@Override
	public boolean isApplicable(String pName) {
		return pName.startsWith(Main.getInstance().getColor(2)) && (pName.endsWith(OFFLINE_SUFFIX) || pName.endsWith("(Off)"));
	}

}
