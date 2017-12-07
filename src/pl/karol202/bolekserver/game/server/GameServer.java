package pl.karol202.bolekserver.game.server;

import java.util.ArrayList;
import java.util.List;

public class GameServer
{
	private List<User> users;
	
	GameServer()
	{
		users = new ArrayList<>();
	}
	
	public void addNewUser(String username)
	{
		User user = new User(username);
		users.add(user);
	}
}