package de.simonsator.partyandfriendsgui.utilities;

import com.google.gson.JsonElement;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class SetHeadOwner113 extends SetHeadOwner {
	@Override
	public void setHeadOwner(SkullMeta pSkull, String playerName, JsonElement playerUUID) {
		OfflinePlayer offlinePlayer;
		if (playerUUID == null)
			offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		else
			offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID.getAsString()));
		pSkull.setOwningPlayer(offlinePlayer);
	}
}
