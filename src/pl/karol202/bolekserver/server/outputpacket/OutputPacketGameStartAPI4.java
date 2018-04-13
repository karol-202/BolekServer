package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class OutputPacketGameStartAPI4 implements OutputPacket
{
	private final int SECRET_IMAGES_CODE = 1;//TODO Set code
	
	private List<String> players;
	private boolean secretImages;
	
	public OutputPacketGameStartAPI4(boolean secretImages)
	{
		players = new ArrayList<>();
		this.secretImages = secretImages;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("players", players.size());
		for(int i = 0; i < players.size(); i++) bundle.putString("player" + i, players.get(i));
		bundle.putInt("secretImages", secretImages ? SECRET_IMAGES_CODE : 0);
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