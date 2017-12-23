package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.game.*;
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
	public void setGameAndPlayer(Game game, Player player)
	{
		connection.setGame(game);
		connection.setPlayer(player);
	}
	
	@Override
	public void resetGameAndPlayer()
	{
		connection.setGame(null);
		connection.setPlayer(null);
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
	public void sendCollaboratorsRevealmentMessages(Stream<Player> collaborators, Player bolek)
	{
		OutputPacketCollaboratorsRevealment packet = new OutputPacketCollaboratorsRevealment(bolek.getName());
		collaborators.forEach(p -> packet.addCollaborator(p.getName()));
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendStackRefillMessage(int totalActs)
	{
		OutputPacketStackRefilled packet = new OutputPacketStackRefilled(totalActs);
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
	public void sendPollIndexChangeMessage(int pollIndex)
	{
		OutputPacketPollIndexChange packet = new OutputPacketPollIndexChange(pollIndex);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendRandomActMessage()
	{
		OutputPacketRandomAct packet = new OutputPacketRandomAct();
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
	public void sendChooseActsOrVetoRequestToPrimeMinister(Act[] acts)
	{
		OutputPacketChooseActsOrVetoPrimeMinister packet = new OutputPacketChooseActsOrVetoPrimeMinister();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPrimeMinisterChoosingActsMessage()
	{
		OutputPacketPrimeMinisterChoosingActs packet = new OutputPacketPrimeMinisterChoosingActs();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendVetoRequest()
	{
		OutputPacketVetoRequest packet = new OutputPacketVetoRequest();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendVetoResponseMessage(boolean response)
	{
		OutputPacketVetoResponse packet = new OutputPacketVetoResponse(response);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendActPassedMessage(int lustrationPassed, int antilustrationPassed)
	{
		OutputPacketActPassed packet = new OutputPacketActPassed(lustrationPassed, antilustrationPassed);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendWinMessage(WinCause cause)
	{
		OutputPacketWin packet = new OutputPacketWin(cause);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendLossMessage(WinCause cause)
	{
		OutputPacketLoss packet = new OutputPacketLoss(cause);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPresidentCheckingPlayerMessage()
	{
		OutputPacketPresidentCheckingPlayer packet = new OutputPacketPresidentCheckingPlayer();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPlayerCheckRequestToPresident(Stream<Player> checkablePlayers)
	{
		OutputPacketCheckPlayerPresident packet = new OutputPacketCheckPlayerPresident();
		checkablePlayers.forEach(p -> packet.addCheckablePlayer(p.getName()));
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPlayerCheckingResultToPresident(int result)
	{
		OutputPacketPlayerCheckingResult packet = new OutputPacketPlayerCheckingResult(result);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPresidentCheckedPlayerMessage(Player checkedPlayer)
	{
		OutputPacketPresidentCheckedPlayer packet = new OutputPacketPresidentCheckedPlayer(checkedPlayer.getName());
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPresidentCheckingPlayerOrActsMessage()
	{
		OutputPacketPresidentCheckingPlayerOrActs packet = new OutputPacketPresidentCheckingPlayerOrActs();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPlayerOrActsCheckingChooseRequestToPresident()
	{
		OutputPacketChoosePlayerOrActsCheckingPresident packet = new OutputPacketChoosePlayerOrActsCheckingPresident();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendActsCheckingResultMessageToPresident(Act[] acts)
	{
		String[] strings = Stream.of(acts).map(Enum::name).toArray(String[]::new);
		OutputPacketActsCheckingResult packet = new OutputPacketActsCheckingResult(strings);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPresidentCheckedActsMessage()
	{
		OutputPacketPresidentCheckedActs packet = new OutputPacketPresidentCheckedActs();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPresidentChoosingPresidentMessage()
	{
		OutputPacketPresidentChoosingPresident packet = new OutputPacketPresidentChoosingPresident();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendChoosePresidentRequestToPresident()
	{
		OutputPacketChoosePresident packet = new OutputPacketChoosePresident();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPresidentLustratingMessage()
	{
		OutputPacketPresidentLustrating packet = new OutputPacketPresidentLustrating();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendLustrationRequestToPresident()
	{
		OutputPacketLustratePresident packet = new OutputPacketLustratePresident();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendYouAreLustratedMessage()
	{
		OutputPacketYouAreLustrated packet = new OutputPacketYouAreLustrated();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPresidentLustratedMessage(Player player, boolean bolek)
	{
		OutputPacketPresidentLustrated packet = new OutputPacketPresidentLustrated(player.getName(), bolek);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendGameExitedMessage()
	{
		OutputPacketGameExited packet = new OutputPacketGameExited();
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendPlayersUpdatedMessage(Stream<Player> player)
	{
		OutputPacketPlayersUpdated packet = new OutputPacketPlayersUpdated();
		player.forEach(p -> packet.addPlayer(p.getName()));
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendTooFewPlayersMessage()
	{
		OutputPacketTooFewPlayers packet = new OutputPacketTooFewPlayers();
		connection.sendPacket(packet);
	}
}