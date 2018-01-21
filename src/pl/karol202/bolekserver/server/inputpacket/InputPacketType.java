package pl.karol202.bolekserver.server.inputpacket;

import java.util.function.Supplier;
import java.util.stream.Stream;

enum InputPacketType
{
	CREATESERVER(InputPacketCreateServer::new),
	LOGIN(InputPacketLogin::new),
	LOGOUT(InputPacketLogout::new),
	READY(InputPacketReady::new),
	MESSAGE(InputPacketMessage::new),
	SETPRIMEMINISTER(InputPacketSetPrimeMinister::new),
	PRIMEMINISTERVOTE(InputPacketPrimeMinisterVote::new),
	DISMISSACTBYPRESIDENT(InputPacketDismissActByPresident::new),
	DISMISSACTBYPRIMEMINISTER(InputPacketDismissActByPrimeMinister::new),
	VETOREQUEST(InputPacketVetoRequest::new),
	VETORESPONSE(InputPacketVetoResponse::new),
	CHECKPLAYERPRESIDENT(InputPacketCheckPlayerPresident::new),
	CHOOSEPLAYERORACTSCHECKINGPRESIDENT(InputPacketChoosePlayerOrActsCheckingPresident::new),
	CHOOSEPRESIDENT(InputPacketChoosePresident::new),
	LUSTRATEPRESIDENT(InputPacketLustratePresident::new),
	EXITGAME(InputPacketExitGame::new),
	PONG(InputPacketPong::new);
	
	private Supplier<InputPacket> packetSupplier;
	
	InputPacketType(Supplier<InputPacket> packetSupplier)
	{
		this.packetSupplier = packetSupplier;
	}
	
	InputPacket createPacket()
	{
		return packetSupplier.get();
	}
	
	static InputPacketType getPacketTypeByName(String name)
	{
		return Stream.of(values()).filter(t -> t.name().equals(name)).findAny().orElse(null);
	}
}