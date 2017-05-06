package net.powerkg.market.gui;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import me.kg.easygui.EasyGui;
import net.powerkg.market.IPublishType;
import net.powerkg.market.MarketHandler;
import net.powerkg.market.file.MarketConfig;

public class GuiPublish extends EasyGui
{

	private HashMap<Integer, IPublishType> typeMap = new HashMap<>();

	public GuiPublish(Player p)
	{
		super(p);

		ArrayList<IPublishType> types = MarketHandler.getPublishTypes();
		int needrow = types.size() / 9 + (types.size() % 9 == 0 ? 0 : 1);
		inv = Bukkit.createInventory(null, needrow * 9, "¡ì9¡ìl" + MarketConfig.translate("infoWantToPublish"));

		for (int i = 0; i < (types.size() > 54 ? 54 : types.size()); i++)
		{
			IPublishType type = types.get(i);
			typeMap.put(i, types.get(i));
			inv.setItem(i, type.getDisplay());
		}
	}

	@Override
	public void onVerifiedEvent(InventoryClickEvent event)
	{
		event.setCancelled(true);
		int slot = event.getRawSlot();

		if (typeMap.containsKey(slot))
		{
			close();
			typeMap.get(slot).whenTryPublish(getUser());
		}
	}

	@Override
	public void onClose(InventoryCloseEvent event)
	{

	}

	@Override
	public void onOpen(InventoryOpenEvent event)
	{

	}

}
