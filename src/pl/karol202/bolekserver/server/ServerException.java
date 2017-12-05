package pl.karol202.bolekserver.server;

class ServerException extends RuntimeException
{
	ServerException(String message)
	{
		super(message);
	}
	
	ServerException(String message, Throwable cause)
	{
		super(message, cause);
	}
}