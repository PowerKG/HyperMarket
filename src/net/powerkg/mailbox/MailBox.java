package net.powerkg.mailbox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.kg.filedata.DataHandler;
import net.powerkg.mailbox.mail.IMail;

public class MailBox
{
	private static final String contentsPath = "Mail.Contents";

	protected File mailFile = null;

	public final String owner;

	MailBox(String owner)
	{
		this.owner = owner;
	}

	private ArrayList<IMail> mails = new ArrayList<>();

	public ArrayList<IMail> getMails()
	{
		return mails;
	}

	private void removeMail(IMail mail)
	{
		mails.remove(mail);
	}

	public void deleteMail(IMail mail)
	{
		if (mailFile == null)
			mailFile = DataHandler.getFile(owner);

		FileConfiguration file = YamlConfiguration.loadConfiguration(mailFile);

		String rawPath = contentsPath + "." + mail.getPathName();

		if (file.contains(rawPath))
			file.set(rawPath, null);

		//Unsaft
		try
		{
			file.save(mailFile);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		removeMail(mail);
	}

	void addMail(IMail mail)
	{
		int i = mails.size();
		mail.setInBoxPathName(toPath(i + 1));
		mails.add(i, mail);
	}

	void sendMail(IMail mail)
	{
		int i = mails.size();
		mail.setInBoxPathName(toPath(i + 1));
		mails.add(i, mail);

		if (mailFile == null)
			mailFile = DataHandler.getFile(owner);

		FileConfiguration config = YamlConfiguration.loadConfiguration(mailFile);

		mail.write(contentsPath + "." + mail.getPathName(), config);

		try
		{
			config.save(mailFile);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void save()
	{
		if (mailFile == null)
			mailFile = DataHandler.getFile(owner);
		saveMailBox(mailFile);
	}

	/**
	 * 从配置文本中加载MailBox所有数据
	 * **/
	public void loadMailBox(File file)
	{
		this.mailFile = file;

		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);

		mails.clear();
		if (fileConfig.contains(contentsPath))
			for (String s : fileConfig.getConfigurationSection(contentsPath).getKeys(false))
			{
				String path = contentsPath + "." + s;
				String mark = fileConfig.getString(path + ".Mark");

				IMail mail = MailBoxHandler.getMarkedMail(this, mark);
				if (mail == null)
					continue;
				mail.read(path, fileConfig);
				addMail(mail);
			}
	}

	/**
	 * 保存数据
	 * **/
	public void saveMailBox(File file)
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set(contentsPath, null);

		if (mails.isEmpty())
		{
			return;
		}
		for (int i = 1; i <= mails.size(); i++)
		{
			IMail mail = mails.get(i - 1);
			String path = contentsPath + "." + toPath(i);
			config.set(path + ".Mark", mail.getMark());
			mail.setInBoxPathName(toPath(i));
			mail.write(path, config);
		}
		try
		{
			config.save(file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static String toPath(int i)
	{
		return "Mail" + i;
	}
}
