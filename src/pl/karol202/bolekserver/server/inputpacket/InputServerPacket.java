package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.server.GameServer;

public interface InputServerPacket extends InputPacket
{
	public void execute(GameServer server);
}