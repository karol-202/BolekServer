package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketStackRefilled implements OutputPacket
{
	private int totalActs;
	
	public OutputPacketStackRefilled(int totalActs)
	{
		this.totalActs = totalActs;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("totalActs", totalActs);
	}
	
	@Override
	public String getName()
	{
		return "STACKREFILLED";
	}
}
