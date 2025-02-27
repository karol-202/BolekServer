package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.Main;
import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionExitGame;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.game.game.Spectator;
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
	private static final int MAX_UNANSWERED_PINGS = 5;
	private static final int PING_TIME = 2000;
	
	private GameServersManager gameServersManager;
	private GameServer gameServer;
	private Game game;
	private User user;
	private Player player;
	private Spectator spectator;
	private int unansweredPings;
	
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private String address;
	
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
		this.address = socket.getInetAddress().getHostAddress();
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
		Main.LOGGER.fine("Listening...");
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
		int bytesRead = 0;
		while(bytesRead != length)
			bytesRead += inputStream.read(bytes, bytesRead, length - bytesRead);
		
		InputPacket packet = InputPacketFactory.createPacket(bytes);
		if(packet == null) Main.LOGGER.warning("Packet received: corrupted: \n"+ new String(bytes));
		else if(!packet.isSilent()) Main.LOGGER.fine("Packet received: " + packet.toString());
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
			Main.LOGGER.fine("Sending packet: " + packet.toString());
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
			Main.LOGGER.info("Closing socket");
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
			Thread.sleep(PING_TIME);
			writePacket(new OutputPacketPing());
			unansweredPings++;
			if(unansweredPings > MAX_UNANSWERED_PINGS)
			{
				Main.LOGGER.warning("Closing socket due to no ping response.");
				closeSocket();
			}
		}
	}
	
	private boolean isConnected()
	{
		return socket.isConnected() && !socket.isClosed();
	}
	
	public String getAddress()
	{
		return address;
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

	public Spectator getSpectator()
	{
		return spectator;
	}

	public void setSpectator(Spectator spectator)
	{
		this.spectator = spectator;
	}

	private void exception(String message, Exception exception)
	{
		new ServerException(message, exception).printStackTrace();
		closeSocket();
	}
}