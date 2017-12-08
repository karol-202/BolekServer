package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.server.UserAdapter;

public class UserAdapterConnection implements UserAdapter
{
	private Connection connection;
	
	public UserAdapterConnection(Connection connection)
	{
		this.connection = connection;
	}
}