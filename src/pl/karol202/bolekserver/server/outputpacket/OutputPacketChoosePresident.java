package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class OutputPacketChoosePresident implements OutputPacket
{
	private boolean update;
	private List<String> availablePlayers;
	
	public OutputPacketChoosePresident(boolean update)
	{
		this.update = update;
		availablePlayers = new ArrayList<>();
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putBoolean("update", update);
		bundle.putInt("availablePlayers", availablePlayers.size());
		for(int i = 0; i < availablePlayers.size(); i++) bundle.putString("availablePlayer" + i, availablePlayers.get(i));
	}
	
	@Override
	public String getName()
	{
		return "CHOOSEPRESIDENT";
	}
	
	public void addAvailablePlayer(String player)
	{
		availablePlayers.add(player);
	}
}
