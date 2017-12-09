package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.ActionsQueue;
import pl.karol202.bolekserver.server.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Game
{
	private List<Player> players;
	private boolean choosingPrimeMinister;
	private boolean votingOnPrimeMinister;
	private Player votedPrimeMinister;
	
	private ActionsQueue<GameAction> actionsQueue;
	
	public Game(List<Player> players)
	{
		this.players = new ArrayList<>(players);
		
		this.actionsQueue = new ActionsQueue<>();
	}
	
	void startGame()
	{
		broadcastGameStart();
		assignRoles();
		assignPresidentPosition(getRandomPlayer());
	}
	
	void nextTurn()
	{
	
	}
	
	boolean choosePrimeMinister(String name)
	{
		if(!choosingPrimeMinister) return false;
		Player player = getPlayerByName(name);
		if(player == null || !player.canBePrimeMinisterNow()) return false;
		
		choosingPrimeMinister = false;
		votingOnPrimeMinister = true;
		votedPrimeMinister = player;
		
		broadcastPrimeMinisterChosen(player);
		broadcastPrimeMinisterVotingRequest();
		return true;
	}
	
	private void assignRoles()
	{
		Player bolek = getRandomPlayerWithoutRole();
		if(bolek != null) bolek.assignRole(Role.BOLEK);
		
		getRandomPlayersWithoutRole().limit(Role.getNumberOfCollaborators(players.size()))
									 .forEach(p -> p.assignRole(Role.COLLABORATOR));
		
		players.stream().filter(p -> p.getRole() == null).forEach(p -> p.assignRole(Role.MINISTER));
		
		sendRoleAssignmentMessages();
		sendCollaboratorsRevealmentMessages();
	}
	
	private void assignPresidentPosition(Player president)
	{
		president.assignPosition(Position.PRESIDENT);
		broadcastPresidentAssignment(president);
		
		letPresidentChoosePrimeMinister(president);
	}
	
	private void letPresidentChoosePrimeMinister(Player president)
	{
		choosingPrimeMinister = true;
		sendPrimeMinisterChooseRequest(president);
	}
	
	
	private Player getRandomPlayerWithoutRole()
	{
		return getRandomPlayersWithoutRole().findFirst().orElse(null);
	}
	
	private Stream<Player> getRandomPlayersWithoutRole()
	{
		return players.stream().filter(p -> p.getRole() == null).collect(Utils.toShuffledStream());
	}
	
	private Player getRandomPlayer()
	{
		Random random = new Random();
		return players.get(random.nextInt(players.size()));
	}
	
	private Player getPlayerByName(String name)
	{
		return players.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
	}
	
	private void broadcastGameStart()
	{
		players.forEach(p -> p.sendGameStartMessage(players.stream()));
	}
	
	private void sendRoleAssignmentMessages()
	{
		players.forEach(p -> p.sendRoleAssignmentMessage(p.getRole()));
	}
	
	private void sendCollaboratorsRevealmentMessages()
	{
		Stream<Player> collaborators = players.stream().filter(p -> p.getRole() == Role.COLLABORATOR);
		players.forEach(p -> p.sendCollaboratorsRevealmentMessage(collaborators));
	}
	
	private void broadcastPresidentAssignment(Player president)
	{
		players.forEach(p -> p.sendPresidentAssignmentMessage(president));
	}
	
	private void sendPrimeMinisterChooseRequest(Player president)
	{
		Stream<Player> candidates = players.stream().filter(Player::canBePrimeMinisterNow);
		president.sendPrimeMinisterChooseRequest(candidates);
	}
	
	private void broadcastPrimeMinisterChosen(Player primeMinister)
	{
		players.forEach(p -> p.sendPrimeMinisterChosenMessage(primeMinister));
	}
	
	private void broadcastPrimeMinisterVotingRequest()
	{
		players.forEach(Player::sendPrimeMinisterVotingRequest);
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