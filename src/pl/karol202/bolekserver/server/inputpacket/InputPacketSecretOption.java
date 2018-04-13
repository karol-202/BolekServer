package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.manager.ConnectionActionFindServer;
import pl.karol202.bolekserver.game.manager.GameServersManager;
import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.game.server.ServerActionSecretOption;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;

public class InputPacketSecretOption implements InputControlPacket
{
	private int serverCode;
	private String secretOption;
	private int secretValue;
	
	@Override
	public void readData(DataBundle bundle)
	{
		serverCode = bundle.getInt("serverCode", 0);
		secretOption = bundle.getString("secretOption", "");
		secretValue = bundle.getInt("secretValue", -1);
	}
	
	@Override
	public void execute(Connection connection, GameServersManager manager)
	{
		GameServer server = manager.addActionAndWaitForResult(new ConnectionActionFindServer(serverCode));
		if(server == null) return;
		server.addActionAndWaitForResult(new ServerActionSecretOption(secretOption, secretValue));
	}
}