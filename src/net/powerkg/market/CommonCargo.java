package net.powerkg.market;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.powerkg.market.file.FileHandler;
import net.powerkg.utils.Tools;

public class CommonCargo extends ICargo
{

	private ItemStack display;

	public CommonCargo(String owner, ItemStack cargo, double cost)
	{
		super(CommonPublishment.instance, owner, cargo, cost);

		display = cargo.clone();

		setUpDisplay();
	}

	public CommonCargo(String owner, ItemStack cargo, double cost, String des)
	{
		super(CommonPublishment.instance, owner, cargo, cost);
		this.description = des;

		display = cargo.clone();

		setUpDisplay();
	}

	private void setUpDisplay()
	{
		Tools.addItemStackLore(display, "", "¡ì9¡ìl©– ¡ì9" + FileHandler.translate("Seller") + ": " + ownerName,
				"¡ìf¡ìl©– " + FileHandler.getSetting().DefaultDescriptionFont + (description == null ? FileHandler.translate("NoDescription") : description),
				"¡ìc¡ìl©– " + FileHandler.translate("Price") + ": ¡ìl" + cost, "¡ì7¡ìo(" + FileHandler.translate("infoClick") + "¡ì7¡ìo)");
	}

	@Override
	public ItemStack getDisplay()
	{
		return display;
	}

	@Override
	public boolean tryGetCargo(Player buyer, int amount)
	{
		int maxStack = base.getMaxStackSize();

		int total = base.getAmount() * amount;

		int needEmpty = (total / maxStack) + (total % maxStack == 0 ? 0 : 1);

		int empty = 0;

		ItemStack[] inventory = buyer.getInventory().getContents();
		for (int i = 0; i < inventory.length; ++i)
		{
			if (inventory[i] == null)
			{
				++empty;
			}
		}

		if (empty >= needEmpty)
		{
			for (int i = 0; i < amount; i++)
			{
				buyer.getInventory().addItem(base);
			}
			return true;
		} else
		{
			buyer.sendMessage("¡ìc" + FileHandler.translate("errLackInventory"));
			return false;
		}
	}

}
