package pl.karol202.bolekserver.game.manager;

import pl.karol202.bolekserver.game.ActionsQueue;
import pl.karol202.bolekserver.game.ErrorReference;
import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameServersManager
{
	public enum ServerCreationError
	{
		INVALID_NAME, TOO_MANY_SERVERS
	}
	
	private static final int MAX_SERVER_NAME_LENGTH = 20;
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
	
	GameServer createNewGameServer(String name, ErrorReference<ServerCreationError> error)
	{
		if(name == null || name.isEmpty() || name.length() > MAX_SERVER_NAME_LENGTH)
		{
			error.setError(ServerCreationError.INVALID_NAME);
			return null;
		}
		if(servers.size() >= MAX_SERVERS)
		{
			error.setError(ServerCreationError.TOO_MANY_SERVERS);
			return null;
		}
		GameServer server = new GameServer(name, getUniqueServerCode());
		servers.add(server);
		
		Server.LOGGER.info("Created new server: " + name + ", " + server.getServerCode());
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
			code = Math.abs(random.nextInt() % 9000) + 1000;
			
			for(GameServer server : servers)
				if(code == server.getServerCode()) continue mainLoop;
			break;
		}
		return code;
	}
	
	private void removeRidiculousServers()
	{
		servers = servers.stream().filter(GameServer::shouldLongerExist).collect(Collectors.toList());
	}
	
	public void run()
	{
		while(!suspend) executeActions();
	}
	
	private void executeActions()
	{
		while(actionsQueue.hasUnprocessedActions())
		{
			ConnectionAction action = actionsQueue.peekActionIfUnprocessed();
			if(action == null) continue;
			Object result = action.execute(this);
			actionsQueue.setResult(action, result);
		}
		servers.forEach(GameServer::executeActions);
		removeRidiculousServers();
		Thread.yield();
	}
	
	public void suspend()
	{
		suspend = true;
	}
	
	@SuppressWarnings("unchecked")
	public <R> R addActionAndWaitForResult(ConnectionAction<R> action)
	{
		if(action == null) return null;
		actionsQueue.addAction(action, false);
		
		do Thread.yield();
		while(!actionsQueue.isResultSetForAction(action));
		
		Object result = actionsQueue.getResult(action);
		actionsQueue.removeAction(action);
		return (R) result;
	}
}