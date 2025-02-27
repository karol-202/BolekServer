package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.PlayerAdapterConnectionAPI4;

import java.util.ArrayList;
import java.util.List;

public class OutputPacketGameStartAPI4 implements OutputPacket
{
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
		
		byte[] code = PlayerAdapterConnectionAPI4.SECRET_IMAGES_CODE;
		bundle.putInt("imagesCodeLength", secretImages ? code.length : 0);
		if(secretImages) for(int i = 0; i < code.length; i++)
			bundle.putInt("imagesCode" + i, code[i]);
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