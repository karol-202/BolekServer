package pl.karol202.bolekserver.game.game;

public class GameActionLustrateByPresident implements GameAction<Game.UserChoosingError>
{
	private Player sender;
	private String player;
	
	public GameActionLustrateByPresident(Player sender, String player)
	{
		this.sender = sender;
		this.player = player;
	}
	
	@Override
	public Game.UserChoosingError execute(Game game)
	{
		return game.lustrateByPresident(sender, player);
	}
}
