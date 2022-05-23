package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.MenuBarCreationEvent;
import de.simonsator.partyandfriendsgui.api.menu.MainMenuClickProcessor;
import de.simonsator.partyandfriendsgui.api.menu.MenuBarItem;
import de.simonsator.partyandfriendsgui.api.menu.MenuManager;
import de.simonsator.partyandfriendsgui.communication.tasks.OpenSettingsMenu;
import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.InventoryTask;
import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.OpenSettingsInventory;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import de.simonsator.partyandfriendsgui.nmsdepending.GlowEffect;
import de.simonsator.partyandfriendsgui.nmsdepending.NewGlowEffect;
import de.simonsator.partyandfriendsgui.nmsdepending.NoGlowEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static de.simonsator.partyandfriendsgui.api.PartyFriendsAPI.*;

/**
 * @author Simonsator
 * @version 1.0.0 on 28.02.2018
 */
public class SettingsMenu extends InventoryAssignmentTask implements Listener, MainMenuClickProcessor {
	private final GlowEffect GLOW_EFFECT;
	private final int REOPEN_DELAY = Main.getInstance().getConfig().getInt("Inventories.SettingsMenu.DelayedRefreshInTicks");

	public SettingsMenu() {
		super(LanguageManager.getInstance().getText(TextIdentifier.SETTINGS_MENU));
		if (Main.getInstance().getConfig().getBoolean("Inventories.MainMenuMenuBar.GlowIfCurrentMenu"))
			GLOW_EFFECT = new NewGlowEffect();
		else GLOW_EFFECT = new NoGlowEffect();
		MenuManager.getInstance().registerMenuClickProcessor(this);
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
		if (ItemManager.getInstance().SETTINGS_ITEM != null)
			MenuManager.getInstance().registerInventoryTask(new OpenSettingsInventory());
	}

	@Override
	public void executeTask(final InventoryClickEvent pEvent) {
		super.executeTask(pEvent);
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), Main.getInstance().blockOutput(0, 6), false) || AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), Main.getInstance().blockOutput(1, 6), false))
			changeSettingFriendRequests((Player) pEvent.getWhoClicked());
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), Main.getInstance().blockOutput(2, 6), false) || AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), Main.getInstance().blockOutput(3, 6), false))
			changeSettingParty((Player) pEvent.getWhoClicked());
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), Main.getInstance().blockOutput(4, 6), false) || AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), Main.getInstance().blockOutput(5, 6), false))
			changeSettingOffline((Player) pEvent.getWhoClicked());
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), Main.getInstance().blockOutput(6, 6), false) || AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), Main.getInstance().blockOutput(7, 6), false))
			changeSettingJump((Player) pEvent.getWhoClicked());
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), Main.getInstance().blockOutput(8, 6), false) || AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), Main.getInstance().blockOutput(9, 6), false))
			PartyFriendsAPI.changeMessageSetting((Player) pEvent.getWhoClicked());
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), Main.getInstance().blockOutput(10, 6), false) || AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), Main.getInstance().blockOutput(11, 6), false))
			PartyFriendsAPI.changeStatusNotifySetting((Player) pEvent.getWhoClicked());
		pEvent.setCancelled(true);
		if (pEvent.getCurrentItem().getType() == ItemManager.getInstance().RED_TERRACOTTA.getType() ||
				pEvent.getCurrentItem().getType() == ItemManager.getInstance().GREEN_TERRACOTTA.getType()) {
			if (Main.getInstance().getConfig()
					.getBoolean("Inventories.SettingsMenu.CloseSettingsInventoryAutomatically"))
				pEvent.getWhoClicked().closeInventory();
			else {
				PartyFriendsAPI.openSettingsInventory((Player) pEvent.getWhoClicked());
				Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> PartyFriendsAPI.openSettingsInventory((Player) pEvent.getWhoClicked()), REOPEN_DELAY);
			}
		}
	}

	@Override
	public void registerTask(InventoryTask pTask) {
		addTask(pTask);
	}

	@EventHandler
	public void onMenuBarCreation(MenuBarCreationEvent pEvent) {
		if (ItemManager.getInstance().SETTINGS_ITEM != null)
			if (pEvent.getPlayer().hasPermission(Main.getInstance().getConfig().getString("Inventories.MainMenuMenuBar.SettingsItem.Permission")) || Main.getInstance().getConfig().getString("Inventories.MainMenuMenuBar.SettingsItem.Permission").equals(""))
				if (pEvent.getMenu().getClass().equals(OpenSettingsMenu.class)) {
					if (!Main.getInstance().getConfig().getBoolean("Inventories.MainMenuMenuBar.DoNotShowItemIfCurrentMenu"))
						pEvent.addMenuBarItem(new MenuBarItem(Main.getInstance().getConfig().getInt("Inventories.MainMenuMenuBar.SettingsItem.Place"), GLOW_EFFECT.addGlow(ItemManager.getInstance().SETTINGS_ITEM.clone())));
				} else
					pEvent.addMenuBarItem(new MenuBarItem(Main.getInstance().getConfig().getInt("Inventories.MainMenuMenuBar.SettingsItem.Place"), ItemManager.getInstance().SETTINGS_ITEM));
	}
}
