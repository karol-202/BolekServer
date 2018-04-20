package pl.karol202.bolekserver.game.game;

public class GameActionSpectate implements GameAction<Void>
{
	private Spectator spectator;
	
	public GameActionSpectate(Spectator spectator)
	{
		this.spectator = spectator;
	}
	
	@Override
	public Void execute(Game target)
	{
		target.spectate(spectator);
		return null;
	}
}