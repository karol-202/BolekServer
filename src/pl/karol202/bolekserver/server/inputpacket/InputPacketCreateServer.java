package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.server.ConnectionActionCreateServer;
import pl.karol202.bolekserver.game.server.GameServersManager;
import pl.karol202.bolekserver.server.DataBundle;

public class InputPacketCreateServer implements InputControlPacket
{
	private String name;
	
	@Override
	public void readData(DataBundle bundle)
	{
		name = bundle.getString("name", "New server");
	}
	
	@Override
	public void execute(GameServersManager manager)
	{
		manager.addActionAndWaitForResult(new ConnectionActionCreateServer(name));
	}
}