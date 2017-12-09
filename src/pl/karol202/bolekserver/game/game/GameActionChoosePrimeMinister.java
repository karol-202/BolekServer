package pl.karol202.bolekserver.game.game;

public class GameActionChoosePrimeMinister implements GameAction<Boolean>
{
	private Player sender;
	private String player;
	
	public GameActionChoosePrimeMinister(Player sender, String player)
	{
		this.sender = sender;
		this.player = player;
	}
	
	@Override
	public Boolean execute(Game game)
	{
		return game.choosePrimeMinister(sender, player);
	}
}