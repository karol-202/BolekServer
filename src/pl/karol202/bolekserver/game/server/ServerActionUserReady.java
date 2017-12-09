package pl.karol202.bolekserver.game.server;

public class ServerActionUserReady implements ServerAction<Void>
{
	private User user;
	
	public ServerActionUserReady(User user)
	{
		this.user = user;
	}
	
	@Override
	public Void execute(GameServer server)
	{
		server.setUserReady(user);
		return null;
	}
}