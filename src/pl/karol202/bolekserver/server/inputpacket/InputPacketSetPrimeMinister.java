package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionChoosePrimeMinister;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketFailure;

public class InputPacketSetPrimeMinister implements InputGamePacket
{
	private String primeMinister;
	
	@Override
	public void readData(DataBundle bundle)
	{
		primeMinister = bundle.getString("primeMinister", "");
	}
	
	@Override
	public void execute(Connection connection, Game game)
	{
		Player sender = connection.getPlayer();
		Game.UserChoosingError result = game.addActionAndWaitForResult(new GameActionChoosePrimeMinister(sender, primeMinister));
		if(result == Game.UserChoosingError.ERROR)
			connection.applyPacket(new OutputPacketFailure());
		else if(result == Game.UserChoosingError.INVALID_USER)
			connection.applyPacket(new OutputPacketFailure(OutputPacketFailure.PROBLEM_INVALID_USER));
	}
}