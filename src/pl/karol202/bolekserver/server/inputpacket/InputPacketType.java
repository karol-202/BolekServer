package pl.karol202.bolekserver.server.inputpacket;

import java.util.function.Supplier;
import java.util.stream.Stream;

enum InputPacketType
{
	CREATESERVER(InputPacketCreateServer::new);
	
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