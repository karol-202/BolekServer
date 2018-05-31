package pl.karol202.bolekserver.game.game;

import java.util.List;

class SpectatorEventListenerAPI4 extends SpectatorEventListener
{
	SpectatorEventListenerAPI4(Spectator spectator, PlayerAdapter adapter)
	{
		super(spectator, adapter);
	}
	
	@Override
	public void onGameStart(List<Player> players, boolean secretImages)
	{
		getAdapter().sendGameStartMessage(players.stream(), secretImages);
		getAdapter().sendPlayersUpdatedMessage(players.stream());
	}
	
	@Override
	public void onRolesAssignment(List<Player> ministers, List<Player> collaborators, Player bolek, boolean bolekKnows)
	{
		getAdapter().sendCollaboratorsRevealmentMessage(ministers.stream(), collaborators.stream(), bolek);
	}
	
	@Override
	public void onStackRefill(int totalActs)
	{
		getAdapter().sendStackRefillMessage(totalActs);
	}
	
	@Override
	public void onPresidentAssignment(Player president)
	{
		getAdapter().sendPresidentAssignmentMessage(president);
	}
	
	@Override
	public void onPrimeMinisterAssignment(Player primeMinister)
	{
		getAdapter().sendPrimeMinisterAssignmentMessage(primeMinister);
	}
	
	@Override
	public void onPresidentChoosingPrimeMinister(Player president, boolean update, List<Player> candidates) { }
	
	@Override
	public void onPrimeMinisterChosen(Player primeMinister)
	{
		getAdapter().sendPrimeMinisterChosenMessage(primeMinister);
	}
	
	@Override
	public void onVotingResult(List<Player> upvoters, int totalVotes, boolean passed)
	{
		getAdapter().sendVotingResultMessage(upvoters.stream(), totalVotes, passed);
	}
	
	@Override
	public void onPollIndexChange(int pollIndex)
	{
		getAdapter().sendPollIndexChangeMessage(pollIndex);
	}
	
	@Override
	public void onRandomActPass()
	{
		getAdapter().sendRandomActMessage();
	}
	
	@Override
	public void onPresidentChoosingActs(Player president, Act[] acts)
	{
		getAdapter().sendPresidentChoosingActsMessage();
	}
	
	@Override
	public void onPrimeMinisterChoosingActs(Player primeMinister, Act[] acts, boolean vetoApplicable)
	{
		getAdapter().sendPrimeMinisterChoosingActsMessage();
	}
	
	@Override
	public void onVetoRequest()
	{
		getAdapter().sendVetoRequestMessage();
	}
	
	@Override
	public void onVetoResponse(boolean accepted)
	{
		getAdapter().sendVetoResponseMessage(accepted);
	}
	
	@Override
	public void onActPassed(int passedLustrationActs, int passedAntilustrationActs)
	{
		getAdapter().sendActPassedMessage(passedLustrationActs, passedAntilustrationActs);
	}
	
	@Override
	public void onWin(boolean ministersWin, WinCause cause, List<Player> ministers, List<Player> collaborators, Player bolek)
	{
		getAdapter().sendWinMessage(ministersWin, cause);
		getAdapter().sendCollaboratorsRevealmentMessage(ministers.stream(), collaborators.stream(), bolek);
		getAdapter().sendGameExitedMessage();
	}
	
	@Override
	public void onPresidentCheckingPlayer(Player president, boolean update, List<Player> checkablePlayers)
	{
		if(!update) getAdapter().sendPresidentCheckingPlayerMessage();
	}
	
	@Override
	public void onPresidentCheckedPlayer(Player president, Player checkedPlayer, int result)
	{
		getAdapter().sendPresidentCheckedPlayerMessage(checkedPlayer);
	}
	
	@Override
	public void onPresidentCheckingPlayerOrActs(Player president)
	{
		getAdapter().sendPresidentCheckingPlayerOrActsMessage();
	}
	
	@Override
	public boolean onPresidentCheckingActs(Player president)
	{
		return false;//Doesn't concern spectators
	}
	
	@Override
	public void onPresidentCheckedActs(Player president, Act[] acts)
	{
		getAdapter().sendPresidentCheckedActsMessage();
	}
	
	@Override
	public void onPresidentChoosingPresident(Player president, boolean update, List<Player> availablePlayers)
	{
		if(!update) getAdapter().sendPresidentChoosingPresidentMessage();
	}
	
	@Override
	public void onPresidentLustratingPlayer(Player president, boolean update, List<Player> availablePlayers)
	{
		if(!update) getAdapter().sendPresidentLustratingMessage();
	}
	
	@Override
	public void onPresidentLustratedPlayer(Player lustratedPlayer)
	{
		getAdapter().sendPresidentLustratedMessage(lustratedPlayer, lustratedPlayer.isBolek());
	}
	
	@Override
	public void onGameExited(Player player, List<Player> allPlayers, List<Player> ministers, List<Player> collaborators, Player bolek, boolean bolekKnows)
	{
		getAdapter().sendPlayersUpdatedMessage(allPlayers.stream());
		getAdapter().sendCollaboratorsRevealmentMessage(ministers.stream(), collaborators.stream(), bolek);
	}
	
	@Override
	public void onTooFewPlayers()
	{
		getAdapter().sendTooFewPlayersMessage();
	}
	
	@Override
	public void onSpectatingStart(Spectator spectator, List<Player> allPlayers, boolean secretImages, List<Player> ministers,
	                              List<Player> collaborators, Player bolek, boolean bolekKnows)
	{
		if(getSpectator() == spectator) getAdapter().sendSpectatingStartMessage(secretImages);
		getAdapter().sendPlayersUpdatedMessage(allPlayers.stream());
		getAdapter().sendCollaboratorsRevealmentMessage(ministers.stream(), collaborators.stream(), bolek);
	}
	
	@Override
	public void onSpectatingSynchronized(Spectator spectator) { }
}