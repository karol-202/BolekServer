package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class OutputPacketVotingResult implements OutputPacket
{
	private List<String> upvoters;
	private int totalVotes;
	private boolean passed;
	
	public OutputPacketVotingResult(int totalVotes, boolean passed)
	{
		this.upvoters = new ArrayList<>();
		this.totalVotes = totalVotes;
		this.passed = passed;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("upvotes", upvoters.size());
		bundle.putInt("totalVotes", totalVotes);
		bundle.putBoolean("passed", passed);
		for(int i = 0; i < upvoters.size(); i++) bundle.putString("upvoter" + i, upvoters.get(i));
	}
	
	@Override
	public String getName()
	{
		return "VOTINGRESULT";
	}
	
	public void addUpvoter(String upvoter)
	{
		upvoters.add(upvoter);
	}
}