package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionChoosePlayerOrActsCheckingByPresident;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;

public class InputPacketChoosePlayerOrActsCheckingPresident implements InputGamePacket
{
	private int choice;
	
	@Override
	public void readData(DataBundle bundle)
	{
		choice = bundle.getInt("choice", choice);
	}
	
	@Override
	public void execute(Connection connection, Game game)
	{
		Player sender = connection.getPlayer();
		boolean result = game.addActionAndWaitForResult(new GameActionChoosePlayerOrActsCheckingByPresident(sender, choice));
		if(!result) connection.sendPacket(new OutputPacketFailure());
	}
}
