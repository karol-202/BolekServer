package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.game.server.UserAdapter;

public class Player extends Participant
{
	private Role role;
	private boolean checked;
	
	public Player(User user, UserAdapter adapter)
	{
		super(user, adapter);
	}
	
	@Override
	EventListener getEventListener()
	{
		return PlayerEventListener.createPlayerEventListener(this, getAdapter());
	}
	
	void init(Game game)
	{
		getAdapter().setGameAndPlayer(game, this);
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