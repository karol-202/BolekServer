package pl.karol202.bolekserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerProperties
{
	private static final String PROPERTIES_FILE = "bolek.conf";
	
	public static int SERVER_PORT;
	public static int MAX_SERVERS;
	public static boolean DEBUG;
	
	static boolean tryToLoadProperties()
	{
		try
		{
			loadProperties();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static void loadProperties() throws IOException, NumberFormatException
	{
		InputStream inputStream = new FileInputStream(PROPERTIES_FILE);
		Properties properties = new Properties();
		properties.load(inputStream);
		SERVER_PORT = Integer.parseInt(properties.getProperty("SERVER_PORT"));
		MAX_SERVERS = Integer.parseInt(properties.getProperty("MAX_SERVERS"));
		DEBUG = Boolean.parseBoolean(properties.getProperty("DEBUG"));
	}
}