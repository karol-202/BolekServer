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
	Connection connection;
	
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
		connection.applyPacket(new OutputPacketLoggedIn(serverName, serverCode));
	}
	
	@Override
	public void sendUsersListMessage(Stream<User> users)
	{
		OutputPacketUsersUpdate packet = new OutputPacketUsersUpdate();
		users.forEach(u -> packet.addUser(u.getName(), u.isReady()));
		connection.applyPacket(packet);
	}
	
	@Override
	public void sendServerStatusMessage(boolean gameAvailable)
	{
		connection.applyPacket(new OutputPacketServerStatus(gameAvailable));
	}
	
	@Override
	public void sendMessage(User sender, String message, boolean newMessage)
	{
		connection.applyPacket(new OutputPacketMessage(sender.getName(), message));
	}
	
	public Connection getConnection()
	{
		return connection;
	}
}