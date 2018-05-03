package pl.karol202.bolekserver.game.game;

import java.util.List;

class PlayerEventListenerAPI3 extends PlayerEventListenerAPI2
{
	PlayerEventListenerAPI3(Player player, PlayerAdapter adapter)
	{
		super(player, adapter);
	}
	
	@Override
	public void onWin(boolean ministersWin, WinCause cause, List<Player> ministers, List<Player> collaborators, Player bolek)
	{
		if(ministersWin && getPlayer().isMinister() || !ministersWin && !getPlayer().isMinister())
			getAdapter().sendWinMessage(ministersWin, cause);
		else getAdapter().sendLossMessage(cause);
		getAdapter().sendCollaboratorsRevealmentMessage(ministers.stream(), collaborators.stream(), bolek);
		getAdapter().sendGameExitedMessage();
	}
}