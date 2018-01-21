package pl.karol202.bolekserver.game.server;

public class Message
{
	private User sender;
	private String message;
	
	public Message(User sender, String message)
	{
		this.sender = sender;
		this.message = message;
	}
	
	User getSender()
	{
		return sender;
	}
	
	String getMessage()
	{
		return message;
	}
}
