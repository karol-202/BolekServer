package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.ActionsQueue;
import pl.karol202.bolekserver.server.Utils;

import java.util.*;
import java.util.stream.Stream;

public class Game
{
	private List<Player> players;
	private Player president;
	private Player primeMinister;
	private Stack<Act> incomingActs;
	private int passedLustrationActs;
	private int passedAntilustrationActs;
	
	private boolean choosingPrimeMinister;
	private boolean votingOnPrimeMinister;
	private Player votedPrimeMinister;
	private Map<Player, Boolean> votes;
	private int failedVotings;
	
	private boolean choosingActsPresident;
	private boolean choosingActsPrimeMinister;
	private Act[] currentActs;
	
	private ActionsQueue<GameAction> actionsQueue;
	
	public Game(List<Player> players)
	{
		this.players = new ArrayList<>(players);
		this.incomingActs = new Stack<>();
		
		this.actionsQueue = new ActionsQueue<>();
	}
	
	void startGame()
	{
		broadcastGameStart();
		assignRoles();
		assignPresidentPosition(getRandomPlayer());
		letPresidentChoosePrimeMinister();
		refillIncomingActsStack();
	}
	
	private void assignRoles()
	{
		Player bolek = getRandomPlayerWithoutRole();
		if(bolek != null) bolek.assignRole(Role.BOLEK);
		
		getRandomPlayersWithoutRole().limit(Role.getNumberOfCollaborators(players.size())).forEach(p -> p.assignRole(Role.COLLABORATOR));
		
		players.stream().filter(p -> p.getRole() == null).forEach(p -> p.assignRole(Role.MINISTER));
		
		sendRoleAssignmentMessages();
		sendCollaboratorsRevealmentMessages();
	}
	
	private void assignPresidentPosition(Player president)
	{
		this.president = president;
		broadcastPresidentAssignment();
	}
	
	private void letPresidentChoosePrimeMinister()
	{
		choosingPrimeMinister = true;
		sendPrimeMinisterChooseRequest();
	}
	
	private void refillIncomingActsStack()
	{
		if(incomingActs.size() >= 3) return;
		incomingActs.clear();
		for(int i = 0; i < Act.LUSTRATION.getCount(); i++) incomingActs.push(Act.LUSTRATION);
		for(int i = 0; i < Act.ANTILUSTRATION.getCount(); i++) incomingActs.push(Act.ANTILUSTRATION);
		Collections.shuffle(incomingActs);
	}
	
	void nextTurn()
	{
		assignPresidentPosition(getNextPlayer(president));
		endPrimeMinisterTerm();
		letPresidentChoosePrimeMinister();
		refillIncomingActsStack();
	}
	
	private void endPrimeMinisterTerm()
	{
		if(primeMinister == null) return;
		primeMinister.endTerm();
		primeMinister = null;
		broadcastPrimeMinisterAssignment();
	}
	
	boolean choosePrimeMinister(Player sender, String name)
	{
		if(!choosingPrimeMinister || sender != president) return false;
		Player player = getPlayerByName(name);
		if(player == null || !player.canBePrimeMinisterNow()) return false;
		
		choosingPrimeMinister = false;
		votingOnPrimeMinister = true;
		votedPrimeMinister = player;
		votes = new HashMap<>();
		
		broadcastPrimeMinisterChosen(player);
		broadcastPrimeMinisterVotingRequest();
		return true;
	}
	
	boolean voteOnPrimeMinister(Player sender, boolean vote)
	{
		if(!votingOnPrimeMinister) return false;
		votes.put(sender, vote);
		checkIfVotingIsEnded();
		return true;
	}
	
	private void checkIfVotingIsEnded()
	{
		for(Player voter : players) if(!votes.containsKey(voter)) return;
		countVotes();
	}
	
	private void countVotes()
	{
		int upvotes = (int) votes.values().stream().filter(b -> b).count();
		int requiredVotes = (int) Math.ceil(votes.size() / 2f);
		boolean passed = upvotes >= requiredVotes;
		
		broadcastVotingResult(passed);
		if(passed) primeMinisterChosen();
		else primeMinisterNotChosen();
		endVoting();
	}
	
	private void primeMinisterChosen()
	{
		assignPrimeMinisterPosition(votedPrimeMinister);
		letPresidentChooseActs();
	}
	
	private void assignPrimeMinisterPosition(Player primeMinister)
	{
		this.primeMinister = primeMinister;
		broadcastPrimeMinisterAssignment();
		failedVotings = 0;
	}
	
	private void primeMinisterNotChosen()
	{
		failedVotings++;
		addActionAndReturnImmediately(new GameActionNextTurn());
	}
	
	private void endVoting()
	{
		votingOnPrimeMinister = false;
		votedPrimeMinister = null;
		votes = null;
	}
	
	private void letPresidentChooseActs()
	{
		choosingActsPresident = true;
		choosingPrimeMinister = false;
		//TODO Sprawdzić czy są jeszcze ustawy
		currentActs = new Act[] { incomingActs.pop(), incomingActs.pop(), incomingActs.pop() };
		sendChooseActsRequestToPresident();
		broadcastPresidentChoosingActs();
	}
	
	boolean dismissActByPresident(Player sender, Act act)
	{
		if(!choosingActsPresident || sender != president || !dismissAct(act)) return false;
		choosingActsPresident = false;
		choosingActsPrimeMinister = true;
		sendChooseActsRequestToPrimeMinister();
		broadcastPrimeMinisterChoosingActs();
		return true;
	}
	
	boolean dismissActByPrimeMinister(Player sender, Act act)
	{
		if(!choosingActsPrimeMinister || sender != primeMinister || !dismissAct(act) || currentActs.length != 1) return false;
		Act lastAct = currentActs[0];
		if(lastAct == Act.LUSTRATION) passedLustrationActs++;
		else if(lastAct == Act.ANTILUSTRATION) passedAntilustrationActs++;
		
		choosingActsPrimeMinister = false;
		currentActs = null;
		broadcastActPassed();
		return true;
	}
	
	private boolean dismissAct(Act actToDismiss)
	{
		int lustration = 0;
		int antilustration = 0;
		for(Act act : currentActs)
		{
			if(act == Act.LUSTRATION) lustration++;
			else if(act == Act.ANTILUSTRATION) antilustration++;
		}
		if(actToDismiss == Act.LUSTRATION && lustration-- == 0) return false;
		else if(actToDismiss == Act.ANTILUSTRATION && antilustration-- == 0) return false;
		currentActs = new Act[lustration + antilustration];
		for(int i = 0; i < lustration; i++) currentActs[i] = Act.LUSTRATION;
		for(int i = 0; i < antilustration; i++) currentActs[lustration + i] = Act.ANTILUSTRATION;
		return true;
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
	
	private Player getNextPlayer(Player player)
	{
		int playerNumber = players.indexOf(player);
		if(++playerNumber >= players.size()) playerNumber = 0;
		return players.get(playerNumber);
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
	
	private void broadcastPresidentAssignment()
	{
		players.forEach(p -> p.sendPresidentAssignmentMessage(president));
	}
	
	private void sendPrimeMinisterChooseRequest()
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
	
	private void broadcastVotingResult(boolean passed)
	{
		Stream<Player> upvoters = votes.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey);
		players.forEach(p -> p.sendVotingResultMessage(upvoters, votes.size(), passed));
	}
	
	private void broadcastPrimeMinisterAssignment()
	{
		players.forEach(p -> p.sendPrimeMinisterAssignmentMessage(primeMinister));
	}
	
	private void sendChooseActsRequestToPresident()
	{
		president.sendChooseActsRequestToPresident(currentActs);
	}
	
	private void broadcastPresidentChoosingActs()
	{
		players.stream().filter(p -> p != president).forEach(Player::sendPresidentChoosingActsMessage);
	}
	
	private void sendChooseActsRequestToPrimeMinister()
	{
		primeMinister.sendChooseActsRequestToPrimeMinister(currentActs);
	}
	
	private void broadcastPrimeMinisterChoosingActs()
	{
		players.stream().filter(p -> p != primeMinister).forEach(Player::sendPrimeMinisterChoosingActsMessage);
	}
	
	private void broadcastActPassed()
	{
		players.forEach(p -> p.sendActPassedMessage(passedLustrationActs, passedAntilustrationActs));
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