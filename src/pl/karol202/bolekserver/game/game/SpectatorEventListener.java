package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.server.PlayerAdapterConnection;

abstract class SpectatorEventListener implements EventListener
{
	private Spectator spectator;
	private PlayerAdapter adapter;
	
	SpectatorEventListener(Spectator spectator, PlayerAdapter adapter)
	{
		this.spectator = spectator;
		this.adapter = adapter;
	}
	
	static SpectatorEventListener createSpectatorEventListener(Spectator spectator, PlayerAdapter playerAdapter)
	{
		if(!(playerAdapter instanceof PlayerAdapterConnection)) return null;
		PlayerAdapterConnection playerAdapterConnection = (PlayerAdapterConnection) playerAdapter;
		int api = playerAdapterConnection.getAPIVersion();
		switch(api)
		{
		case 4: return new SpectatorEventListenerAPI4(spectator, playerAdapter);
		case 5: return new SpectatorEventListenerAPI5(spectator, playerAdapter);
		}
		return null;
	}
	
	Spectator getSpectator()
	{
		return spectator;
	}
	
	PlayerAdapter getAdapter()
	{
		return adapter;
	}
}