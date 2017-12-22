package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionCheckPlayerByPresident;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;

public class InputPacketCheckPlayerPresident implements InputGamePacket
{
	private String checkedPlayer;
	
	@Override
	public void readData(DataBundle bundle)
	{
		checkedPlayer = bundle.getString("checkedPlayer", "");
	}
	
	@Override
	public void execute(Connection connection, Game game)
	{
		Player sender = connection.getPlayer();
		boolean result = game.addActionAndWaitForResult(new GameActionCheckPlayerByPresident(sender, checkedPlayer));
		if(!result) connection.sendPacket(new OutputPacketFailure());
	}
}
