package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketSpectatingSynchronized implements OutputPacket
{
	@Override
	public void saveData(DataBundle bundle) { }
	
	@Override
	public String getName()
	{
		return "SPECTATINGSYNCHRONIZED";
	}
}