package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.ActionsQueue;
import pl.karol202.bolekserver.server.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Game
{
	private List<Player> players;
	
	private ActionsQueue<GameAction> actionsQueue;
	
	public Game(List<Player> players)
	{
		this.players = new ArrayList<>(players);
		
		this.actionsQueue = new ActionsQueue<>();
	}
	
	void startGame()
	{
		assignRoles();
		broadcastGameStart();
	}
	
	private void assignRoles()
	{
		Player bolek = getRandomPlayerWithoutRole();
		if(bolek != null) bolek.assignRole(Role.BOLEK);
		
		getRandomPlayersWithoutRole().limit(Role.getNumberOfCollaborators(players.size()))
									 .forEach(p -> p.assignRole(Role.COLLABORATOR));
		
		players.stream().filter(p -> p.getRole() == null).forEach(p -> p.assignRole(Role.MINISTER));
		
		sendRolesMessagesOut();
		sendCollaboratorsRevealmentMessages();
	}
	
	private Player getRandomPlayerWithoutRole()
	{
		return getRandomPlayersWithoutRole().findFirst().orElse(null);
	}
	
	private Stream<Player> getRandomPlayersWithoutRole()
	{
		return players.stream().filter(p -> p.getRole() == null).collect(Utils.toShuffledStream());
	}
	
	private void broadcastGameStart()
	{
		players.forEach(p -> p.sendGameStartMessage(players.stream()));
	}
	
	private void sendRolesMessagesOut()
	{
		players.forEach(p -> p.sendRoleAssignmentMessage(p.getRole()));
	}
	
	private void sendCollaboratorsRevealmentMessages()
	{
		Stream<Player> collaborators = players.stream().filter(p -> p.getRole() == Role.COLLABORATOR);
		players.forEach(p -> p.sendCollaboratorsRevealmentMessage(collaborators));
	}
	
	public void executeActions()
	{
		while(!actionsQueue.isEmpty())
		{
			GameAction action = actionsQueue.pollAction();
			Object result = action.execute(this);
			actionsQueue.setResult(action, result);
		}
	}
	
	public <R> R addActionAndWaitForResult(GameAction<R> action)
	{
		if(action == null) return null;
		actionsQueue.addAction(action);
		
		Object result;
		do result = actionsQueue.getResult(action);
		while(result == null);
		
		return (R) result;
	}
	
	public void addActionAndReturnImmediately(GameAction action)
	{
		if(action != null) actionsQueue.addAction(action);
	}
}