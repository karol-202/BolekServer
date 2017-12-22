package pl.karol202.bolekserver.game.game;

public class GameActionLustrateByPresident implements GameAction<Boolean>
{
	private Player sender;
	private String player;
	
	public GameActionLustrateByPresident(Player sender, String player)
	{
		this.sender = sender;
		this.player = player;
	}
	
	@Override
	public Boolean execute(Game game)
	{
		return game.lustrateByPresident(sender, player);
	}
}
