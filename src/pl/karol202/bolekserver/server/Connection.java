package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionExitGame;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.game.manager.GameServersManager;
import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.game.server.ServerActionRemoveUser;
import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.server.inputpacket.*;
import pl.karol202.bolekserver.server.outputpacket.OutputPacket;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketEncoder;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketPing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection
{
	private static final boolean PING_ENABLE = true;
	private static final int MAX_UNANSWERED_PINGS = 10;
	
	private GameServersManager gameServersManager;
	private GameServer gameServer;
	private Game game;
	private User user;
	private Player player;
	private int unansweredPings;
	
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	Connection(GameServersManager gameServersManager)
	{
		this.gameServersManager = gameServersManager;
	}
	
	boolean connect(Socket socket)
	{
		return tryToInitConnection(socket);
	}
	
	private boolean tryToInitConnection(Socket socket)
	{
		try
		{
			initConnection(socket);
		}
		catch(IOException e)
		{
			exception("Cannot connect to client.", e);
			return false;
		}
		return true;
	}
	
	private void initConnection(Socket socket) throws IOException
	{
		this.socket = socket;
		this.inputStream = socket.getInputStream();
		this.outputStream = socket.getOutputStream();
	}
	
	void run()
	{
		if(!isConnected()) return;
		new Thread(this::tryToListen).start();
		if(PING_ENABLE) new Thread(this::tryToStartConnectionChecking).start();
	}
	
	private void tryToListen()
	{
		try
		{
			listen();
		}
		catch(IOException e)
		{
			exception("Error while listening.", e);
		}
	}
	
	private void listen() throws IOException
	{
		Server.LOGGER.fine("Listening...");
		while(isConnected())
		{
			InputPacket packet = receivePacket();
			if(packet != null) executePacket(packet);
			else break;
		}
		closeSocket();
	}
	
	private InputPacket receivePacket() throws IOException
	{
		int length = Utils.readInt(inputStream);
		if(length <= 0) return null;
		byte[] bytes = new byte[length];
		int bytesRead = inputStream.read(bytes);
		if(bytesRead != length) return null;
		
		InputPacket packet = InputPacketFactory.createPacket(bytes);
		if(packet == null) Server.LOGGER.warning("Packet received: corrupted");
		else if(!packet.isSilent()) Server.LOGGER.fine("Packet received: " + packet.toString());
		return packet;
	}
	
	private void executePacket(InputPacket packet)
	{
		if(packet instanceof InputPacketPong)
			unansweredPings = 0;
		else if(packet instanceof InputControlPacket && gameServersManager != null)
			((InputControlPacket) packet).execute(this, gameServersManager);
		else if(packet instanceof InputServerPacket && gameServer != null)
			((InputServerPacket) packet).execute(this, gameServer);
		else if(packet instanceof InputGamePacket && game != null)
			((InputGamePacket) packet).execute(this, game);
	}
	
	public void sendPacket(OutputPacket packet)
	{
		if(!isConnected()) return;
		try
		{
			Server.LOGGER.fine("Sending packet: " + packet.toString());
			writePacket(packet);
		}
		catch(IOException e)
		{
			exception("Cannot send packet.", e);
		}
	}
	
	private synchronized void writePacket(OutputPacket packet) throws IOException
	{
		byte[] bytes = OutputPacketEncoder.encodePacket(packet);
		if(bytes == null || bytes.length == 0 || !isConnected()) return;
		outputStream.write(Utils.writeInt(bytes.length));
		outputStream.write(bytes);
	}
	
	private void closeSocket()
	{
		tryToExitGame();
		tryToLogout();
		if(!isConnected()) return;
		try
		{
			Server.LOGGER.info("Closing socket");
			socket.close();
		}
		catch(IOException e)
		{
			new ServerException("Cannot close socket", e).printStackTrace();
		}
	}
	
	public void tryToExitGame()
	{
		if(game != null && player != null) game.addActionAndReturnImmediately(new GameActionExitGame(player));
		game = null;
		player = null;
	}
	
	private void tryToLogout()
	{
		if(gameServer != null && user != null) gameServer.addActionAndReturnImmediately(new ServerActionRemoveUser(user));
	}
	
	private void tryToStartConnectionChecking()
	{
		try
		{
			startConnectionChecking();
		}
		catch(Exception e)
		{
			exception("Exception on connection checking.", e);
		}
	}
	
	private void startConnectionChecking() throws InterruptedException, IOException
	{
		while(isConnected())
		{
			Thread.sleep(1000);
			writePacket(new OutputPacketPing());
			unansweredPings++;
			if(unansweredPings > MAX_UNANSWERED_PINGS) closeSocket();
		}
	}
	
	private boolean isConnected()
	{
		return socket.isConnected() && !socket.isClosed();
	}
	
	public boolean isGameServerSet()
	{
		return gameServer != null;
	}
	
	public boolean isGameSet()
	{
		return game != null;
	}
	
	public void setGameServer(GameServer gameServer)
	{
		this.gameServer = gameServer;
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	public User getUser()
	{
		return user;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	private void exception(String message, Exception exception)
	{
		new ServerException(message, exception).printStackTrace();
		closeSocket();
	}
}