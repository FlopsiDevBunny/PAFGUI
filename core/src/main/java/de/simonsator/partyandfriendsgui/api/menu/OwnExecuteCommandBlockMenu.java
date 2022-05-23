package de.simonsator.partyandfriendsgui.api.menu;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.manager.ItemManager;
import de.simonsator.partyandfriendsgui.manager.ItemManagerSetupHelper;
import de.simonsator.partyandfriendsgui.utilities.OwnExecuteCommandContainer;
import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

public interface OwnExecuteCommandBlockMenu {
	@Deprecated
	default List<OwnExecuteCommandContainer> createExecuteCommandContainerList(String pKeyPrefix) {
		return createExecuteCommandContainerList(Main.getInstance().getConfig(), pKeyPrefix);
	}

	default List<OwnExecuteCommandContainer> createExecuteCommandContainerList(Configuration pConfig, String pKeyPrefix) {
		List<OwnExecuteCommandContainer> ownExecuteCommandContainerList = new ArrayList<>();
		ItemManagerSetupHelper setupHelper = new ItemManagerSetupHelper(pConfig, ItemManager.getInstance().PLAYER_HEAD);
		for (String key : pConfig.getConfigurationSection(pKeyPrefix).getKeys(false)) {
			key = pKeyPrefix + "." + key + ".";
			if (pConfig.getBoolean(key + "Use") && conditionForAdding(pConfig, key))
				ownExecuteCommandContainerList.add(new OwnExecuteCommandContainer(key, setupHelper));
		}
		return ownExecuteCommandContainerList;
	}

	boolean conditionForAdding(Configuration pConfig, String pKey);
}
