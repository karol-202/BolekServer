package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class OutputPacketCheckPlayerPresident implements OutputPacket
{
	private boolean update;
	private List<String> checkablePlayers;
	
	public OutputPacketCheckPlayerPresident(boolean update)
	{
		this.update = update;
		this.checkablePlayers = new ArrayList<>();
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putBoolean("update", update);
		bundle.putInt("checkablePlayers", checkablePlayers.size());
		for(int i = 0; i < checkablePlayers.size(); i++) bundle.putString("checkablePlayer" + i, checkablePlayers.get(i));
	}
	
	@Override
	public String getName()
	{
		return "CHECKPLAYERPRESIDENT";
	}
	
	public void addCheckablePlayer(String player)
	{
		checkablePlayers.add(player);
	}
}
