package pl.karol202.bolekserver.server.outputpacket;

import pl.karol202.bolekserver.server.DataBundle;

public class OutputPacketFailure implements OutputPacket
{
	public static final int PROBLEM_SERVER_INVALID_NAME = 1;
	public static final int PROBLEM_SERVER_TOO_MANY = 2;
	public static final int PROBLEM_USER_INVALID_NAME = 3;
	public static final int PROBLEM_USER_TOO_MANY = 4;
	public static final int PROBLEM_USER_NAME_BUSY = 5;
	public static final int PROBLEM_SERVER_CODE_INVALID = 6;
	
	private int problem;
	
	public OutputPacketFailure()
	{
		problem = 0;
	}
	
	public OutputPacketFailure(int problem)
	{
		this.problem = problem;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("problem", problem);
	}
	
	@Override
	public String getName()
	{
		return "FAILURE";
	}
	
	@Override
	public String toString()
	{
		return getClass().toString() + "(problem: " + problem + ")";
	}
}