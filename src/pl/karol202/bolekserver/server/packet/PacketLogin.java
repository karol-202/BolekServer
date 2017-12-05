package pl.karol202.bolekserver.server.packet;

import pl.karol202.bolekserver.game.GameManager;

import java.io.InputStream;

public class PacketLogin extends Packet
{
	public PacketLogin(InputStream inputStream)
	{
		super(inputStream);
	}
	
	@Override
	public void execute(GameManager gameManager)
	{
	
	}
}