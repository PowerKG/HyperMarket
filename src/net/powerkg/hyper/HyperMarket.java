package net.powerkg.hyper;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.kg.easygui.GuiHandler;
import me.kg.fastuse.FastUse;
import me.kg.filedata.DataHandler;
import net.powerkg.mailbox.MailBoxHandler;
import net.powerkg.market.MarketCargoSaver;
import net.powerkg.market.MarketHandler;
import net.powerkg.market.file.MarketConfig;
import net.powerkg.market.handler.CommandHandler;
import net.powerkg.market.handler.EconomyHandler;

public class HyperMarket extends JavaPlugin
{
	private static HyperMarket instance;

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
	public static final MarketConfig configHandler = new MarketConfig();
	//命令处理器
	public static final CommandHandler commandHandler = new CommandHandler();
	//玩家邮箱(存储货物)
	public static final MailBoxHandler mailboxHandler = new MailBoxHandler();

	@Override
	public void onEnable()
	{
		HyperMarket.instance = this;
		console = getServer().getConsoleSender();

		
		//初始化Easy-Use
		GuiHandler.init(this);
		FastUse.init(this);
		DataHandler.init(this);

		//初始化各个系统

		if (!configHandler.load(this))
		{
			tellConsole("§c配置文本加载失败.");
		}

		if (!economyHandler.load(this))
		{
			tellConsole("§c加载金融失败,插件停止加载,请确定是否安装Vault插件.");
			return;
		}

		mailboxHandler.load(this);

		commandHandler.load(this);

		MarketHandler.init();
		
		MarketCargoSaver.readCargos();
	}

	public static HyperMarket getInstance()
	{
		return instance;
	}

	public static void tellConsole(String msg)
	{
		console.sendMessage("§c[HyperMarket] §f" + msg);
	}
}
