package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.game.server.ServerActionUserReady;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;

public class InputPacketReady implements InputServerPacket
{
	@Override
	public void readData(DataBundle bundle) { }
	
	@Override
	public void execute(Connection connection, GameServer server)
	{
		if(connection.getUser() == null) return;
		server.addActionAndWaitForResult(new ServerActionUserReady(connection.getUser()));
	}
}