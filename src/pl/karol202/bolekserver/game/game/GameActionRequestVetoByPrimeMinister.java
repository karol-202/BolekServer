package pl.karol202.bolekserver.game.game;

public class GameActionRequestVetoByPrimeMinister implements GameAction<Boolean>
{
	private Player player;
	
	public GameActionRequestVetoByPrimeMinister(Player player)
	{
		this.player = player;
	}
	
	@Override
	public Boolean execute(Game game)
	{
		return game.requestVetoByPrimeMinister(player);
	}
}
