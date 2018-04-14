package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class OutputPacketGameStartAPI4 implements OutputPacket
{
	private static final byte[] SECRET_IMAGES_CODE = { 48, -120, 18, 9, -58, 111, -107, 100, 17, -123, 81, -65, 86, 102, 31, -117 };
	
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
		bundle.putInt("imagesCodeLength", secretImages ? SECRET_IMAGES_CODE.length : 0);
		if(secretImages) for(int i = 0; i < SECRET_IMAGES_CODE.length; i++)
			bundle.putInt("imagesCode" + i, SECRET_IMAGES_CODE[i]);
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