package pl.karol202.bolekserver.game.server;

import java.util.stream.Stream;

public interface UserAdapter
{
	void sendLoggedInMessage(String serverName, int serverCode);
	
	void sendUsersListMessage(Stream<User> users);
	
	void sendServerStatusMessage(boolean gameAvailable);
	
	void sendMessage(User sender, String message);
}