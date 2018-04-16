package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.game.server.UserAdapter;

public class Spectator extends Participant
{
	Spectator(User user, UserAdapter adapter)
	{
		super(user, Player.createPlayerAdapterConnection(adapter));
	}
}