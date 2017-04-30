package me.kg.easygui;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public class GuiHandler implements Listener
{
	private static GuiHandler instance = null;

	private GuiHandler()
	{

	}

	private static ArrayList<EasyGui> Guis = new ArrayList<>();

	public static void init(Plugin plugin)
	{
		if (instance == null)
			plugin.getServer().getPluginManager().registerEvents(instance = new GuiHandler(), plugin);
	}

	public static void registerGui(EasyGui gui)
	{
		Guis.add(gui);
	}

	@EventHandler
	public void onClienk(InventoryClickEvent e)
	{
		if (!(e.getWhoClicked() instanceof Player))
			return;
		for (EasyGui gui : Guis)
		{
			if (gui.isInv(e.getInventory()) && gui.getUser().equals((Player) e.getWhoClicked()))
			{
				gui.onEvent(e);
				break;
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e)
	{
		if (!(e.getPlayer() instanceof Player))
			return;

		for (EasyGui gui : Guis)
			if (gui.isInv(e.getInventory()) && gui.getUser().equals((Player) e.getPlayer()))
			{
				gui.onClose(e);
				Guis.remove(gui);
				break;
			}

	}

	@EventHandler
	public void onOpen(InventoryOpenEvent e)
	{
		if (!(e.getPlayer() instanceof Player))
			return;
		for (EasyGui gui : Guis)
			if (gui.isInv(e.getInventory()) && gui.getUser().equals((Player) e.getPlayer()))
			{
				gui.onOpen(e);
				break;
			}
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent e)
	{
		ArrayList<EasyGui> clones = new ArrayList<>();
		clones.addAll(Guis);

		for (EasyGui gui : clones)
		{
			Guis.remove(gui);
			gui.getUser().closeInventory();
		}
	}

}
