package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.server.Game;

public interface InputGamePacket extends InputPacket
{
	public void execute(Game game);
}