package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketActPassed implements OutputPacket
{
	private int lustrationPassed;
	private int antilustrationPassed;
	
	public OutputPacketActPassed(int lustrationPassed, int antilustrationPassed)
	{
		this.lustrationPassed = lustrationPassed;
		this.antilustrationPassed = antilustrationPassed;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("lustrationPassed", lustrationPassed);
		bundle.putInt("antilustrationPassed", antilustrationPassed);
	}
	
	@Override
	public String getName()
	{
		return "ACTPASSED";
	}
}