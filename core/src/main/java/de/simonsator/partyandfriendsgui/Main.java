package de.simonsator.partyandfriendsgui;

import de.simonsator.dependencies.Dependency;
import de.simonsator.dependencies.GsonForOldVersions;
import de.simonsator.partyandfriendsgui.api.menu.MenuManager;
import de.simonsator.partyandfriendsgui.commands.*;
import de.simonsator.partyandfriendsgui.communication.BungeecordCommunication;
import de.simonsator.partyandfriendsgui.inventory.PAFClickManager;
import de.simonsator.partyandfriendsgui.listener.InformAboutInitError;
import de.simonsator.partyandfriendsgui.listener.ItemsManager;
import de.simonsator.partyandfriendsgui.listener.OnEntityDamage;
import de.simonsator.partyandfriendsgui.listener.PlayerSwapHandItem;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.ItemManagerSetupHelper;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import de.simonsator.partyandfriendsgui.utilities.inventorynamegetter.InventoryNameGetterPost113;
import de.simonsator.partyandfriendsgui.utilities.inventorynamegetter.LegacyInventoryNameGetter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/**
 * The main class of the GUI. Here gets everything initialized
 *
 * @author Simonsator
 * @version 1.0.0
 */
public class Main extends JavaPlugin {
	private final static Dependency[] DEPENDENCIES = new Dependency[]{new GsonForOldVersions()};
	/**
	 * The main instance
	 */
	private static Main main;
	/**
	 * The language
	 */
	private String language;
	private ItemManagerSetupHelper itemManagerSetupHelper;

	public static Main getInstance() {
		return main;
	}

	/**
	 * Will be executed on enable
	 */
	@Override
	public void onEnable() {
		main = this;
		loadConfig();
		initManager();
		initLanguage();
		if (!dependenciesLoaded())
			return;
		registerPluginChannel();
		registerCommand();
		registerListeners();
	}

	@Override
	public void onDisable() {
		if (getConfig().getBoolean("General.CloseInventoryOnReload"))
			for (Player player : Bukkit.getServer().getOnlinePlayers())
				player.closeInventory();
	}

	private boolean dependenciesLoaded() {
		for (Dependency dependency : DEPENDENCIES) {
			if (Bukkit.getServer().getPluginManager().getPlugin(dependency.getName()) == null) {
				if (!dependency.needed())
					continue;
				Bukkit.getServer().getConsoleSender().sendMessage("§eThe dependency " + dependency.getName() + " is missing. Trying to download it.");
				if (dependency.download()) {
					Bukkit.getServer().getConsoleSender().sendMessage("§eDownload was successful.");
					Bukkit.getServer().reload();
				} else {
					Bukkit.getServer().getConsoleSender().sendMessage("§4------------------------------------");
					Bukkit.getServer().getConsoleSender().sendMessage("§4------------------------------------");
					Bukkit.getServer().getConsoleSender().sendMessage("§4------------------------------------");
					Bukkit.getServer().getConsoleSender().sendMessage("§4The dependency " + dependency.getName() + " could not be found and neither be downloaded. Download it manually from §e" + dependency.getUrl());
					Bukkit.getServer().getConsoleSender().sendMessage("§4The GUI won't work without it. Going to stop PartyAndFriendsGUI.");
					Bukkit.getServer().getConsoleSender().sendMessage("§4------------------------------------");
					Bukkit.getServer().getConsoleSender().sendMessage("§4------------------------------------");
					Bukkit.getServer().getConsoleSender().sendMessage("§4------------------------------------");
				}
				return false;
			}
		}
		return true;
	}

	private void initManager() {
		new LanguageManager(getConfig());
		new ItemManager(getConfig());
		if (getServer().getBukkitVersion().contains("1.12") || Bukkit.getServer().getVersion().contains("1.11") ||
				Bukkit.getServer().getVersion().contains("1.10") || Bukkit.getServer().getVersion().contains("1.9") ||
				Bukkit.getServer().getVersion().contains("1.8") || Bukkit.getServer().getVersion().contains("1.7")) {
			new LegacyInventoryNameGetter();
		} else {
			new InventoryNameGetterPost113();
		}
		itemManagerSetupHelper = new ItemManagerSetupHelper(getConfig(), ItemManager.getInstance().PLAYER_HEAD);
	}

	private void initLanguage() {
		language = getConfig().getString("General.Language");
		if (getConfig().getBoolean("General.UseOwnLanguageFile"))
			language = "own";
	}

	private void registerCommand() {
		if (getConfig().getBoolean("Commands.FriendRequest.Enabled"))
			registerCommand(new FriendRequestCommand(getConfig().getStringList("Commands.FriendRequest.Names")));
		if (getConfig().getBoolean("Commands.FriendsGUI.Enabled"))
			registerCommand(new FriendsGUICommand(getConfig().getStringList("Commands.FriendsGUI.Names")));
		if (getConfig().getBoolean("Commands.FriendSettings.Enabled"))
			registerCommand(new OpenFriendSettingsCommand(getConfig().getStringList("Commands.FriendSettings.Names")));
		if (getConfig().getBoolean("Commands.Hide.Enabled"))
			registerCommand(new HideCommand(getConfig().getStringList("Commands.Hide.Names")));
		if (getConfig().getBoolean("Commands.PartyGUI.Enabled") && getConfig().getBoolean("Inventories.PartyGUI.Enabled"))
			registerCommand(new PartyGUICommand(getConfig().getStringList("Commands.PartyGUI.Names")));
	}

	private void registerPluginChannel() {
		getServer().getMessenger().registerOutgoingPluginChannel(this, BungeecordCommunication.CHANNEL);
		getServer().getMessenger().registerIncomingPluginChannel(this, BungeecordCommunication.CHANNEL,
				new BungeecordCommunication(LanguageManager.getInstance().getText(TextIdentifier.FRIEND_HEAD_ONLINE_SUFFIX), LanguageManager.getInstance().getText(TextIdentifier.FRIEND_HEAD_OFFLINE_SUFFIX)));
	}

	/**
	 * Registers the listeners
	 */
	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new MenuManager(), this);
		ItemsManager itemsManager = new ItemsManager(this);
		getServer().getPluginManager().registerEvents(itemsManager, this);
		getServer().getPluginManager().registerEvents(new PAFClickManager(itemsManager, LanguageManager.getInstance().getText(TextIdentifier.FRIEND_HEAD_ONLINE_SUFFIX), LanguageManager.getInstance().getText(TextIdentifier.FRIEND_HEAD_OFFLINE_SUFFIX)), this);
		if (getConfig().getBoolean("General.SendFriendshipInvitationsByHittingAPlayer"))
			getServer().getPluginManager().registerEvents(new OnEntityDamage(itemsManager), this);
		if (!(getServer().getBukkitVersion().contains("1.7") || getServer().getBukkitVersion().contains("1.8")))
			getServer().getPluginManager().registerEvents(new PlayerSwapHandItem(itemsManager), this);
	}

	private String getLanguage() {
		return language;
	}

	public void loadConfig() {
		getDataFolder().mkdir();
		File file = new File(getDataFolder(), "config.yml");
		try {
			if (!file.exists()) {
				InputStream in = getResource(file.getName());
				OutputStream out = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();
				in.close();
			}
			getConfig().load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
			getConfig().options().header("You need to activate UseOwnLanguageFile to change the messages");
			getConfig().options().copyDefaults(true);
			if (getServer().getBukkitVersion().contains("1.8") || getServer().getBukkitVersion().contains("1.7")) {
				setDefaults("General.SoundOfHideGUI", "NOTE_PIANO");
			} else {
				if (getServer().getBukkitVersion().contains("1.7") || getServer().getBukkitVersion().contains("1.8") || getServer().getBukkitVersion().contains("1.9") || getServer().getBukkitVersion().contains("1.10") || getServer().getBukkitVersion().contains("1.11") || getServer().getBukkitVersion().contains("1.12")) {
					setDefaults("General.SoundOfHideGUI", "BLOCK_NOTE_BASS");
				} else {
					setDefaults("General.SoundOfHideGUI", "BLOCK_NOTE_BLOCK_BASS");
				}
			}
			if (getServer().getBukkitVersion().contains("1.7") || getServer().getBukkitVersion().contains("1.8") || getServer().getBukkitVersion().contains("1.9") || getServer().getBukkitVersion().contains("1.10") || getServer().getBukkitVersion().contains("1.11") || getServer().getBukkitVersion().contains("1.12")) {
				setDefaults("Inventories.FriendAcceptMenu.AcceptItem.ItemData", "STAINED_CLAY");
				setDefaults("Inventories.FriendAcceptMenu.AcceptItem.MetaData", 5);
				setDefaults("Inventories.FriendAcceptMenu.DenyItem.ItemData", "STAINED_CLAY");
				setDefaults("Inventories.FriendAcceptMenu.DenyItem.MetaData", 14);
				setDefaults("Inventories.SettingsMenu.PlaceHolderItem.ItemData", "STAINED_GLASS_PANE");
				setDefaults("Inventories.SettingsMenu.PlaceHolderItem.MetaData", 15);
				setDefaults("Inventories.SettingsMenu.PartyInviteSetting.TopItem.ItemData", "FIREWORK");
				setDefaults("Inventories.SettingsMenu.FriendJumpSetting.TopItem.ItemData", "WOOD_PLATE");
				setDefaults("Inventories.MainMenuMenuBar.SettingsItem.ItemData", "REDSTONE_COMPARATOR");
				setDefaults("Inventories.MainMenuMenuBar.FriendItem.ItemData", "GOLD_HELMET");
				setDefaults("Inventories.MainMenuMenuBar.PlaceHolderItem.ItemData", "STAINED_GLASS_PANE");
				setDefaults("Inventories.MainMenuMenuBar.PlaceHolderItem.MetaData", 15);
				setDefaults("Inventories.MainMenuMenuBar.NextPageButton.ItemData", "SIGN");
				setDefaults("Inventories.MainMenuMenuBar.PreviousPageButton.ItemData", "SIGN");
				setDefaults("Inventories.MainMenuMenuBar.PartyItem.ItemData", "FIREWORK");
				setDefaults("Inventories.HideInventory.PlaceHolderItem.ItemData", "STAINED_GLASS_PANE");
				setDefaults("Inventories.HideInventory.PlaceHolderItem.MetaData", 15);
				setDefaults("Inventories.FriendDetailView.InviteIntoParty.ItemData", "FIREWORK");
				setDefaults("Inventories.FriendDetailView.JumpTo.ItemData", "WOOD_PLATE");
				setDefaults("Inventories.FriendDetailView.PlaceHolderItem.ItemData", "STAINED_GLASS_PANE");
				setDefaults("Inventories.FriendDetailView.PlaceHolderItem.MetaData", 15);
				setDefaults("Inventories.FriendAcceptMenu.PlaceHolderItem.ItemData", "STAINED_GLASS_PANE");
				setDefaults("Inventories.FriendAcceptMenu.PlaceHolderItem.MetaData", 15);
				setDefaults("Inventories.FriendMenu.PlaceHolderItem.ItemData", "STAINED_GLASS_PANE");
				setDefaults("Inventories.FriendMenu.PlaceHolderItem.MetaData", 15);
				setDefaults("Inventories.PartyGUI.MainMenu.PlaceHolderItem.ItemData", "STAINED_GLASS_PANE");
				setDefaults("Inventories.PartyGUI.MainMenu.PlaceHolderItem.MetaData", 15);
				setDefaults("Inventories.SettingsMenu.NotifyOnlineStatusChange.TopItem.ItemData", "EXP_BOTTLE");
				setDefaults("Inventories.PartyGUI.PartyMemberDetailView.MakeLeader.ItemData", "BOOK_AND_QUILL");
				setDefaults("Inventories.PartyGUI.PartyMemberDetailView.PlaceHolderItem.ItemData", "STAINED_GLASS_PANE");
				setDefaults("Inventories.PartyGUI.PartyMemberDetailView.PlaceHolderItem.MetaData", 15);
				setDefaults("Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmAccept.ItemData", "STAINED_CLAY");
				setDefaults("Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmDecline.ItemData", "STAINED_CLAY");
				setDefaults("Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmAccept.MetaData", 14);
				setDefaults("Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmDecline.MetaData", 5);
				setDefaults("Inventories.ConfirmFriendDeleteMenu.PlaceHolderItem.ItemData", "STAINED_GLASS_PANE");
				setDefaults("Inventories.ConfirmFriendDeleteMenu.PlaceHolderItem.MetaData", 15);
				setDefaults("Inventories.HideInventory.HideModeItems.ShowAll.ItemData", "STAINED_CLAY");
				setDefaults("Inventories.HideInventory.HideModeItems.ShowAll.MetaData", 5);
				setDefaults("Inventories.HideInventory.HideModeItems.HideAll.ItemData", "STAINED_CLAY");
				setDefaults("Inventories.HideInventory.HideModeItems.HideAll.MetaData", 14);
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyFriendsAndPeopleOfTheServerTeam.ItemData", "STAINED_CLAY");
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyFriendsAndPeopleOfTheServerTeam.MetaData", 4);
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyFriends.ItemData", "STAINED_CLAY");
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyFriends.MetaData", 1);
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyPeopleOfTheServerTeamItem.ItemData", "STAINED_CLAY");
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyPeopleOfTheServerTeamItem.MetaData", 10);
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyPartyMembers.ItemData", "STAINED_CLAY");
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyPartyMembers.MetaData", 6);

			} else {
				if (getServer().getBukkitVersion().contains("1.13")) {
					setDefaults("Inventories.MainMenuMenuBar.NextPageButton.ItemData", "SIGN");
					setDefaults("Inventories.MainMenuMenuBar.PreviousPageButton.ItemData", "SIGN");
				} else {
					setDefaults("Inventories.MainMenuMenuBar.NextPageButton.ItemData", "OAK_SIGN");
					setDefaults("Inventories.MainMenuMenuBar.PreviousPageButton.ItemData", "OAK_SIGN");
				}
				setDefaults("Inventories.FriendAcceptMenu.AcceptItem.ItemData", "GREEN_TERRACOTTA");
				setDefaults("Inventories.FriendAcceptMenu.AcceptItem.MetaData", 0);
				setDefaults("Inventories.FriendAcceptMenu.DenyItem.ItemData", "RED_TERRACOTTA");
				setDefaults("Inventories.FriendAcceptMenu.DenyItem.MetaData", 0);
				setDefaults("Inventories.SettingsMenu.PlaceHolderItem.ItemData", "BLACK_STAINED_GLASS_PANE");
				setDefaults("Inventories.SettingsMenu.PlaceHolderItem.MetaData", 0);
				setDefaults("Inventories.SettingsMenu.PartyInviteSetting.TopItem.ItemData", "FIREWORK_ROCKET");
				setDefaults("Inventories.SettingsMenu.FriendJumpSetting.TopItem.ItemData", "OAK_PRESSURE_PLATE");
				setDefaults("Inventories.MainMenuMenuBar.SettingsItem.ItemData", "COMPARATOR");
				setDefaults("Inventories.MainMenuMenuBar.FriendItem.ItemData", "GOLDEN_HELMET");
				setDefaults("Inventories.MainMenuMenuBar.PartyItem.ItemData", "FIREWORK_ROCKET");
				setDefaults("Inventories.MainMenuMenuBar.PlaceHolderItem.ItemData", "BLACK_STAINED_GLASS_PANE");
				setDefaults("Inventories.MainMenuMenuBar.PlaceHolderItem.MetaData", 0);
				setDefaults("Inventories.HideInventory.PlaceHolderItem.ItemData", "BLACK_STAINED_GLASS_PANE");
				setDefaults("Inventories.HideInventory.PlaceHolderItem.MetaData", 0);
				setDefaults("Inventories.FriendDetailView.InviteIntoParty.ItemData", "FIREWORK_ROCKET");
				setDefaults("Inventories.FriendDetailView.JumpTo.ItemData", "OAK_PRESSURE_PLATE");
				setDefaults("Inventories.FriendDetailView.PlaceHolderItem.ItemData", "BLACK_STAINED_GLASS_PANE");
				setDefaults("Inventories.FriendDetailView.PlaceHolderItem.MetaData", 0);
				setDefaults("Inventories.FriendAcceptMenu.PlaceHolderItem.ItemData", "BLACK_STAINED_GLASS_PANE");
				setDefaults("Inventories.FriendAcceptMenu.PlaceHolderItem.MetaData", 0);
				setDefaults("Inventories.PartyGUI.MainMenu.PlaceHolderItem.ItemData", "BLACK_STAINED_GLASS_PANE");
				setDefaults("Inventories.PartyGUI.MainMenu.PlaceHolderItem.MetaData", 0);
				setDefaults("Inventories.PartyGUI.PartyMemberDetailView.MakeLeader.ItemData", "WRITABLE_BOOK");
				setDefaults("Inventories.PartyGUI.PartyMemberDetailView.PlaceHolderItem.ItemData", "BLACK_STAINED_GLASS_PANE");
				setDefaults("Inventories.PartyGUI.PartyMemberDetailView.PlaceHolderItem.MetaData", 0);
				setDefaults("Inventories.FriendMenu.PlaceHolderItem.ItemData", "BLACK_STAINED_GLASS_PANE");
				setDefaults("Inventories.FriendMenu.PlaceHolderItem.MetaData", 0);
				setDefaults("Inventories.SettingsMenu.NotifyOnlineStatusChange.TopItem.ItemData", "EXPERIENCE_BOTTLE");
				setDefaults("Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmAccept.ItemData", "RED_TERRACOTTA");
				setDefaults("Inventories.ConfirmFriendDeleteMenu.FriendRemoveConfirmDecline.ItemData", "GREEN_TERRACOTTA");
				setDefaults("Inventories.ConfirmFriendDeleteMenu.PlaceHolderItem.ItemData", "BLACK_STAINED_GLASS_PANE");
				setDefaults("Inventories.ConfirmFriendDeleteMenu.PlaceHolderItem.MetaData", 0);
				setDefaults("Inventories.HideInventory.HideModeItems.ShowAll.ItemData", "GREEN_TERRACOTTA");
				setDefaults("Inventories.HideInventory.HideModeItems.ShowAll.MetaData", 0);
				setDefaults("Inventories.HideInventory.HideModeItems.HideAll.ItemData", "RED_TERRACOTTA");
				setDefaults("Inventories.HideInventory.HideModeItems.HideAll.MetaData", 0);
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyFriendsAndPeopleOfTheServerTeam.ItemData", "YELLOW_TERRACOTTA");
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyFriendsAndPeopleOfTheServerTeam.MetaData", 0);
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyFriends.ItemData", "ORANGE_TERRACOTTA");
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyFriends.MetaData", 0);
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyPeopleOfTheServerTeamItem.ItemData", "PURPLE_TERRACOTTA");
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyPeopleOfTheServerTeamItem.MetaData", 0);
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyPartyMembers.ItemData", "PINK_TERRACOTTA");
				setDefaults("Inventories.HideInventory.HideModeItems.ShowOnlyPartyMembers.MetaData", 0);
			}
			if (getServer().getBukkitVersion().contains("1.7")) {
				setDefaults("Inventories.FriendDetailView.DeleteItem.ItemData", "LAVA_BUCKET");
				setDefaults("Inventories.PartyGUI.PartyMemberDetailView.KickItem.ItemData", "LAVA_BUCKET");
				setDefaults("Inventories.PartyGUI.MainMenu.LeaveParty.ItemData", "LAVA_BUCKET");
			} else {
				setDefaults("Inventories.FriendDetailView.DeleteItem.ItemData", "BARRIER");
				setDefaults("Inventories.PartyGUI.PartyMemberDetailView.KickItem.ItemData", "BARRIER");
				setDefaults("Inventories.PartyGUI.MainMenu.LeaveParty.ItemData", "BARRIER");
				setDefaults("Inventories.MainMenuMenuBar.GlowIfCurrentMenu", true);
				setDefaults("Inventories.HideInventory.GlowIfSelectedHideMode", true);
			}
			saveConfig();
			for (String path : getConfig().getKeys(true))
				if (getConfig().isString(path))
					getConfig().set(path, ChatColor.translateAlternateColorCodes('&', getConfig().getString(path)));
			if (getConfig().getBoolean("General.UseOwnLanguageFile")) {
				String offlineSuffix = getConfig().getString("Messages.Experimental.FriendHeadOfflineSuffix");
				String onlineSuffix = getConfig().getString("Messages.Experimental.FriendHeadOnlineSuffix");
				if (offlineSuffix.equals(onlineSuffix)) {
					String errorMessage = "§4The value  which was provided by you in the config under \"Messages.Experimental.FriendHeadOfflineSuffix\" and \"Messages.Experimental.FriendHeadOnlineSuffix\" are the same, but they may not be the same. Please change one of those values to another one. The default suffixes will be used.";
					suffixLoadingError(errorMessage);
				} else {
					if (offlineSuffix.length() > 9 || onlineSuffix.length() > 9) {
						String errorMessage = "§4The value  which was provided by you in the config under \"Messages.Experimental.FriendHeadOfflineSuffix\" and/or \"Messages.Experimental.FriendHeadOnlineSuffix\" are too long. They maximum allowed length of those values is 12. The default suffixes will be used.";
						suffixLoadingError(errorMessage);
					} else if (offlineSuffix.isEmpty() || onlineSuffix.isEmpty()) {
						String errorMessage = "§4The value  which was provided by you in the config under \"Messages.Experimental.FriendHeadOfflineSuffix\" and/or \"Messages.Experimental.FriendHeadOnlineSuffix\" is empty. The default suffixes will be used.";
						suffixLoadingError(errorMessage);
					}
				}
			}
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void suffixLoadingError(String pErrorMessage) {
		Bukkit.getServer().getConsoleSender().sendMessage(pErrorMessage);
		new InformAboutInitError(pErrorMessage);
		getConfig().set("Messages.Experimental.FriendHeadOfflineSuffix", "Offline");
		getConfig().set("Messages.Experimental.FriendHeadOnlineSuffix", "Online");
	}

	private void setDefaults(String pEntry, Object pValue) {
		if (getConfig().get(pEntry) == null)
			getConfig().set(pEntry, pValue);
	}

	public String getColor(int colorNumber) {
		switch (colorNumber) {
			case 0:
				if (getLanguage().equalsIgnoreCase("own"))
					if (getConfig().getString("Messages.FriendPlayerColorOnline").length() == 2) {
						return getConfig().getString("Messages.FriendPlayerColorOnline");
					} else {
						System.out.println("[PartyAndFriends] The colors must have exactly two chars.");
						return "§5";
					}
				else
					return "§5";
			case 1:
				if (getLanguage().equalsIgnoreCase("own"))
					if (getConfig().getString("Messages.FriendsStatusOnlineColor").length() == 2) {
						return getConfig().getString("Messages.FriendsStatusOnlineColor");
					} else {
						System.out.println("[PartyAndFriends] The colors must have exactly two chars.");
						return "§a";
					}
				else
					return "§a";
			case 2:
				if (getLanguage().equalsIgnoreCase("own")) {
					if (getConfig().getString("Messages.FriendPlayerColorOffline").length() == 2)
						return getConfig().getString("Messages.FriendPlayerColorOffline");
					else {
						System.out.println("[PartyAndFriends] The colors must have exactly two chars.");
						return "§7";
					}
				} else
					return "§7";
			case 3:
				if (getLanguage().equalsIgnoreCase("own"))
					if (getConfig().getString("Messages.FriendsStatusOfflineColor").length() == 2)
						return getConfig().getString("Messages.FriendsStatusOfflineColor");
					else {
						System.out.println("[PartyAndFriends] The colors must have exactly two chars.");
						return "§c";
					}
				else
					return "§c";
			case 4:
				if (getLanguage().equalsIgnoreCase("own"))
					if (getConfig().getString("Messages.FriendRequestFriendColor").length() == 2)
						return getConfig().getString("Messages.FriendsStatusOfflineColor");
					else {
						System.out.println("[PartyAndFriends] The colors must have exactly two chars.");
						return "§7";
					}
				else
					return "§7";
			case 5:
				if (getLanguage().equalsIgnoreCase("own"))
					if (getConfig().getString("Inventories.PartyGUI.MainMenu.PartyMembers.Color").length() == 2)
						return getConfig().getString("Inventories.PartyGUI.MainMenu.PartyMembers.Color");
					else {
						System.out.println("[PartyAndFriends] The colors must have exactly two chars.");
					}
				return "§b";
			case 6:
				if (getLanguage().equalsIgnoreCase("own"))
					if (getConfig().getString("Inventories.PartyGUI.MainMenu.Leader.Color").length() == 2)
						return getConfig().getString("Inventories.PartyGUI.MainMenu.Leader.Color");
					else {
						System.out.println("[PartyAndFriends] The colors must have exactly two chars.");
					}
				return "§6";
			default:
				return "§r";
		}
	}

	/**
	 * Outputs a block.
	 *
	 * @param itemNumber      The item number of the searched item
	 * @param inventoryNumber The inventory number, where the the item can be found
	 * @return Returns the searched item (makes it easier for changes)
	 */
	@Deprecated
	public ItemStack blockOutput(int itemNumber, int inventoryNumber) {
		switch (inventoryNumber) {
			case -1:
				return ItemManager.getInstance().BACK_ITEM;
			case 0:
				switch (itemNumber) {
					case 0:
						return ItemManager.getInstance().SETTINGS_ITEM;
					default:
						return null;
				}
			case 2:
				switch (itemNumber) {
					case 0:
						return ItemManager.getInstance().FRIEND_REQUEST_ACCEPT_MENU_ACCEPT;
					case 1:
						return ItemManager.getInstance().FRIEND_REQUEST_ACCEPT_MENU_DENY;
					default:
						return null;
				}
			case 3:
				switch (itemNumber) {
					case 0:
						return ItemManager.getInstance().HIDE_ITEM;
					default:
						return null;
				}
			case 4:
				switch (itemNumber) {
					case 0:
						return ItemManager.getInstance().FRIEND_DELETE;
					case 1:
						return ItemManager.getInstance().FRIEND_INVITE_PARTY;
					case 2:
						return ItemManager.getInstance().JUMP_TO_FRIEND;
					default:
						return null;
				}
			case 5:
				switch (itemNumber) {
					case 0:
						return setDisplayName(TextIdentifier.SHOW_ALL_ITEM, ItemManager.getInstance().GREEN_TERRACOTTA.clone());
					case 1:
						return setDisplayName(TextIdentifier.SHOW_ONLY_YOUR_FRIENDS_ITEM, ItemManager.getInstance().ORANGE_TERRACOTTA.clone());
					case 2:
						return setDisplayName(TextIdentifier.SHOW_FRIENDS_AND_PEOPLE_WITH_PERMISSION_ITEM, ItemManager.getInstance().YELLOW_TERRACOTTA.clone());
					case 3:
						return setDisplayName(TextIdentifier.SHOW_ONLY_PEOPLE_FROM_THE_SERVER_ITEM, ItemManager.getInstance().PURPLE_TERRACOTTA.clone());
					case 4:
						return setDisplayName(TextIdentifier.SHOW_NOBODY_ITEM, ItemManager.getInstance().RED_TERRACOTTA.clone());
					default:
						return null;
				}
			case 6: {
				switch (itemNumber) {
					case 0:
						return setDisplayName(TextIdentifier.YOU_RECEIVE_FRIEND_REQUESTS_ITEM, ItemManager.getInstance().GREEN_TERRACOTTA.clone());
					case 1:
						return setDisplayName(TextIdentifier.YOU_RECEIVE_NO_FRIEND_REQUESTS_ITEM, ItemManager.getInstance().RED_TERRACOTTA.clone());
					case 2:
						return setDisplayName(TextIdentifier.YOU_RECEIVE_PARTY_INVITATIONS_ITEM, ItemManager.getInstance().GREEN_TERRACOTTA.clone());
					case 3:
						return setDisplayName(TextIdentifier.YOU_RECEIVE_NO_PARTY_INVITATIONS_ITEM, ItemManager.getInstance().RED_TERRACOTTA.clone());
					case 4:
						return setDisplayName(TextIdentifier.YOUR_STATUS_WILL_BE_SHOWN_ONLINE_ITEM, ItemManager.getInstance().GREEN_TERRACOTTA.clone());
					case 5:
						return setDisplayName(TextIdentifier.YOUR_STATUS_WILL_BE_SHOWN_OFFLINE_ITEM, ItemManager.getInstance().RED_TERRACOTTA.clone());
					case 6:
						if (!getConfig().getBoolean(("General.DisableJump")))
							return setDisplayName(TextIdentifier.NO_JUMP_ITEM, ItemManager.getInstance().GREEN_TERRACOTTA.clone());
						return null;
					case 7:
						if (!getConfig().getBoolean(("General.DisableJump")))
							return setDisplayName(TextIdentifier.JUMP_ALLOWED_ITEM, ItemManager.getInstance().RED_TERRACOTTA.clone());
						return null;
					case 8:
						return setDisplayName(TextIdentifier.YOU_RECEIVE_MESSAGES_ITEM, ItemManager.getInstance().GREEN_TERRACOTTA.clone());
					case 9:
						return setDisplayName(TextIdentifier.YOU_RECEIVE_NO_MESSAGES_ITEM, ItemManager.getInstance().RED_TERRACOTTA.clone());
					case 10:
						return setDisplayName(TextIdentifier.YOU_RECEIVE_ONLINE_STATUS_NOTIFICATION_ITEM, ItemManager.getInstance().GREEN_TERRACOTTA.clone());
					case 11:
						return setDisplayName(TextIdentifier.YOU_RECEIVE_NO_ONLINE_STATUS_NOTIFICATION_ITEM, ItemManager.getInstance().RED_TERRACOTTA.clone());
					default:
						return null;
				}
			}
			case 7:
				switch (itemNumber) {
					case 0: {
						return ItemManager.getInstance().WANT_RECEIVE_FRIEND_REQUESTS_ITEM;
					}
					case 1: {
						return ItemManager.getInstance().WANT_RECEIVE_PARTY_INVITATIONS_ITEM;
					}
					case 2: {
						return ItemManager.getInstance().WANT_TO_BE_SHOWN_AS_ONLINE_ITEM;
					}
					case 3: {
						return ItemManager.getInstance().SHOULD_FRIENDS_BE_ABLE_TO_JUMP_TO_YOU_ITEM;
					}
					case 4: {
						return ItemManager.getInstance().WANT_RECEIVE_MESSAGES_ITEM;
					}
				}
			case 8:
				switch (itemNumber) {
					case 0:
						return ItemManager.getInstance().NEXT_PAGE_ITEM;
					case 1:
						return ItemManager.getInstance().PREVIOUS_PAGE_ITEM;
					default:
						return null;
				}
		}
		return null;
	}

	private ItemStack setDisplayName(TextIdentifier pIdentifier, ItemStack pItemStack) {
		ItemMeta meta = pItemStack.getItemMeta();
		meta.setDisplayName(LanguageManager.getInstance().getText(pIdentifier));
		pItemStack.setItemMeta(meta);
		return pItemStack;
	}

	private ItemStack setDisplayName(TextIdentifier pIdentifier, String pMaterialIdentifier) {
		return setDisplayName(pIdentifier, new ItemStack(itemManagerSetupHelper.MATERIAL_CONVERTER.getMaterial(getConfig().getString("ItemDataNames." + pMaterialIdentifier))));
	}

	@Deprecated
	private ItemStack getGreenLoamStackWithName(TextIdentifier pIdentifier) {
		return setDisplayName(pIdentifier, ItemManager.getInstance().GREEN_TERRACOTTA.clone());
	}

	@Deprecated
	private ItemStack getRedLoamStackWithName(TextIdentifier pIdentifier) {
		return setDisplayName(pIdentifier, ItemManager.getInstance().RED_TERRACOTTA.clone());
	}

	@Deprecated
	public ItemStack getLoamStackWithName(TextIdentifier pIdentifier, short pShort) {
		return getLoamStackWithName112(pIdentifier, pShort);
	}

	public ItemStack getLoamStackWithName112(TextIdentifier pIdentifier, short pShort) {
		return setDisplayName(pIdentifier, new ItemStack(ItemManager.getInstance().STAINED_CLAY_MATERIAL, 1, pShort));
	}

	private void registerCommand(BukkitCommand pCommand) {
		registerCommand(pCommand, this);
	}

	public void registerCommand(BukkitCommand pCommand, JavaPlugin pPlugin) {
		try {
			final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
			commandMap.register(pPlugin.getName(), pCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
