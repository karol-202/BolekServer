package pl.karol202.bolekserver.game;

public class ErrorReference<E extends Enum>
{
	private E error;
	
	public E getError()
	{
		return error;
	}
	
	public void setError(E error)
	{
		this.error = error;
	}
}
