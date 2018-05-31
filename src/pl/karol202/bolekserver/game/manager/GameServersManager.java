package pl.karol202.bolekserver.game.manager;

import pl.karol202.bolekserver.Main;
import pl.karol202.bolekserver.ServerProperties;
import pl.karol202.bolekserver.game.ErrorReference;
import pl.karol202.bolekserver.game.Looper;
import pl.karol202.bolekserver.game.Target;
import pl.karol202.bolekserver.game.server.GameServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameServersManager implements Target
{
	public enum ServerCreationError
	{
		INVALID_NAME, TOO_MANY_SERVERS
	}
	
	private Looper looper;
	private List<GameServer> servers;
	
	public GameServersManager(Looper looper)
	{
		this.looper = looper;
		this.servers = new ArrayList<>();
	}
	
	GameServer createNewGameServer(String name, ErrorReference<ServerCreationError> error)
	{
		if(name == null || name.isEmpty() || name.length() > ServerProperties.MAX_SERVER_NAME_LENGTH)
		{
			error.setError(ServerCreationError.INVALID_NAME);
			return null;
		}
		if(servers.size() >= ServerProperties.MAX_SERVERS)
		{
			error.setError(ServerCreationError.TOO_MANY_SERVERS);
			return null;
		}
		GameServer server = new GameServer(looper, name, getUniqueServerCode(), ServerProperties.DEBUG);
		server.setServerListener(() -> removeEmptyServer(server));
		servers.add(server);
		
		Main.LOGGER.info("Created new server: " + name + ", " + server.getServerCode());
		return server;
	}
	
	GameServer findServer(int serverCode)
	{
		return servers.stream().filter(s -> s.getServerCode() == serverCode).findAny().orElse(null);
	}
	
	ServersData getServersData()
	{
		int activeServers = 0;
		for(GameServer server : servers) if(server.isGameStarted()) activeServers++;
		return new ServersData(servers.size(), activeServers);
	}
	
	private int getUniqueServerCode()
	{
		Random random = new Random();
		int code;
		
		mainLoop:
		while(true)
		{
			code = Math.abs(random.nextInt() % 9000) + 1000;
			
			for(GameServer server : servers)
				if(code == server.getServerCode()) continue mainLoop;
			break;
		}
		return code;
	}
	
	private void removeEmptyServer(GameServer server)
	{
		servers.remove(server);
		Main.LOGGER.info("Removed server: " + server.getName() + ", " + server.getServerCode());
	}
	
	public <R> R addActionAndWaitForResult(ConnectionAction<R> action)
	{
		return looper.addActionAndWaitForResult(action, this);
	}
}