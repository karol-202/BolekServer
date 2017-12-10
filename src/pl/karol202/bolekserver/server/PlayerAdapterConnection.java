package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.game.Act;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.game.game.PlayerAdapter;
import pl.karol202.bolekserver.game.game.Role;
import pl.karol202.bolekserver.server.outputpacket.*;

import java.util.stream.Stream;

public class PlayerAdapterConnection implements PlayerAdapter
{
	private Connection connection;
	
	public PlayerAdapterConnection(Connection connection)
	{
		this.connection = connection;
	}
	
	@Override
	public void setPlayer(Player player)
	{
		connection.setPlayer(player);
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
	
	@Override
	public void sendPresidentAssignmentMessage(Player president)
	{
		OutputPacketPresidentAssigned packet = new OutputPacketPresidentAssigned(president.getName());
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPrimeMinisterChooseRequest(Stream<Player> candidates)
	{
		OutputPacketChoosePrimeMinister packet = new OutputPacketChoosePrimeMinister();
		candidates.forEach(p -> packet.addCandidate(p.getName()));
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPrimeMinisterChosenMessage(Player primeMinister)
	{
		OutputPacketPrimeMinisterChosen packet = new OutputPacketPrimeMinisterChosen(primeMinister.getName());
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPrimeMinisterVotingRequest()
	{
		OutputPacketVoteOnPrimeMinister packet = new OutputPacketVoteOnPrimeMinister();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendVotingResultMessage(Stream<Player> upvoters, int totalVotes, boolean passed)
	{
		OutputPacketVotingResult packet = new OutputPacketVotingResult(totalVotes, passed);
		upvoters.forEach(p -> packet.addUpvoter(p.getName()));
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPrimeMinisterAssignmentMessage(Player primeMinister)
	{
		String name = primeMinister != null ? primeMinister.getName() : "";
		OutputPacketPrimeMinisterAssigned packet = new OutputPacketPrimeMinisterAssigned(name);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendChooseActsRequestToPresident(Act[] acts)
	{
		String[] strings = Stream.of(acts).map(Enum::name).toArray(String[]::new);
		OutputPacketChooseActsPresident packet = new OutputPacketChooseActsPresident(strings);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPresidentChoosingActsMessage()
	{
		OutputPacketPresidentChoosingActs packet = new OutputPacketPresidentChoosingActs();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendChooseActsRequestToPrimeMinister(Act[] acts)
	{
		String[] strings = Stream.of(acts).map(Enum::name).toArray(String[]::new);
		OutputPacketChooseActsPrimeMinister packet = new OutputPacketChooseActsPrimeMinister(strings);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPrimeMinisterChoosingActsMessage()
	{
		OutputPacketPrimeMinisterChoosingActs packet = new OutputPacketPrimeMinisterChoosingActs();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendActPassedMessage(int lustrationPassed, int antilustrationPassed)
	{
		OutputPacketActPassed packet = new OutputPacketActPassed(lustrationPassed, antilustrationPassed);
		connection.sendPacket(packet);
	}
}