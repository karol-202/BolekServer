package pl.karol202.bolekserver;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;

public class ServerProperties
{
	public static Level LOGGING_LEVEL = Level.INFO;
	public static int SERVER_PORT = 60606;
	public static int MAX_SERVERS = 10;
	public static int MAX_SERVER_NAME_LENGTH = 20;
	public static boolean DEBUG = false;
	public static int MAX_USERNAME_LENGTH = 20;
	
	static void tryToLoadProperties()
	{
		try
		{
			loadProperties();
		}
		catch(Exception e)
		{
			throw new RuntimeException("Cannot load properties. Try to delete " + Main.FILE_PROPERTIES, e);
		}
	}
	
	private static void loadProperties() throws IOException, NumberFormatException
	{
		File file = new File(Main.FILE_PROPERTIES);
		if(!file.exists())
		{
			saveDefaultProperties(file);
			return;
		}
		InputStream inputStream = new FileInputStream(file);
		
		Properties properties = new Properties();
		properties.load(inputStream);
		LOGGING_LEVEL = Level.parse(properties.getProperty("LOGGING_LEVEL", ""));
		SERVER_PORT = Integer.parseInt(properties.getProperty("SERVER_PORT", ""));
		MAX_SERVERS = Integer.parseInt(properties.getProperty("MAX_SERVERS", ""));
		MAX_SERVER_NAME_LENGTH = Integer.parseInt(properties.getProperty("MAX_SERVER_NAME_LENGTH", ""));
		DEBUG = Boolean.parseBoolean(properties.getProperty("DEBUG", ""));
		MAX_USERNAME_LENGTH = Integer.parseInt(properties.getProperty("MAX_USERNAME_LENGTH", ""));
		inputStream.close();
	}
	
	private static void saveDefaultProperties(File file) throws IOException
	{
		file.createNewFile();
		OutputStream outputStream = new FileOutputStream(file);
		
		Properties properties = new Properties();
		properties.setProperty("LOGGING_LEVEL", String.valueOf(LOGGING_LEVEL));
		properties.setProperty("SERVER_PORT", String.valueOf(SERVER_PORT));
		properties.setProperty("MAX_SERVERS", String.valueOf(MAX_SERVERS));
		properties.setProperty("MAX_SERVER_NAME_LENGTH", String.valueOf(MAX_SERVER_NAME_LENGTH));
		properties.setProperty("DEBUG", String.valueOf(DEBUG));
		properties.setProperty("MAX_USERNAME_LENGTH", String.valueOf(MAX_USERNAME_LENGTH));
		properties.store(outputStream, "Default properties");
	}
}