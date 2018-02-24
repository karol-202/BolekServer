package pl.karol202.bolekserver.game.server;

import pl.karol202.bolekserver.game.ErrorReference;

public class ServerActionAddUser implements ServerAction<Boolean>
{
	private User user;
	private ErrorReference<GameServer.UserAddingError> error;
	
	public ServerActionAddUser(User user, ErrorReference<GameServer.UserAddingError> error)
	{
		this.user = user;
		this.error = error;
	}
	
	@Override
	public Boolean execute(GameServer server)
	{
		return server.addNewUser(user, error);
	}
}