package pl.karol202.bolekserver.game.game;

import java.util.List;

public interface EventListener
{
	void onGameStart(List<Player> playerStream, boolean secretImages);
	
	void onRolesAssignment(List<Player> ministers, List<Player> collaborators, Player bolek, boolean bolekKnows);
	
	void onStackRefill(int totalActs);
	
	void onPresidentAssignment(Player president);
	
	void onPrimeMinisterAssignment(Player primeMinister);
	
	void onPresidentChoosingPrimeMinister(Player president, boolean update, List<Player> candidates);
	
	void onPrimeMinisterChosen(Player primeMinister);
	
	void onVotingResult(List<Player> upvoters, int totalVotes, boolean passed);
	
	void onPollIndexChange(int pollIndex);
	
	void onRandomActPass();
	
	void onPresidentChoosingActs(Player president, Act[] acts);
	
	void onPrimeMinisterChoosingActs(Player primeMinister, Act[] acts, boolean vetoApplicable);
	
	void onVetoRequest();
	
	void onVetoResponse(boolean accepted);
	
	void onActPassed(int passedLustrationActs, int passedAntilustrationActs);
	
	void onWin(boolean ministersWin, WinCause cause, List<Player> ministers, List<Player> collaborators, Player bolek);
	
	void onPresidentCheckingPlayer(Player president, boolean update, List<Player> checkablePlayers);
	
	void onPresidentCheckedPlayer(Player president, Player checkedPlayer, int result);
	
	void onPresidentCheckingPlayerOrActs(Player president);
	
	//Return value of this method indicates whether the game should wait for the president's response or not(true - wait, false - don't wait)
	boolean onPresidentCheckingActs(Player president);
	
	void onPresidentCheckedActs(Player president, Act[] acts);
	
	void onPresidentChoosingPresident(Player president, boolean update, List<Player> availablePlayers);
	
	void onPresidentLustratingPlayer(Player president, boolean update, List<Player> availablePlayers);
	
	void onPresidentLustratedPlayer(Player lustratedPlayer);
	
	void onGameExited(Player player, List<Player> allPlayers, List<Player> ministers, List<Player> collaborators, Player bolek, boolean bolekKnows);
	
	void onTooFewPlayers();
	
	void onSpectatingStart(Spectator spectator, List<Player> allPlayers, boolean secretImages, List<Player> ministers, List<Player> collaborators, Player bolek, boolean bolekKnows);
}