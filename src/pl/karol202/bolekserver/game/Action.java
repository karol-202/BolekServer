package pl.karol202.bolekserver.game;

public interface Action<T extends Target, R>
{
	R execute(T target);
}