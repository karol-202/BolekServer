package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionRequestVetoByPrimeMinister;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;

public class InputPacketVetoRequest implements InputGamePacket
{
	@Override
	public void readData(DataBundle bundle) { }
	
	@Override
	public void execute(Connection connection, Game game)
	{
		Player sender = connection.getPlayer();
		boolean result = game.addActionAndWaitForResult(new GameActionRequestVetoByPrimeMinister(sender));
		if(!result) connection.applyPacket(new OutputPacketFailure());
	}
}
