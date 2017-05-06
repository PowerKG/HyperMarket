package me.kg.filedata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class DataHandler
{

	private static File DatasPath = null;
	private static HashMap<String, Object> customDef = new HashMap<>();

	public static void addDefData(String path, Object obj)
	{
		customDef.put(path, obj);
	}

	public static boolean init(Plugin plugin)
	{

		/* 初始化路径 */
		if (DatasPath == null)
			DatasPath = new File(plugin.getDataFolder() + "/userdata");

		/* 初始化文件夹 */
		if (!DatasPath.exists())
			DatasPath.mkdir();

		return true;
	}

	/**
	 * 检查玩家文件夹是否存在
	 * 
	 * @param fileName
	 *            文件(玩家名字)
	 **/
	public static boolean exists(String fileName)
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		if (f.exists())
			return true;
		return false;
	}

	/**
	 * 创建玩家默认文件夹
	 * 
	 * @param fileName
	 *            文件(玩家名字)
	 * 
	 * @return 返回该玩家默认文件夹的config
	 * 
	 * @exception 抛出写入文件错误
	 **/

	public static FileConfiguration createPlayerFile(String fileName) throws IOException
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		if (!f.exists())
			f.createNewFile();
		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(f);

		/* 写入初始数据 */
		for (String s : customDef.keySet())
		{
			fileConfig.set(s, customDef.get(s));
		}

		fileConfig.save(f);

		return fileConfig;
	}

	/**
	 * 保存玩家文件夹
	 * 
	 * @param saveFileConfigurtion
	 *            需要保存的文件夹
	 * @param fileName
	 *            玩家名字
	 **/
	public static void save(FileConfiguration saveFileConfigurtion, String fileName) throws IOException
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		saveFileConfigurtion.save(f);
	}

	/**
	 * 向文件的路径写入一个数据(同时检查如果文件不存在就创建)
	 * 
	 * @param fileName
	 *            文件路径名字(file.getName())
	 * @param path
	 *            路径
	 * @param contents
	 *            写入的内容
	 * @throws PlayerFileNotFoundException
	 *             找不到玩家的文件夹
	 * 
	 * @exception 创建文件出错
	 **/
	public static void writeTo(String fileName, String path, Object contents) throws IOException, PlayerFileNotFoundException
	{
		FileConfiguration c = get(fileName);
		c.set(path, contents);
		save(c, fileName);
	}

	/**
	 * 获取玩家文件内的项目
	 * 
	 * @param needToGet
	 *            需要获取的玩家名字
	 * @param path
	 *            获取的路径
	 * @throws PlayerFileNotFoundException
	 *             找不到玩家的文件夹
	 * 
	 **/
	public static Object getObject(String needToGet, String path) throws PlayerFileNotFoundException
	{

		FileConfiguration f = get(needToGet.toLowerCase());
		if (f != null)
			return f.get(path);

		return null;
	}

	public static File getFile(String fileName)
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		return f;
	}

	/**
	 * 获取玩家config
	 * 
	 * @param fileName
	 *            需要获取的玩家名字
	 * @throws PlayerFileNotFoundException
	 *             找不到玩家的文件夹
	 * 
	 **/
	public static FileConfiguration get(String fileName) throws PlayerFileNotFoundException
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		if (f.exists())
			return YamlConfiguration.loadConfiguration(f);
		throw new PlayerFileNotFoundException(f);
	}

	public static Object getProperty(String fileName, String Property)
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		if (f.exists())
			return YamlConfiguration.loadConfiguration(f).get("Property." + Property);
		return null;
	}

	public static boolean setProperty(String fileName, String Property, Object obj)
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		if (!f.exists())
		{
			try
			{
				createPlayerFile(fileName.toLowerCase());
			} catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		config.set("Property." + Property, obj);

		try
		{
			config.save(f);
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 获取所有存在的玩家config
	 **/
	public static File[] getExistsPlayerFile()
	{
		return DatasPath.listFiles();
	}

	/**
	 * 获取玩家文件路径
	 **/
	public static File getPathFile()
	{
		return DatasPath;
	}
}
