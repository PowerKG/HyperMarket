package me.kg.easygui;

public abstract class ParmRunnable
{
	public Object[] objs;

	public ParmRunnable(Object... objs)
	{
		this.objs = objs;
	}

	public abstract void run();

}
