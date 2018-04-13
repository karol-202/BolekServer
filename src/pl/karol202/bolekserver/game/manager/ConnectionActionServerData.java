package pl.karol202.bolekserver.game.manager;

public class ConnectionActionServerData implements ConnectionAction<ServersData>
{
	@Override
	public ServersData execute(GameServersManager target)
	{
		return target.getServersData();
	}
}