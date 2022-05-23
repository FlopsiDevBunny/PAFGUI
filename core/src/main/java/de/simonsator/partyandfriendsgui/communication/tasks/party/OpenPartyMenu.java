package de.simonsator.partyandfriendsgui.communication.tasks.party;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import de.simonsator.partyandfriendsgui.api.events.creation.HeadCreationEvent;
import de.simonsator.partyandfriendsgui.api.events.creation.menu.MainMenuCreationEvent;
import de.simonsator.partyandfriendsgui.api.menu.MainMenu;
import de.simonsator.partyandfriendsgui.api.menu.MenuManager;
import de.simonsator.partyandfriendsgui.communication.BungeecordCommunication;
import de.simonsator.partyandfriendsgui.communication.tasks.CommunicationTask;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.LanguageManager;
import de.simonsator.partyandfriendsgui.manager.TextIdentifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class OpenPartyMenu extends CommunicationTask implements MainMenu {
	private final int MIN_SIZE = Main.getInstance().getConfig().getInt("Inventories.PartyGUI.MainMenu.Minimum-Lines") * 9;
	private final int PLUS_PAGE;

	public OpenPartyMenu() {
		super("OpenPartyMenu");
		if (Main.getInstance().getConfig().getBoolean("Inventories.PartyGUI.MainMenu.StartsWithOne"))
			PLUS_PAGE = 1;
		else PLUS_PAGE = 0;
	}

	@Override
	public void executeTask(Player pPlayer, JsonObject pJObj) {
		MenuManager.getInstance().setLastOpened(pPlayer, this);
		if (!pJObj.get("isInParty").getAsBoolean())
			createNoPartyMenu(pPlayer);
		else
			createPartyMenu(pPlayer, pJObj);
	}

	private void createNoPartyMenu(Player pPlayer) {
		int size = 27;
		if (MIN_SIZE > size)
			size = MIN_SIZE;
		Inventory inv = Bukkit.createInventory(null, size,
				LanguageManager.getInstance().getText(TextIdentifier.PARTY_MENU_NAME) + PLUS_PAGE + LanguageManager.getInstance().getText(TextIdentifier.PAST_PARTY_PAGE));
		inv.setItem(4, ItemManager.getInstance().HOW_TO_CREATE_A_PARTY);
		addPlaceholders(inv);
		createMenuBar(pPlayer, inv, false, false);
		pPlayer.openInventory(inv);
	}

	private void addPlaceholders(Inventory pInv) {
		for (int i = 0; i < 9; i++)
			if (pInv.getItem(i) == null)
				pInv.setItem(i, ItemManager.getInstance().PARTY_MENU_PLACEHOLDER);
	}

	private void createPartyMenu(Player pPlayer, JsonObject pJObj) {
		JsonArray partyMembers = pJObj.get("partyMembers").getAsJsonArray();
		int partySize = partyMembers.size();
		int in = partySize + 1;
		int toSet = (in / 9 + 1) * 9 + 27;
		if (MIN_SIZE > toSet)
			toSet = MIN_SIZE;
		if (partySize >= 27)
			toSet = 45;
		if (toSet < MIN_SIZE)
			toSet = MIN_SIZE;
		int page = pJObj.get("page").getAsInt();
		Inventory inv = Bukkit.createInventory(null, toSet,
				LanguageManager.getInstance().getText(TextIdentifier.PARTY_MENU_NAME) + (page + PLUS_PAGE) + LanguageManager.getInstance().getText(TextIdentifier.PAST_PARTY_PAGE));
		inv.setItem(4, createHead(pJObj.get("leader").getAsJsonObject(), true));
		int momHeadNumber = 0;
		int start = page * 18;
		for (int i = start; i < partyMembers.size() && momHeadNumber < 18; i++) {
			inv.setItem(momHeadNumber + 9, createHead(partyMembers.get(i).getAsJsonObject(), false));
			momHeadNumber++;
		}
		addPlaceholders(inv);
		createMenuBar(pPlayer, inv, page != 0, start + momHeadNumber < (partyMembers.size()));
		Bukkit.getServer().getPluginManager().callEvent(new MainMenuCreationEvent(pPlayer, inv, pJObj, getClass()));
		pPlayer.openInventory(inv);
	}

	private ItemStack createHead(JsonObject pPartyMember, boolean pIsPartyLeader) {
		ItemStack item = new ItemStack(ItemManager.getInstance().PLAYER_HEAD_MATERIAL, 1, (short) 3);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<>();
		String playerName = pPartyMember.getAsJsonObject().get("playerName").getAsString();
		if (pIsPartyLeader) {
			meta.setDisplayName(Main.getInstance().getColor(6) + playerName);
			lore.add(LanguageManager.getInstance().getText(TextIdentifier.LORE_IS_LEADER));
		} else {
			meta.setDisplayName(Main.getInstance().getColor(5) + playerName);
			lore.add(LanguageManager.getInstance().getText(TextIdentifier.LORE_IS_MEMBER));
		}
		if (Main.getInstance().getConfig().getBoolean("Inventories.PartyGUI.MainMenu.PartyMembers.ShowServer"))
			lore.add(LanguageManager.getInstance().getText(TextIdentifier.ONLINE_ON).replace("[SERVER]", pPartyMember.getAsJsonObject().get("serverName").getAsString()));
		meta.setLore(lore);
		item.setItemMeta(meta);
		if (Main.getInstance().getConfig().getBoolean("General.SkinHeadDownload")) {
			SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
			BungeecordCommunication.getInstance().SET_HEAD_OWNER.setHeadOwner(skullMeta, playerName, pPartyMember.get("playerUUID"));

			item.setItemMeta(skullMeta);
		}
		Bukkit.getServer().getPluginManager().callEvent(new HeadCreationEvent(item, true, pPartyMember, this));
		return item;
	}

	@Override
	public void openMenu(Player pPlayer) {
		PartyFriendsAPI.openPartyInventory(pPlayer, 0);
	}
}
