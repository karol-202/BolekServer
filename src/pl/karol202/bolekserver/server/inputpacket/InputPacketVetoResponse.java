package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionRespondOnVeto;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;

public class InputPacketVetoResponse implements InputGamePacket
{
	private boolean accept;
	
	@Override
	public void readData(DataBundle bundle)
	{
		accept = bundle.getBoolean("accepted", false);
	}
	
	@Override
	public void execute(Connection connection, Game game)
	{
		Player sender = connection.getPlayer();
		boolean result = game.addActionAndWaitForResult(new GameActionRespondOnVeto(sender, accept));
		if(!result) connection.applyPacket(new OutputPacketFailure());
	}
}
