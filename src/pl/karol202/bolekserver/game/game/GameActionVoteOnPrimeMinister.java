package pl.karol202.bolekserver.game.game;

public class GameActionVoteOnPrimeMinister implements GameAction<Boolean>
{
	private Player sender;
	private boolean vote;
	
	public GameActionVoteOnPrimeMinister(Player sender, boolean vote)
	{
		this.sender = sender;
		this.vote = vote;
	}
	
	@Override
	public Boolean execute(Game game)
	{
		return game.voteOnPrimeMinister(sender, vote);
	}
}