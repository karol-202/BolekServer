package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.GameManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	private static final int PORT = 606;
	
	private GameManager gameManager;
	private ServerSocket serverSocket;
	
	public Server(GameManager gameManager)
	{
		this.gameManager = gameManager;
		
		startServerSocket();
		tryToWaitForClients();
	}
	
	private void startServerSocket()
	{
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
		while(!serverSocket.isClosed())
		{
			Socket socket = serverSocket.accept();
			Connection connection = new Connection(gameManager);
			if(connection.connect(socket)) continue;
			createConnectionThread(connection);
		}
	}
	
	private void createConnectionThread(Connection connection)
	{
		new Thread(connection::run).start();
	}
}