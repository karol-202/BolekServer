package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class OutputPacketPlayersUpdated implements OutputPacket
{
	private List<String> players;
	
	public OutputPacketPlayersUpdated()
	{
		this.players = new ArrayList<>();
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
		return "PLAYERSUPDATED";
	}
	
	public void addPlayer(String player)
	{
		players.add(player);
	}
}
