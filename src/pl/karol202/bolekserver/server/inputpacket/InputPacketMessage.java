package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.game.server.ServerActionMessage;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;

public class InputPacketMessage implements InputServerPacket
{
	private String message;
	
	@Override
	public void readData(DataBundle bundle)
	{
		message = bundle.getString("message", "");
	}
	
	@Override
	public void execute(Connection connection, GameServer server)
	{
		server.addActionAndWaitForResult(new ServerActionMessage(connection.getUser(), message));
	}
}
