package pl.karol202.bolekserver.game.game;

import java.util.stream.Stream;

public interface PlayerAdapter
{
	void setGameAndPlayer(Game game, Player player);
	
	void resetGameAndPlayer();
	
	void sendGameStartMessage(Stream<Player> players);
	
	void sendRoleAssignmentMessage(Role role);
	
	void sendCollaboratorsRevealmentMessages(Stream<Player> collaborators, Player bolek);
	
	void sendStackRefillMessage(int totalActs);
	
	void sendPresidentAssignmentMessage(Player president);
	
	void sendPrimeMinisterChooseRequest(Stream<Player> candidates);
	
	void sendPrimeMinisterChosenMessage(Player primeMinister);
	
	void sendPrimeMinisterVotingRequest();
	
	void sendVotingResultMessage(Stream<Player> upvoters, int totalVotes, boolean passed);
	
	void sendPrimeMinisterAssignmentMessage(Player primeMinister);
	
	void sendPollIndexChangeMessage(int pollIndex);
	
	void sendRandomActMessage();
	
	void sendChooseActsRequestToPresident(Act[] acts);
	
	void sendPresidentChoosingActsMessage();
	
	void sendChooseActsRequestToPrimeMinister(Act[] acts);
	
	void sendPrimeMinisterChoosingActsMessage();
	
	void sendActPassedMessage(int lustrationPassed, int antilustrationPassed);
	
	void sendWinMessage(WinCause cause);
	
	void sendLossMessage(WinCause cause);
	
	void sendPresidentCheckingPlayerMessage();
	
	void sendPlayerCheckRequestToPresident(Stream<Player> checkablePlayers);
	
	void sendPlayerCheckingResultToPresident(int result);
	
	void sendPresidentCheckedPlayerMessage(Player checkedPlayer);
	
	void sendPresidentCheckingPlayerOrActsMessage();
	
	void sendPlayerOrActsCheckingChooseRequestToPresident();
	
	void sendActsCheckingResultMessageToPresident(Act[] acts);
	
	void sendPresidentCheckedActsMessage();
	
	void sendPresidentChoosingPresidentMessage();
	
	void sendChoosePresidentRequestToPresident();
	
	void sendPresidentLustratingMessage();
	
	void sendLustrationRequestToPresident();
	
	void sendYouAreLustratedMessage();
	
	void sendPresidentLustratedMessage(Player player, boolean bolek);
}