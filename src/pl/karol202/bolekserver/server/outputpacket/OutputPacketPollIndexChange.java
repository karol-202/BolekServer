package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketPollIndexChange implements OutputPacket
{
	private int pollIndex;
	
	public OutputPacketPollIndexChange(int pollIndex)
	{
		this.pollIndex = pollIndex;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("pollIndex", pollIndex);
	}
	
	@Override
	public String getName()
	{
		return "POLLINDEXCHANGE";
	}
}
