package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class OutputPacketGameStart implements OutputPacket
{
	private List<String> players;
	
	public OutputPacketGameStart()
	{
		players = new ArrayList<>();
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("players", players.size());
		for(int i = 0; i < players.size(); i++) bundle.putString("player" + i, players.get(i));
	}
	
	@Override
	public String getName()
	{
		return "GAMESTART";
	}
	
	public void addPlayer(String player)
	{
		players.add(player);
	}
}