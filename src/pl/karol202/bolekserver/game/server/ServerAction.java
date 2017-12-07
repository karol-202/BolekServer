package pl.karol202.bolekserver.game.server;

import pl.karol202.bolekserver.game.Action;

public interface ServerAction<R> extends Action
{
	R execute(GameServer server);
}