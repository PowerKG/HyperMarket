package net.powerkg.mailbox.mail;

import java.util.Date;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.powerkg.mailbox.GuiMailBox;
import net.powerkg.market.file.MarketConfig;
import net.powerkg.market.gui.GuiHint;
import net.powerkg.utils.Tools;

public class MailItem extends IMail
{

	private static ItemStack defDisplay = new ItemStack(Material.STORAGE_MINECART);

	private ItemStack inItem;

	private int Amount;
	private double Cost;

	private ItemStack ownShow = null;

	private String Sender = null;

	private boolean IsTrade = false;

	public MailItem(String sender, int amount, ItemStack item, Date theData)
	{
		date = theData;
		this.inItem = item;
		this.Sender = sender;
		this.Amount = amount;
	}

	public MailItem(String sender, Boolean isTrade, double cost, int amount, ItemStack item, Date theData)
	{
		this(sender, amount, item, theData);
		this.IsTrade = isTrade;
		this.Cost = cost;
	}

	public MailItem()
	{
	}

	private ItemStack loadDisplay()
	{
		if (ownShow == null)
		{
			ownShow = defDisplay.clone();
			Tools.setItemStack(ownShow, "¡ìf" + MarketConfig.translate("Topic") + ": " + MarketConfig.translate("infoReceivedItem"), null);

			String tradeInfo = null;
			if (IsTrade)
				tradeInfo = " ¡ìf¡ìl| ¡ìf¡ìo¡ìl" + MarketConfig.translate("infoReplaceableMailItemBuy").replaceAll("%Cost", "" + Cost).replaceAll("%Seller", "" + Sender);

			Tools.addItemStackLore(ownShow, (IsTrade ? null : "¡ìf©A¡ìl" + MarketConfig.translate("Sender") + ": " + Sender), "", tradeInfo, " ¡ìf¡ìl| ¡ìf¡ìo" + MarketConfig.translate("infoContainInside"),
					" ¡ìf¡ìl- ¡ì9" + Amount + " " + MarketConfig.translate("Part") + " " + Tools.toInfo(inItem));

			stickInfo(ownShow);
			Tools.addItemStackLore(ownShow, "¡ìe¡ìo(" + MarketConfig.translate("infoRightClickToConfirmDelete") + ")", "¡ìe¡ìo<" + MarketConfig.translate("infoGetWhenConfirm") + ">");
		}
		return ownShow;
	}

	@Override
	public boolean click(Player player, boolean isRightClick)
	{
		if (isRightClick)
		{
			int maxStack = inItem.getMaxStackSize();

			int total = inItem.getAmount() * Amount;

			int needEmpty = (total / maxStack) + (total % maxStack == 0 ? 0 : 1);

			int empty = 0;

			ItemStack[] inventory = player.getInventory().getContents();
			for (int i = 0; i < inventory.length; ++i)
			{
				if (inventory[i] == null)
				{
					++empty;
				}
			}

			if (empty >= needEmpty)
			{
				for (int i = 0; i < Amount; i++)
				{
					player.getInventory().addItem(inItem);
				}
				inBox.deleteMail(this);
				return false;
			} else
			{
				GuiHint.hint(player, "¡ìc" + MarketConfig.translate("errLackInventory"), new GuiMailBox(player));
				return false;
			}
		}
		return true;
	}

	@Override
	public void write(String path, FileConfiguration file)
	{
		file.set(path + ".Date", date.getTime());
		file.set(path + ".Sender", Sender);
		file.set(path + ".IsTrade", IsTrade);
		file.set(path + ".Cost", Cost);
		file.set(path + ".Amount", Amount);
		file.set(path + ".Item", inItem);
	}

	@Override
	public void read(String path, FileConfiguration file)
	{
		date = new Date(file.getLong(path + ".Date"));
		Sender = file.getString(path + ".Sender");
		IsTrade = file.getBoolean(path + "IsTrade");
		Cost = file.getDouble(path + ".Cost");
		Amount = file.getInt(path + ".Amount");
		inItem = file.getItemStack(path + ".Item");
	}

	@Override
	public String getMark()
	{
		return this.getClass().getSimpleName();
	}

	@Override
	public ItemStack getDisplay()
	{
		return loadDisplay();
	}

}
