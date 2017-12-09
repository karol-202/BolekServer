package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketPresidentAssigned implements OutputPacket
{
	private String player;
	
	public OutputPacketPresidentAssigned(String player)
	{
		this.player = player;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("player", player);
	}
	
	@Override
	public String getName()
	{
		return "PRESIDENTASSIGNED";
	}
}