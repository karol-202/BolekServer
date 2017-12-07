package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.manager.ConnectionActionLogin;
import pl.karol202.bolekserver.game.manager.GameServersManager;
import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketLoggedIn;

public class InputPacketLogin implements InputControlPacket
{
	private int serverCode;
	private String username;
	
	@Override
	public void readData(DataBundle bundle)
	{
		serverCode = bundle.getInt("serverCode", -1);
		username = bundle.getString("username", "Unnamed user");
	}
	
	@Override
	public void execute(Connection connection, GameServersManager manager)
	{
		GameServer server = manager.addActionAndWaitForResult(new ConnectionActionLogin(serverCode, username));
		connection.setGameServer(server);
		
		connection.sendPacket(new OutputPacketLoggedIn(serverCode));
	}
}