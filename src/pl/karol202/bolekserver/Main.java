package pl.karol202.bolekserver;

import pl.karol202.bolekserver.game.manager.GameServersManager;
import pl.karol202.bolekserver.server.Server;

public class Main
{
	private GameServersManager gameServersManager;
	private Server server;
	
	private Main()
	{
		Server.configureLogger();
		
		gameServersManager = new GameServersManager();
		runGameManagerThread();
		
		server = new Server(gameServersManager);
	}
	
	private void runGameManagerThread()
	{
		new Thread(() -> gameServersManager.run()).start();
	}
	
	public static void main(String[] args)
	{
		new Main();
	}
}