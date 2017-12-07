package pl.karol202.bolekserver.game.server;

public interface ConnectionAction<R> extends Action
{
	R execute(GameServersManager manager);
}