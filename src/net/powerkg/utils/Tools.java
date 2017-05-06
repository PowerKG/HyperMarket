package net.powerkg.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.powerkg.market.ICargo;
import net.powerkg.market.file.MarketConfig;

public class Tools
{
	public static final ItemStack setItemStack(ItemStack item, String title, List<String> lore)
	{
		ItemMeta itemMeta = item.getItemMeta();
		if (title != null)
			itemMeta.setDisplayName(title);
		if (lore != null)
			itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);

		return item;
	}

	public static final ItemStack addItemStackLore(ItemStack item, String... str)
	{
		ItemMeta itemMeta = item.getItemMeta();

		List<String> lore;
		if (itemMeta.hasLore())
			lore = itemMeta.getLore();
		else
			lore = new ArrayList<>();

		lore.addAll(Arrays.asList(str));
		itemMeta.setLore(lore);

		item.setItemMeta(itemMeta);

		return item;
	}

	public static String toInfo(ItemStack stack)
	{
		return stack.getAmount() + " X " + (stack.getItemMeta().hasDisplayName() ? stack.getItemMeta().getDisplayName() : stack.getType().toString());
	}

	public static String toInfo(int amount, ItemStack item)
	{

		return amount + " " + MarketConfig.translate("Part") + " " + Tools.toInfo(item);
	}
}
