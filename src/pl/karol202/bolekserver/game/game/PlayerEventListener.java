package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.server.PlayerAdapterConnection;

import java.util.List;

class PlayerEventListener implements EventListener
{
	private Player player;
	private PlayerAdapter adapter;
	
	PlayerEventListener(Player player, PlayerAdapter adapter)
	{
		this.player = player;
		this.adapter = adapter;
	}
	
	static PlayerEventListener createPlayerEventListener(Player player, PlayerAdapter playerAdapter)
	{
		if(!(playerAdapter instanceof PlayerAdapterConnection)) return null;
		PlayerAdapterConnection playerAdapterConnection = (PlayerAdapterConnection) playerAdapter;
		int api = playerAdapterConnection.getAPIVersion();
		switch(api)
		{
		case 1: return new PlayerEventListener(player, playerAdapter);
		case 2: return new PlayerEventListenerAPI2(player, playerAdapter);
		case 3: return new PlayerEventListenerAPI3(player, playerAdapter);
		case 4: return new PlayerEventListenerAPI4(player, playerAdapter);
		case 5: return new PlayerEventListenerAPI5(player, playerAdapter);
		}
		return null;
	}
	
	@Override
	public void onGameStart(List<Player> players, boolean secretImages)
	{
		adapter.sendGameStartMessage(players.stream(), secretImages);
		adapter.sendPlayersUpdatedMessage(players.stream());
	}
	
	@Override
	public void onRolesAssignment(List<Player> ministers, List<Player> collaborators, Player bolek, boolean bolekKnows)
	{
		getAdapter().sendRoleAssignmentMessage(player.getRole());
		if(player.isCollaborator() || (bolekKnows && player.isBolek()))
			adapter.sendCollaboratorsRevealmentMessage(ministers.stream(), collaborators.stream(), bolek);
	}
	
	@Override
	public void onStackRefill(int totalActs)
	{
		adapter.sendStackRefillMessage(totalActs);
	}
	
	@Override
	public void onPresidentAssignment(Player president)
	{
		adapter.sendPresidentAssignmentMessage(president);
	}
	
	@Override
	public void onPrimeMinisterAssignment(Player primeMinister)
	{
		adapter.sendPrimeMinisterAssignmentMessage(primeMinister);
	}
	
	@Override
	public void onPresidentChoosingPrimeMinister(Player president, boolean update, List<Player> candidates)
	{
		if(player == president) adapter.sendPrimeMinisterChooseRequest(update, candidates.stream());
	}
	
	@Override
	public void onPrimeMinisterChosen(Player primeMinister)
	{
		adapter.sendPrimeMinisterChosenMessage(primeMinister);
		adapter.sendPrimeMinisterVotingRequest();
	}
	
	@Override
	public void onVotingResult(List<Player> upvoters, int totalVotes, boolean passed)
	{
		adapter.sendVotingResultMessage(upvoters.stream(), totalVotes, passed);
	}
	
	@Override
	public void onPollIndexChange(int pollIndex)
	{
		adapter.sendPollIndexChangeMessage(pollIndex);
	}
	
	@Override
	public void onRandomActPass()
	{
		adapter.sendRandomActMessage();
	}
	
	@Override
	public void onPresidentChoosingActs(Player president, Act[] acts)
	{
		if(player == president) adapter.sendChooseActsRequestToPresident(acts);
		else adapter.sendPresidentChoosingActsMessage();
	}
	
	@Override
	public void onPrimeMinisterChoosingActs(Player primeMinister, Act[] acts, boolean vetoApplicable)
	{
		if(player == primeMinister)
		{
			if(vetoApplicable) adapter.sendChooseActsOrVetoRequestToPrimeMinister(acts);
			else adapter.sendChooseActsRequestToPrimeMinister(acts);
		}
		else adapter.sendPrimeMinisterChoosingActsMessage();
	}
	
	@Override
	public void onVetoRequest()
	{
		adapter.sendVetoRequestMessage();
	}
	
	@Override
	public void onVetoResponse(boolean accepted)
	{
		adapter.sendVetoResponseMessage(accepted);
	}
	
	@Override
	public void onActPassed(int passedLustrationActs, int passedAntilustrationActs)
	{
		adapter.sendActPassedMessage(passedLustrationActs, passedAntilustrationActs);
	}
	
	@Override
	public void onWin(boolean ministersWin, WinCause cause, List<Player> ministers, List<Player> collaborators, Player bolek)
	{
		if(ministersWin && player.isMinister() || !ministersWin && !player.isMinister())
			adapter.sendWinMessage(ministersWin, cause);
		else adapter.sendLossMessage(cause);
		adapter.sendGameExitedMessage();
	}
	
	@Override
	public void onPresidentCheckingPlayer(Player president, boolean update, List<Player> checkablePlayers)
	{
		if(player == president) adapter.sendPlayerCheckRequestToPresident(update, checkablePlayers.stream());
		else adapter.sendPresidentCheckingPlayerMessage();
	}
	
	@Override
	public void onPresidentCheckedPlayer(Player president, Player checkedPlayer, int result)
	{
		if(player == president) adapter.sendPlayerCheckingResultToPresident(result);
		else adapter.sendPresidentCheckedPlayerMessage(checkedPlayer);
	}
	
	@Override
	public void onPresidentCheckingPlayerOrActs(Player president)
	{
		if(player == president) adapter.sendPlayerOrActsCheckingChooseRequestToPresident();
		else adapter.sendPresidentCheckingPlayerOrActsMessage();
	}
	
	@Override
	public boolean onPresidentCheckingActs(Player president)
	{
		return false;//Don't wait for president's response
	}
	
	@Override
	public void onPresidentCheckedActs(Player president, Act[] acts)
	{
		if(player == president) adapter.sendActsCheckingResultMessageToPresident(acts);
		else adapter.sendPresidentCheckedActsMessage();
	}
	
	@Override
	public void onPresidentChoosingPresident(Player president, boolean update, List<Player> availablePlayers)
	{
		if(player == president) adapter.sendChoosePresidentRequestToPresident(update, availablePlayers.stream());
		else adapter.sendPresidentChoosingPresidentMessage();
	}
	
	@Override
	public void onPresidentLustratingPlayer(Player president, boolean update, List<Player> availablePlayers)
	{
		if(player == president) adapter.sendLustrationRequestToPresident(update, availablePlayers.stream());
		else adapter.sendPresidentLustratingMessage();
	}
	
	@Override
	public void onPresidentLustratedPlayer(Player lustratedPlayer)
	{
		if(player == lustratedPlayer) adapter.sendYouAreLustratedMessage();
		else adapter.sendPresidentLustratedMessage(lustratedPlayer, lustratedPlayer.isBolek());
	}
	
	@Override
	public void onGameExited(Player exitedPlayer, List<Player> allPlayers, List<Player> ministers, List<Player> collaborators, Player bolek, boolean bolekKnows)
	{
		if(player == exitedPlayer)
		{
			adapter.sendGameExitedMessage();
			getPlayer().reset();
		}
		else adapter.sendPlayersUpdatedMessage(allPlayers.stream());
	}
	
	@Override
	public void onTooFewPlayers()
	{
		adapter.sendTooFewPlayersMessage();
		getPlayer().reset();
	}
	
	@Override
	public void onSpectatingStart(Spectator spectator, List<Player> allPlayers, boolean secretImages, List<Player> ministers,
	                              List<Player> collaborators, Player bolek, boolean bolekKnows) { }
	
	Player getPlayer()
	{
		return player;
	}
	
	PlayerAdapter getAdapter()
	{
		return adapter;
	}
}