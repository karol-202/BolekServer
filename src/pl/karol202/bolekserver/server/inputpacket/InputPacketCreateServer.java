package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.manager.ConnectionActionCreateServer;
import pl.karol202.bolekserver.game.manager.GameServersManager;
import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.game.server.ServerActionAddUser;
import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketLoggedIn;

public class InputPacketCreateServer implements InputControlPacket
{
	private String name;
	private String username;
	
	@Override
	public void readData(DataBundle bundle)
	{
		name = bundle.getString("name", "New server");
		username = bundle.getString("username", "Unnamed user");
	}
	
	@Override
	public void execute(Connection connection, GameServersManager manager)
	{
		GameServer server = manager.addActionAndWaitForResult(new ConnectionActionCreateServer(name));
		if(server == null)
		{
			connection.sendPacket(new OutputPacketFailure());
			return;
		}
		connection.setGameServer(server);
		
		User user = server.addActionAndWaitForResult(new ServerActionAddUser(username, connection));
		if(user == null)
		{
			connection.sendPacket(new OutputPacketFailure());
			return;
		}
		connection.setUser(user);
		
		connection.sendPacket(new OutputPacketLoggedIn(server.getName(), server.getServerCode()));
	}
}