package pl.karol202.bolekserver.game.server;

import pl.karol202.bolekserver.game.ActionsQueue;
import pl.karol202.bolekserver.server.Connection;

import java.util.ArrayList;
import java.util.List;

public class GameServer
{
	private static final int MAX_USERS = 10;
	
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
	
	User addNewUser(String username, Connection connection)
	{
		if(users.size() >= MAX_USERS) return null;
		broadcastUsersUpdate();
		
		User user = new User(username, connection);
		users.add(user);
		return user;
	}
	
	boolean removeUser(User user)
	{
		if(!users.contains(user)) return false;
		broadcastUsersUpdate();
		
		users.remove(user);
		return true;
	}
	
	void sendUsersListToUser(User user)
	{
		user.getUserAdapter().sendUsersList(users.stream());
	}
	
	private void broadcastUsersUpdate()
	{
		users.forEach(u -> u.getUserAdapter().sendUsersList(users.stream()));
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
	
	public String getName()
	{
		return name;
	}
	
	public int getServerCode()
	{
		return serverCode;
	}
}