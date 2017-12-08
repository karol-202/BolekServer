package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.game.server.ServerActionGetUsers;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;

public class InputPacketGetUsers implements InputServerPacket
{
	@Override
	public void readData(DataBundle bundle) { }
	
	@Override
	public void execute(Connection connection, GameServer server)
	{
		server.addActionAndWaitForResult(new ServerActionGetUsers(connection.getUser()));
	}
}