package pl.karol202.bolekserver.game.server;

public class ServerActionAddUser implements ServerAction<Void>
{
	private String username;
	
	public ServerActionAddUser(String username)
	{
		this.username = username;
	}
	
	@Override
	public Void execute(GameServer server)
	{
		server.addNewUser(username);
		return null;
	}
}