package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionChoosePresident;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;

public class InputPacketChoosePresident implements InputGamePacket
{
	private String president;
	
	@Override
	public void readData(DataBundle bundle)
	{
		president = bundle.getString("president", "");
	}
	
	@Override
	public void execute(Connection connection, Game game)
	{
		Player sender = connection.getPlayer();
		Game.UserChoosingError result = game.addActionAndWaitForResult(new GameActionChoosePresident(sender, president));
		if(result == Game.UserChoosingError.ERROR)
			connection.sendPacket(new OutputPacketFailure());
		else if(result == Game.UserChoosingError.INVALID_USER)
			connection.sendPacket(new OutputPacketFailure(OutputPacketFailure.PROBLEM_INVALID_USER));
	}
}
