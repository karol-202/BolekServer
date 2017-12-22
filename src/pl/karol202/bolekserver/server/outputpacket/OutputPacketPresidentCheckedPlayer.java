package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketPresidentCheckedPlayer implements OutputPacket
{
	private String checkedPlayer;
	
	public OutputPacketPresidentCheckedPlayer(String checkedPlayer)
	{
		this.checkedPlayer = checkedPlayer;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("checkedPlayer", checkedPlayer);
	}
	
	@Override
	public String getName()
	{
		return "PRESIDENTCHECKEDPLAYER";
	}
}
