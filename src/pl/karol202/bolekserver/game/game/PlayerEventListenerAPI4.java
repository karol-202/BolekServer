package pl.karol202.bolekserver.game.game;

import java.util.List;

class PlayerEventListenerAPI4 extends PlayerEventListenerAPI3
{
	PlayerEventListenerAPI4(Player player, PlayerAdapter adapter)
	{
		super(player, adapter);
	}
	
	@Override
	public void onWin(boolean ministersWin, WinCause cause, List<Player> ministers, List<Player> collaborators, Player bolek)
	{
		getAdapter().sendWinMessage(ministersWin, cause);
		getAdapter().sendCollaboratorsRevealmentMessage(ministers.stream(), collaborators.stream(), bolek);
		getAdapter().sendGameExitedMessage();
	}
	
	@Override
	public void onGameExited(Player exitedPlayer, List<Player> allPlayers, List<Player> ministers, List<Player> collaborators, Player bolek, boolean bolekKnows)
	{
		if(getPlayer() == exitedPlayer)
		{
			getAdapter().sendGameExitedMessage();
			getPlayer().reset();
		}
		else
		{
			getAdapter().sendPlayersUpdatedMessage(allPlayers.stream());
			if(getPlayer().isCollaborator() || (bolekKnows && getPlayer().isBolek()))
				getAdapter().sendCollaboratorsRevealmentMessage(ministers.stream(), collaborators.stream(), bolek);
		}
	}
	
	@Override
	public void onSpectatingStart(Spectator spectator, List<Player> allPlayers, boolean secretImages, List<Player> ministers, List<Player> collaborators, Player bolek, boolean bolekKnows)
	{
		getAdapter().sendPlayersUpdatedMessage(allPlayers.stream());
		if(getPlayer().isCollaborator() || (bolekKnows && getPlayer().isBolek()))
			getAdapter().sendCollaboratorsRevealmentMessage(ministers.stream(), collaborators.stream(), bolek);
	}
}