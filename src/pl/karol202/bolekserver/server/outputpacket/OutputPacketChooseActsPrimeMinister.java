package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketChooseActsPrimeMinister implements OutputPacket
{
	private String[] acts;
	
	public OutputPacketChooseActsPrimeMinister(String[] acts)
	{
		this.acts = acts;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		if(acts.length != 2) return;
		bundle.putString("act1", acts[0]);
		bundle.putString("act2", acts[1]);
	}
	
	@Override
	public String getName()
	{
		return "CHOOSEACTSPRIMEMINISTER";
	}
}