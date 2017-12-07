package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.manager.GameServersManager;
import pl.karol202.bolekserver.server.Connection;

public interface InputControlPacket extends InputPacket
{
	void execute(Connection connection, GameServersManager manager);
}