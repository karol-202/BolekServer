package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public interface InputPacket
{
	public void readData(DataBundle bundle);
}