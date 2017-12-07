package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.server.Game;
import pl.karol202.bolekserver.game.server.GameServer;
import pl.karol202.bolekserver.game.server.GameServersManager;
import pl.karol202.bolekserver.server.inputpacket.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class Connection
{
	private GameServersManager gameServersManager;
	private GameServer gameServer;
	private Game game;
	
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
		if(packet instanceof InputControlPacket) ((InputControlPacket) packet).execute(gameServersManager);
		else if(packet instanceof InputServerPacket) ((InputServerPacket) packet).execute(gameServer);
		else if(packet instanceof InputGamePacket) ((InputGamePacket) packet).execute(game);
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
	
	private void exception(String message, Exception exception)
	{
		new ServerException(message, exception).printStackTrace();
		closeSocket();
	}
}