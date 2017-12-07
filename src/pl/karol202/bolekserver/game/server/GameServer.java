package pl.karol202.bolekserver.game.server;

import pl.karol202.bolekserver.game.ActionsQueue;

import java.util.ArrayList;
import java.util.List;

public class GameServer
{
	private String name;
	private int serverCode;
	private List<User> users;
	
	private ActionsQueue<ServerAction> actionsQueue;
	
	public GameServer(String name, int serverCode)
	{
		this.name = name;
		this.serverCode = serverCode;
		this.users = new ArrayList<>();
		
		this.actionsQueue = new ActionsQueue<>();
	}
	
	void addNewUser(String username)
	{
		User user = new User(username);
		users.add(user);
	}
	
	public void executeActions()
	{
		if(actionsQueue.isEmpty()) return;
		ServerAction action = actionsQueue.pollAction();
		Object result = action.execute(this);
		actionsQueue.setResult(action, result);
	}
	
	public <R> R addActionAndWaitForResult(ServerAction<R> action)
	{
		actionsQueue.addAction(action);
		
		Object result;
		do result = actionsQueue.getResult(action);
		while(result == null);
		
		return (R) result;
	}
	
	public void addActionAndReturnImmediately(ServerAction action)
	{
		actionsQueue.addAction(action);
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getServerCode()
	{
		return serverCode;
	}
}