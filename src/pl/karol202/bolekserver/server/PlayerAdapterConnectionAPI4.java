package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.game.game.Role;
import pl.karol202.bolekserver.game.game.WinCause;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketGameStartAPI4;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketSpectatingStart;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketWinAPI4;

import java.util.stream.Stream;

public class PlayerAdapterConnectionAPI4 extends PlayerAdapterConnectionAPI3
{
	public static final byte[] SECRET_IMAGES_CODE = { 48, -120, 18, 9, -58, 111, -107, 100, 17, -123, 81, -65, 86, 102, 31, -117 };
	
	public PlayerAdapterConnectionAPI4(Connection connection)
	{
		super(connection);
	}
	
	@Override
	public void sendGameStartMessage(Stream<Player> players, boolean secretImages)
	{
		OutputPacketGameStartAPI4 packet = new OutputPacketGameStartAPI4(secretImages);
		players.forEach(p -> packet.addPlayer(p.getName()));
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendWinMessage(boolean ministers, WinCause cause, Role role)
	{
		OutputPacketWinAPI4 packet = new OutputPacketWinAPI4(ministers, cause);
		connection.sendPacket(packet);
	}
	
	@Override
	public void sendSpectatingStartMessage(boolean secretImages)
	{
		OutputPacketSpectatingStart packet = new OutputPacketSpectatingStart(secretImages);
		connection.sendPacket(packet);
	}
}