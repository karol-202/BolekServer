package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketMessageAPI3;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketUsersUpdateAPI3;

import java.util.stream.Stream;

public class UserAdapterConnectionAPI3 extends UserAdapterConnectionAPI2
{
	public UserAdapterConnectionAPI3(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public int getAPIVersion()
	{
		return 3;
	}
	
	@Override
	public void sendUsersListMessage(Stream<User> users)
	{
		OutputPacketUsersUpdateAPI3 packet = new OutputPacketUsersUpdateAPI3();
		users.forEach(u -> packet.addUser(u.getName(), u.isReady(), u.getAddress()));
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendMessage(User sender, String message, boolean newMessage)
	{
		connection.sendPacket(new OutputPacketMessageAPI3(sender.getName(), message, newMessage));
	}
}