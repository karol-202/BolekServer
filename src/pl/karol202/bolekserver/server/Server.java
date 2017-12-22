package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.manager.GameServersManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class Server
{
	static final Logger LOGGER = Logger.getLogger("bolek");
	
	private static final int PORT = 6006;
	
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
		Handler[] handlers = new Handler[LOGGER.getHandlers().length];
		System.arraycopy(LOGGER.getHandlers(), 0, handlers, 0, handlers.length);
		for(Handler handler : handlers) LOGGER.removeHandler(handler);
		LOGGER.addHandler(new LoggerHandler());
	}
	
	private void startServerSocket()
	{
		LOGGER.info("Starting server");
		try
		{
			serverSocket = new ServerSocket(PORT);
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
		LOGGER.info("Waiting for clients...");
		while(!serverSocket.isClosed())
		{
			Socket socket = serverSocket.accept();
			LOGGER.info("Connected to client");
			Connection connection = new Connection(gameServersManager);
			if(!connection.connect(socket)) continue;
			createConnectionThread(connection);
		}
	}
	
	private void createConnectionThread(Connection connection)
	{
		new Thread(connection::run).start();
	}
}