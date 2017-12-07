package pl.karol202.bolekserver.server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Utils
{
	public static int readInt(InputStream inputStream) throws IOException
	{
		byte[] bytes = new byte[4];
		int length = inputStream.read(bytes);
		if(length != 4) return -1;
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		return buffer.getInt();
	}
}