package pl.karol202.bolekserver.server;

public class PlayerAdapterConnectionAPI2 extends PlayerAdapterConnection
{
	PlayerAdapterConnectionAPI2(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public int getAPIVersion()
	{
		return 2;
	}
}