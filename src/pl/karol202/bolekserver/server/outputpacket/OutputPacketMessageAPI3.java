package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketMessageAPI3 implements OutputPacket
{
	private String sender;
	private String message;
	private boolean newMessage;
	
	public OutputPacketMessageAPI3(String sender, String message, boolean newMessage)
	{
		this.sender = sender;
		this.message = message;
		this.newMessage = newMessage;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("sender", sender);
		bundle.putString("message", message);
		bundle.putBoolean("newMessage", newMessage);
	}
	
	@Override
	public String getName()
	{
		return "MESSAGE";
	}
}
