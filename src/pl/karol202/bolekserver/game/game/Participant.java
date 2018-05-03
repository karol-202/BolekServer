package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.game.server.UserAdapter;
import pl.karol202.bolekserver.server.PlayerAdapterConnection;

import java.util.List;

public abstract class Participant implements EventListener
{
	private User user;
	private PlayerAdapter adapter;
	private EventListener eventListener;
	
	Participant(User user, UserAdapter adapter)
	{
		this.user = user;
		this.adapter = PlayerAdapterConnection.createPlayerAdapterConnection(adapter);
		this.eventListener = getEventListener();
	}
	
	abstract EventListener getEventListener();
	
	@Override
	public void onGameStart(List<Player> playerStream, boolean secretImages)
	{
		eventListener.onGameStart(playerStream, secretImages);
	}
	
	@Override
	public void onRolesAssignment(List<Player> ministers, List<Player> collaborators, Player bolek, boolean bolekKnows)
	{
		eventListener.onRolesAssignment(ministers, collaborators, bolek, bolekKnows);
	}
	
	@Override
	public void onStackRefill(int totalActs)
	{
		eventListener.onStackRefill(totalActs);
	}
	
	@Override
	public void onPresidentAssignment(Player president)
	{
		eventListener.onPresidentAssignment(president);
	}
	
	@Override
	public void onPrimeMinisterAssignment(Player primeMinister)
	{
		eventListener.onPrimeMinisterAssignment(primeMinister);
	}
	
	@Override
	public void onPresidentChoosingPrimeMinister(Player president, boolean update, List<Player> candidates)
	{
		eventListener.onPresidentChoosingPrimeMinister(president, update, candidates);
	}
	
	@Override
	public void onPrimeMinisterChosen(Player primeMinister)
	{
		eventListener.onPrimeMinisterChosen(primeMinister);
	}
	
	@Override
	public void onVotingResult(List<Player> upvoters, int totalVotes, boolean passed)
	{
		eventListener.onVotingResult(upvoters, totalVotes, passed);
	}
	
	@Override
	public void onPollIndexChange(int pollIndex)
	{
		eventListener.onPollIndexChange(pollIndex);
	}
	
	@Override
	public void onRandomActPass()
	{
		eventListener.onRandomActPass();
	}
	
	@Override
	public void onPresidentChoosingActs(Player president, Act[] acts)
	{
		eventListener.onPresidentChoosingActs(president, acts);
	}
	
	@Override
	public void onPrimeMinisterChoosingActs(Player primeMinister, Act[] acts, boolean vetoApplicable)
	{
		eventListener.onPrimeMinisterChoosingActs(primeMinister, acts, vetoApplicable);
	}
	
	@Override
	public void onVetoRequest()
	{
		eventListener.onVetoRequest();
	}
	
	@Override
	public void onVetoResponse(boolean accepted)
	{
		eventListener.onVetoResponse(accepted);
	}
	
	@Override
	public void onActPassed(int passedLustrationActs, int passedAntilustrationActs)
	{
		eventListener.onActPassed(passedLustrationActs, passedAntilustrationActs);
	}
	
	@Override
	public void onWin(boolean ministersWin, WinCause cause, List<Player> ministers, List<Player> collaborators, Player bolek)
	{
		eventListener.onWin(ministersWin, cause, ministers, collaborators, bolek);
	}
	
	@Override
	public void onPresidentCheckingPlayer(Player president, boolean update, List<Player> checkablePlayers)
	{
		eventListener.onPresidentCheckingPlayer(president, update, checkablePlayers);
	}
	
	@Override
	public void onPresidentCheckedPlayer(Player president, Player checkedPlayer, int result)
	{
		eventListener.onPresidentCheckedPlayer(president, checkedPlayer, result);
	}
	
	@Override
	public void onPresidentCheckingPlayerOrActs(Player president)
	{
		eventListener.onPresidentCheckingPlayerOrActs(president);
	}
	
	@Override
	public boolean onPresidentCheckingActs(Player president)
	{
		return eventListener.onPresidentCheckingActs(president);
	}
	
	@Override
	public void onPresidentCheckedActs(Player president, Act[] acts)
	{
		eventListener.onPresidentCheckedActs(president, acts);
	}
	
	@Override
	public void onPresidentChoosingPresident(Player president, boolean update, List<Player> availablePlayers)
	{
		eventListener.onPresidentChoosingPresident(president, update, availablePlayers);
	}
	
	@Override
	public void onPresidentLustratingPlayer(Player president, boolean update, List<Player> availablePlayers)
	{
		eventListener.onPresidentLustratingPlayer(president, update, availablePlayers);
	}
	
	@Override
	public void onPresidentLustratedPlayer(Player lustratedPlayer)
	{
		eventListener.onPresidentLustratedPlayer(lustratedPlayer);
	}
	
	@Override
	public void onGameExited(Player player, List<Player> allPlayers, List<Player> ministers, List<Player> collaborators, Player bolek, boolean bolekKnows)
	{
		eventListener.onGameExited(player, allPlayers, ministers, collaborators, bolek, bolekKnows);
	}
	
	@Override
	public void onTooFewPlayers()
	{
		eventListener.onTooFewPlayers();
	}
	
	@Override
	public void onSpectatingStart(Spectator spectator, List<Player> allPlayers, boolean secretImages, List<Player> ministers,
	                              List<Player> collaborators, Player bolek, boolean bolekKnows)
	{
		eventListener.onSpectatingStart(spectator, allPlayers, secretImages, ministers, collaborators, bolek, bolekKnows);
	}
	
	void reset()
	{
		adapter.resetAll();
	}
	
	public User getUser()
	{
		return user;
	}
	
	public String getName()
	{
		return user.getName();
	}
	
	PlayerAdapter getAdapter()
	{
		return adapter;
	}
}