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
	private boolean canBePrimeMinister;
	
	public Player(User user, UserAdapter adapter)
	{
		this.user = user;
		if(adapter instanceof UserAdapterConnection)
			this.adapter = new PlayerAdapterConnection(((UserAdapterConnection) adapter).getConnection());
		this.adapter.setPlayer(this);
	}
	
	void assignRole(Role role)
	{
		if(this.role == null) this.role = role;
	}
	
	void endTerm()
	{
		canBePrimeMinister = false;
	}
	
	void nextTurn()
	{
		canBePrimeMinister = true;
	}
	
	void sendGameStartMessage(Stream<Player> players)
	{
		adapter.sendGameStartMessage(players);
	}
	
	void sendRoleAssignmentMessage(Role role)
	{
		adapter.sendRoleAssignmentMessage(role);
	}
	
	void sendCollaboratorsRevealmentMessage(Stream<Player> collaborators)
	{
		adapter.sendCollaboratorsRevealmentMessages(collaborators);
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
	
	public String getName()
	{
		return user.getName();
	}
	
	Role getRole()
	{
		return role;
	}
	
	boolean canBePrimeMinisterNow()
	{
		return canBePrimeMinister;
	}
}