package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketCollaboratorsRevealmentAPI3;

import java.util.List;

public class PlayerAdapterConnectionAPI3 extends PlayerAdapterConnection
{
	public PlayerAdapterConnectionAPI3(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public void sendCollaboratorsRevealmentMessage(List<Player> ministers, List<Player> collaborators, Player bolek)
	{
		OutputPacketCollaboratorsRevealmentAPI3 packet = new OutputPacketCollaboratorsRevealmentAPI3(bolek.getName());
		ministers.forEach(p -> packet.addMinister(p.getName()));
		collaborators.forEach(p -> packet.addCollaborator(p.getName()));
		connection.sendPacket(packet);
	}
}