package net.powerkg.market;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class IPublishType
{
	protected IPublish publish = null;

	public IPublishType()
	{
	}

	public IPublishType(IPublish publish)
	{
		this.publish = publish;
	}

	/**
	 * 当玩家点击发布并选中该类型时
	 * **/
	public void whenTryPublish(Player user)
	{		
		if (publish != null)
			publish.tryToPublish(user);
	}

	/**
	 * 当玩家点击绑定该类型的物品时
	 * **/
	public abstract void whenCargoClick(ICargo cargo, Player user, boolean isRightClick);

	public abstract ItemStack getDisplay();
}
