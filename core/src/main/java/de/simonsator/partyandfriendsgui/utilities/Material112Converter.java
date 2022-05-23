package de.simonsator.partyandfriendsgui.utilities;

import org.bukkit.Material;

public class Material112Converter extends MaterialConverter {

	@Override
	public Material getMaterial(String pMaterialName) {
		return Material.valueOf(pMaterialName);
	}
}
