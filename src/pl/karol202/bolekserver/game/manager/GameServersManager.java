package pl.karol202.bolekserver.game.manager;

import pl.karol202.bolekserver.game.ActionsQueue;
import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.game.server.ServerActionAddUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameServersManager
{
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
		GameServer server = new GameServer(name, getUniqueServerCode());
		servers.add(server);
		return server;
	}
	
	GameServer loginToServer(int serverCode, String username)
	{
		GameServer server = servers.stream().filter(s -> s.getServerCode() == serverCode).findAny().orElse(null);
		if(server != null) server.addActionAndReturnImmediately(new ServerActionAddUser(username));
		return server;
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
		while(!suspend)
		{
			executeActions();
			servers.forEach(GameServer::executeActions);
		}
	}
	
	private void executeActions()
	{
		if(actionsQueue.isEmpty()) return;
		ConnectionAction action = actionsQueue.pollAction();
		Object result = action.execute(this);
		actionsQueue.setResult(action, result);
	}
	
	public void suspend()
	{
		suspend = true;
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