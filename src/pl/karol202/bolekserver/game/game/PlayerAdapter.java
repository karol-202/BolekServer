package pl.karol202.bolekserver.game.game;

import java.util.stream.Stream;

public interface PlayerAdapter
{
	void setGameAndPlayer(Game game, Player player);
	
	void setGameAndSpectator(Game game, Spectator spectator);
	
	void resetAll();
	
	void sendGameStartMessage(Stream<Player> players, boolean secretImages);
	
	void sendRoleAssignmentMessage(Role role);
	
	void sendCollaboratorsRevealmentMessage(Stream<Player> ministers, Stream<Player> collaborators, Player bolek);
	
	void sendStackRefillMessage(int totalActs);
	
	void sendPresidentAssignmentMessage(Player president);
	
	void sendPrimeMinisterChooseRequest(boolean update, Stream<Player> candidates);
	
	void sendPrimeMinisterChosenMessage(Player primeMinister);
	
	void sendPrimeMinisterVotingRequest();
	
	void sendVotingResultMessage(Stream<Player> upvoters, int totalVotes, boolean passed);
	
	void sendPrimeMinisterAssignmentMessage(Player primeMinister);
	
	void sendPollIndexChangeMessage(int pollIndex);
	
	void sendRandomActMessage();
	
	void sendChooseActsRequestToPresident(Act[] acts);
	
	void sendPresidentChoosingActsMessage();
	
	void sendChooseActsRequestToPrimeMinister(Act[] acts);
	
	void sendChooseActsOrVetoRequestToPrimeMinister(Act[] acts);
	
	void sendPrimeMinisterChoosingActsMessage();
	
	void sendVetoRequest();
	
	void sendVetoResponseMessage(boolean response);
	
	void sendActPassedMessage(int lustrationPassed, int antilustrationPassed);
	
	void sendWinMessage(boolean ministers, WinCause cause, Role role);
	
	void sendPresidentCheckingPlayerMessage();
	
	void sendPlayerCheckRequestToPresident(boolean update, Stream<Player> checkablePlayers);
	
	void sendPlayerCheckingResultToPresident(int result);
	
	void sendPresidentCheckedPlayerMessage(Player checkedPlayer);
	
	void sendPresidentCheckingPlayerOrActsMessage();
	
	void sendPlayerOrActsCheckingChooseRequestToPresident();
	
	void sendActsCheckingResultMessageToPresident(Act[] acts);
	
	void sendPresidentCheckedActsMessage();
	
	void sendPresidentChoosingPresidentMessage();
	
	void sendChoosePresidentRequestToPresident(boolean update, Stream<Player> availablePlayers);
	
	void sendPresidentLustratingMessage();
	
	void sendLustrationRequestToPresident(boolean update, Stream<Player> availablePlayers);
	
	void sendYouAreLustratedMessage();
	
	void sendPresidentLustratedMessage(Player player, boolean bolek);
	
	void sendGameExitedMessage();
	
	void sendPlayersUpdatedMessage(Stream<Player> player);
	
	void sendTooFewPlayersMessage();
}