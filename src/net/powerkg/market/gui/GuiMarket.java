package net.powerkg.market.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import me.kg.easygui.EasyGui;
import net.powerkg.market.ICargo;
import net.powerkg.market.MarketHandler;
import net.powerkg.market.file.FileHandler;
import net.powerkg.utils.Tools;

public class GuiMarket extends EasyGui
{
	private static HashMap<String, Integer> pageCache = new HashMap<>();

	private static ItemStack next/*上一页*/
			, pre/*下一页*/
			, publish/*发布*/
			, sort/*切换排序方式(未完工)*/
			, search/*查找市场*/;

	private static final int slotOptionStart = 45;

	private int pageNow = 1;
	private int pageMax = 1;

	private HashMap<Integer, ICargo> cargoMap = new HashMap<>();
	static
	{
		//预加载

		next = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		Tools.setItemStack(next, "§2" + FileHandler.translate("Page Up"), null);
		pre = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3);
		Tools.setItemStack(pre, "§2" + FileHandler.translate("Page Down"), null);

		publish = new ItemStack(Material.BOOK_AND_QUILL);
		Tools.setItemStack(publish, "§a" + FileHandler.translate("Publish"), Arrays.asList("", "§7§o(" + FileHandler.translate("infoJump") + "§7)"));

		sort = new ItemStack(Material.PAINTING);
		Tools.setItemStack(sort, null, null);

		search = new ItemStack(Material.COMPASS);
		Tools.setItemStack(search, "§c" + FileHandler.translate("Search"), Arrays.asList("", "§7§o" + FileHandler.translate("infoJump") + "§7)"));
	}

	public GuiMarket(Player p)
	{
		super(p);
		inv = Bukkit.createInventory(null, 54, "§c§l" + FileHandler.translate("Market"));

		load();
	}

	public GuiMarket(Player p, int page)
	{
		super(p);
		inv = Bukkit.createInventory(null, 54, "§c§l" + FileHandler.translate("Market"));

		pageNow = page;

		load();
	}

	private void reload()
	{
		inv.clear();
		cargoMap.clear();
		load();
	}

	private void load()
	{
		loadCargo();
		setOption();
	}

	private void loadCargo()
	{
		ArrayList<ICargo> cargos = MarketHandler.getCargos();
		if (!cargos.isEmpty())
			pageMax = cargos.size() / 45 + (cargos.size() % 45 == 0 ? 0 : 1);
		else
			pageMax = 1;
		for (int i = 0; i < (pageNow == pageMax ? cargos.size() % 45 : 45); i++)
		{
			ICargo cargo = cargos.get((pageNow - 1) * 45 + i);
			cargoMap.put(i, cargo);
			inv.setItem(i, cargo.getDisplay());
		}
	}

	private void refreshOption()
	{
		ItemStack fnext = next.clone(), fpre = pre.clone();
		Tools.setItemStack(fnext, null, Arrays.asList("§9" + FileHandler.translate("Now Page Is") + " §6[§l" + pageNow + " §6/ §l" + pageMax + "]"));
		Tools.setItemStack(fpre, null, Arrays.asList("§9" + FileHandler.translate("Now Page Is") + " §6[§l" + pageNow + " §6/ §l" + pageMax + "]"));

		inv.setItem(slotOptionStart + 0, fpre);
		inv.setItem(slotOptionStart + 1, fnext);
	}

	private void setOption()
	{
		refreshOption();

		inv.setItem(slotOptionStart + 4, publish);

		inv.setItem(slotOptionStart + 8, search);
	}

	public static void open(Player p)
	{
		int page = (pageCache.containsKey(p.getName().toLowerCase()) ? pageCache.get(p.getName().toLowerCase()) : 1);
		new GuiMarket(p, page).show();
	}

	@Override
	public void onVerifiedEvent(InventoryClickEvent event)
	{
		event.setCancelled(true);

		int slot = event.getRawSlot();
		boolean isRightClick = false;

		if (event.getAction().equals(InventoryAction.PICKUP_HALF))
		{
			isRightClick = true;
		}

		if (slot >= 45 && slot <= 53)
		{
			if (slot == slotOptionStart + 0 || slot == slotOptionStart + 1)
			{

				int newPage = pageNow;
				if (slot == slotOptionStart + 0)
				{
					if (newPage - 1 < 1)
						newPage = pageMax;
					else
						--newPage;
				} else if (slot == slotOptionStart + 1)
				{
					if (newPage + 1 > pageMax)
					{
						newPage = 1;
					} else
						++newPage;
				}

				if (pageNow != newPage)
				{
					pageNow = newPage;
					reload();

					if (FileHandler.getSetting().UsePageCache)
					{
						String name = getUser().getName().toLowerCase();

						if (newPage == 1)
							pageCache.remove(name);
						else
							pageCache.put(name, newPage);
					}
				}
			}
			switch (slot)
			{
			case slotOptionStart + 4:
				//publish
				close();
				new GuiPublish(getUser()).show();
				break;
			case slotOptionStart + 8:
				//search
				break;
			}
		} else
		{
			if (cargoMap.containsKey(slot))
			{
				close();
				cargoMap.get(slot).cargoClick(getUser(), isRightClick);
			}
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
