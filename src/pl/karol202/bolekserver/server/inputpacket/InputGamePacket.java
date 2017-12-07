package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.server.Connection;

public interface InputGamePacket extends InputPacket
{
	void execute(Connection connection, Game game);
}