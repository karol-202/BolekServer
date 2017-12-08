package pl.karol202.bolekserver.game.server;

public class ServerActionRemoveUser implements ServerAction<Boolean>
{
	private User user;
	
	public ServerActionRemoveUser(User user)
	{
		this.user = user;
	}
	
	@Override
	public Boolean execute(GameServer server)
	{
		return server.removeUser(user);
	}
}