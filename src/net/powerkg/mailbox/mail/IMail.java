package net.powerkg.mailbox.mail;

import java.util.Date;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.powerkg.mailbox.MailBox;
import net.powerkg.market.file.MarketConfig;
import net.powerkg.utils.Tools;

public abstract class IMail
{
	protected Date date = null;
	protected MailBox inBox = null;
	protected String path = null;

	public IMail()
	{
	}

	public Date getDate()
	{
		return date;
	}

	protected void stickInfo(ItemStack item)
	{
		if (date != null)
		{
			Tools.addItemStackLore(item, "", "   ¡ì7¡ìo" + MarketConfig.getDateFormat().format(date) + " " + MarketConfig.translate("Received"));
		}
	}

	public void setInBoxPathName(String str)
	{
		this.path = str;
	}

	public String getPathName()
	{
		return path;
	}

	public void encase(MailBox box)
	{
		this.inBox = box;
	}

	public abstract ItemStack getDisplay();

	public abstract boolean click(Player player, boolean isRightClick);

	public abstract String getMark();

	public abstract void write(String path, FileConfiguration file);

	public abstract void read(String path, FileConfiguration file);

}
