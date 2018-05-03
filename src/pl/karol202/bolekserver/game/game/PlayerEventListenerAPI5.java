package pl.karol202.bolekserver.game.game;

class PlayerEventListenerAPI5 extends PlayerEventListenerAPI4
{
	PlayerEventListenerAPI5(Player player, PlayerAdapter adapter)
	{
		super(player, adapter);
	}
	
	@Override
	public boolean onPresidentCheckingActs(Player president)
	{
		if(getPlayer() == president) getAdapter().sendActsCheckingRequestToPresident();
		else getAdapter().sendPresidentCheckingActsMessage();
		return true;
	}
}