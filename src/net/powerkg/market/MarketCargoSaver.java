package net.powerkg.market;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class MarketCargoSaver
{

	private static HashMap<String, Class<? extends ICargo>> cargoTypeMap = new HashMap<>();

	private static final String pathPublishType = ".PublishType", pathCargoType = ".CargoType";

	private static File basePath;

	public static void load(File f)
	{
		basePath = f;

		
	}

	public static void readCargos()
	{
		File[] files = basePath.listFiles();

		ArrayList<ICargo> cargos = new ArrayList<>();

		for (File file : files)
		{
			System.out.println(file.getName());
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			for (String s : config.getConfigurationSection("").getKeys(false))
			{

				IPublishType type = MarketHandler.getPublishType(config.getString(s + pathPublishType));
				if (type == null)
					continue;

				Class<? extends ICargo> cargoType = findCargoType(config.getString(s + pathCargoType));
				if (cargoType == null)
					continue;


				ICargo cargo = null;
				try
				{
					cargo = (ICargo) cargoType.newInstance();
				} catch (InstantiationException e)
				{
				} catch (IllegalAccessException e)
				{
				}


				if (cargo == null)
					continue;
				
				cargo.read(s, file);
				cargos.add(cargo);
			}
		}
		
		
		MarketHandler.initCargos(cargos);
	}

	public static void saveCargo(Player owner, ICargo cargo)
	{
		//unfinished
		File file = getOwnMarket(owner.getName());
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		String path = cargo.getPublishTime().getTime() + "";

		config.set(path + pathPublishType, cargo.getPublishType().getMark());
		config.set(path + pathCargoType, cargo.getMark());
		cargo.write(path, config);
		try
		{
			config.save(file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void deleteCargo(String ownerName, ICargo cargo)
	{
		//unfinished
		File file = getOwnMarket(ownerName);
		if (!file.exists())
			return;
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set(cargo.getPublishTime().getTime() + "", null);

		try
		{
			config.save(file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static File getOwnMarket(String name)
	{
		File f = new File(basePath, name.toLowerCase() + ".yml");
		return f;
	}

	public static void registerCargoType(Class<? extends ICargo> clazz)
	{
		cargoTypeMap.put(clazz.getSimpleName(), clazz);
	}

	public static Class<? extends ICargo> findCargoType(String mark)
	{
		return cargoTypeMap.get(mark);
	}

}
