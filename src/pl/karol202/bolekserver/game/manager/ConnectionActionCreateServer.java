package pl.karol202.bolekserver.game.manager;

import pl.karol202.bolekserver.game.ErrorReference;
import pl.karol202.bolekserver.game.server.GameServer;

public class ConnectionActionCreateServer implements ConnectionAction<GameServer>
{
	private String name;
	private ErrorReference<GameServersManager.ServerCreationError> error;
	
	public ConnectionActionCreateServer(String name, ErrorReference<GameServersManager.ServerCreationError> error)
	{
		this.name = name;
		this.error = error;
	}
	
	@Override
	public GameServer execute(GameServersManager manager)
	{
		return manager.createNewGameServer(name, error);
	}
}