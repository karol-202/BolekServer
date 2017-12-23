package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.game.server.UserAdapter;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketServerStatus;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketSetReady;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketUsersUpdate;

import java.util.stream.Stream;

public class UserAdapterConnection implements UserAdapter
{
	private Connection connection;
	
	public UserAdapterConnection(Connection connection)
	{
		this.connection = connection;
	}
	
	@Override
	public void sendUsersListMessage(Stream<User> users)
	{
		OutputPacketUsersUpdate packet = new OutputPacketUsersUpdate();
		users.forEach(u -> packet.addUser(u.getName()));
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendUserReadinessMessage(String username)
	{
		OutputPacketSetReady packet = new OutputPacketSetReady(username);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendServerStatusMessage(boolean gameAvailable)
	{
		OutputPacketServerStatus packet = new OutputPacketServerStatus(gameAvailable);
		connection.sendPacket(packet);
	}
	
	public Connection getConnection()
	{
		return connection;
	}
}