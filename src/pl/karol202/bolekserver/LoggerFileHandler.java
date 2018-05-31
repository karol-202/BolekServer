package pl.karol202.bolekserver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

class LoggerFileHandler extends StreamHandler
{
	LoggerFileHandler() throws FileNotFoundException
	{
		super();
		setOutputStream(new FileOutputStream(Main.FILE_LOG, true));
		setFormatter(new LoggerFormatter());
		setLevel(Level.FINEST);
	}
	
	@Override
	public synchronized void publish(LogRecord record)
	{
		super.publish(record);
		flush();
	}
	
	@Override
	public synchronized void close() throws SecurityException
	{
		flush();
	}
}
