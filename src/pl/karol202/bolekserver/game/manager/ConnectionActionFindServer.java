package pl.karol202.bolekserver.game.manager;

import pl.karol202.bolekserver.game.server.GameServer;

public class ConnectionActionFindServer implements ConnectionAction<GameServer>
{
	private int serverCode;
	
	public ConnectionActionFindServer(int serverCode)
	{
		this.serverCode = serverCode;
	}
	
	@Override
	public GameServer execute(GameServersManager manager)
	{
		return manager.findServer(serverCode);
	}
}