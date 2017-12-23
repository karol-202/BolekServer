package pl.karol202.bolekserver.game.game;

public class GameActionExitGame implements GameAction<Void>
{
	private Player player;
	
	public GameActionExitGame(Player player)
	{
		this.player = player;
	}
	
	@Override
	public Void execute(Game game)
	{
		game.exitGame(player);
		return null;
	}
}
