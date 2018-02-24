package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.ErrorReference;
import pl.karol202.bolekserver.game.manager.ConnectionActionCreateServer;
import pl.karol202.bolekserver.game.manager.GameServersManager;
import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.game.server.ServerActionAddUser;
import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;

public class InputPacketCreateServer implements InputControlPacket
{
	private String name;
	private String username;
	private int apiVersion;
	
	@Override
	public void readData(DataBundle bundle)
	{
		name = bundle.getString("name", "New server");
		username = bundle.getString("username", "Unnamed user");
		apiVersion = bundle.getInt("apiVersion", 1);
	}
	
	@Override
	public void execute(Connection connection, GameServersManager manager)
	{
		if(connection.isGameServerSet() || connection.isGameSet())
		{
			connection.sendPacket(new OutputPacketFailure());
			return;
		}
		
		ErrorReference<GameServersManager.ServerCreationError> scError = new ErrorReference<>();
		GameServer server = manager.addActionAndWaitForResult(new ConnectionActionCreateServer(name, scError));
		if(server == null)
		{
			connection.sendPacket(new OutputPacketFailure(getServerCreationProblemCode(scError.getError())));
			return;
		}
		
		User user = new User(username, connection, apiVersion);
		ErrorReference<GameServer.UserAddingError> uaError = new ErrorReference<>();
		boolean result = server.addActionAndWaitForResult(new ServerActionAddUser(user, uaError));
		if(!result)
		{
			connection.sendPacket(new OutputPacketFailure(getUserAddingProblemCode(uaError.getError())));
			return;
		}
		connection.setGameServer(server);
		connection.setUser(user);
	}
	
	private int getServerCreationProblemCode(GameServersManager.ServerCreationError error)
	{
		if(error == null) return 0;
		switch(error)
		{
		case INVALID_NAME: return OutputPacketFailure.PROBLEM_SERVER_INVALID_NAME;
		case TOO_MANY_SERVERS: return OutputPacketFailure.PROBLEM_SERVER_TOO_MANY;
		}
		return 0;
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