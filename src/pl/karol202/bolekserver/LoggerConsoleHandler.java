package pl.karol202.bolekserver;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

class LoggerConsoleHandler extends StreamHandler
{
	LoggerConsoleHandler()
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
