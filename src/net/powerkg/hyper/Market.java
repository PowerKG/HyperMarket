package net.powerkg.hyper;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.kg.easygui.GuiHandler;
import me.kg.fastuse.FastUse;
import net.powerkg.market.MarketHandler;
import net.powerkg.market.file.FileHandler;
import net.powerkg.market.handler.CommandHandler;
import net.powerkg.market.handler.EconomyHandler;

public class Market extends JavaPlugin
{
	private static Market instance;

	private static ConsoleCommandSender console;

	/**
	 * ！如果你是新人
	 * 
	 * 以下这些系统完全是因为强迫症并且追求扩展性
	 * 你完全可以用很简单的步骤代替！
	 * **/

	//金融系统
	public static final EconomyHandler economyHandler = new EconomyHandler();
	//设置处理系统
	public static final FileHandler configHandler = new FileHandler();
	//命令处理器
	public static final CommandHandler commandHandler = new CommandHandler();

	@Override
	public void onEnable()
	{
		Market.instance = this;
		console = getServer().getConsoleSender();

		//初始化Easy-Use
		GuiHandler.init(this);
		FastUse.init(this);

		//初始化各个系统
		if (!economyHandler.setupEconomy())
		{
			tellConsole("§c加载金融失败,插件停止加载,请确定是否安装Vault插件.");
			return;
		}

		if (!configHandler.initConfig(this))
		{
			tellConsole("§c配置文本加载失败.");
		}

		commandHandler.load();

		MarketHandler.init();
	}

	public static Market getInstance()
	{
		return instance;
	}

	public static void tellConsole(String msg)
	{
		console.sendMessage("§c[HyperMarket] §f" + msg);
	}
}
