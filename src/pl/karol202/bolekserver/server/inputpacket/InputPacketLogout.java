package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.game.server.ServerActionRemoveUser;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketLoggedOut;

public class InputPacketLogout implements InputServerPacket
{
	@Override
	public void readData(DataBundle bundle)
	{ }
	
	@Override
	public void execute(Connection connection, GameServer server)
	{
		connection.tryToExitGame();
		boolean result = connection.getUser() != null &&
						 server.addActionAndWaitForResult(new ServerActionRemoveUser(connection.getUser()));
		if(result) connection.applyPacket(new OutputPacketLoggedOut());
		else
		{
			connection.applyPacket(new OutputPacketFailure());
			return;
		}
		
		connection.setGameServer(null);
		connection.setUser(null);
	}
}