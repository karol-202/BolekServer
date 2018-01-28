package pl.karol202.bolekserver.game.game;

public class GameActionChoosePrimeMinister implements GameAction<Game.UserChoosingError>
{
	private Player sender;
	private String player;
	
	public GameActionChoosePrimeMinister(Player sender, String player)
	{
		this.sender = sender;
		this.player = player;
	}
	
	@Override
	public Game.UserChoosingError execute(Game game)
	{
		return game.choosePrimeMinister(sender, player);
	}
}