package pl.karol202.bolekserver;

import pl.karol202.bolekserver.game.server.GameServersManager;
import pl.karol202.bolekserver.server.Server;

public class Main
{
	private GameServersManager gameServersManager;
	private Server server;
	
	private Main()
	{
		gameServersManager = new GameServersManager();
		server = new Server(gameServersManager);
	}
	
	public static void main(String[] args)
	{
		new Main();
	}
}