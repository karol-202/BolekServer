package pl.karol202.bolekserver.game.game;

public class GameActionRespondOnVeto implements GameAction<Boolean>
{
	private Player sender;
	private boolean accept;
	
	public GameActionRespondOnVeto(Player sender, boolean accept)
	{
		this.sender = sender;
		this.accept = accept;
	}
	
	@Override
	public Boolean execute(Game game)
	{
		return game.respondOnVeto(sender, accept);
	}
}
