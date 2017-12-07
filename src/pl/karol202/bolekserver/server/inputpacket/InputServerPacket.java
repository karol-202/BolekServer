package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.server.Connection;

public interface InputServerPacket extends InputPacket
{
	void execute(Connection connection, GameServer server);
}