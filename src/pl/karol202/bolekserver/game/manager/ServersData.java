package pl.karol202.bolekserver.game.manager;

public class ServersData
{
	private int servers;
	private int activeServers;
	
	ServersData(int servers, int activeServers)
	{
		this.servers = servers;
		this.activeServers = activeServers;
	}
	
	public int getServers()
	{
		return servers;
	}
	
	public int getActiveServers()
	{
		return activeServers;
	}
}