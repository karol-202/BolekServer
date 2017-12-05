package pl.karol202.bolekserver.server;

import pl.karol202.bolekserver.game.GameManager;
import pl.karol202.bolekserver.server.packet.Packet;
import pl.karol202.bolekserver.server.packet.PacketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class Connection
{
	private GameManager gameManager;
	
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	Connection(GameManager gameManager)
	{
		this.gameManager = gameManager;
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
		if(!isConnected()) return;
		tryToListen();
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
		byte[] bytes = new byte[1024];
		int length;
		while((length = inputStream.read(bytes)) != -1)
		{
			Packet packet = PacketFactory.createPacket(bytes, length);
			if(packet == null) invalidPacket(bytes);
			else packet.execute(gameManager);
		}
	}
	
	public void closeSocket()
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
	
	private void invalidPacket(byte[] bytes)
	{
		new ServerException("Invalid packet: " + bytes[0]).printStackTrace();
	}
	
	private void exception(String message, Exception exception)
	{
		new ServerException(message, exception).printStackTrace();
		closeSocket();
	}
}