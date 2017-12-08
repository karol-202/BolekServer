package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketLoggedIn implements OutputPacket
{
	private String serverName;
	private int serverCode;
	
	public OutputPacketLoggedIn(String serverName, int serverCode)
	{
		this.serverName = serverName;
		this.serverCode = serverCode;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("serverName", serverName);
		bundle.putInt("serverCode", serverCode);
	}
	
	@Override
	public String getName()
	{
		return "LOGGEDIN";
	}
}