package net.powerkg.market.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import me.kg.easygui.EasyGui;
import net.powerkg.hyper.HyperMarket;
import net.powerkg.market.file.MarketConfig;

public class GuiHint extends EasyGui
{

	private EasyGui gui;

	public GuiHint(Player p, String title, EasyGui back)
	{
		super(p);

		this.gui = back;

		if (gui == null)
			inv = Bukkit.createInventory(null, 0, title + "¡ìc(" + MarketConfig.translate("infoCloseToBack") + ")");
		else
			inv = Bukkit.createInventory(null, 0, title);
	}

	@Override
	public void onVerifiedEvent(InventoryClickEvent event)
	{
		event.setCancelled(true);
		close();
		jumpBack();
	}

	@Override
	public void onClose(InventoryCloseEvent event)
	{
		Bukkit.getScheduler().runTaskLaterAsynchronously(HyperMarket.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				if (getUser().isOnline())
					jumpBack();
			}
		}, 1L);
	}

	@Override
	public void onOpen(InventoryOpenEvent event)
	{

	}

	public void jumpBack()
	{
		if (gui != null)
			gui.show();
	}

	public static void hint(Player p, String title)
	{
		new GuiHint(p, title, null).show();
	}

	public static void hint(Player p, String title, EasyGui guiback)
	{
		new GuiHint(p, title, guiback).show();
	}

}
