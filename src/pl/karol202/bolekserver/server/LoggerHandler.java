package pl.karol202.bolekserver.server;

import java.util.logging.ConsoleHandler;

class LoggerHandler extends ConsoleHandler
{
	LoggerHandler()
	{
		super();
		setOutputStream(System.out);
		setFormatter(new LoggerFormatter());
	}
}
