package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.game.server.UserAdapter;

public class Spectator extends Participant
{
	public Spectator(User user, UserAdapter adapter)
	{
		super(user, adapter);
	}
	
	@Override
	EventListener getEventListener()
	{
		return SpectatorEventListener.createSpectatorEventListener(this, getAdapter());
	}
	
	void init(Game game)
	{
		getAdapter().setGameAndSpectator(game, this);
	}
}