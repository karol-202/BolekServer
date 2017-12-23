package pl.karol202.bolekserver.game.server;

import java.util.stream.Stream;

public interface UserAdapter
{
	void sendUsersListMessage(Stream<User> users);
	
	void sendUserReadinessMessage(String username);
	
	void sendServerStatusMessage(boolean gameAvailable);
}