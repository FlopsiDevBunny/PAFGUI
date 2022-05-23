package de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment.party;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.MenuBarCreationEvent;
import de.simonsator.partyandfriendsgui.api.menu.MainMenuClickProcessor;
import de.simonsator.partyandfriendsgui.api.menu.MenuBarItem;
import de.simonsator.partyandfriendsgui.api.menu.MenuManager;
import de.simonsator.partyandfriendsgui.communication.tasks.party.OpenPartyMenu;
import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.InventoryTask;
import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.LeavePartyClickTask;
import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.OpenPartyMemberDetailView;
import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.OpenPartyMenuClickTask;
import de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask.party.PartyChangePage;
import de.simonsator.partyandfriendsgui.inventory.tasks.inventoryassignment.InventoryAssignmentTask;
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
import org.bukkit.inventory.ItemStack;

public class PartyMenu extends InventoryAssignmentTask implements Listener, MainMenuClickProcessor {
	private final GlowEffect GLOW_EFFECT;

	public PartyMenu() {
		super("PartyMenu");
		if (Main.getInstance().getConfig().getBoolean("Inventories.MainMenuMenuBar.GlowIfCurrentMenu"))
			GLOW_EFFECT = new NewGlowEffect();
		else GLOW_EFFECT = new NoGlowEffect();
		tasks.add(new OpenPartyMemberDetailView());
		tasks.add(new PartyChangePage(-1, ItemManager.getInstance().PREVIOUS_PAGE_ITEM));
		tasks.add(new PartyChangePage(1, ItemManager.getInstance().NEXT_PAGE_ITEM));
		if (ItemManager.getInstance().LEAVE_PARTY_ITEM != null)
			tasks.add(new LeavePartyClickTask());
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
		MenuManager.getInstance().registerMenuClickProcessor(this);
		if (ItemManager.getInstance().OPEN_PARTY_GUI_ITEM != null)
			MenuManager.getInstance().registerInventoryTask(new OpenPartyMenuClickTask());
	}

	@Override
	public void executeTask(InventoryClickEvent pEvent) {
		cancel(pEvent);
		super.executeTask(pEvent);
		if (pEvent.getSlot() == 4 ||
				AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().PARTY_MENU_PLACEHOLDER, false)) {
			return;
		}
		if (AdvancedItem.itemsAreEqual(pEvent.getCurrentItem(), ItemManager.getInstance().MENU_BAR_PLACEHOLDER_ITEM, false))
			return;
		if (!pEvent.getCurrentItem().getType().equals(ItemManager.getInstance().PLAYER_HEAD_MATERIAL))
			if (Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch") && pEvent.getSlot() < pEvent.getInventory().getSize() - 9)
				pEvent.getWhoClicked().closeInventory();
	}

	@Override
	public boolean isApplicable(String pName) {
		return pName.startsWith(LanguageManager.getInstance().getText(TextIdentifier.PARTY_MENU_NAME));
	}

	@EventHandler
	public void onMenuBarCreation(MenuBarCreationEvent pEvent) {
		if (ItemManager.getInstance().OPEN_PARTY_GUI_ITEM != null)
			if (pEvent.getPlayer().hasPermission(Main.getInstance().getConfig().getString("Inventories.MainMenuMenuBar.PartyItem.Permission")) || Main.getInstance().getConfig().getString("Inventories.MainMenuMenuBar.PartyItem.Permission").equals(""))
				if (pEvent.getMenu().getClass().equals(OpenPartyMenu.class)) {
					if (!Main.getInstance().getConfig().getBoolean("Inventories.MainMenuMenuBar.DoNotShowItemIfCurrentMenu"))
						pEvent.addMenuBarItem(new MenuBarItem(Main.getInstance().getConfig().getInt("Inventories.MainMenuMenuBar.PartyItem.Place"), GLOW_EFFECT.addGlow(ItemManager.getInstance().OPEN_PARTY_GUI_ITEM.clone())));
				} else
					pEvent.addMenuBarItem(new MenuBarItem(Main.getInstance().getConfig().getInt("Inventories.MainMenuMenuBar.PartyItem.Place"), ItemManager.getInstance().OPEN_PARTY_GUI_ITEM));
		if (ItemManager.getInstance().LEAVE_PARTY_ITEM != null && pEvent.getMenu().getClass().equals(OpenPartyMenu.class)) {
			ItemStack item = pEvent.getInventory().getItem(4);
			if (item != null && item.getType() == ItemManager.getInstance().PLAYER_HEAD_MATERIAL)
				pEvent.addMenuBarItem(new MenuBarItem(Main.getInstance().getConfig().getInt("Inventories.PartyGUI.MainMenu.LeaveParty.Place"), ItemManager.getInstance().LEAVE_PARTY_ITEM));
		}
	}

	@Override
	public void registerTask(InventoryTask pTask) {
		tasks.add(pTask);
	}
}
