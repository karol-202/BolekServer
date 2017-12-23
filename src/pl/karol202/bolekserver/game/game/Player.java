package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.game.server.UserAdapter;
import pl.karol202.bolekserver.server.PlayerAdapterConnection;
import pl.karol202.bolekserver.server.UserAdapterConnection;

import java.util.stream.Stream;

public class Player
{
	private User user;
	private PlayerAdapter adapter;
	private Role role;
	private boolean checked;
	
	public Player(User user, UserAdapter adapter)
	{
		this.user = user;
		if(adapter instanceof UserAdapterConnection)
			this.adapter = new PlayerAdapterConnection(((UserAdapterConnection) adapter).getConnection());
	}
	
	void init(Game game)
	{
		adapter.setGameAndPlayer(game, this);
	}
	
	void assignRole(Role role)
	{
		if(this.role == null) this.role = role;
	}
	
	void sendGameStartMessage(Stream<Player> players)
	{
		adapter.sendGameStartMessage(players);
	}
	
	void sendRoleAssignmentMessage(Role role)
	{
		adapter.sendRoleAssignmentMessage(role);
	}
	
	void sendCollaboratorsRevealmentMessage(Stream<Player> collaborators, Player bolek)
	{
		adapter.sendCollaboratorsRevealmentMessages(collaborators, bolek);
	}
	
	void sendStackRefillMessage(int totalActs)
	{
		adapter.sendStackRefillMessage(totalActs);
	}
	
	void sendPresidentAssignmentMessage(Player player)
	{
		adapter.sendPresidentAssignmentMessage(player);
	}
	
	void sendPrimeMinisterChooseRequest(Stream<Player> candidates)
	{
		adapter.sendPrimeMinisterChooseRequest(candidates);
	}
	
	void sendPrimeMinisterChosenMessage(Player player)
	{
		adapter.sendPrimeMinisterChosenMessage(player);
	}
	
	void sendPrimeMinisterVotingRequest()
	{
		adapter.sendPrimeMinisterVotingRequest();
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
	
	void sendChooseActsRequestToPresident(Act[] acts)
	{
		adapter.sendChooseActsRequestToPresident(acts);
	}
	
	void sendPresidentChoosingActsMessage()
	{
		adapter.sendPresidentChoosingActsMessage();
	}
	
	void sendChooseActsRequestToPrimeMinister(Act[] acts)
	{
		adapter.sendChooseActsRequestToPrimeMinister(acts);
	}
	
	void sendChooseActsOrVetoRequestToPrimeMinister(Act[] acts)
	{
		adapter.sendChooseActsOrVetoRequestToPrimeMinister(acts);
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
	
	void sendWinMessage(WinCause cause)
	{
		adapter.sendWinMessage(cause);
	}
	
	void sendLossMessage(WinCause cause)
	{
		adapter.sendLossMessage(cause);
	}
	
	void sendPresidentCheckingPlayerMessage()
	{
		adapter.sendPresidentCheckingPlayerMessage();
	}
	
	void sendPlayerCheckRequestToPresident(Stream<Player> checkablePlayers)
	{
		adapter.sendPlayerCheckRequestToPresident(checkablePlayers);
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
	
	void sendPlayerOrActsCheckingChooseRequestToPresident()
	{
		adapter.sendPlayerOrActsCheckingChooseRequestToPresident();
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
	
	void sendChoosePresidentRequestToPresident()
	{
		adapter.sendChoosePresidentRequestToPresident();
	}
	
	void sendPresidentLustratingMessage()
	{
		adapter.sendPresidentLustratingMessage();
	}
	
	void sendLustrationRequestToPresident()
	{
		adapter.sendLustrationRequestToPresident();
	}
	
	void sendYouAreLustratedMessage()
	{
		adapter.sendYouAreLustratedMessage();
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
	
	void reset()
	{
		adapter.resetGameAndPlayer();
	}
	
	public String getName()
	{
		return user.getName();
	}
	
	Role getRole()
	{
		return role;
	}
	
	boolean isMinister()
	{
		return role == Role.MINISTER;
	}
	
	boolean isCollaborator()
	{
		return role == Role.COLLABORATOR;
	}
	
	boolean isBolek()
	{
		return role == Role.BOLEK;
	}
	
	boolean wasChecked()
	{
		return checked;
	}
	
	void setChecked()
	{
		this.checked = true;
	}
}