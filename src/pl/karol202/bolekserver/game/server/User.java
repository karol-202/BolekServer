package pl.karol202.bolekserver.game.server;

import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.UserAdapterConnection;

import java.util.stream.Stream;

public class User
{
	private String name;
	private UserAdapter adapter;
	private boolean ready;
	
	User(String name, Connection connection)
	{
		this.name = name;
		this.adapter = new UserAdapterConnection(connection);
		this.ready = false;
	}
	
	void sendLoggedInMessage(String serverName, int serverCode)
	{
		adapter.sendLoggedInMessage(serverName, serverCode);
	}
	
	void sendUsersListMessage(Stream<User> users)
	{
		adapter.sendUsersListMessage(users);
	}
	
	void sendServerStatus(boolean gameAvailable)
	{
		adapter.sendServerStatusMessage(gameAvailable);
	}
	
	void sendMessage(User sender, String message)
	{
		adapter.sendMessage(sender, message);
	}
	
	public String getName()
	{
		return name;
	}
	
	UserAdapter getAdapter()
	{
		return adapter;
	}
	
	public boolean isReady()
	{
		return ready;
	}
	
	void setReady(boolean ready)
	{
		this.ready = ready;
	}
}