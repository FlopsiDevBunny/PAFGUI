package de.simonsator.partyandfriendsgui.listener;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.api.menu.MenuManager;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import de.simonsator.partyandfriendsgui.utilities.Splitter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ItemsManager implements Listener {
	private final Set<UUID> GUI_COOLDOWN;
	private final Set<UUID> HIDE_COOLDOWN;
	private final Long TIME_GUI;
	private final Long TIME_HIDE;
	private final JavaPlugin PLUGIN;
	private final List<String> ENABLED_WORLDS;
	private final boolean SUPPORT_MULTI_WORLD;
	private final ItemStack PROFILE_ITEM;
	private final Set<String> lastFriendRequestRequests = new HashSet<>();

	public ItemsManager(JavaPlugin pPlugin) {
		PLUGIN = pPlugin;
		TIME_GUI = Main.getInstance().getConfig().getLong("Inventories.ToolBar.ProfileItem.Cooldown.Time") * 20;
		TIME_HIDE = Main.getInstance().getConfig().getLong("Inventories.ToolBar.HideItem.Cooldown.Time") * 20;
		SUPPORT_MULTI_WORLD = Main.getInstance().getConfig().getBoolean("Inventories.ToolBar.MultiWorld.SupportMultiWorld");
		if (SUPPORT_MULTI_WORLD)
			ENABLED_WORLDS = Main.getInstance().getConfig().getStringList("Inventories.ToolBar.MultiWorld.EnabledWorlds");
		else ENABLED_WORLDS = null;
		if (TIME_GUI != 0) {
			GUI_COOLDOWN = new HashSet<>();
		} else
			GUI_COOLDOWN = null;
		if (TIME_HIDE != 0) {
			HIDE_COOLDOWN = new HashSet<>();
		} else
			HIDE_COOLDOWN = null;
		Splitter splitter = new Splitter(LanguageManager.getInstance().getText(TextIdentifier.HEAD_NAME), "%LINE_BREAK%");
		String profileItemName = splitter.next();
		List<String> PROFILE_ITEM_LORE = new LinkedList<>();
		while (splitter.hasNext())
			PROFILE_ITEM_LORE.add(splitter.next());
		if (SUPPORT_MULTI_WORLD)
			Main.getInstance().getServer().getPluginManager().registerEvents(new OnWorldSwitch(this), Main.getInstance());
		if (Main.getInstance().getConfig().getBoolean("Inventories.ToolBar.ProfileItem.Use")) {
			if (ItemManager.getInstance().USE_ITEM_META_DATA)
				PROFILE_ITEM = new ItemStack(ItemManager.getInstance().PLAYER_HEAD_MATERIAL, 1, (short) 3);
			else
				PROFILE_ITEM = new ItemStack(ItemManager.getInstance().PLAYER_HEAD_MATERIAL, 1);

			ItemMeta meta = PROFILE_ITEM.getItemMeta();
			meta.setDisplayName(profileItemName);
			meta.setLore(PROFILE_ITEM_LORE);
			PROFILE_ITEM.setItemMeta(meta);
		} else {
			PROFILE_ITEM = null;
		}
	}

	public static boolean isHideItem(ItemStack pItem) {
		return AdvancedItem.itemsAreEqual(ItemManager.getInstance().HIDE_ITEM, pItem, false);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent pEvent) {
		if (pEvent.getEntity() != null) {
			pEvent.getDrops().remove(getHead(pEvent.getEntity()));
			pEvent.getDrops().remove(ItemManager.getInstance().HIDE_ITEM);
		}
	}

	/**
	 * The OnJoin listener
	 *
	 * @param pEvent The event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(final PlayerJoinEvent pEvent) {
		Main.getInstance().getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			final Player player = pEvent.getPlayer();
			final ItemStack head = getHead(player);
			setItems(player, head);
			if ((ItemManager.getInstance().HIDE_ITEM != null)
					|| Main.getInstance().getConfig().getBoolean("Commands.Hide.Enabled")) {
				Main.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Main.getInstance(),
						() -> PartyFriendsAPI.loadHideMode(pEvent.getPlayer(), true), Main.getInstance().getConfig().getLong("General.Compatibility.TicksUntilMessageChannelIsUsedAfterJoin"));
			}
			if (Main.getInstance().getConfig().getBoolean("General.PreventItemsOfGettingOverwritten"))
				Main.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Main.getInstance(),
						() -> setItems(player, head), 1L);
		});
	}

	public void setItems(Player pPlayer, ItemStack pHead) {
		if (!SUPPORT_MULTI_WORLD || ENABLED_WORLDS.contains(pPlayer.getWorld().getName())) {
			if (Main.getInstance().getConfig().getBoolean("Inventories.ToolBar.ProfileItem.Use"))
				if ((Main.getInstance().getConfig().getString("Inventories.ToolBar.ProfileItem.Permission").equals("")) || pPlayer.hasPermission(Main.getInstance().getConfig().getString("Inventories.ToolBar.ProfileItem.Permission")))
					pPlayer.getInventory().setItem(Main.getInstance().getConfig().getInt("Inventories.ToolBar.ProfileItem.Place"), pHead);
			if (ItemManager.getInstance().HIDE_ITEM != null)
				if ((Main.getInstance().getConfig().getString("Inventories.ToolBar.HideItem.Permission").equals("")) || pPlayer.hasPermission(Main.getInstance().getConfig().getString("Inventories.ToolBar.HideItem.Permission")))
					setHideTool(Main.getInstance().getConfig().getInt("Inventories.ToolBar.HideItem.Place"), pPlayer);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent pEvent) {
		setItems(pEvent.getPlayer(), getHead(pEvent.getPlayer()));
	}

	/**
	 * Sets the hide item
	 *
	 * @param pPlace The place of the friend item
	 * @param player The player
	 */
	private void setHideTool(int pPlace, Player player) {
		player.getInventory().setItem(pPlace, ItemManager.getInstance().HIDE_ITEM);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryMove(InventoryMoveItemEvent pEvent) {
		if (isHideItem(pEvent.getItem()) || isFriendHead(pEvent.getItem()))
			pEvent.setCancelled(true);
	}

	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent event) {
		if (event.getClick().isKeyboardClick()) {
			Inventory inv = event.getWhoClicked().getInventory();
			int pressed = event.getHotbarButton();
			if (event.getAction().name().equals("HOTBAR_SWAP") && inv.getSize() > pressed && pressed >= 0) {
				ItemStack item = inv.getItem(pressed);
				if (isFriendHead(item) || isHideItem(item))
					event.setCancelled(true);
			}
		}
	}

	/**
	 * The OnInterActed listener
	 *
	 * @param pEvent The event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent pEvent) {
		if (pEvent.getAction() == Action.RIGHT_CLICK_AIR || pEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (pEvent.getItem() == null)
				return;
			if (isFriendHead(pEvent.getItem()))
				if (executeFurther(GUI_COOLDOWN, pEvent, TIME_GUI, TextIdentifier.GUI_COOLDOWN, Main.getInstance().getConfig().getString("Inventories.ToolBar.ProfileItem.Cooldown.ByPassPermission")))
					MenuManager.getInstance().openMenuFor(pEvent.getPlayer());
			if (isHideItem(pEvent.getItem())) {
				if (executeFurther(HIDE_COOLDOWN, pEvent, TIME_HIDE, TextIdentifier.HIDE_COOLDOWN, Main.getInstance().getConfig().getString("Inventories.ToolBar.HideItem.Cooldown.ByPassPermission")))
					PartyFriendsAPI.openHideInventory(pEvent.getPlayer());
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent pEvent) {
		if (Main.getInstance().getConfig().getBoolean("General.SendFriendRequestByRightClickingAndSneaking"))
			if (pEvent.getRightClicked() instanceof Player) {
				if (pEvent.getPlayer().isSneaking()) {
					Player rightClicked = (Player) pEvent.getRightClicked();
					if (!lastFriendRequestRequests.contains(rightClicked.getUniqueId().toString() + pEvent.getPlayer().getUniqueId().toString())) {
						lastFriendRequestRequests.add(rightClicked.getUniqueId().toString() + pEvent.getPlayer().getUniqueId().toString());
						PLUGIN.getServer().getScheduler().scheduleSyncDelayedTask(PLUGIN, () -> lastFriendRequestRequests.remove(rightClicked.getUniqueId().toString() + pEvent.getPlayer().getUniqueId().toString()), 5);
						PartyFriendsAPI.sendOrAcceptFriendRequest(pEvent.getPlayer(), (rightClicked).getName());
						pEvent.setCancelled(true);
					}
				}
			}
	}

	private boolean executeFurther(final Set<UUID> pSet, final PlayerInteractEvent pEvent, long pCoolDownTime, TextIdentifier pFailMessage, String pByPassPermission) {
		pEvent.setCancelled(true);
		if (pSet != null) {
			if (pEvent.getPlayer().hasPermission(pByPassPermission))
				return true;
			if (pSet.contains(pEvent.getPlayer().getUniqueId())) {
				pEvent.getPlayer().sendMessage(LanguageManager.getInstance().getText(pFailMessage));
				return false;
			}
			pSet.add(pEvent.getPlayer().getUniqueId());
			PLUGIN.getServer().getScheduler().scheduleSyncDelayedTask(PLUGIN, () -> pSet.remove(pEvent.getPlayer().getUniqueId()), pCoolDownTime);
		}
		return true;
	}

	/**
	 * The onItemDropEvent listener
	 *
	 * @param pEvent The event
	 */
	@EventHandler
	public void onItemDropEvent(PlayerDropItemEvent pEvent) {
		if (isFriendHead(pEvent.getItemDrop().getItemStack()) || isHideItem(pEvent.getItemDrop().getItemStack()))
			pEvent.setCancelled(true);
	}

	/**
	 * Out puts the friend head item
	 *
	 * @param pPlayer The player
	 * @return The friend head item
	 */
	public ItemStack getHead(Player pPlayer) {
		if (PROFILE_ITEM == null)
			return null;
		ItemStack head = PROFILE_ITEM.clone();
		if (Main.getInstance().getConfig().getBoolean("Inventories.ToolBar.ProfileItem.UsePlayerSkin") && Main.getInstance().getConfig().getBoolean("General.SkinHeadDownload")) {
			SkullMeta skull = (SkullMeta) head.getItemMeta();
			skull.setOwner(pPlayer.getName());
			head.setItemMeta(skull);
		}
		return head;
	}

	public boolean isFriendHead(ItemStack pItemStack) {
		return AdvancedItem.itemsAreEqual(PROFILE_ITEM, pItemStack, false);
	}


}
