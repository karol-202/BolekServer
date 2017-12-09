package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketSetReady implements OutputPacket
{
	private String username;
	
	public OutputPacketSetReady(String username)
	{
		this.username = username;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("username", username);
	}
	
	@Override
	public String getName()
	{
		return "SETREADY";
	}
}