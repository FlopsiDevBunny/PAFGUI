package de.simonsator.partyandfriendsgui.utilities;

import org.bukkit.Material;

public class Material113Converter extends MaterialConverter {

	@Override
	public Material getMaterial(String pMaterialName) {
		Material material = Material.getMaterial(pMaterialName, false);
		if (material != null)
			return material;
		return Material.getMaterial(pMaterialName, true);
	}
}
