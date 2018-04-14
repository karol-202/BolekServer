package pl.karol202.bolekserver.game.server;

import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.UserAdapterConnection;
import pl.karol202.bolekserver.server.UserAdapterConnectionAPI3;
import pl.karol202.bolekserver.server.UserAdapterConnectionAPI4;

import java.util.stream.Stream;

public class User
{
	private String name;
	private UserAdapter adapter;
	private boolean ready;
	private String address;
	
	public User(String name, Connection connection, int apiVersion)
	{
		this.name = name;
		this.adapter = createUserAdapter(connection, apiVersion);
		this.ready = false;
		this.address = connection.getAddress();
	}
	
	private UserAdapter createUserAdapter(Connection connection, int apiVersion)
	{
		switch(apiVersion)
		{
		case 1:
		case 2: return new UserAdapterConnection(connection);
		case 3: return new UserAdapterConnectionAPI3(connection);
		case 4: return new UserAdapterConnectionAPI4(connection);
		}
		return null;
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
	
	void sendMessage(User sender, String message, boolean newMessage)
	{
		adapter.sendMessage(sender, message, newMessage);
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
	
	public String getAddress()
	{
		return address;
	}
}