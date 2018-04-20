package pl.karol202.bolekserver.game.game;

public class GameActionStopSpectating implements GameAction<Void>
{
	private Spectator spectator;
	
	public GameActionStopSpectating(Spectator spectator)
	{
		this.spectator = spectator;
	}
	
	@Override
	public Void execute(Game target)
	{
		target.stopSpectating(spectator);
		return null;
	}
}