package pl.karol202.bolekserver.game.server;

public interface ServerAction<R> extends Action
{
	R execute(GameServer server);
}