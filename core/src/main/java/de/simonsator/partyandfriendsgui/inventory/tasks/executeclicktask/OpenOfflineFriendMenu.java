package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.FriendDetailViewCreationEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.StringTokenizer;

/**
 * @author Simonsator
 * @version 1.0.0 on 06.08.16.
 */
public class OpenOfflineFriendMenu extends OpenFriendDetailView {

	public OpenOfflineFriendMenu(String pOfflineSuffix) {
		super("(" + pOfflineSuffix, Main.getInstance().getColor(2));
	}

	@Override
	String shortUp(String pHandOver) {
		if (pHandOver.length() > 32) {
			StringTokenizer stn = new StringTokenizer(pHandOver, "(");
			pHandOver = stn.nextToken() + "(Off)";
		}
		return pHandOver;
	}

	@Override
	protected Inventory createInventory(String pHandOver, Player pPlayer) {
		Inventory inv = createStandardInventory(pHandOver);
		Bukkit.getServer().getPluginManager().callEvent(new FriendDetailViewCreationEvent(inv, pPlayer, false));
		return inv;
	}
}
