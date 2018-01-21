package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public interface InputPacket
{
	void readData(DataBundle bundle);
	
	default boolean isSilent()
	{
		return false;
	}
}