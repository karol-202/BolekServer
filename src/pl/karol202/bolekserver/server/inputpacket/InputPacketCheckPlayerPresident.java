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
		Game.UserChoosingError result = game.addActionAndWaitForResult(new GameActionCheckPlayerByPresident(sender, checkedPlayer));
		if(result == Game.UserChoosingError.ERROR)
			connection.sendPacket(new OutputPacketFailure());
		else if(result == Game.UserChoosingError.INVALID_USER)
			connection.sendPacket(new OutputPacketFailure(OutputPacketFailure.PROBLEM_INVALID_USER));
	}
}
