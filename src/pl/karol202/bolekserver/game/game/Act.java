package pl.karol202.bolekserver.game.game;

public enum Act
{
	LUSTRATION(6),
	ANTILUSTRATION(11);
	
	private int count;
	
	Act(int count)
	{
		this.count = count;
	}
	
	int getCount()
	{
		return count;
	}
	
	public static Act getActByName(String act)
	{
		if(act.equals(LUSTRATION.name())) return LUSTRATION;
		else if(act.equals(ANTILUSTRATION.name())) return ANTILUSTRATION;
		else return null;
	}
	
	public static int getTotalActsCount()
	{
		int total = 0;
		for(Act act : values()) total += act.count;
		return total;
	}
}