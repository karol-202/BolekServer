package pl.karol202.bolekserver.server.packet;

import pl.karol202.bolekserver.game.GameManager;

import java.io.InputStream;

public abstract class Packet
{
	InputStream inputStream;
	
	Packet(InputStream inputStream)
	{
		this.inputStream = inputStream;
	}
	
	public abstract void execute(GameManager gameManager);
}