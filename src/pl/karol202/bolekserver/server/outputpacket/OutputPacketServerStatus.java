package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketServerStatus implements OutputPacket
{
	private boolean gameAvailable;
	
	public OutputPacketServerStatus(boolean gameAvailable)
	{
		this.gameAvailable = gameAvailable;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putBoolean("gameAvailable", gameAvailable);
	}
	
	@Override
	public String getName()
	{
		return "SERVERSTATUS";
	}
}
