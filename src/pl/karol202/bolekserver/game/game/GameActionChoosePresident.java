package pl.karol202.bolekserver.game.game;

public class GameActionChoosePresident implements GameAction<Game.UserChoosingError>
{
	private Player sender;
	private String president;
	
	public GameActionChoosePresident(Player sender, String president)
	{
		this.sender = sender;
		this.president = president;
	}
	
	@Override
	public Game.UserChoosingError execute(Game game)
	{
		return game.choosePresident(sender, president);
	}
}
