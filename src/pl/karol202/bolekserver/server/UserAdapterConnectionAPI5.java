package pl.karol202.bolekserver.server;

public class UserAdapterConnectionAPI5 extends UserAdapterConnectionAPI4
{
	public UserAdapterConnectionAPI5(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public int getAPIVersion()
	{
		return 5;
	}
}