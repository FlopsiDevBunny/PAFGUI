package de.simonsator.partyandfriendsgui.inventory.tasks.executeclicktask;

import de.simonsator.partyandfriendsgui.Main;
import de.simonsator.partyandfriendsgui.api.AdvancedItem;
import de.simonsator.partyandfriendsgui.utilities.inventorynamegetter.InventoryNameGetter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Simonsator
 * @version 1.0.0 on 12.07.16.
 */
public abstract class ChangePage extends InventoryTask {
	private final int CHANGE;
	private final ItemStack APPLICABLE_ITEM;
	private final String PREFIX;
	private final String POSTFIX;

	protected ChangePage(int pChange, ItemStack pApplicableItem, String pPrefix, boolean startsWithOne) {
		int minus = 0;
		if (startsWithOne)
			minus = 1;
		CHANGE = pChange - minus;
		APPLICABLE_ITEM = pApplicableItem;
		PREFIX = pPrefix;
		POSTFIX = "";
	}

	protected ChangePage(int pChange, ItemStack pApplicableItem, String pPrefix, String pPostFix, boolean startsWithOne) {
		int minus = 0;
		if (startsWithOne)
			minus = 1;
		CHANGE = pChange - minus;
		APPLICABLE_ITEM = pApplicableItem;
		PREFIX = pPrefix;
		POSTFIX = pPostFix;
	}

	@Override
	public void execute(InventoryClickEvent pEvent) {
		String num = InventoryNameGetter.getInstance().getName(pEvent).substring(PREFIX.length());
		num = num.substring(0, num.length() - POSTFIX.length());
		if (Main.getInstance().getConfig().getBoolean("General.Compatibility.ResetCursorOnMenuSwitch"))
			pEvent.getWhoClicked().closeInventory();
		pEvent.setCancelled(true);
		openMenu((Player) pEvent.getWhoClicked(),
				(Integer.parseInt(num) + CHANGE));
	}

	protected abstract void openMenu(Player pPlayer, int pPage);

	@Override
	public boolean isApplicable(InventoryClickEvent pEvent) {
		return AdvancedItem.itemsAreEqual(APPLICABLE_ITEM, pEvent.getCurrentItem(), false);
	}

}
