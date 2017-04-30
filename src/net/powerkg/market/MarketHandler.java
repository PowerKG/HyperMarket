package net.powerkg.market;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.powerkg.market.file.FileHandler;
import net.powerkg.market.handler.EconomyHandler;

public class MarketHandler
{
	private static ArrayList<IPublishType> publishTypes = new ArrayList<>();

	private static ArrayList<ICargo> cargos = new ArrayList<>();

	public static void init()
	{
		//设置默认发布类型
		MarketHandler.addPublishType(CommonPublishment.instance);
	}

	/**
	 * 在市场中添加新货物
	 * **/
	private static void addCargo(ICargo cargo)
	{
		cargos.add(cargo);
	}

	private static void removeCargo(ICargo cargo)
	{
		cargos.remove(cargo);
	}

	/**
	 * 在市场中发布物品并扣取相应钱
	 * **/
	public static boolean tryPublishCargoWithCost(Player publisher, ICargo cargo, double cost)
	{
		if (EconomyHandler.hasMoney(publisher.getName(), cost))
		{
			addCargo(cargo);
			EconomyHandler.costMoney(publisher.getName(), cost);
			return true;
		} else
			return false;
	}

	public static boolean tryBuyCargo(Player buyer, ICargo cargo, double cost)
	{
		if (cargos.contains(cargo))
			if (EconomyHandler.hasMoney(buyer.getName(), cost))
			{
				EconomyHandler.costMoney(buyer.getName(), cost);
				playerEarnMoney(cargo, cost);
				removeCargo(cargo);
				return true;
			}
		return false;
	}

	private static boolean playerEarnMoney(ICargo cargo, double money)
	{
		Player p = (Player) Bukkit.getPlayer(cargo.ownerName);
		if (p != null)
			p.sendMessage("§6§l" + FileHandler.translate("EarnMoney") + ": " + money);
		EconomyHandler.giveMoney(cargo.ownerName, money);
		return false;
	}

	/**
	 * 添加新的发布类型
	 * **/
	public static void addPublishType(IPublishType type)
	{
		publishTypes.add(type);
	}

	public static ArrayList<ICargo> getCargos()
	{
		return cargos;
	}

	public static ArrayList<IPublishType> getPublishTypes()
	{
		return publishTypes;
	}
}
