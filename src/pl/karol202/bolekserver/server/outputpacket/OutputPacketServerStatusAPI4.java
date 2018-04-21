package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketServerStatusAPI4 implements OutputPacket
{
	private boolean gameAvailable;
	private int minUsers;
	
	public OutputPacketServerStatusAPI4(boolean gameAvailable, int minUsers)
	{
		this.gameAvailable = gameAvailable;
		this.minUsers = minUsers;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putBoolean("gameAvailable", gameAvailable);
		bundle.putInt("minUsers", minUsers);
	}
	
	@Override
	public String getName()
	{
		return "SERVERSTATUS";
	}
}
