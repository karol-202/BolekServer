package pl.karol202.bolekserver.server;

public class UserAdapterConnectionAPI4 extends UserAdapterConnectionAPI3
{
	public UserAdapterConnectionAPI4(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public int getAPIVersion()
	{
		return 4;
	}
}