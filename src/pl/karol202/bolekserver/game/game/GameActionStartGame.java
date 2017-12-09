package pl.karol202.bolekserver.game.game;

public class GameActionStartGame implements GameAction<Void>
{
	@Override
	public Void execute(Game game)
	{
		game.startGame();
		return null;
	}
}