package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.server.User;

import java.util.stream.Stream;

public abstract class Participant
{
	private User user;
	private PlayerAdapter adapter;
	
	Participant(User user, PlayerAdapter adapter)
	{
		this.user = user;
		this.adapter = adapter;
	}
	
	void sendGameStartMessage(Stream<Player> players, boolean secretImages)
	{
		adapter.sendGameStartMessage(players, secretImages);
	}
	
	void sendCollaboratorsRevealmentMessage(Stream<Player> ministers, Stream<Player> collaborators, Player bolek)
	{
		adapter.sendCollaboratorsRevealmentMessage(ministers, collaborators, bolek);
	}
	
	void sendStackRefillMessage(int totalActs)
	{
		adapter.sendStackRefillMessage(totalActs);
	}
	
	void sendPresidentAssignmentMessage(Player player)
	{
		adapter.sendPresidentAssignmentMessage(player);
	}
	
	void sendPrimeMinisterChosenMessage(Player player)
	{
		adapter.sendPrimeMinisterChosenMessage(player);
	}
	
	void sendVotingResultMessage(Stream<Player> upvoters, int totalVotes, boolean passed)
	{
		adapter.sendVotingResultMessage(upvoters, totalVotes, passed);
	}
	
	void sendPrimeMinisterAssignmentMessage(Player player)
	{
		adapter.sendPrimeMinisterAssignmentMessage(player);
	}
	
	void sendPollIndexChangeMessage(int pollIndex)
	{
		adapter.sendPollIndexChangeMessage(pollIndex);
	}
	
	void sendRandomActMessage()
	{
		adapter.sendRandomActMessage();
	}
	
	void sendPresidentChoosingActsMessage()
	{
		adapter.sendPresidentChoosingActsMessage();
	}
	
	void sendPrimeMinisterChoosingActsMessage()
	{
		adapter.sendPrimeMinisterChoosingActsMessage();
	}
	
	void sendVetoRequest()
	{
		adapter.sendVetoRequest();
	}
	
	void sendVetoResponseMessage(boolean accepted)
	{
		adapter.sendVetoResponseMessage(accepted);
	}
	
	void sendActPassedMessage(int lustrationPassed, int antilustrationPassed)
	{
		adapter.sendActPassedMessage(lustrationPassed, antilustrationPassed);
	}
	
	void sendWinMessage(boolean ministers, WinCause cause)
	{
		adapter.sendWinMessage(ministers, cause, null);//Null is overridden in Player
	}
	
	void sendPresidentCheckingPlayerMessage()
	{
		adapter.sendPresidentCheckingPlayerMessage();
	}
	
	void sendPlayerCheckingResultToPresident(int result)
	{
		adapter.sendPlayerCheckingResultToPresident(result);
	}
	
	void sendPresidentCheckedPlayerMessage(Player checkedPlayer)
	{
		adapter.sendPresidentCheckedPlayerMessage(checkedPlayer);
	}
	
	void sendPresidentCheckingPlayerOrActsMessage()
	{
		adapter.sendPresidentCheckingPlayerOrActsMessage();
	}
	
	void sendActsCheckingResultMessageToPresident(Act[] acts)
	{
		adapter.sendActsCheckingResultMessageToPresident(acts);
	}
	
	void sendPresidentCheckedActsMessage()
	{
		adapter.sendPresidentCheckedActsMessage();
	}
	
	void sendPresidentChoosingPresidentMessage()
	{
		adapter.sendPresidentChoosingPresidentMessage();
	}
	
	void sendPresidentLustratingMessage()
	{
		adapter.sendPresidentLustratingMessage();
	}
	
	void sendPresidentLustratedMessage(Player player, boolean bolek)
	{
		adapter.sendPresidentLustratedMessage(player, bolek);
	}
	
	void sendGameExitedMessage()
	{
		adapter.sendGameExitedMessage();
	}
	
	void sendPlayersUpdatedMessage(Stream<Player> players)
	{
		adapter.sendPlayersUpdatedMessage(players);
	}
	
	void sendTooFewPlayers()
	{
		adapter.sendTooFewPlayersMessage();
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