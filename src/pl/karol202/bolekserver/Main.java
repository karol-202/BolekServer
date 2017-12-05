package pl.karol202.bolekserver;

import pl.karol202.bolekserver.game.GameManager;
import pl.karol202.bolekserver.server.Server;

public class Main
{
	private GameManager gameManager;
	private Server server;
	
	private Main()
	{
		gameManager = new GameManager();
		server = new Server(gameManager);
	}
	
	public static void main(String[] args)
	{
		new Main();
	}
}