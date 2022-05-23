package de.simonsator.partyandfriendsgui.nmsdepending;

import org.bukkit.inventory.ItemStack;

public class NoGlowEffect extends GlowEffect {
	@Override
	public ItemStack addGlow(ItemStack item) {
		return item;
	}
}
