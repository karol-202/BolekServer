package pl.karol202.bolekserver.game.server;

import java.util.ArrayList;
import java.util.List;

public class GameServersManager
{
	private List<GameServer> servers;
	
	private ActionsQueue<ConnectionAction<?>> actionsQueue;
	
	public GameServersManager()
	{
		servers = new ArrayList<>();
		
		actionsQueue = new ActionsQueue<>();
	}
	
	GameServer createNewGameServer()
	{
		GameServer server = new GameServer();
		servers.add(server);
		return server;
	}
	
	public <R> R addActionAndWaitForResult(ConnectionAction<R> action)
	{
		actionsQueue.addAction(action);
		
		Object result;
		do result = actionsQueue.getResult(action);
		while(result == null);
		
		return (R) result;
	}
}