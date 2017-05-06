package net.powerkg.market.file;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigSetting
{
	public final String head;

	public final Boolean UsePageCache = false, AllowColorfulDescription = true;
	public final String DefaultDescriptionFont = null, DateFormat = null;

	ConfigSetting(Class<?> clazz, FileConfiguration config, String head)
	{
		this.head = head;
		OhUseTheFuckingDarkReflect(head, this, clazz, config);
	}

	ConfigSetting(FileConfiguration config)
	{
		this.head = "Setting.";
		OhUseTheFuckingDarkReflect(head, this, ConfigSetting.class, config);
	}

	private static void OhUseTheFuckingDarkReflect(String head, Object obj, Class<?> clazz, FileConfiguration config)
	{
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields)
		{

			if (!(Modifier.isFinal(f.getModifiers()) && !Modifier.isStatic(f.getModifiers())))
			{
				continue;
			}

			f.setAccessible(true);
			if (f.getType().equals(Boolean.class))
			{
				try
				{
					f.set(obj, config.getBoolean(head + f.getName()));
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (NullPointerException e)
				{

				}
			}
			if (f.getType().equals(Integer.class))
			{
				try
				{
					f.set(obj, config.getInt(head + f.getName()));
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (NullPointerException e)
				{

				}
			}
			if (f.getType().equals(String.class))
			{
				try
				{
					if (config.getString(head + f.getName()) != null)
						f.set(obj, config.getString(head + f.getName()).replaceAll("&", "¡ì"));
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (NullPointerException e)
				{
				}
			}
			if (f.getType().equals(Double.class))
			{
				try
				{
					f.set(obj, config.getDouble(head + f.getName()));
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (NullPointerException e)
				{

				}
			}
		}
	}
}
