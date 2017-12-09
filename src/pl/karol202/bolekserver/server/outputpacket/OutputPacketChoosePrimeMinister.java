package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class OutputPacketChoosePrimeMinister implements OutputPacket
{
	private List<String> candidates;
	
	public OutputPacketChoosePrimeMinister()
	{
		candidates = new ArrayList<>();
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("candidates", candidates.size());
		for(int i = 0; i < candidates.size(); i++) bundle.putString("candidate" + i, candidates.get(i));
	}
	
	@Override
	public String getName()
	{
		return "CHOOSEPRIMEMINISTER";
	}
	
	public void addCandidate(String candidate)
	{
		candidates.add(candidate);
	}
}