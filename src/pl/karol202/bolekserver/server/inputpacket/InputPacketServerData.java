package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.manager.ConnectionActionServerData;
import pl.karol202.bolekserver.game.manager.GameServersManager;
import pl.karol202.bolekserver.game.manager.ServersData;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketServerDataAPI4;

public class InputPacketServerData implements InputControlPacket
{
	@Override
	public void readData(DataBundle bundle)
	{ }
	
	@Override
	public void execute(Connection connection, GameServersManager manager)
	{
		ServersData data = manager.addActionAndWaitForResult(new ConnectionActionServerData());
		connection.applyPacket(new OutputPacketServerDataAPI4(data));
	}
}