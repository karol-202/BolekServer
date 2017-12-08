package pl.karol202.bolekserver.game.server;

public class ServerActionGetUsers implements ServerAction<Void>
{
	private User user;
	
	public ServerActionGetUsers(User user)
	{
		this.user = user;
	}
	
	@Override
	public Void execute(GameServer server)
	{
		server.sendUsersListToUser(user);
		return null;
	}
}