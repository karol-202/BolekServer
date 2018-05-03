package pl.karol202.bolekserver.game.game;

public class GameActionCheckActsByPresident implements GameAction<Boolean>
{
	private Player sender;
	
	public GameActionCheckActsByPresident(Player sender)
	{
		this.sender = sender;
	}
	
	@Override
	public Boolean execute(Game target)
	{
		return target.checkActsByPresident(sender);
	}
}