package pl.karol202.bolekserver.game.server;

public class ServerActionSpectate implements ServerAction<Boolean>
{
	private User user;
	
	public ServerActionSpectate(User user)
	{
		this.user = user;
	}
	
	@Override
	public Boolean execute(GameServer target)
	{
		return target.spectate(user);
	}
}