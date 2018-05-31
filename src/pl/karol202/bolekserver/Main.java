package pl.karol202.bolekserver;

import pl.karol202.bolekserver.game.Looper;
import pl.karol202.bolekserver.game.manager.GameServersManager;
import pl.karol202.bolekserver.server.Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
	static final String FILE_PROPERTIES = "bolek.conf";
	static final String FILE_LOG = "bolek.log";
	
	public static final Logger LOGGER = Logger.getLogger("bolek");
	
	private Main()
	{
		Looper looper = new Looper();
		runLooper(looper);
		
		Server.startServer(new GameServersManager(looper));
	}
	
	private void runLooper(Looper looper)
	{
		Thread thread = new Thread(looper::run);
		thread.setUncaughtExceptionHandler((t, e) -> uncaughtException(e));
		thread.start();
	}
	
	public static void main(String[] args)
	{
		try
		{
			setup();
			new Main();
		}
		catch(Exception exception)
		{
			uncaughtException(exception);
		}
	}
	
	private static void setup() throws IOException
	{
		configureLogger();
		ServerProperties.tryToLoadProperties();
		setLoggerLevel();
	}
	
	private static void configureLogger() throws FileNotFoundException
	{
		LOGGER.setUseParentHandlers(false);
		LOGGER.addHandler(new LoggerConsoleHandler());
		LOGGER.addHandler(new LoggerFileHandler());
	}
	
	private static void setLoggerLevel()
	{
		LOGGER.setLevel(ServerProperties.LOGGING_LEVEL);
	}
	
	private static void uncaughtException(Throwable throwable)
	{
		LOGGER.log(Level.SEVERE, "Uncaught exception", throwable);
	}
}