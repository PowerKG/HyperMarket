package net.powerkg.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.entity.Player;

import net.powerkg.mailbox.MailBoxHandler;
import net.powerkg.mailbox.mail.MailPost;
import net.powerkg.market.file.MarketConfig;
import net.powerkg.market.handler.EconomyHandler;
import net.powerkg.utils.Tools;

public class MarketHandler
{
	private static DateComparator dateComparator = new DateComparator();

	private static ArrayList<IPublishType> publishTypes = new ArrayList<>();
	private static HashMap<String, IPublishType> publishtypeMap = new HashMap<>();

	private static ArrayList<ICargo> publishedCargos = new ArrayList<>();

	public static void init()
	{
		//设置默认发布类型
		MarketHandler.addPublishType(CommonPublishment.instance);
		MarketCargoSaver.registerCargoType(CommonCargo.class);
	}

	static void initCargos(ArrayList<ICargo> cargos)
	{
		Collections.sort(cargos, dateComparator);
		publishedCargos.addAll(cargos);
	}

	/**
	 * 在市场中添加新货物
	 * **/
	private static void addCargo(ICargo cargo)
	{
		publishedCargos.add(cargo);
	}

	private static void removeCargo(ICargo cargo)
	{
		publishedCargos.remove(cargo);
	}

	/**
	 * 在市场中发布物品并扣取相应钱
	 * **/
	public static boolean tryPublishCargoWithCost(Player publisher, ICargo cargo, double cost)
	{
		if (EconomyHandler.hasMoney(publisher.getName(), cost))
		{
			EconomyHandler.costMoney(publisher.getName(), cost);
			addCargo(cargo);

			MarketCargoSaver.saveCargo(publisher, cargo);

			return true;
		} else
			return false;
	}

	public static boolean tryBuyCargo(Player buyer, ICargo cargo, double cost, int amount)
	{
		if (publishedCargos.contains(cargo))
			if (EconomyHandler.hasMoney(buyer.getName(), cost))
			{
				EconomyHandler.costMoney(buyer.getName(), cost);
				playerEarnMoney(cargo, buyer, cost, amount);
				removeCargo(cargo);
				MarketCargoSaver.deleteCargo(cargo.getOwner(), cargo);
				return true;
			}
		return false;
	}

	private static boolean playerEarnMoney(ICargo cargo, Player buyer, double cost, int amount)
	{
		MailBoxHandler.sendMail(cargo.ownerName,
				new MailPost(new Date(), "§9" + buyer.getName() + " §f" + MarketConfig.translate("Costed") + " §6" + cost + " §f" + MarketConfig.translate("BoughtYourCargo"),
						"§f" + Tools.toInfo(amount, cargo.base), MarketConfig.translate("Official")));

		EconomyHandler.giveMoney(cargo.ownerName, cost);
		return false;
	}

	/**
	 * 添加新的发布类型
	 * **/
	public static void addPublishType(IPublishType type)
	{
		publishtypeMap.put(type.getMark(), type);
		publishTypes.add(type);
	}

	public static ArrayList<ICargo> getCargos()
	{
		return publishedCargos;
	}

	public static ArrayList<IPublishType> getPublishTypes()
	{
		return publishTypes;
	}

	public static IPublishType getPublishType(String name)
	{
		return publishtypeMap.get(name);
	}

}

class DateComparator implements Comparator<ICargo>
{

	@Override
	public int compare(ICargo o1, ICargo o2)
	{
		return 0;
	}

}
