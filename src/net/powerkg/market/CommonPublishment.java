package net.powerkg.market;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.powerkg.market.file.MarketConfig;
import net.powerkg.market.gui.GuiCommonBuy;
import net.powerkg.market.gui.GuiCommonPublishment;
import net.powerkg.utils.Tools;

public class CommonPublishment extends IPublishType implements IPublish
{
	public static final CommonPublishment instance = new CommonPublishment();

	private static final ItemStack def = new ItemStack(Material.SIGN);
	static
	{
		Tools.setItemStack(def, "¡ìc" + MarketConfig.translate("infoCommonPublish"), null);
	}

	public CommonPublishment()
	{
		publish = this;
	}

	@Override
	public void tryToPublish(Player user)
	{
		GuiCommonPublishment.open(user);
	}

	@Override
	public ItemStack getDisplay()
	{
		return def;
	}

	@Override
	public void whenCargoClick(ICargo cargo, Player user, boolean isRightClick)
	{
		if (!isRightClick)
		{
			new GuiCommonBuy(cargo, user).show();
		}
	}

	@Override
	public String getMark()
	{
		return CommonPublishment.class.getSimpleName();
	}
}
