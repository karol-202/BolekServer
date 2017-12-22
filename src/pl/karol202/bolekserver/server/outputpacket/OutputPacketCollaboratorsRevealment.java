package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class OutputPacketCollaboratorsRevealment implements OutputPacket
{
	private List<String> collaborators;
	private String bolek;
	
	public OutputPacketCollaboratorsRevealment(String bolek)
	{
		this.collaborators = new ArrayList<>();
		this.bolek = bolek;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("collaborators", collaborators.size());
		for(int i = 0; i < collaborators.size(); i++) bundle.putString("collborator" + i, collaborators.get(i));
		bundle.putString("bolek", bolek);
	}
	
	@Override
	public String getName()
	{
		return "COLLABORATORSREVEALMENT";
	}
	
	public void addCollaborator(String collaborator)
	{
		collaborators.add(collaborator);
	}
}