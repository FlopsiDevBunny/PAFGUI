package de.simonsator.partyandfriendsgui.manager;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.simonsator.custommodeldatasetter.CustomModelDataSetter;
import de.simonsator.custommodeldatasetter.SupportedMCCustomModelDataSetter;
import de.simonsator.custommodeldatasetter.UnsupportedMCCustomModelDataSetter;
import de.simonsator.partyandfriendsgui.utilities.Material112Converter;
import de.simonsator.partyandfriendsgui.utilities.Material113Converter;
import de.simonsator.partyandfriendsgui.utilities.MaterialConverter;
import de.simonsator.partyandfriendsgui.utilities.Splitter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author simonbrungs
 * @version 1.0.0 06.01.17
 */
public class ItemManagerSetupHelper {
	public final MaterialConverter MATERIAL_CONVERTER;
	private final Configuration CONFIG;
	private final boolean NOT_VER_19 = !Bukkit.getServer().getVersion().contains("1.7");
	private final ItemStack PLAYER_HEAD;
	private final CustomModelDataSetter CUSTOM_MODEL_DATA;

	@Deprecated
	public ItemManagerSetupHelper(Configuration pConfig) {
		this(pConfig, null);
	}

	public ItemManagerSetupHelper(Configuration pConfig, ItemStack playerHead) {
		CONFIG = pConfig;
		if (Bukkit.getVersion().contains("1.7") || Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12")) {
			MATERIAL_CONVERTER = new Material112Converter();
			CUSTOM_MODEL_DATA = new UnsupportedMCCustomModelDataSetter();
		} else {
			if (Bukkit.getVersion().contains("1.13."))
				CUSTOM_MODEL_DATA = new UnsupportedMCCustomModelDataSetter();
			else CUSTOM_MODEL_DATA = new SupportedMCCustomModelDataSetter();
			MATERIAL_CONVERTER = new Material113Converter();
		}
		PLAYER_HEAD = playerHead;
	}

	private ItemStack setDisplayName(String pText, ItemStack pItemStack) {
		ItemDisplay itemDisplay = toItemDisplay(pText);
		ItemMeta meta = pItemStack.getItemMeta();
		assert meta != null;
		meta.setDisplayName(itemDisplay.NAME);
		meta.setLore(itemDisplay.LORE);
		if (NOT_VER_19)
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		pItemStack.setItemMeta(meta);
		return pItemStack;
	}

	public ItemStack getItemStack(TextIdentifier pTextIdentifier, String pItemMaterial, String pItemMetaData, String pUseBase64, String pBase64Value) {
		return getItemStack(pTextIdentifier, pItemMaterial, pItemMetaData, pUseBase64, pBase64Value, (Integer) null);
	}

	public ItemStack getItemStack(TextIdentifier pTextIdentifier, String pItemMaterial, String pItemMetaData, String pUseBase64, String pBase64Value, String pCustomModelData) {
		return getItemStack(LanguageManager.getInstance().getText(pTextIdentifier), pItemMaterial, pItemMetaData, pUseBase64, pBase64Value, CONFIG.getInt(pCustomModelData));
	}

	public ItemStack getItemStack(TextIdentifier pTextIdentifier, String pItemMaterial, String pItemMetaData, String pUseBase64, String pBase64Value, Integer pCustomModelData) {
		return getItemStack(LanguageManager.getInstance().getText(pTextIdentifier), pItemMaterial, pItemMetaData, pUseBase64, pBase64Value, pCustomModelData);
	}

	public ItemStack getItemStack(String pText, String pItemMaterial, String pItemMetaData, String pUseBase64, String pBase64Value) {
		return getItemStack(pText, pItemMaterial, pItemMetaData, pUseBase64, pBase64Value, (Integer) null);
	}

	public ItemStack getItemStack(String pText, String pItemMaterial, String pItemMetaData, String pUseBase64, String pBase64Value, String pCustomModelData) {
		return getItemStack(pText, pItemMaterial, pItemMetaData, pUseBase64, pBase64Value, CONFIG.getInt(pCustomModelData));
	}

	public ItemStack getItemStack(String pText, String pItemMaterial, String pItemMetaData, String pUseBase64, String pBase64Value, Integer pCustomModelData) {
		if (getConfig().getBoolean(pUseBase64)) {
			if (Bukkit.getVersion().contains("1.7")) {
				System.out.println("Custom textures are not supported in this Minecraft version. Please use a newer minecraft version.");
				return getItemStack(pText, pItemMaterial, pItemMetaData);
			}
			ItemStack itemStack = getCustomTextureHead(getConfig().getString(pBase64Value));
			if (itemStack != null) {
				setDisplayName(pText, itemStack);
				return itemStack;
			}
			System.out.println("Invalid custom texture on " + pBase64Value);
		}
		return getItemStack(pText, pItemMaterial, pItemMetaData, pCustomModelData);
	}

	public ItemStack getItemStack(TextIdentifier pTextIdentifier, String pItemMaterial, String pItemMetaData) {
		return getItemStack(LanguageManager.getInstance().getText(pTextIdentifier), pItemMaterial, pItemMetaData);
	}

	public ItemStack getItemStack(String pText, String pItemMaterial, String pItemMetaData) {
		return getItemStack(pText, pItemMaterial, pItemMetaData, null);
	}

	public ItemStack getItemStack(String pText, String pItemMaterial, String pItemMetaData, Integer pCustomModelData) {
		ItemStack itemStack;
		short metaData = (short) CONFIG.getInt(pItemMetaData);
		if (metaData != 0)
			itemStack = new ItemStack(MATERIAL_CONVERTER.getMaterial(CONFIG.getString(pItemMaterial)), 1, metaData);
		else
			itemStack = new ItemStack(MATERIAL_CONVERTER.getMaterial(CONFIG.getString(pItemMaterial)), 1);
		setDisplayName(pText, itemStack);
		CUSTOM_MODEL_DATA.setCustomModelData(itemStack, pCustomModelData);
		return itemStack;
	}

	private ItemDisplay toItemDisplay(String pText) {
		Splitter splitter = new Splitter(pText, "%LINE_BREAK%");
		String displayName = splitter.next();
		List<String> lore = new LinkedList<>();
		while (splitter.hasNext())
			lore.add(splitter.next());
		return new ItemDisplay(displayName, lore);
	}

	private ItemStack getCustomTextureHead(String value) {
		ItemStack head = PLAYER_HEAD.clone();
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), "");
		profile.getProperties().put("textures", new Property("textures", value));
		Method setProfileMethod;
		try {
			assert meta != null;
			setProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
			setProfileMethod.setAccessible(true);
			setProfileMethod.invoke(meta, profile);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchMethodException | InvocationTargetException e) {
			Field profileField;
			try {
				profileField = meta.getClass().getDeclaredField("profile");
				profileField.setAccessible(true);
				profileField.set(meta, profile);
			} catch (NoSuchFieldException | IllegalAccessException ex) {
				System.out.println("Custom textures are not supported in this Minecraft version. Please use a newer minecraft version.");
				return null;
			}
		}
		head.setItemMeta(meta);
		return head;
	}

	public Configuration getConfig() {
		return CONFIG;
	}

	private static class ItemDisplay {
		private final String NAME;
		private final List<String> LORE;

		private ItemDisplay(String name, List<String> lore) {
			NAME = name;
			LORE = lore;
		}
	}
}
