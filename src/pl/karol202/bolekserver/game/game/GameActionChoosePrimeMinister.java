package pl.karol202.bolekserver.game.game;

public class GameActionChoosePrimeMinister implements GameAction<Boolean>
{
	private String player;
	
	public GameActionChoosePrimeMinister(String player)
	{
		this.player = player;
	}
	
	@Override
	public Boolean execute(Game game)
	{
		return game.choosePrimeMinister(player);
	}
}