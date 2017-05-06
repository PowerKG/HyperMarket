package me.kg.easygui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public abstract class EasyGui
{
	protected Inventory inv = null;
	private boolean verifyClick = true;
	private Player p;

	public EasyGui(Player p, int row, String title)
	{
		this.p = p;
		inv = Bukkit.getServer().createInventory(null, row * 9, title);
	}

	public EasyGui(Player p)
	{
		this.p = p;
	}

	public Inventory getInv()
	{
		return inv;
	}

	public Player getUser()
	{
		return p;
	}

	public boolean isInv(Inventory CInv)
	{
		if (inv.equals(CInv))
			return true;
		return false;
	}

	public final void show()
	{
		if (inv != null)
		{
			GuiHandler.registerGui(this);
			p.openInventory(inv);
		} else
			p.closeInventory();
	}

	public final void close()
	{
		getUser().closeInventory();
	}

	public void setVerifyClick(boolean flag)
	{
		verifyClick = flag;
	}

	public void onEvent(InventoryClickEvent event)
	{
		if (verifyClick)
		{
			if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR))
			{
				if (event.getAction().equals(InventoryAction.NOTHING))
					return;
				onVerifiedEvent(event);
			}
		} else
		{
			onVerifiedEvent(event);
		}
	}

	public void onForceClose()
	{

	}

	/**
	 * 如果你想要跳转到其他页面请使用
	 * 		e.setCancelled(true);
	 * 来取消点击,否则会导致BUG
	 * **/
	public abstract void onVerifiedEvent(InventoryClickEvent event);

	public abstract void onClose(InventoryCloseEvent event);

	public abstract void onOpen(InventoryOpenEvent event);
}
