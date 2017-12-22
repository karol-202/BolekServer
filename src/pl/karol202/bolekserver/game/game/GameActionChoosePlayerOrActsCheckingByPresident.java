package pl.karol202.bolekserver.game.game;

public class GameActionChoosePlayerOrActsCheckingByPresident implements GameAction<Boolean>
{
	private Player sender;
	private int choice;
	
	public GameActionChoosePlayerOrActsCheckingByPresident(Player sender, int choice)
	{
		this.sender = sender;
		this.choice = choice;
	}
	
	@Override
	public Boolean execute(Game game)
	{
		return game.choosePlayerOrActsCheckingByPresident(sender, choice);
	}
}
