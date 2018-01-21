package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class InputPacketPong implements InputPacket
{
	@Override
	public void readData(DataBundle bundle) { }
	
	@Override
	public boolean isSilent()
	{
		return true;
	}
}
