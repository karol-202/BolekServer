package pl.karol202.bolekserver.game.server;

public class ServerActionSecretOption implements ServerAction<Void>
{
	private String secretOption;
	private int secretValue;
	
	public ServerActionSecretOption(String secretOption, int secretValue)
	{
		this.secretOption = secretOption;
		this.secretValue = secretValue;
	}
	
	@Override
	public Void execute(GameServer target)
	{
		target.setSecretOption(secretOption, secretValue);
		return null;
	}
}