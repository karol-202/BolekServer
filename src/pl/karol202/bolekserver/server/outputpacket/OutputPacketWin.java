package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.game.game.WinCause;
import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketWin implements OutputPacket
{
	private WinCause cause;
	
	public OutputPacketWin(WinCause cause)
	{
		this.cause = cause;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("cause", cause.ordinal());
	}
	
	@Override
	public String getName()
	{
		return "WIN";
	}
}