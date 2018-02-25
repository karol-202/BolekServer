package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.game.server.UserAdapter;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketLoggedIn;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketMessage;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketServerStatus;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketUsersUpdate;

import java.util.stream.Stream;

public class UserAdapterConnection implements UserAdapter
{
	private Connection connection;
	
	public UserAdapterConnection(Connection connection)
	{
		this.connection = connection;
	}
	
	public int getAPIVersion()
	{
		return 1;
	}
	
	@Override
	public void sendLoggedInMessage(String serverName, int serverCode)
	{
		connection.sendPacket(new OutputPacketLoggedIn(serverName, serverCode));
	}
	
	@Override
	public void sendUsersListMessage(Stream<User> users)
	{
		OutputPacketUsersUpdate packet = new OutputPacketUsersUpdate();
		users.forEach(u -> packet.addUser(u.getName(), u.isReady(), u.getAddress()));
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendServerStatusMessage(boolean gameAvailable)
	{
		connection.sendPacket(new OutputPacketServerStatus(gameAvailable));
	}
	
	@Override
	public void sendMessage(User sender, String message)
	{
		connection.sendPacket(new OutputPacketMessage(sender.getName(), message));
	}
	
	public Connection getConnection()
	{
		return connection;
	}
}