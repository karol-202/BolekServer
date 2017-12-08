package pl.karol202.bolekserver.game.server;

import java.util.stream.Stream;

public interface UserAdapter
{
	void sendUsersList(Stream<User> users);
}