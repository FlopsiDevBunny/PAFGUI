package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.MenuBarCreationEvent;
import de.simonsator.partyandfriendsgui.api.menu.MainMenuClickProcessor;
import de.simonsator.partyandfriendsgui.api.menu.MenuBarItem;
import de.simonsator.partyandfriendsgui.api.menu.MenuManager;
import de.simonsator.partyandfriendsgui.communication.tasks.OpenFriendRequestMenu;
import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.InventoryTask;
import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.OpenFriendRequestMenuInventoryTask;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import de.simonsator.partyandfriendsgui.nmsdepending.GlowEffect;
import de.simonsator.partyandfriendsgui.nmsdepending.NewGlowEffect;
import de.simonsator.partyandfriendsgui.nmsdepending.NoGlowEffect;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * @author Simonsator
 * @version 1.0.0 on 07.08.16.
 */
public class FriendRequestMenu extends InventoryAssignmentTask implements Listener, MainMenuClickProcessor {
	private final GlowEffect GLOW_EFFECT;

	public FriendRequestMenu() {
		super(LanguageManager.getInstance().getText(TextIdentifier.FRIEND_REQUEST_MENU));
		if (Main.getInstance().getConfig().getBoolean("Inventories.MainMenuMenuBar.GlowIfCurrentMenu"))
			GLOW_EFFECT = new NewGlowEffect();
		else GLOW_EFFECT = new NoGlowEffect();
		MenuManager.getInstance().registerMenuClickProcessor(this);
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
		if (ItemManager.getInstance().FRIEND_REQUEST_ITEM != null)
			MenuManager.getInstance().registerInventoryTask(new OpenFriendRequestMenuInventoryTask());
	}

	@Override
	public void executeTask(InventoryClickEvent pEvent) {
		super.executeTask(pEvent);
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().NO_PENDING_FRIEND_REQUESTS_ITEM, false)) {
			pEvent.setCancelled(true);
			return;
		}
		pEvent.setCancelled(true);
		if (pEvent.getCurrentItem().getItemMeta().getDisplayName().startsWith(Main.getInstance().getColor(4)) && pEvent.getCurrentItem().getType().equals(ItemManager.getInstance().PLAYER_HEAD_MATERIAL)) {
			if (Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch") && pEvent.getSlot() < pEvent.getInventory().getSize() - 9)
				pEvent.getWhoClicked().closeInventory();
			Inventory inv = Bukkit.createInventory(null, 9, pEvent.getCurrentItem().getItemMeta().getDisplayName());
			inv.setItem(2, ItemManager.getInstance().FRIEND_REQUEST_ACCEPT_MENU_ACCEPT);
			inv.setItem(5, ItemManager.getInstance().FRIEND_REQUEST_ACCEPT_MENU_DENY);
			inv.setItem(8, ItemManager.getInstance().BACK_ITEM);
			for (int i = 0; i < inv.getSize(); i++)
				if (inv.getItem(i) == null)
					inv.setItem(i, ItemManager.getInstance().FRIEND_ACCEPT_MENU_PLACEHOLDER);
			pEvent.getWhoClicked().openInventory(inv);
		}
	}

	@Override
	public void registerTask(InventoryTask pTask) {
		addTask(pTask);
	}

	@EventHandler
	public void onMenuBarCreation(MenuBarCreationEvent pEvent) {
		if (ItemManager.getInstance().FRIEND_REQUEST_ITEM != null)
			if (pEvent.getPlayer().hasPermission(Main.getInstance().getConfig().getString("Inventories.MainMenuMenuBar.FriendItem.Permission")) || Main.getInstance().getConfig().getString("Inventories.MainMenuMenuBar.FriendItem.Permission").equals(""))
				if (pEvent.getMenu().getClass().equals(OpenFriendRequestMenu.class)) {
					if (!Main.getInstance().getConfig().getBoolean("Inventories.MainMenuMenuBar.DoNotShowItemIfCurrentMenu"))
						pEvent.addMenuBarItem(new MenuBarItem(Main.getInstance().getConfig().getInt("Inventories.MainMenuMenuBar.FriendRequestItem.Place"), GLOW_EFFECT.addGlow(ItemManager.getInstance().FRIEND_REQUEST_ITEM.clone())));
				} else
					pEvent.addMenuBarItem(new MenuBarItem(Main.getInstance().getConfig().getInt("Inventories.MainMenuMenuBar.FriendRequestItem.Place"), ItemManager.getInstance().FRIEND_REQUEST_ITEM));
	}

}
