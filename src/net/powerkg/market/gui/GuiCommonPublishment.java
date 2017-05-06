package net.powerkg.market.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.kg.easygui.EasyGui;
import me.kg.fastuse.FastUse;
import me.kg.fastuse.IResultHandler;
import me.kg.fastuse.JamResult;
import me.kg.fastuse.JamType;
import net.powerkg.market.CommonCargo;
import net.powerkg.market.MarketHandler;
import net.powerkg.market.file.MarketConfig;
import net.powerkg.market.handler.EconomyHandler;
import net.powerkg.utils.Tools;

public class GuiCommonPublishment extends EasyGui implements IResultHandler
{
	public static final int sCost = 0;
	public static final int sDes = 1;

	private static HashMap<UUID, GuiCommonPublishment> cacheMap = new HashMap<>();

	private static ItemStack chooseHint/*选择物品的提示*/
			, setCost/*设置价格*/
			, setDescription/*设置描述*/
			, publish/*发布*/;

	private int amount = 1;
	private double cost = 0.0;
	private String Description = null;

	private int setting = sCost;

	static
	{
		chooseHint = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
		Tools.setItemStack(chooseHint, "§e" + MarketConfig.translate("infoCommonChooseHint"), null);

		setCost = new ItemStack(Material.EMERALD);
		Tools.setItemStack(setCost, "§c§l" + MarketConfig.translate("Set Cost"), null);

		setDescription = new ItemStack(Material.SIGN);
		Tools.setItemStack(setDescription, "§c§l" + MarketConfig.translate("Set Description"), null);

		publish = new ItemStack(Material.NETHER_STAR);
		Tools.setItemStack(publish, "§c§l" + MarketConfig.translate("Confirm"), null);
	}

	private ItemStack ownSetCost, ownSetDescription, ownPublish, colorfulHint, reset;
	private int confirm = 0;

	private GuiCommonPublishment(Player p)
	{
		super(p);
		inv = Bukkit.createInventory(null, 36, "§9§l" + MarketConfig.translate("infoCommonChooseAndSet"));
		initDisplayItem();
		setupHint();
	}

	public static void open(Player p)
	{
		if (cacheMap.containsKey(p.getUniqueId()))
		{
			cacheMap.get(p.getUniqueId()).show();
		} else
			new GuiCommonPublishment(p).show();
	}

	private void initDisplayItem()
	{
		ownSetCost = setCost.clone();
		ownSetDescription = setDescription.clone();
		colorfulHint = chooseHint.clone();
		ownPublish = publish.clone();

		updata();
	}

	private boolean none()
	{
		return (Description == null && cost == 0 && (inv.getItem(13) == null || inv.getItem(13).getType().equals(Material.AIR)));
	}

	private void reset()
	{
		Description = null;
		cost = 0;
		inv.setItem(13, null);

		updata();
	}

	private void updata()
	{
		refreashInfoItem();
		refreashColorfulHint();
		refreashResetItem();
		refreashConfirm();
	}

	private void refreashInfoItem()
	{
		Tools.setItemStack(ownSetCost, null, Arrays.asList("§c" + MarketConfig.translate("NowCost") + ": §l" + cost, "", "§7§o(" + MarketConfig.translate("infoJump") + ")"));
		Tools.setItemStack(ownSetDescription, null, Arrays.asList("§6" + MarketConfig.translate("NowDescription") + ": " + MarketConfig.getSetting().DefaultDescriptionFont
				+ (Description == null ? MarketConfig.translate("NoDescription") : Description), "", "§7§o(" + MarketConfig.translate("infoJump") + ")"));

		inv.setItem(27, ownSetCost);
		inv.setItem(28, ownSetDescription);
	}

	private void refreashConfirm()
	{
		confirm = 0;

		if (isEmpty(13))
		{
			Tools.setItemStack(ownPublish, "§c" + MarketConfig.translate("CantPublish"), Arrays.asList("", "§9" + MarketConfig.translate("NoSelected")));
		} else
		{
			Tools.setItemStack(ownPublish, "§2" + MarketConfig.translate("ConfirmToPublish"),
					Arrays.asList("§6§l" + MarketConfig.translate("Total") + " §6§l" + amount + " " + MarketConfig.translate("Part") + " §6§l" + Tools.toInfo(inv.getItem(13)),
							"§6" + MarketConfig.translate("EachPriceIs") + ": §6§l" + cost));

			if (MarketConfig.getTaxSetting().EnablePublish)
			{
				Tools.addItemStackLore(ownPublish, "",
						"§6" + MarketConfig.translate("NeedToCost") + ": §9(" + amount + "*" + cost + "*" + MarketConfig.getTaxSetting().PublishTax + "%(" + MarketConfig.translate("Tax") + ")"
								+ ") -> §6§l" + needToCost(),
						"§6" + MarketConfig.translate("YourBalance") + ": §c§l" + (EconomyHandler.getBalance(getUser().getName())), (EconomyHandler.getBalance(getUser().getName()) >= needToCost()
								? "§2§o(" + MarketConfig.translate("infoClickChoose") + ")" : "§c§o(" + MarketConfig.translate("infoNotEnoughMoney") + ")"));
			} else
			{
				Tools.addItemStackLore(ownPublish, "", "§7§o(" + MarketConfig.translate("infoClickChoose") + ")");
			}
		}
		inv.setItem(35, ownPublish);
	}

	private double needToCost()
	{
		double ncost = amount * cost * (MarketConfig.getTaxSetting().EnablePublish ? MarketConfig.getTaxSetting().PublishTax : 1);
		BigDecimal b = new BigDecimal(ncost);
		return b.setScale(1, RoundingMode.UP).doubleValue();
	}

	private void refreashResetItem()
	{
		if (none())
		{
			reset = new ItemStack(Material.TORCH);
			Tools.setItemStack(reset, "§c" + MarketConfig.translate("Reset All"), null);
		} else
		{
			reset = new ItemStack(Material.REDSTONE_TORCH_ON);
			Tools.setItemStack(reset, "§c" + MarketConfig.translate("Reset All"), null);
		}
		inv.setItem(34, reset);
	}

	private boolean isEmpty(int slot)
	{
		ItemStack item = inv.getItem(slot);
		return item == null || item.getType().equals(Material.AIR);
	}

	private void setupHint()
	{
		for (int i = 0; i < 27; i++)
		{
			if (i == 4)
				continue;
			if (i == 22)
				continue;
			if (i >= 9 && i <= 17)
				continue;
			inv.setItem(i, chooseHint);
		}
	}

	private void refreashColorfulHint()
	{
		ItemStack item = inv.getItem(13);

		if (item != null && !item.getType().equals(Material.AIR))
		{
			colorfulHint.setDurability((short) 5);
		} else
		{
			colorfulHint.setDurability((short) 14);
		}

		inv.setItem(4, colorfulHint);
		inv.setItem(22, colorfulHint);
		for (int i = 0; i < 9; i++)
		{
			if (i + 9 == 13)
				continue;
			inv.setItem(i + 9, colorfulHint);
		}

	}

	@Override
	public void onVerifiedEvent(InventoryClickEvent event)
	{
		event.setCancelled(true);

		int slot = event.getRawSlot();
		ItemStack item = event.getCurrentItem();

		if (slot == 13 || slot >= 36)
		{
			if (slot == 13)
				inv.setItem(13, null);
			else
				inv.setItem(13, item.clone());
			updata();
		}
		if (slot == 27 || slot == 28)
		{
			int needset = -1;
			switch (slot)
			{
			case 27:
				needset = sCost;
				getUser().sendMessage("§c" + MarketConfig.translate("infoSetCost"));
				break;
			case 28:
				needset = sDes;
				getUser().sendMessage("§c" + MarketConfig.translate("infoSetDescription"));
				break;
			}
			setting = needset;
			close();
			FastUse.jamMsg(getUser(), this);
		}
		if (slot == 34)
		{
			if (!none())
			{
				reset();
			}
		}
		if (slot == 35)
		{
			if (confirm == 0)
			{
				if (!none())
				{
					if (EconomyHandler.getBalance(getUser().getName()) < needToCost())
						Tools.setItemStack(inv.getItem(35), "§c" + MarketConfig.translate("infoNotEnoughMoney"), null);
					else
					{
						Tools.setItemStack(inv.getItem(35), "§c" + MarketConfig.translate("Reconfirm"), null);
						confirm = 1;
					}
				}
			} else if (confirm == 1)
			{
				ItemStack cloneCargo = inv.getItem(13);

				if (getUser().getInventory().first(cloneCargo) == -1)
				{
					confirm = 0;
					Tools.setItemStack(inv.getItem(35), "§c" + MarketConfig.translate("背包中没有该物品"), null);
					return;
				}

				Inventory inv = getUser().getInventory();
				inv.setItem(inv.first(cloneCargo), null);

				CommonCargo cargo = new CommonCargo(getUser().getName(), cloneCargo, cost, Description);
				MarketHandler.tryPublishCargoWithCost(getUser(), cargo, needToCost());
				close();
				reset();

				getUser().sendMessage("§2§l" + MarketConfig.translate("infoPublishSuccessful"));
			}
		}
	}

	@Override
	public void onClose(InventoryCloseEvent event)
	{
		cacheMap.put(getUser().getUniqueId(), this);
	}

	@Override
	public void onOpen(InventoryOpenEvent event)
	{

	}

	@Override
	public void handld(JamType type, JamResult result)
	{
		UUID uid = getUser().getUniqueId();

		if (result.isSuccessful())
		{
			String msg = (String) result.result;

			if (msg.replaceAll(" ", "").equalsIgnoreCase("...") || msg.replaceAll(" ", "").equalsIgnoreCase("。。。"))
			{
				this.show();
			} else
			{
				if (setting == sDes)
				{
					if (MarketConfig.getSetting().AllowColorfulDescription)
					{
						msg = msg.replaceAll("&", "§");
					} else
					{
						msg = msg.replaceAll("§", "&");
					}

					Description = msg;
					updata();
					this.show();
				}
				if (setting == sCost)
				{
					String args[] = msg.split("\\.");

					if (args.length == 2)
					{
						if (!(args[0].matches("[0-9]+") && args[1].matches("[0-9]+")))
						{
							getUser().sendMessage("§c" + MarketConfig.translate("infoWroingVault"));
							FastUse.jamMsg(getUser(), this);
							return;
						}
						if (args[1].length() > 2 || args[1].length() == 0)
						{
							getUser().sendMessage("§c" + MarketConfig.translate("infoOverlengthDecimal"));
							FastUse.jamMsg(getUser(), this);
							return;
						}
						int num1 = Integer.parseInt(args[0]);
						int num2 = Integer.parseInt(args[1]);

						double d;
						if (args[1].length() == 1)
							d = num1 + ((double) num2) / 10;
						else
							d = num1 + ((double) num2) / 100;

						cost = d;
					} else if (args.length == 1)
					{
						if (!(args[0].matches("[0-9]+")))
						{
							getUser().sendMessage("§c" + MarketConfig.translate("infoWroingVault"));
							FastUse.jamMsg(getUser(), this);
							return;
						}
						cost = (double) Integer.parseInt(args[0]);
					} else
					{
						getUser().sendMessage("§c" + MarketConfig.translate("infoWroingVault"));
						FastUse.jamMsg(getUser(), this);
						return;
					}
					updata();
					this.show();
				}
			}
		} else
		{
			if (result.isPlayerExited())
				cacheMap.remove(uid);
		}
	}
}
