package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class OutputPacketUsersUpdate implements OutputPacket
{
	private List<String> users;
	private List<Boolean> readiness;
	
	public OutputPacketUsersUpdate()
	{
		users = new ArrayList<>();
		readiness = new ArrayList<>();
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("length", users.size());
		for(int i = 0; i < users.size(); i++)
		{
			bundle.putString("user" + i, users.get(i));
			bundle.putBoolean("ready" + i, readiness.get(i));
		}
	}
	
	@Override
	public String getName()
	{
		return "USERSUPDATE";
	}
	
	public void addUser(String user, boolean ready)
	{
		users.add(user);
		readiness.add(ready);
	}
}