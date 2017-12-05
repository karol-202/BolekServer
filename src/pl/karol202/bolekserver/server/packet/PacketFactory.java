package pl.karol202.bolekserver.server.packet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class PacketFactory
{
	public static Packet createPacket(byte[] bytes, int length)
	{
		if(bytes.length <= 0) return null;
		InputStream inputStream = new ByteArrayInputStream(bytes, 0, length);
		byte type = bytes[0];
		return PacketType.createPacketFromId((int) type, inputStream);
	}
}