package me.kg.fastuse;

public class JamResult
{
	public static final int successful = 0;
	public static final int exit = 1;
	public static final int extrude = 2;

	public final Object result;
	public final int resultType;

	public JamResult(Object result, int type)
	{
		this.result = result;
		this.resultType = type;
	}

	public boolean isSuccessful()
	{
		return resultType == successful;
	}

	public boolean isPlayerExited()
	{
		return resultType == exit;
	}

	public boolean isJamExtruded()
	{
		return resultType == extrude;
	}
}
