package pl.karol202.bolekserver.game.manager;

import pl.karol202.bolekserver.game.ActionsQueue;
import pl.karol202.bolekserver.game.server.GameServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameServersManager
{
	private static final int MAX_SERVERS = 10;
	
	private List<GameServer> servers;
	
	private ActionsQueue<ConnectionAction> actionsQueue;
	private boolean suspend;
	
	public GameServersManager()
	{
		this.servers = new ArrayList<>();
		
		this.actionsQueue = new ActionsQueue<>();
		this.suspend = false;
	}
	
	GameServer createNewGameServer(String name)
	{
		if(name == null || servers.size() >= MAX_SERVERS) return null;
		GameServer server = new GameServer(name, getUniqueServerCode());
		servers.add(server);
		return server;
	}
	
	GameServer findServer(int serverCode)
	{
		return servers.stream().filter(s -> s.getServerCode() == serverCode).findAny().orElse(null);
	}
	
	private int getUniqueServerCode()
	{
		Random random = new Random();
		int code;
		
		mainLoop:
		while(true)
		{
			code = (random.nextInt() % 9000) + 1000;
			
			for(GameServer server : servers)
				if(code == server.getServerCode()) continue mainLoop;
			break;
		}
		return code;
	}
	
	public void run()
	{
		while(!suspend) executeActions();
	}
	
	private void executeActions()
	{
		while(!actionsQueue.isEmpty())
		{
			ConnectionAction action = actionsQueue.pollAction();
			Object result = action.execute(this);
			actionsQueue.setResult(action, result);
		}
		servers.forEach(GameServer::executeActions);
	}
	
	public void suspend()
	{
		suspend = true;
	}
	
	public <R> R addActionAndWaitForResult(ConnectionAction<R> action)
	{
		if(action == null) return null;
		actionsQueue.addAction(action);
		
		Object result;
		do result = actionsQueue.getResult(action);
		while(result == null);
		
		return (R) result;
	}
}