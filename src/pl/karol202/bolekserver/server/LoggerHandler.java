package pl.karol202.bolekserver.server;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

class LoggerHandler extends StreamHandler
{
	LoggerHandler()
	{
		super();
		setOutputStream(System.out);
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
