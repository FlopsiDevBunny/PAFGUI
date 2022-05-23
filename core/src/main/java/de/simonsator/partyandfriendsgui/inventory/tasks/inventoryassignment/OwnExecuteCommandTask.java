package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.api.menu.OwnExecuteCommandBlockMenu;
import de.simonsator.partyandfriendsgui.utilities.OwnExecuteCommandContainer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static de.simonsator.partyandfriendsgui.api.PartyFriendsAPI.executeBungeeCordCommand;

public abstract class OwnExecuteCommandTask extends InventoryAssignmentTask implements OwnExecuteCommandBlockMenu {
	private final List<OwnExecuteCommandContainer> ownExecuteCommandContainerList;

	public OwnExecuteCommandTask(String pName, String pKeyPrefix) {
		this(pName, pKeyPrefix, Main.getInstance().getConfig());
	}

	public OwnExecuteCommandTask(String pName, String pKeyPrefix, Configuration pConfig) {
		super(pName);
		ownExecuteCommandContainerList = createExecuteCommandContainerList(pConfig, pKeyPrefix);
	}

	protected void checkForOwnExecuteCommand(InventoryClickEvent pEvent, String pPlayerName, int pPlaceModifier) {
		for (OwnExecuteCommandContainer executeCommandContainer : ownExecuteCommandContainerList) {
			if (AdvancedItem.itemsAreEqual(executeCommandContainer.ITEM, pEvent.getCurrentItem(), false) && executeCommandContainer.PLACE + pPlaceModifier == pEvent.getSlot()) {
				String command = executeCommandContainer.COMMAND;
				if (pPlayerName != null)
					command = command.replace("%player%", pPlayerName);
				if (executeCommandContainer.IS_BUNGEECORD_COMMAND) {
					executeBungeeCordCommand(command, (Player) pEvent.getWhoClicked());
				} else {
					Bukkit.getServer().dispatchCommand(pEvent.getWhoClicked(), command);
				}
				if (!executeCommandContainer.KEEP_OPEN) {
					pEvent.getWhoClicked().closeInventory();
				}
			}
		}
	}
}
