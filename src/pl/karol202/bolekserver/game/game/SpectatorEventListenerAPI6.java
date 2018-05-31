package pl.karol202.bolekserver.game.game;

public class SpectatorEventListenerAPI6 extends SpectatorEventListenerAPI5
{
	SpectatorEventListenerAPI6(Spectator spectator, PlayerAdapter adapter)
	{
		super(spectator, adapter);
	}
	
	@Override
	public void onSpectatingSynchronized(Spectator spectator)
	{
		if(getSpectator() == spectator) getAdapter().sendSpectatingSynchronizedMessage();
	}
}