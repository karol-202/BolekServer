package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.PlayerAdapterConnectionAPI4;

public class OutputPacketSpectatingStart implements OutputPacket
{
	private boolean secretImages;
	
	public OutputPacketSpectatingStart(boolean secretImages)
	{
		this.secretImages = secretImages;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		byte[] code = PlayerAdapterConnectionAPI4.SECRET_IMAGES_CODE;
		bundle.putInt("imagesCodeLength", secretImages ? code.length : 0);
		if(secretImages) for(int i = 0; i < code.length; i++)
			bundle.putInt("imagesCode" + i, code[i]);
	}
	
	@Override
	public String getName()
	{
		return "SPECTATINGSTART";
	}
}