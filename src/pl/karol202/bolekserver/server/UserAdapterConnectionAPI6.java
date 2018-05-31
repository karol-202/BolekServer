package pl.karol202.bolekserver.server;

public class UserAdapterConnectionAPI6 extends UserAdapterConnectionAPI5
{
	public UserAdapterConnectionAPI6(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public int getAPIVersion()
	{
		return 6;
	}
}