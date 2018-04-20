package pl.karol202.bolekserver.game.server;

public class ServerActionSpectate implements ServerAction<Void>
{
	private User user;
	
	public ServerActionSpectate(User user)
	{
		this.user = user;
	}
	
	@Override
	public Void execute(GameServer target)
	{
		target.spectate(user);
		return null;
	}
}