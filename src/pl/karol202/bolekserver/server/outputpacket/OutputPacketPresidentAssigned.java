package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketPresidentAssigned implements OutputPacket
{
	private String president;
	
	public OutputPacketPresidentAssigned(String president)
	{
		this.president = president;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("president", president);
	}
	
	@Override
	public String getName()
	{
		return "PRESIDENTASSIGNED";
	}
}