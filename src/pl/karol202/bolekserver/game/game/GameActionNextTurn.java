package pl.karol202.bolekserver.game.game;

//Must be package-private
class GameActionNextTurn implements GameAction<Void>
{
	@Override
	public Void execute(Game game)
	{
		game.nextTurn();
		return null;
	}
}