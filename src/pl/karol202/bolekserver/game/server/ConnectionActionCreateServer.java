package pl.karol202.bolekserver.game.server;

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
		return manager.createNewGameServer();
	}
}