package net.powerkg.market;

import java.io.File;
import java.util.Date;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.powerkg.mailbox.MailBoxHandler;
import net.powerkg.mailbox.mail.MailItem;
import net.powerkg.market.file.MarketConfig;
import net.powerkg.utils.Tools;

public class CommonCargo extends ICargo
{

	private ItemStack display = null;

	public CommonCargo(String owner, ItemStack cargo, double cost)
	{
		super(CommonPublishment.instance, new Date(), owner, cargo, cost);

		setUpDisplay();
	}

	public CommonCargo(String owner, ItemStack cargo, double cost, String des)
	{
		super(CommonPublishment.instance, new Date(), owner, cargo, cost);
		this.description = des;

		setUpDisplay();
	}

	public CommonCargo()
	{
	}

	private void setUpDisplay()
	{
		if (display == null)
			display = base.clone();

		Tools.addItemStackLore(display, "", "¡ì9¡ìl©– ¡ì9" + MarketConfig.translate("Seller") + ": " + ownerName,
				"¡ìf¡ìl©– " + MarketConfig.getSetting().DefaultDescriptionFont + (description == null ? MarketConfig.translate("NoDescription") : description),
				"¡ìc¡ìl©– " + MarketConfig.translate("Price") + ": ¡ìl" + cost, "¡ì7¡ìo(" + MarketConfig.translate("infoClick") + "¡ì7¡ìo)");
	}

	@Override
	public ItemStack getDisplay()
	{
		return display;
	}

	@Override
	public boolean tryGetCargo(Player buyer, int amount)
	{
		MailBoxHandler.sendMail(buyer.getName(), new MailItem(ownerName, true, cost, amount, base, new Date()));
		return true;
	}

	@Override
	public void read(String path, File file)
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		this.publishTime = new Date(config.getLong(path + ".Date"));
		this.ownerName = config.getString(path + ".Owner");
		this.cost = config.getDouble(path + ".Cost");
		this.description = config.getString(path + "Description");
		this.base = config.getItemStack(path + ".Cargo");

		setUpDisplay();
	}

	@Override
	public void write(String path, FileConfiguration config)
	{
		config.set(path + ".Date", publishTime.getTime());
		config.set(path + ".Owner", ownerName);
		config.set(path + ".Cost", cost);
		config.set(path + ".Description", this.description);
		config.set(path + ".Cargo", base);
	}

	@Override
	public String getMark()
	{
		return CommonCargo.class.getSimpleName();
	}

}
