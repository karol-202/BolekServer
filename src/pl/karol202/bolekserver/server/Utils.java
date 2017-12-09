package pl.karol202.bolekserver.server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	
	public static byte[] writeInt(int value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(value);
		return buffer.array();
	}
	
	public static <T> Collector<T, ?, Stream<T>> toShuffledStream()
	{
		return Collectors.collectingAndThen(Collectors.toList(), list -> {
			Collections.shuffle(list);
			return list.stream();
		});
	}
}