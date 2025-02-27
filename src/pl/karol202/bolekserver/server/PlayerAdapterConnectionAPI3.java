package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketCollaboratorsRevealmentAPI3;

import java.util.stream.Stream;

public class PlayerAdapterConnectionAPI3 extends PlayerAdapterConnectionAPI2
{
	PlayerAdapterConnectionAPI3(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public int getAPIVersion()
	{
		return 3;
	}
	
	@Override
	public void sendCollaboratorsRevealmentMessage(Stream<Player> ministers, Stream<Player> collaborators, Player bolek)
	{
		OutputPacketCollaboratorsRevealmentAPI3 packet = new OutputPacketCollaboratorsRevealmentAPI3(bolek.getName());
		ministers.forEach(p -> packet.addMinister(p.getName()));
		collaborators.forEach(p -> packet.addCollaborator(p.getName()));
		connection.sendPacket(packet);
	}
}