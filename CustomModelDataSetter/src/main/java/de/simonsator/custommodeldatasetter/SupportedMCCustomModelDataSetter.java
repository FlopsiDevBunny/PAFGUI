package de.simonsator.custommodeldatasetter;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SupportedMCCustomModelDataSetter extends CustomModelDataSetter {
	@Override
	public void setCustomModelData(ItemStack pItemStack, Integer pCustomModelData) {
		if (pCustomModelData != null && pCustomModelData != 0) {
			ItemMeta meta = pItemStack.getItemMeta();
			assert meta != null;
			meta.setCustomModelData(pCustomModelData);
			pItemStack.setItemMeta(meta);
		}
	}
}
