package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Act;
import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionDismissActByPrimeMinister;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;

public class InputPacketDismissActByPrimeMinister implements InputGamePacket
{
	private Act act;
	
	@Override
	public void readData(DataBundle bundle)
	{
		act = Act.getActByName(bundle.getString("act", ""));
	}
	
	@Override
	public void execute(Connection connection, Game game)
	{
		Player sender = connection.getPlayer();
		boolean result = game.addActionAndWaitForResult(new GameActionDismissActByPrimeMinister(sender, act));
		if(!result) connection.applyPacket(new OutputPacketFailure());
	}
}