package pl.karol202.bolekserver.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LoggerFormatter extends Formatter
{
	private final Date DATE = new Date();

	public synchronized String format(LogRecord record)
	{
		DATE.setTime(record.getMillis());
		String message = record.getMessage();
		String throwable = "";
		if(record.getThrown() != null)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println();
			record.getThrown().printStackTrace(pw);
			pw.close();
			throwable = sw.toString();
		}
		return String.format("%s %s %s %s%n",
				DATE.toString(),
				record.getLevel().getLocalizedName(),
				message,
				throwable);
	}
}
