package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketChooseActsOrVetoPrimeMinister implements OutputPacket
{
	@Override
	public void saveData(DataBundle bundle) { }
	
	@Override
	public String getName()
	{
		return "CHOOSEACTSORVETOPRIMEMINISTER";
	}
}
