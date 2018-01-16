package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketMessage implements OutputPacket
{
	private String sender;
	private String message;
	
	public OutputPacketMessage(String sender, String message)
	{
		this.sender = sender;
		this.message = message;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("sender", sender);
		bundle.putString("message", message);
	}
	
	@Override
	public String getName()
	{
		return "MESSAGE";
	}
}
