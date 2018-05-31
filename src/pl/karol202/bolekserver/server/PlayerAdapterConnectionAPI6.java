package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.server.outputpacket.OutputPacketSpectatingSynchronized;

public class PlayerAdapterConnectionAPI6 extends PlayerAdapterConnectionAPI5
{
	PlayerAdapterConnectionAPI6(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public void sendSpectatingSynchronizedMessage()
	{
		OutputPacketSpectatingSynchronized packet = new OutputPacketSpectatingSynchronized();
		connection.sendPacket(packet);
	}
}