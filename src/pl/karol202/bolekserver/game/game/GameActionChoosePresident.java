package pl.karol202.bolekserver.game.game;

public class GameActionChoosePresident implements GameAction<Boolean>
{
	private Player sender;
	private String president;
	
	public GameActionChoosePresident(Player sender, String president)
	{
		this.sender = sender;
		this.president = president;
	}
	
	@Override
	public Boolean execute(Game game)
	{
		return game.choosePresident(sender, president);
	}
}
