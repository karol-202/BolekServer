package pl.karol202.bolekserver.game.server;

import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.UserAdapterConnection;

public class User
{
	private String name;
	private UserAdapter adapter;
	
	User(String name, Connection connection)
	{
		this.name = name;
		this.adapter = new UserAdapterConnection(connection);
	}
	
	public String getName()
	{
		return name;
	}
	
	UserAdapter getUserAdapter()
	{
		return adapter;
	}
}