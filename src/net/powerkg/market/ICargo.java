package net.powerkg.market;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ICargo
{
	protected String ownerName;

	protected IPublishType publishType;

	protected ItemStack base;
	protected double cost;

	protected String description = null;

	public ICargo(IPublishType type, String owner, ItemStack base, double cost)
	{
		this.publishType = type;

		this.ownerName = owner;

		this.base = base;
		this.cost = cost;
	}

	public ICargo(String owner, ItemStack base, double cost)
	{
		this.publishType = null;

		this.ownerName = owner;

		this.base = base;
		this.cost = cost;
	}

	/**
	 * 当玩家点击该物品后
	 * **/
	public void cargoClick(Player user, boolean isRightClick)
	{
		if (publishType != null)
			publishType.whenCargoClick(this, user, isRightClick);
		else
			CommonPublishment.instance.whenCargoClick(this, user, isRightClick);
	}

	public ItemStack getBase()
	{
		return base;
	}

	public double getCost()
	{
		return cost;
	}

	/**
	 * 若返回值为
	 * 	True 执行完结(如完成结算等)步骤
	 *  False 取消执行
	 * **/
	public abstract boolean tryGetCargo(Player buyer, int amount);

	public abstract ItemStack getDisplay();

}
