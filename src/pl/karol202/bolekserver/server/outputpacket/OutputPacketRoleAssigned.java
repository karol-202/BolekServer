package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.game.game.Role;
import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketRoleAssigned implements OutputPacket
{
	private Role role;
	
	public OutputPacketRoleAssigned(Role role)
	{
		this.role = role;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("role", role.name());
	}
	
	@Override
	public String getName()
	{
		return "ROLEASSIGNED";
	}
}