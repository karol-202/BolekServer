package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketPlayerCheckingResult implements OutputPacket
{
	private int result;
	
	public OutputPacketPlayerCheckingResult(int result)
	{
		this.result = result;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("result", result);
	}
	
	@Override
	public String getName()
	{
		return "PLAYERCHECKINGRESULT";
	}
}
