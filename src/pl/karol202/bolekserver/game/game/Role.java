package pl.karol202.bolekserver.game.game;

public enum Role
{
	MINISTER, COLLABORATOR, BOLEK;
	
	public static int getNumberOfCollaborators(int totalPlayers)
	{
		if(totalPlayers <= 5) return 1;
		else if(totalPlayers <= 7) return 2;
		else return 3;
	}
}