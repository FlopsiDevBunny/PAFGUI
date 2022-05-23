package de.simonsator.partyandfriendsgui.communication.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.MainMenuCreationEvent;
import de.simonsator.partyandfriendsgui.api.menu.MainMenu;
import de.simonsator.partyandfriendsgui.api.menu.MenuManager;
import de.simonsator.partyandfriendsgui.communication.BungeecordCommunication;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @author Simonsator
 * @version 1.0.0 on 07.07.2016
 */
public class OpenFriendRequestMenu extends CommunicationTask implements MainMenu {
	private final int MIN_SIZE = Main.getInstance().getConfig().getInt("Inventories.FriendRequestMenu.Size");

	public OpenFriendRequestMenu() {
		super("OpenFriendRequestMenu");
	}

	@Override
	public void executeTask(Player pPlayer, JsonObject pJObj) {
		MenuManager.getInstance().setLastOpened(pPlayer, this);
		JsonArray friendRequests = pJObj.get("friendRequests").getAsJsonArray();
		int toSet = ((friendRequests.size() + 2) / 9 + 1) * 9;
		if (toSet >= 27)
			toSet = 45;
		if (MIN_SIZE > toSet)
			toSet = MIN_SIZE;
		Inventory inv = Bukkit.createInventory(null, toSet, LanguageManager.getInstance().getText(TextIdentifier.FRIEND_REQUEST_MENU));
		if (friendRequests.size() == 0) {
			inv.setItem(0, ItemManager.getInstance().NO_PENDING_FRIEND_REQUESTS_ITEM);
		} else {
			for (int i = 0; i < friendRequests.size() && i < toSet - 9; i++) {
				JsonObject singleRequest = friendRequests.get(i).getAsJsonObject();
				String playerName = singleRequest.get("playerName").getAsString();
				ItemStack item = new ItemStack(ItemManager.getInstance().PLAYER_HEAD_MATERIAL, 1, (short) 3);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(Main.getInstance().getColor(4) + playerName);
				item.setItemMeta(meta);
				if (Main.getInstance().getConfig().getBoolean("General.SkinHeadDownload")) {
					SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
					BungeecordCommunication.getInstance().SET_HEAD_OWNER.setHeadOwner(skullMeta, playerName, singleRequest.get("playerUUID"));
					item.setItemMeta(skullMeta);
				}
				inv.addItem(item);
			}
		}
		createMenuBar(pPlayer, inv);
		Bukkit.getServer().getPluginManager().callEvent(new MainMenuCreationEvent(pPlayer, inv, pJObj, getClass()));

		// Custom Force Back Main Menu
		inv.setItem(8, ItemManager.getInstance().OPEN_FRIEND_GUI_ITEM);

		pPlayer.openInventory(inv);
	}

	@Override
	public void openMenu(Player pPlayer) {
		PartyFriendsAPI.openFriendRequestsMenu(pPlayer);
	}
}