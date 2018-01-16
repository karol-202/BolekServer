package pl.karol202.bolekserver.game.server;

public class ServerActionMessage implements ServerAction<Void>
{
	private User sender;
	private String message;
	
	public ServerActionMessage(User sender, String message)
	{
		this.sender = sender;
		this.message = message;
	}
	
	@Override
	public Void execute(GameServer server)
	{
		server.sendMessage(sender, message);
		return null;
	}
}
