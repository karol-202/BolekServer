package pl.karol202.bolekserver.game.game;

import java.util.stream.Stream;

public interface PlayerAdapter
{
	void setPlayer(Player player);
	
	void sendGameStartMessage(Stream<Player> players);
	
	void sendRoleAssignmentMessage(Role role);
	
	void sendCollaboratorsRevealmentMessages(Stream<Player> collaborators);
	
	void sendPresidentAssignmentMessage(Player president);
	
	void sendPrimeMinisterChooseRequest(Stream<Player> candidates);
	
	void sendPrimeMinisterChosenMessage(Player primeMinister);
	
	void sendPrimeMinisterVotingRequest();
	
	void sendVotingResultMessage(Stream<Player> upvoters, int totalVotes, boolean passed);
	
	void sendPrimeMinisterAssignmentMessage(Player primeMinister);
}