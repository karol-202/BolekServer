package pl.karol202.bolekserver.server.packet;

import java.io.InputStream;
import java.util.function.Function;

enum PacketType
{
	LOGIN(1, PacketLogin::new);
	
	private int id;
	private Function<InputStream, ? extends Packet> packetCreator;
	
	PacketType(int id, Function<InputStream, ? extends Packet> packetCreator)
	{
		this.id = id;
		this.packetCreator = packetCreator;
	}
	
	private Packet createPacket(InputStream inputStream)
	{
		return packetCreator.apply(inputStream);
	}
	
	static Packet createPacketFromId(int id, InputStream inputStream)
	{
		for(PacketType type : values())
			if(type.id == id) return type.createPacket(inputStream);
		return null;
	}
}