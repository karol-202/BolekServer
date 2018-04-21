package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.server.outputpacket.OutputPacketServerStatusAPI4;

public class UserAdapterConnectionAPI4 extends UserAdapterConnectionAPI3
{
	public UserAdapterConnectionAPI4(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public int getAPIVersion()
	{
		return 4;
	}
	
	@Override
	public void sendServerStatusMessage(boolean gameAvailable, int minUsers)
	{
		connection.sendPacket(new OutputPacketServerStatusAPI4(gameAvailable, minUsers));
	}
}