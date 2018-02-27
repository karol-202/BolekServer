package pl.karol202.bolekserver;

import pl.karol202.bolekserver.game.Looper;
import pl.karol202.bolekserver.game.manager.GameServersManager;
import pl.karol202.bolekserver.server.Server;

public class Main
{
	private Looper looper;
	private GameServersManager gameServersManager;
	private Server server;
	
	private Main()
	{
		Server.configureLogger();
		
		looper = new Looper();
		runLooper();
		
		gameServersManager = new GameServersManager(looper);
		server = new Server(gameServersManager);
	}
	
	private void runLooper()
	{
		Thread thread = new Thread(() -> looper.run());
		thread.setUncaughtExceptionHandler((t, e) -> e.printStackTrace());
		thread.start();
	}
	
	public static void main(String[] args)
	{
		new Main();
	}
}