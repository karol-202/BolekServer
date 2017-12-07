package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public interface OutputPacket
{
	void saveData(DataBundle bundle);
	
	String getName();
}