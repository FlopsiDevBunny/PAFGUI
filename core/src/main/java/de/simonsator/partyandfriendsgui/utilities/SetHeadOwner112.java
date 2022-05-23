package de.simonsator.partyandfriendsgui.utilities;

import com.google.gson.JsonElement;
import org.bukkit.inventory.meta.SkullMeta;

public class SetHeadOwner112 extends SetHeadOwner {
	@Override
	public void setHeadOwner(SkullMeta pSkull, String playerName, JsonElement playerUUID) {
		pSkull.setOwner(playerName);
	}
}
