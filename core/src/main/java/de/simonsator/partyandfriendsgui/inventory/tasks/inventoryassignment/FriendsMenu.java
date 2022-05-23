package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.MenuBarCreationEvent;
import de.simonsator.partyandfriendsgui.api.menu.MainMenuClickProcessor;
import de.simonsator.partyandfriendsgui.api.menu.MenuBarItem;
import de.simonsator.partyandfriendsgui.api.menu.MenuManager;
import de.simonsator.partyandfriendsgui.communication.tasks.OpenFriendMenu;
import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.*;
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

/**
 * @author Simonsator
 * @version 1.0.0 on 12.07.16.
 */
public class FriendsMenu extends InventoryAssignmentTask implements Listener, MainMenuClickProcessor {
	private final GlowEffect GLOW_EFFECT;

	public FriendsMenu(String pOnlineSuffix, String pOfflineSuffix) {
		super("FriendMenu");
		if (Main.getInstance().getConfig().getBoolean("Inventories.MainMenuMenuBar.GlowIfCurrentMenu"))
			GLOW_EFFECT = new NewGlowEffect();
		else GLOW_EFFECT = new NoGlowEffect();
		tasks.add(new FriendsChangePage(-1, ItemManager.getInstance().PREVIOUS_PAGE_ITEM));
		tasks.add(new FriendsChangePage(1, ItemManager.getInstance().NEXT_PAGE_ITEM));
		tasks.add(new OpenOnlineFriendMenu(pOnlineSuffix));
		tasks.add(new OpenOfflineFriendMenu(pOfflineSuffix));
		if (Main.getInstance().getConfig().getBoolean("Inventories.FriendMenu.Sorting.Enabled"))
			tasks.add(new FriendSortingClickTask());
		MenuManager.getInstance().registerMenuClickProcessor(this);
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
		if (ItemManager.getInstance().OPEN_FRIEND_GUI_ITEM != null)
			MenuManager.getInstance().registerInventoryTask(new OpenFriendsMenuClickTask());
	}

	@Override
	public void executeTask(InventoryClickEvent pEvent) {
		cancel(pEvent);
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().MENU_BAR_PLACEHOLDER_ITEM, false) || AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().NO_FRIENDS, false))
			return;
		if (Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch") && pEvent.getSlot() < pEvent.getInventory().getSize() - 9 && !AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().FRIEND_MENU_PLACEHOLDER, false))
			pEvent.getWhoClicked().closeInventory();
		super.executeTask(pEvent);
	}

	@Override
	public boolean isApplicable(String pName) {
		return pName.startsWith(LanguageManager.getInstance().getText(TextIdentifier.FRIENDS_MENU));
	}

	@Override
	public void registerTask(InventoryTask pTask) {
		addTask(pTask);
	}

	@EventHandler
	public void onMenuBarCreation(MenuBarCreationEvent pEvent) {
		if (ItemManager.getInstance().OPEN_FRIEND_GUI_ITEM != null)
			if (pEvent.getPlayer().hasPermission(Main.getInstance().getConfig().getString("Inventories.MainMenuMenuBar.FriendItem.Permission")) || Main.getInstance().getConfig().getString("Inventories.MainMenuMenuBar.FriendItem.Permission").equals(""))
				if (pEvent.getMenu().getClass().equals(OpenFriendMenu.class)) {
					if (!Main.getInstance().getConfig().getBoolean("Inventories.MainMenuMenuBar.DoNotShowItemIfCurrentMenu"))
						pEvent.addMenuBarItem(new MenuBarItem(Main.getInstance().getConfig().getInt("Inventories.MainMenuMenuBar.FriendItem.Place"), GLOW_EFFECT.addGlow(ItemManager.getInstance().OPEN_FRIEND_GUI_ITEM.clone())));
				} else
					pEvent.addMenuBarItem(new MenuBarItem(Main.getInstance().getConfig().getInt("Inventories.MainMenuMenuBar.FriendItem.Place"), ItemManager.getInstance().OPEN_FRIEND_GUI_ITEM));
	}
}
