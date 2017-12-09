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
	
	public Player(User user, UserAdapter adapter)
	{
		this.user = user;
		if(adapter instanceof UserAdapterConnection)
			this.adapter = new PlayerAdapterConnection(((UserAdapterConnection) adapter).getConnection());
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
	
	public String getName()
	{
		return user.getName();
	}
	
	Role getRole()
	{
		return role;
	}
	
	void assignRole(Role role)
	{
		if(this.role == null) this.role = role;
	}
}