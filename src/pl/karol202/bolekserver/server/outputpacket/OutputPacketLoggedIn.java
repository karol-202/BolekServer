package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketLoggedIn implements OutputPacket
{
	private int serverCode;
	
	public OutputPacketLoggedIn(int serverCode)
	{
		this.serverCode = serverCode;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("serverCode", serverCode);
	}
	
	@Override
	public String getName()
	{
		return "LOGGEDIN";
	}
}