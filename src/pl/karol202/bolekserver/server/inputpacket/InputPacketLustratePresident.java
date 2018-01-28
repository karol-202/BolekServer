package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionLustrateByPresident;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;

public class InputPacketLustratePresident implements InputGamePacket
{
	private String player;
	
	@Override
	public void readData(DataBundle bundle)
	{
		player = bundle.getString("player", "");
	}
	
	@Override
	public void execute(Connection connection, Game game)
	{
		Player sender = connection.getPlayer();
		Game.UserChoosingError result = game.addActionAndWaitForResult(new GameActionLustrateByPresident(sender, player));
		if(result == Game.UserChoosingError.ERROR)
			connection.sendPacket(new OutputPacketFailure());
		else if(result == Game.UserChoosingError.INVALID_USER)
			connection.sendPacket(new OutputPacketFailure(OutputPacketFailure.PROBLEM_INVALID_USER));
	}
}
