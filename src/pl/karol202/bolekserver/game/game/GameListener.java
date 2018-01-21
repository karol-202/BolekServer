package pl.karol202.bolekserver.game.game;

public interface GameListener
{
	void onGameEnd();
	
	void onPlayerLeaveGame(Player player);
}
