package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Act;
import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionDismissActByPresident;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;

public class InputPacketDismissActByPresident implements InputGamePacket
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
		boolean result = game.addActionAndWaitForResult(new GameActionDismissActByPresident(sender, act));
		if(!result) connection.applyPacket(new OutputPacketFailure());
	}
}