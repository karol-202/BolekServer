package pl.karol202.bolekserver.server;

public class UserAdapterConnectionAPI2 extends UserAdapterConnection
{
	public UserAdapterConnectionAPI2(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public int getAPIVersion()
	{
		return 2;
	}
}