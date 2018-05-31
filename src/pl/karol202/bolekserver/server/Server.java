package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.Main;
import pl.karol202.bolekserver.ServerProperties;
import pl.karol202.bolekserver.game.manager.GameServersManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	private static Server instance;
	
	public static void startServer(GameServersManager gameServersManager)
	{
		if(Server.instance != null) throw new IllegalStateException("Server is already started.");
		instance = new Server(gameServersManager);
	}
	
	private GameServersManager gameServersManager;
	private ServerSocket serverSocket;
	
	private Server(GameServersManager gameServersManager)
	{
		this.gameServersManager = gameServersManager;
		
		startServerSocket();
		tryToWaitForClients();
	}
	
	private void startServerSocket()
	{
		Main.LOGGER.info("Starting server");
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
		Main.LOGGER.fine("Waiting for clients...");
		while(!serverSocket.isClosed())
		{
			Socket socket = serverSocket.accept();
			Main.LOGGER.info("Connected to client");
			Connection connection = new Connection(gameServersManager);
			if(!connection.connect(socket)) continue;
			connection.run();
		}
	}
}