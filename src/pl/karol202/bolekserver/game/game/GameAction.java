package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.Action;

public interface GameAction<R> extends Action
{
	R execute(Game game);
}