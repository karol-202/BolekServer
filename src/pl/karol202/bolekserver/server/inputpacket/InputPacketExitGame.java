package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionExitGame;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;

public class InputPacketExitGame implements InputGamePacket
{
	@Override
	public void readData(DataBundle bundle)
	{ }
	
	@Override
	public void execute(Connection connection, Game game)
	{
		game.addActionAndReturnImmediately(new GameActionExitGame(connection.getPlayer()));
	}
}
