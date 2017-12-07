package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.server.GameServersManager;

public interface InputControlPacket extends InputPacket
{
	public void execute(GameServersManager manager);
}