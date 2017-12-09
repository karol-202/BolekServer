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
	
	void sendUsersList(Stream<User> users)
	{
		adapter.sendUsersList(users);
	}
	
	void sendUserReadiness(String username)
	{
		adapter.sendUserReadiness(username);
	}
	
	public String getName()
	{
		return name;
	}
	
	UserAdapter getAdapter()
	{
		return adapter;
	}
	
	boolean isReady()
	{
		return ready;
	}
	
	void setReady(boolean ready)
	{
		this.ready = ready;
	}
}