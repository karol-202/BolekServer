package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.game.manager.GameServersManager;
import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.game.server.User;
import pl.karol202.bolekserver.server.inputpacket.*;
import pl.karol202.bolekserver.server.outputpacket.OutputPacket;
import pl.karol202.bolekserver.server.outputpacket.OutputPacketEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection
{
	private GameServersManager gameServersManager;
	private GameServer gameServer;
	private Game game;
	private User user;
	private Player player;
	
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	Connection(GameServersManager gameServersManager)
	{
		this.gameServersManager = gameServersManager;
	}
	
	public boolean connect(Socket socket)
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
	
	public void run()
	{
		if(isConnected()) tryToListen();
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
		while(true)
		{
			InputPacket packet = receivePacket();
			if(packet != null) executePacket(packet);
			else break;
		}
	}
	
	private InputPacket receivePacket() throws IOException
	{
		int length = Utils.readInt(inputStream);
		if(length <= 0) return null;
		byte[] bytes = new byte[length];
		int bytesRead = inputStream.read(bytes);
		if(bytesRead != length) return null;
		
		return InputPacketFactory.createPacket(bytes);
	}
	
	private void executePacket(InputPacket packet)
	{
		if(packet instanceof InputControlPacket && gameServersManager != null)
			((InputControlPacket) packet).execute(this, gameServersManager);
		else if(packet instanceof InputServerPacket && gameServer != null)
			((InputServerPacket) packet).execute(this, gameServer);
		else if(packet instanceof InputGamePacket && game != null)
			((InputGamePacket) packet).execute(this, game);
	}
	
	public void sendPacket(OutputPacket packet)
	{
		try
		{
			writePacket(packet);
		}
		catch(IOException e)
		{
			exception("Cannot send packet.", e);
		}
	}
	
	private void writePacket(OutputPacket packet) throws IOException
	{
		byte[] bytes = OutputPacketEncoder.encodePacket(packet);
		if(bytes == null || bytes.length == 0 && !isConnected()) return;
		outputStream.write(Utils.writeInt(bytes.length));
		outputStream.write(bytes);
	}
	
	private void closeSocket()
	{
		if(!isConnected()) return;
		try
		{
			socket.close();
		}
		catch(IOException e)
		{
			new ServerException("Cannot close socket", e).printStackTrace();
		}
	}
	
	private boolean isConnected()
	{
		return socket.isConnected() && !socket.isClosed();
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