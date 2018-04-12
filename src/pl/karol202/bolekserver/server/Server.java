package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.ServerProperties;
import pl.karol202.bolekserver.game.manager.GameServersManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server
{
	public static final Logger LOGGER = Logger.getLogger("bolek");
	
	private GameServersManager gameServersManager;
	private ServerSocket serverSocket;
	
	public Server(GameServersManager gameServersManager)
	{
		this.gameServersManager = gameServersManager;
		
		startServerSocket();
		tryToWaitForClients();
	}
	
	public static void configureLogger()
	{
		LOGGER.setLevel(Level.FINER);
		LOGGER.setUseParentHandlers(false);
		LOGGER.addHandler(new LoggerHandler());
	}
	
	private void startServerSocket()
	{
		LOGGER.info("Starting server");
		try
		{
			serverSocket = new ServerSocket(ServerProperties.SERVER_PORT);
		}
		catch(IOException e)
		{
			throw new ServerException("Cannot open server.", e);
		}
	}
	
	private void tryToWaitForClients()
	{
		try
		{
			waitForClients();
		}
		catch(IOException e)
		{
			throw new ServerException("Error while waiting for clients.", e);
		}
	}
	
	private void waitForClients() throws IOException
	{
		LOGGER.fine("Waiting for clients...");
		while(!serverSocket.isClosed())
		{
			Socket socket = serverSocket.accept();
			LOGGER.info("Connected to client");
			Connection connection = new Connection(gameServersManager);
			if(!connection.connect(socket)) continue;
			connection.run();
		}
	}
}