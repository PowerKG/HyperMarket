package net.powerkg.market.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.powerkg.hyper.HyperMarket;
import net.powerkg.mailbox.GuiMailBox;
import net.powerkg.market.gui.GuiMarket;

public class CommandHyperMarket implements CommandExecutor
{
	private static CommandHyperMarket cmd = new CommandHyperMarket();

	public static void load()
	{
		Bukkit.getServer().getPluginCommand("HyperMarket").setExecutor(cmd);
		Bukkit.getServer().getPluginCommand("HM").setExecutor(cmd);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			HyperMarket.tellConsole("必须是玩家才能执行这个命令.");
			return false;
		}

		Player p = (Player) sender;

		if (args.length == 1 && args[0].equalsIgnoreCase("show"))
		{
			GuiMarket.open(p);
		}
		if (args[0].equalsIgnoreCase("test"))
		{
			new GuiMailBox(p).show();
		}
		return false;
	}

}
