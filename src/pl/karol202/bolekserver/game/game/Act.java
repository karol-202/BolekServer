package pl.karol202.bolekserver.game.game;

public enum Act
{
	LUSTRATION(9),
	ANTILUSTRATION(9);
	
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
}