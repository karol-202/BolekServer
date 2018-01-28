package pl.karol202.bolekserver.game.game;

public class GameActionCheckPlayerByPresident implements GameAction<Game.UserChoosingError>
{
	private Player sender;
	private String checkedPlayer;
	
	public GameActionCheckPlayerByPresident(Player sender, String checkedPlayer)
	{
		this.sender = sender;
		this.checkedPlayer = checkedPlayer;
	}
	
	@Override
	public Game.UserChoosingError execute(Game game)
	{
		return game.checkPlayerByPresident(sender, checkedPlayer);
	}
}
