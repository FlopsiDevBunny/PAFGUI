package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.FriendDetailViewCreationEvent;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.StringTokenizer;

/**
 * @author Simonsator
 * @version 1.0.0 on 06.08.16.
 */
public class OpenOnlineFriendMenu extends OpenFriendDetailView {
	private final String ONLINE_SUFFIX;

	public OpenOnlineFriendMenu(String pOnlineSuffix) {
		super("(" + pOnlineSuffix, Main.getInstance().getColor(0));
		ONLINE_SUFFIX = " (" + pOnlineSuffix + ")";
	}

	@Override
	String shortUp(String pHandOver) {
		StringTokenizer st = new StringTokenizer(pHandOver, " ");
		pHandOver = st.nextToken() + ONLINE_SUFFIX;
		if (pHandOver.length() > 32) {
			StringTokenizer stn = new StringTokenizer(pHandOver, "(");
			pHandOver = stn.nextToken() + "(On)";
		}
		return pHandOver;
	}

	@Override
	protected Inventory createInventory(String pHandOver, Player pPlayer) {
		Inventory inv = createStandardInventory(pHandOver);
		inv.setItem(Main.getInstance().getConfig().getInt("Inventories.FriendDetailView.InviteIntoParty.Place"),
				ItemManager.getInstance().FRIEND_INVITE_PARTY);
		inv.setItem(Main.getInstance().getConfig().getInt("Inventories.FriendDetailView.JumpTo.Place"),
				ItemManager.getInstance().JUMP_TO_FRIEND);
		Bukkit.getServer().getPluginManager().callEvent(new FriendDetailViewCreationEvent(inv, pPlayer, true));
		return inv;
	}
}
