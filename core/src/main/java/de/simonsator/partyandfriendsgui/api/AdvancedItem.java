package de.simonsator.partyandfriendsgui.api;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AdvancedItem {
	public static boolean itemsAreEqual(ItemStack pFirstItem, ItemStack pSecondItem, boolean pIgnoreEnchantment) {
		if (pFirstItem == null || pSecondItem == null)
			return false;
		if (pFirstItem.getType() != pSecondItem.getType())
			return false;
		ItemMeta firstMeta = pFirstItem.getItemMeta();
		ItemMeta secondMeta = pSecondItem.getItemMeta();
		if (firstMeta != null) {
			if (secondMeta == null)
				return false;
			if (pFirstItem.getDurability() != pSecondItem.getDurability())
				return false;
			String firstDisplayName = firstMeta.getDisplayName();
			String secondDisplayName = secondMeta.getDisplayName();
			if (firstDisplayName != null) {
				if (firstDisplayName.equals(secondDisplayName)) {
					List<String> item1List = firstMeta.getLore();
					List<String> item2List = secondMeta.getLore();
					if ((item1List != null && item1List.size() > 0) || (item2List != null && item2List.size() > 0)) {
						if (item1List == null || !item1List.equals(item2List)) {
							return false;
						}
					}
					if (pIgnoreEnchantment)
						return true;
					return pFirstItem.getEnchantments().equals(pSecondItem.getEnchantments());
				} else
					return false;
			}
			return true;
		}
		return secondMeta == null;
	}
}
