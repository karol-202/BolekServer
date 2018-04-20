package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionStopSpectating;
import pl.karol202.bolekserver.game.game.Spectator;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;

public class InputPacketStopSpectating implements InputGamePacket
{
	@Override
	public void readData(DataBundle bundle) { }
	
	@Override
	public void execute(Connection connection, Game game)
	{
		Spectator spectator = connection.getSpectator();
		game.addActionAndWaitForResult(new GameActionStopSpectating(spectator));
	}
}