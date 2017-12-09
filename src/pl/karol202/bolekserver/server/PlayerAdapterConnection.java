package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.game.game.PlayerAdapter;
import pl.karol202.bolekserver.game.game.Role;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketCollaboratorsRevealment;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketGameStart;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketRoleAssigned;

import java.util.stream.Stream;

public class PlayerAdapterConnection implements PlayerAdapter
{
	private Connection connection;
	
	public PlayerAdapterConnection(Connection connection)
	{
		this.connection = connection;
	}
	
	@Override
	public void sendGameStartMessage(Stream<Player> players)
	{
		OutputPacketGameStart packet = new OutputPacketGameStart();
		players.forEach(p -> packet.addPlayer(p.getName()));
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendRoleAssignmentMessage(Role role)
	{
		OutputPacketRoleAssigned packet = new OutputPacketRoleAssigned(role);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendCollaboratorsRevealmentMessages(Stream<Player> collaborators)
	{
		OutputPacketCollaboratorsRevealment packet = new OutputPacketCollaboratorsRevealment();
		collaborators.forEach(p -> packet.addCollaborator(p.getName()));
		connection.sendPacket(packet);
	}
}