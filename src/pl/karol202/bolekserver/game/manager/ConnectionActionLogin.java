package pl.karol202.bolekserver.game.manager;

import pl.karol202.bolekserver.game.server.GameServer;

public class ConnectionActionLogin implements ConnectionAction<GameServer>
{
	private int serverCode;
	private String username;
	
	public ConnectionActionLogin(int serverCode, String username)
	{
		this.serverCode = serverCode;
		this.username = username;
	}
	
	@Override
	public GameServer execute(GameServersManager manager)
	{
		return manager.loginToServer(serverCode, username);
	}
}