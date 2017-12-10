package pl.karol202.bolekserver.game.game;

public class GameActionDismissActByPrimeMinister implements GameAction<Boolean>
{
	private Player sender;
	private Act act;
	
	public GameActionDismissActByPrimeMinister(Player sender, Act act)
	{
		this.sender = sender;
		this.act = act;
	}
	
	@Override
	public Boolean execute(Game game)
	{
		return game.dismissActByPrimeMinister(sender, act);
	}
}