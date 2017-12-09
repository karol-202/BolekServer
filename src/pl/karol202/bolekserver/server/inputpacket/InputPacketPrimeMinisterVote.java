package pl.karol202.bolekserver.server.inputpacket;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionVoteOnPrimeMinister;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Connection;
import pl.karol202.bolekserver.server.DataBundle;

public class InputPacketPrimeMinisterVote implements InputGamePacket
{
	private boolean vote;
	
	@Override
	public void readData(DataBundle bundle)
	{
		vote = bundle.getInt("vote", 0) > 0;
	}
	
	@Override
	public void execute(Connection connection, Game game)
	{
		Player sender = connection.getPlayer();
		game.addActionAndWaitForResult(new GameActionVoteOnPrimeMinister(sender, vote));
	}
}