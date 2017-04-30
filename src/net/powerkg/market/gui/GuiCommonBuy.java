package net.powerkg.market.gui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import me.kg.easygui.EasyGui;
import net.powerkg.market.ICargo;
import net.powerkg.market.MarketHandler;
import net.powerkg.market.file.FileHandler;
import net.powerkg.market.handler.EconomyHandler;
import net.powerkg.utils.Tools;

public class GuiCommonBuy extends EasyGui
{

	public static final ItemStack sp = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), add = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5),
			remove = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), confirm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);

	static
	{
		Tools.setItemStack(sp, "��c" + FileHandler.translate("GuiBuyHint"), null);

		Tools.setItemStack(add, "��2" + FileHandler.translate("AddBuyAmount"), null);
		Tools.setItemStack(remove, "��c" + FileHandler.translate("ReduceBuyAmount"), null);

		Tools.setItemStack(confirm, "��6��l" + FileHandler.translate("ConfirmToBuy"), null);
	}

	private int amount = 1;
	private ICargo cargo;

	public GuiCommonBuy(ICargo cargo, Player p)
	{
		super(p);

		this.cargo = cargo;
		inv = Bukkit.createInventory(null, 9, "��c" + FileHandler.translate("Buy"));

		inv.setItem(0, cargo.getDisplay());
		inv.setItem(1, sp);

		refreshButton();
	}

	public void refreshButton()
	{
		ItemStack newAdd = add.clone();
		Tools.setItemStack(newAdd, null, Arrays.asList("��9<" + FileHandler.translate("NowBuyAmount") + ": ��6��l" + amount + "��9 >"));
		inv.setItem(2, newAdd);

		ItemStack newRemove = remove.clone();
		Tools.setItemStack(newRemove, null, Arrays.asList("��9<" + FileHandler.translate("NowBuyAmount") + ": ��6��l" + amount + "��9 >"));
		inv.setItem(3, newRemove);

		ItemStack newConfirm = confirm.clone();
		Tools.setItemStack(newConfirm, null,
				Arrays.asList("��6��l" + FileHandler.translate("Total") + " ��6��l" + amount + " " + FileHandler.translate("Part") + " ��6��l" + Tools.toInfo(cargo.getBase()),
						"��c" + FileHandler.translate("NeedToCost") + " ��6��l" + cargo.getCost() * amount, "",
						"��9" + FileHandler.translate("YourBalance") + ": " + EconomyHandler.getBalance(getUser().getName()),
						(canBuy() ? "��2��o" + FileHandler.translate("Reconfirm") : "��6��o" + FileHandler.translate("infoNotEnoughMoney"))));
		inv.setItem(8, newConfirm);
	}

	private boolean canBuy()
	{
		return EconomyHandler.hasMoney(getUser().getName(), amount * cargo.getCost());
	}

	private double getCost()
	{
		return amount * cargo.getCost();
	}

	@Override
	public void onVerifiedEvent(InventoryClickEvent event)
	{
		event.setCancelled(true);
		if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
			return;
		//		if (event.getRawSlot() == 2)
		//		{
		//			++amount;
		//			refreshButton();
		//		}
		//		if (event.getRawSlot() == 3)
		//		{
		//			if (amount - 1 > 0)
		//				--amount;
		//			refreshButton();
		//		}
		if (event.getRawSlot() == 8)
		{
			close();
			if (canBuy())
			{
				if (cargo.tryGetCargo(getUser(), amount))
				{
					MarketHandler.tryBuyCargo(getUser(), cargo, getCost());
					getUser().sendMessage("��2" + FileHandler.translate("infoSuccessfulBuy"));
				}
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
