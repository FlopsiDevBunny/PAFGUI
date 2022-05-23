package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.listener.InformAboutInitError;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Simonsator
 * @version 1.0.0 on 06.08.2016
 */
public class HideMenu extends OwnExecuteCommandTask {
	private Sound soundOnClick;

	public HideMenu() {
		super(LanguageManager.getInstance().getText(TextIdentifier.HIDE_PLAYERS_MENU), "Inventories.HideInventory.Own");
		if (Main.getInstance().getConfig().getBoolean("General.SoundForHideGUI")) {
			try {
				soundOnClick = Sound.valueOf(Main.getInstance().getConfig().getString("General.SoundOfHideGUI"));
			} catch (IllegalArgumentException e) {
				String errorMessage = "§4The sound which was provided by you in the config under \"General.SoundOfHideGUI\" does not exist. Just delete this line entry and a " +
						"valid sound will be created. Notice that between some Minecraft versions were renamed/deleted/added. As long as this error is not corrected there will be no sound in the hide gui.";
				Bukkit.getServer().getConsoleSender().sendMessage(errorMessage);
				new InformAboutInitError(errorMessage);
				e.printStackTrace();
				soundOnClick = null;
			}
		} else soundOnClick = null;
	}

	@Override
	public void executeTask(InventoryClickEvent pEvent) {
		cancel(pEvent);
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().HIDE_SHOW_ALL_ITEM, true))
			setHideMode((Player) pEvent.getWhoClicked(), 0);
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().HIDE_SHOW_ONLY_YOUR_FRIENDS_ITEM, true))
			setHideMode((Player) pEvent.getWhoClicked(), 1);
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().HIDE_SHOW_FRIENDS_AND_PEOPLE_WITH_PERMISSION_ITEM, true))
			setHideMode((Player) pEvent.getWhoClicked(), 2);
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().HIDE_SHOW_ONLY_PEOPLE_FROM_THE_SERVER_ITEM, true))
			setHideMode((Player) pEvent.getWhoClicked(), 3);
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().HIDE_ALL_ITEM, true))
			setHideMode((Player) pEvent.getWhoClicked(), 4);
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().HIDE_SHOW_ONLY_PARTY_MEMBERS_ITEM, true))
			setHideMode((Player) pEvent.getWhoClicked(), 5);
		checkForOwnExecuteCommand(pEvent, pEvent.getWhoClicked().getName(), 0);
	}

	private void setHideMode(Player player, int pHideMode) {
		PartyFriendsAPI.setHideMode(player, pHideMode);
		if (soundOnClick != null)
			player.playSound(player.getLocation(), soundOnClick, 10, 1);
		player.closeInventory();
	}

	@Override
	public boolean conditionForAdding(Configuration pConfig, String pKey) {
		return true;
	}
}
