package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.ErrorReference;
import pl.karol202.bolekserver.game.manager.ConnectionActionFindServer;
import pl.karol202.bolekserver.game.manager.GameServersManager;
import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.game.server.ServerActionAddUser;
import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;

public class InputPacketLogin implements InputControlPacket
{
	private int serverCode;
	private String username;
	private int apiVersion;
	
	@Override
	public void readData(DataBundle bundle)
	{
		serverCode = bundle.getInt("serverCode", -1);
		username = bundle.getString("username", "Unnamed user");
		apiVersion = bundle.getInt("apiVersion", 1);
	}
	
	@Override
	public void execute(Connection connection, GameServersManager manager)
	{
		if(connection.isGameServerSet() || connection.isGameSet())
		{
			connection.applyPacket(new OutputPacketFailure());
			return;
		}
		
		GameServer server = manager.addActionAndWaitForResult(new ConnectionActionFindServer(serverCode));
		if(server == null)
		{
			connection.applyPacket(new OutputPacketFailure(OutputPacketFailure.PROBLEM_SERVER_CODE_INVALID));
			return;
		}
		
		User user = new User(username, connection, apiVersion);
		ErrorReference<GameServer.UserAddingError> error = new ErrorReference<>();
		boolean result = server.addActionAndWaitForResult(new ServerActionAddUser(user, error));
		if(!result)
		{
			connection.applyPacket(new OutputPacketFailure(getUserAddingProblemCode(error.getError())));
			return;
		}
		connection.setGameServer(server);
		connection.setUser(user);
	}
	
	private int getUserAddingProblemCode(GameServer.UserAddingError error)
	{
		if(error == null) return 0;
		switch(error)
		{
		case INVALID_NAME: return OutputPacketFailure.PROBLEM_USER_INVALID_NAME;
		case TOO_MANY_USERS: return OutputPacketFailure.PROBLEM_USER_TOO_MANY;
		case USERNAME_BUSY: return OutputPacketFailure.PROBLEM_USER_NAME_BUSY;
		}
		return 0;
	}
}