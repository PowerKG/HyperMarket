package net.powerkg.utils;

import org.bukkit.Warning;

@SuppressWarnings("all")
@Deprecated
public class debug
{
	public static void out(Object... objs)
	{
		String s = "";
		for (int i = 0; i < objs.length; i++)
		{
			s += objs[i];
			if (i != objs.length && i + 1 != objs.length)
				s += ":";
		}
		System.out.println(s);
	}

	public static void outAsLine(Object... objs)
	{
		int i = 1;
		System.out.println("¡ì - new line out");
		for (Object obj : objs)
		{
			System.out.println("(" + (i++) + ") - " + obj);
		}
	}

	public static void outTrack()
	{
		new Error("DebugStackTracker").printStackTrace();
	}

	public static void outTrack(Object... objs)
	{
		System.out.println("¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì-Debug-¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì¡ì");
		String s = "Out: ";
		for (int i = 0; i < objs.length; i++)
		{
			s += objs[i];
			if (i != objs.length && i + 1 != objs.length)
				s += ":";
		}
		System.out.println(s);
		new Error("DebugStackTracker").printStackTrace();
	}

	public static void outDeclaredMethodsOfClass(Class<?> clazz)
	{
		debug.outAsLine(clazz.getDeclaredMethods());
	}

	public static void outFieldOfClass(Class<?> clazz)
	{
		debug.outAsLine(clazz.getFields());
	}

	public static void outDeclaredFieldOfClass(Class<?> clazz)
	{
		debug.outAsLine(clazz.getDeclaredFields());
	}

	public static void outDeclaredConstructorOfClass(Class<?> clazz)
	{
		debug.outAsLine(clazz.getDeclaredConstructors());
	}
}
