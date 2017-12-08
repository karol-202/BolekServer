package pl.karol202.bolekserver.game.server;

import pl.karol202.bolekserver.server.Connection;

public class ServerActionAddUser implements ServerAction<User>
{
	private String username;
	private Connection connection;
	
	public ServerActionAddUser(String username, Connection connection)
	{
		this.username = username;
		this.connection = connection;
	}
	
	@Override
	public User execute(GameServer server)
	{
		return server.addNewUser(username, connection);
	}
}