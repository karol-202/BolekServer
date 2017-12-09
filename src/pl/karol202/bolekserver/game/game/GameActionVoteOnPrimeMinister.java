package pl.karol202.bolekserver.game.game;

public class GameActionVoteOnPrimeMinister implements GameAction<Void>
{
	private Player sender;
	private boolean vote;
	
	public GameActionVoteOnPrimeMinister(Player sender, boolean vote)
	{
		this.sender = sender;
		this.vote = vote;
	}
	
	@Override
	public Void execute(Game game)
	{
		game.voteOnPrimeMinister(sender, vote);
		return null;
	}
}