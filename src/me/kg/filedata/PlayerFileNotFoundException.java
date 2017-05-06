package me.kg.filedata;

import java.io.File;

public class PlayerFileNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public final File file;

	public PlayerFileNotFoundException(File f)
	{
		this.file = f;
	}

}
