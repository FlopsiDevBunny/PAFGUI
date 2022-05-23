package de.simonsator.partyandfriendsgui.utilities;

import com.google.gson.JsonElement;
import org.bukkit.inventory.meta.SkullMeta;

public abstract class SetHeadOwner {
	public abstract void setHeadOwner(SkullMeta skullMeta, String playerName, JsonElement playerUUID);
}
