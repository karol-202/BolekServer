package pl.karol202.bolekserver.game.manager;

import pl.karol202.bolekserver.game.server.GameServer;

public class ConnectionActionCreateServer implements ConnectionAction<GameServer>
{
	private String name;
	
	public ConnectionActionCreateServer(String name)
	{
		this.name = name;
	}
	
	@Override
	public GameServer execute(GameServersManager manager)
	{
		return manager.createNewGameServer(name);
	}
}