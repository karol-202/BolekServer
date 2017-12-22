package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketActsCheckingResult implements OutputPacket
{
	private String[] acts;
	
	public OutputPacketActsCheckingResult(String[] acts)
	{
		this.acts = acts;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		if(acts.length != 3) return;
		bundle.putString("act1", acts[0]);
		bundle.putString("act2", acts[1]);
		bundle.putString("act3", acts[2]);
	}
	
	@Override
	public String getName()
	{
		return "ACTSCHECKINGRESULT";
	}
}
