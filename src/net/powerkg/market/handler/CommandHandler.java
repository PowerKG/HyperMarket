package net.powerkg.market.handler;

import org.bukkit.plugin.Plugin;

import net.powerkg.hyper.HyperHandler;
import net.powerkg.market.command.CommandHyperMarket;

public class CommandHandler implements HyperHandler
{
	@Override
	public boolean load(Plugin plugin)
	{
		CommandHyperMarket.load();
		return true;
	}
}
