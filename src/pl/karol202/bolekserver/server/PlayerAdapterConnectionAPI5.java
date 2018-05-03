package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.server.outputpacket.OutputPacketCheckActsPresident;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketPresidentCheckingActs;

public class PlayerAdapterConnectionAPI5 extends PlayerAdapterConnectionAPI4
{
	PlayerAdapterConnectionAPI5(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public int getAPIVersion()
	{
		return 5;
	}
	
	@Override
	public void sendPresidentCheckingActsMessage()
	{
		OutputPacketPresidentCheckingActs packet = new OutputPacketPresidentCheckingActs();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendActsCheckingRequestToPresident()
	{
		OutputPacketCheckActsPresident packet = new OutputPacketCheckActsPresident();
		connection.sendPacket(packet);
	}
}