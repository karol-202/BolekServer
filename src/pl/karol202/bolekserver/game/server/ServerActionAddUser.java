package pl.karol202.bolekserver.game.server;

import pl.karol202.bolekserver.game.ErrorReference;
import pl.karol202.bolekserver.server.Connection;

public class ServerActionAddUser implements ServerAction<User>
{
	private String username;
	private Connection connection;
	private ErrorReference<GameServer.UserAddingError> error;
	
	public ServerActionAddUser(String username, Connection connection, ErrorReference<GameServer.UserAddingError> error)
	{
		this.username = username;
		this.connection = connection;
		this.error = error;
	}
	
	@Override
	public User execute(GameServer server)
	{
		return server.addNewUser(username, connection, error);
	}
}