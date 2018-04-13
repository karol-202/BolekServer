package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketGameStartAPI4;

import java.util.stream.Stream;

public class PlayerAdapterConnectionAPI4 extends PlayerAdapterConnectionAPI3
{
	public PlayerAdapterConnectionAPI4(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public void sendGameStartMessage(Stream<Player> players, boolean secretImages)
	{
		OutputPacketGameStartAPI4 packet = new OutputPacketGameStartAPI4(secretImages);
		players.forEach(p -> packet.addPlayer(p.getName()));
		connection.sendPacket(packet);
	}
}