package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketPrimeMinisterAssigned implements OutputPacket
{
	private String primeMinister;
	
	public OutputPacketPrimeMinisterAssigned(String primeMinister)
	{
		this.primeMinister = primeMinister;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("primeMinister", primeMinister);
	}
	
	@Override
	public String getName()
	{
		return "PRIMEMINISTERASSIGNED";
	}
}