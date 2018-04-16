package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.game.game.WinCause;
import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketWinAPI4 implements OutputPacket
{
	private boolean ministers;
	private WinCause cause;
	
	public OutputPacketWinAPI4(boolean ministers, WinCause cause)
	{
		this.ministers = ministers;
		this.cause = cause;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putBoolean("ministers", ministers);
		bundle.putInt("cause", cause.ordinal());
	}
	
	@Override
	public String getName()
	{
		return "WIN";
	}
}