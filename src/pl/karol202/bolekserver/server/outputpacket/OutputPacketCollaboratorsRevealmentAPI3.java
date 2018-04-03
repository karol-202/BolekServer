package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class OutputPacketCollaboratorsRevealmentAPI3 implements OutputPacket
{
	private List<String> ministers;
	private List<String> collaborators;
	private String bolek;
	
	public OutputPacketCollaboratorsRevealmentAPI3(String bolek)
	{
		this.ministers = new ArrayList<>();
		this.collaborators = new ArrayList<>();
		this.bolek = bolek;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("ministers", ministers.size());
		for(int i = 0; i < ministers.size(); i++) bundle.putString("minister" + i, ministers.get(i));
		bundle.putInt("collaborators", collaborators.size());
		for(int i = 0; i < collaborators.size(); i++) bundle.putString("collaborator" + i, collaborators.get(i));
		bundle.putString("bolek", bolek);
	}
	
	@Override
	public String getName()
	{
		return "COLLABORATORSREVEALMENT";
	}
	
	public void addMinister(String minister)
	{
		ministers.add(minister);
	}
	
	public void addCollaborator(String collaborator)
	{
		collaborators.add(collaborator);
	}
}