package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.game.manager.ServersData;
import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketServerDataAPI4 implements OutputPacket
{
	private int servers;
	private int activeServers;
	
	public OutputPacketServerDataAPI4(ServersData data)
	{
		this.servers = data.getServers();
		this.activeServers = data.getActiveServers();
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("servers", servers);
		bundle.putInt("activeServers", activeServers);
	}
	
	@Override
	public String getName()
	{
		return "SERVERDATA";
	}
}