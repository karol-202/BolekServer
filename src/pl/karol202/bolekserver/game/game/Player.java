package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.game.server.UserAdapter;
import pl.karol202.bolekserver.server.*;

import java.util.stream.Stream;

public class Player extends Participant
{
	private Role role;
	private boolean checked;
	
	Player(User user, UserAdapter adapter)
	{
		super(user, createPlayerAdapterConnection(adapter));
	}
	
	static PlayerAdapter createPlayerAdapterConnection(UserAdapter userAdapter)
	{
		if(!(userAdapter instanceof UserAdapterConnection)) return null;
		UserAdapterConnection userAdapterConnection = (UserAdapterConnection) userAdapter;
		Connection connection = userAdapterConnection.getConnection();
		int api = userAdapterConnection.getAPIVersion();
		switch(api)
		{
		case 1:
		case 2: return new PlayerAdapterConnection(connection);
		case 3: return new PlayerAdapterConnectionAPI3(connection);
		case 4: return new PlayerAdapterConnectionAPI4(connection);
		}
		return null;
	}
	
	void init(Game game)
	{
		getAdapter().setGameAndPlayer(game, this);
	}
	
	void sendRoleAssignmentMessage(Role role)
	{
		getAdapter().sendRoleAssignmentMessage(role);
	}
	
	void sendPrimeMinisterChooseRequest(boolean update, Stream<Player> candidates)
	{
		getAdapter().sendPrimeMinisterChooseRequest(update, candidates);
	}
	
	void sendPrimeMinisterVotingRequest()
	{
		getAdapter().sendPrimeMinisterVotingRequest();
	}
	
	void sendChooseActsRequestToPresident(Act[] acts)
	{
		getAdapter().sendChooseActsRequestToPresident(acts);
	}
	
	void sendChooseActsRequestToPrimeMinister(Act[] acts)
	{
		getAdapter().sendChooseActsRequestToPrimeMinister(acts);
	}
	
	void sendChooseActsOrVetoRequestToPrimeMinister(Act[] acts)
	{
		getAdapter().sendChooseActsOrVetoRequestToPrimeMinister(acts);
	}
	
	void sendWinMessage(boolean ministers, WinCause cause)
	{
		getAdapter().sendWinMessage(ministers, cause, role);
	}
	
	void sendPlayerCheckRequestToPresident(boolean update, Stream<Player> checkablePlayers)
	{
		getAdapter().sendPlayerCheckRequestToPresident(update, checkablePlayers);
	}
	
	void sendPlayerOrActsCheckingChooseRequestToPresident()
	{
		getAdapter().sendPlayerOrActsCheckingChooseRequestToPresident();
	}
	
	void sendChoosePresidentRequestToPresident(boolean update, Stream<Player> availablePlayers)
	{
		getAdapter().sendChoosePresidentRequestToPresident(update, availablePlayers);
	}
	
	void sendLustrationRequestToPresident(boolean update, Stream<Player> availablePlayers)
	{
		getAdapter().sendLustrationRequestToPresident(update, availablePlayers);
	}
	
	void sendYouAreLustratedMessage()
	{
		getAdapter().sendYouAreLustratedMessage();
	}
	
	void assignRole(Role role)
	{
		if(this.role == null) this.role = role;
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