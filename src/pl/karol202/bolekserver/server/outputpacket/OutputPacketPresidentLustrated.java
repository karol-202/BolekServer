package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketPresidentLustrated implements OutputPacket
{
	private String player;
	private boolean bolek;
	
	public OutputPacketPresidentLustrated(String player, boolean bolek)
	{
		this.player = player;
		this.bolek = bolek;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("player", player);
		bundle.putBoolean("bolek", bolek);
	}
	
	@Override
	public String getName()
	{
		return "PRESIDENTLUSTRATED";
	}
}
