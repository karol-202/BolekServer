package pl.karol202.bolekserver.game.game;

import java.util.stream.Stream;

public interface PlayerAdapter
{
	void sendGameStartMessage(Stream<Player> players);
	
	void sendRoleAssignmentMessage(Role role);
	
	void sendCollaboratorsRevealmentMessages(Stream<Player> collaborators);
	
	void sendPresidentAssignmentMessage(Player player);
	
	void sendPrimeMinisterChooseRequest(Stream<Player> candidates);
	
	void sendPrimeMinisterChosenMessage(Player player);
	
	void sendPrimeMinisterVotingRequest();
}