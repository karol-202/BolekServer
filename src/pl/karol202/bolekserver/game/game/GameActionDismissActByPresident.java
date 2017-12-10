package pl.karol202.bolekserver.game.game;

public class GameActionDismissActByPresident implements GameAction<Boolean>
{
	private Player sender;
	private Act act;
	
	public GameActionDismissActByPresident(Player sender, Act act)
	{
		this.sender = sender;
		this.act = act;
	}
	
	@Override
	public Boolean execute(Game game)
	{
		return game.dismissActByPresident(sender, act);
	}
}