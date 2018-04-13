package pl.karol202.bolekserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

public class ServerProperties
{
	private static final String PROPERTIES_FILE = "bolek.conf";
	
	public static int SERVER_PORT;
	public static int MAX_SERVERS;
	public static boolean DEBUG;
	public static Level LOGGING_LEVEL;
	
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
		SERVER_PORT = Integer.parseInt(properties.getProperty("SERVER_PORT", "60606"));
		MAX_SERVERS = Integer.parseInt(properties.getProperty("MAX_SERVERS", "10"));
		DEBUG = Boolean.parseBoolean(properties.getProperty("DEBUG", "false"));
		LOGGING_LEVEL = Level.parse(properties.getProperty("LOGGING_LEVEL", "INFO"));
	}
}