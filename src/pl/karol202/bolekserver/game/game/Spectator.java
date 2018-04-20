package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.game.server.UserAdapter;

public class Spectator extends Participant
{
	public Spectator(User user, UserAdapter adapter)
	{
		super(user, Player.createPlayerAdapterConnection(adapter));
	}
	
	void init(Game game)
	{
		getAdapter().setGameAndSpectator(game, this);
	}
	
	void sendSpectatingStartMessage(boolean secretImages)
	{
		getAdapter().sendSpectatingStartMessage(secretImages);
	}
}