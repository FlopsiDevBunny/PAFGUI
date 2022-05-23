package de.simonsator.partyandfriendsgui.utilities;

import de.simonsator.partyandfriendsgui.manager.ItemManagerSetupHelper;
import org.bukkit.inventory.ItemStack;

public class OwnExecuteCommandContainer {
	public final ItemStack ITEM;
	public final String COMMAND;
	public final int PLACE;
	public final boolean IS_BUNGEECORD_COMMAND;
	public final boolean KEEP_OPEN;

	public OwnExecuteCommandContainer(String itemPart, ItemManagerSetupHelper setupHelper) {
		COMMAND = setupHelper.getConfig().getString(itemPart + "Command");
		PLACE = setupHelper.getConfig().getInt(itemPart + "Place");
		ITEM = setupHelper.getItemStack(setupHelper.getConfig().getString(itemPart + "Text"), itemPart + "ItemData", itemPart + "MetaData", itemPart + "UseCustomTexture", itemPart + "Base64CustomTexture", itemPart + "CustomModelData");
		IS_BUNGEECORD_COMMAND = setupHelper.getConfig().getBoolean(itemPart + "BungeeCordCommand");
		KEEP_OPEN = setupHelper.getConfig().getBoolean(itemPart + "KeepOpen");
	}

}
