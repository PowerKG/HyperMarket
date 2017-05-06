package net.powerkg.mailbox;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.kg.filedata.DataHandler;
import net.powerkg.hyper.HyperHandler;
import net.powerkg.mailbox.mail.IMail;
import net.powerkg.mailbox.mail.MailItem;
import net.powerkg.mailbox.mail.MailPost;

public class MailBoxHandler implements HyperHandler
{
	private static HashMap<String, MailBox> mailboxMap = new HashMap<>();
	private static HashMap<String, Class<? extends IMail>> loadMarkMap = new HashMap<>();

	@Override
	public boolean load(Plugin plugin)
	{
		registerMark(MailPost.class);
		registerMark(MailItem.class);

		return true;
	}

	public static IMail getMarkedMail(MailBox box, String mark)
	{
		if (!loadMarkMap.containsKey(mark))
		{
			return null;
		}
		IMail mail = null;
		try
		{
			mail = (IMail) loadMarkMap.get(mark).newInstance();
		} catch (InstantiationException e)
		{
		} catch (IllegalAccessException e)
		{
		} catch (IllegalArgumentException e)
		{
		} catch (SecurityException e)
		{
		}
		mail.encase(box);
		return mail;
	}

	private static void registerMark(Class<? extends IMail> mail)
	{
		loadMarkMap.put(mail.getSimpleName(), mail);
	}

	private static MailBox loadMailBox(String name)
	{
		if (mailboxMap.containsKey(name.toLowerCase()))
			return mailboxMap.get(name.toLowerCase());

		MailBox mailbox = new MailBox(name);
		if (DataHandler.exists(name))
		{
			mailbox.loadMailBox(DataHandler.getFile(name));
		}
		mailboxMap.put(name.toLowerCase(), mailbox);

		return mailbox;
	}

	public static MailBox getMailBox(Player player)
	{
		return loadMailBox(player.getName());
	}

	public static MailBox getMailBox(String name)
	{
		return loadMailBox(name);
	}

	public static void sendMail(String target, IMail mail)
	{
		MailBox box = getMailBox(target);
		mail.encase(box);
		box.sendMail(mail);
	}

	public static void sendMail(Player target, IMail mail)
	{
		MailBox box = getMailBox(target);
		mail.encase(box);
		box.sendMail(mail);
	}
}
