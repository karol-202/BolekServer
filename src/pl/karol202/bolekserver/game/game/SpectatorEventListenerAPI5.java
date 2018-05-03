package pl.karol202.bolekserver.game.game;

class SpectatorEventListenerAPI5 extends SpectatorEventListenerAPI4
{
	SpectatorEventListenerAPI5(Spectator spectator, PlayerAdapter adapter)
	{
		super(spectator, adapter);
	}
	
	@Override
	public boolean onPresidentCheckingActs(Player president)
	{
		getAdapter().sendPresidentCheckingActsMessage();
		return true;//Doesn't concern spectators
	}
}