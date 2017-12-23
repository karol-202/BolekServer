package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.ActionsQueue;
import pl.karol202.bolekserver.server.Utils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Game
{
	private List<Player> players;
	private boolean gameEnd;
	
	private Player president;
	private Player primeMinister;
	private boolean extraordinaryPresident;
	private boolean doNotChangePresident;
	
	//Failed voting
	private int pollIndex;
	private Player previousPresident;
	private Player previousPrimeMinister;
	
	private boolean choosingPrimeMinister;
	private boolean votingOnPrimeMinister;
	private Player votedPrimeMinister;
	private Map<Player, Boolean> votes;
	
	private Stack<Act> incomingActs;
	private int passedLustrationActs;
	private int passedAntilustrationActs;
	
	private boolean choosingActsPresident;
	private boolean choosingActsPrimeMinister;
	private boolean vetoing;
	private Act[] currentActs;
	
	private boolean checkingPlayerByPresident;
	private boolean checkingPlayerOrActsByPresident;
	private boolean choosingPresident;
	private boolean lustratingByPresident;
	
	private ActionsQueue<GameAction> actionsQueue;
	private OnGameEndListener onGameEndListener;
	
	public Game(List<Player> players)
	{
		this.players = new ArrayList<>(players);
		this.players.forEach(p -> p.init(this));
		this.incomingActs = new Stack<>();
		
		this.actionsQueue = new ActionsQueue<>();
	}
	
	private void removePlayer(Player player)
	{
		players.remove(player);
		//TODO Wysłać powiadomienie o aktualizacji listy graczy
		//TODO Sprawdzić pod kątek bycia prezydentem
		if(player == primeMinister) primeMinister = null;
		else if(player == previousPrimeMinister) previousPrimeMinister = null;
	}
	
	void startGame()
	{
		broadcastGameStart();
		assignRoles();
		assignPresidentPosition(getRandomPlayer());
		letPresidentChoosePrimeMinister();
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
	
	void nextTurn()
	{
		assignNextPresident();
		endPrimeMinisterTerm();
		letPresidentChoosePrimeMinister();
	}
	
	private void refillIncomingActsStack()
	{
		if(incomingActs.size() >= 3) return;
		incomingActs.clear();
		for(int i = 0; i < Act.LUSTRATION.getCount(); i++) incomingActs.push(Act.LUSTRATION);
		for(int i = 0; i < Act.ANTILUSTRATION.getCount(); i++) incomingActs.push(Act.ANTILUSTRATION);
		Collections.shuffle(incomingActs);
		
		broadcastStackRefill();
	}
	
	private void assignNextPresident()
	{
		if(doNotChangePresident)
		{
			doNotChangePresident = false;
			return;
		}
		//                            Nie powinno mieć miejsca
		if(!extraordinaryPresident || previousPresident == null) assignPresidentPosition(getNextPlayer(president));
		else assignPresidentPosition(getNextPlayer(previousPresident));
		extraordinaryPresident = false;
	}
	
	private void assignPresidentPosition(Player president)
	{
		previousPresident = this.president;
		this.president = president;
		broadcastPresidentAssignment();
	}
	
	private void endPrimeMinisterTerm()
	{
		this.previousPrimeMinister = primeMinister;
		primeMinister = null;
		broadcastPrimeMinisterAssignment();
	}
	
	private void letPresidentChoosePrimeMinister()
	{
		choosingPrimeMinister = true;
		sendPrimeMinisterChooseRequest();
	}
	
	private boolean canPlayerBePrimeMinister(Player player)
	{
		return player != president && player != previousPresident && player != previousPrimeMinister;
	}
	
	boolean choosePrimeMinister(Player sender, String name)
	{
		if(!choosingPrimeMinister || sender != president) return false;
		Player player = getPlayerByName(name);
		if(player == null || !canPlayerBePrimeMinister(player)) return false;
		
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
		resetPollIndex();
		if(primeMinister.isBolek() && isBolekCausingCollaboratorsWin()) collaboratorsWin(WinCause.BOLEK);
		else letPresidentChooseActs();
	}
	
	private void assignPrimeMinisterPosition(Player primeMinister)
	{
		this.primeMinister = primeMinister;
		broadcastPrimeMinisterAssignment();
	}
	
	private void resetPollIndex()
	{
		pollIndex = 0;
		broadcastPollIndexChange();
	}
	
	private void primeMinisterNotChosen()
	{
		incrementPollIndex();
		addActionAndReturnImmediately(new GameActionNextTurn());
	}
	
	private void incrementPollIndex()
	{
		pollIndex++;
		broadcastPollIndexChange();
		if(pollIndex >= 3) pollIndexOverflow();
	}
	
	private void pollIndexOverflow()
	{
		passRandomAct();
		previousPresident = null;
		previousPrimeMinister = null;
		resetPollIndex();
	}
	
	private void passRandomAct()
	{
		broadcastRandomActPassed();
		currentActs = new Act[] { incomingActs.pop() };
		refillIncomingActsStack();
		passAct();
		currentActs = null;
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
		currentActs = new Act[] { incomingActs.pop(), incomingActs.pop(), incomingActs.pop() };
		refillIncomingActsStack();
		sendChooseActsRequestToPresident();
		broadcastPresidentChoosingActs();
	}
	
	boolean dismissActByPresident(Player sender, Act act)
	{
		if(!choosingActsPresident || sender != president || !dismissAct(act)) return false;
		choosingActsPresident = false;
		choosingActsPrimeMinister = true;
		
		if(isVetoApplicable()) sendChooseActsOrVetoRequestToPrimeMinister();
		else sendChooseActsRequestToPrimeMinister();
		broadcastPrimeMinisterChoosingActs();
		return true;
	}
	
	boolean dismissActByPrimeMinister(Player sender, Act act)
	{
		if(!choosingActsPrimeMinister || sender != primeMinister || !dismissAct(act) || currentActs.length != 1) return false;
		passAct();
		choosingActsPrimeMinister = false;
		currentActs = null;
		return true;
	}
	
	boolean requestVetoByPrimeMinister(Player sender)
	{
		if(!choosingActsPrimeMinister || sender != primeMinister || !isVetoApplicable()) return false;
		choosingActsPrimeMinister = false;
		vetoing = true;
		
		broadcastVetoRequest();
		return true;
	}
	
	boolean respondOnVeto(Player sender, boolean accept)
	{
		if(!vetoing || sender != president) return false;
		vetoing = false;
		
		broadcastVetoResponse(accept);
		if(accept) vetoAccepted();
		else vetoRejected();
		return true;
	}
	
	private void vetoAccepted()
	{
		currentActs = null;
		incrementPollIndex();
		addActionAndReturnImmediately(new GameActionNextTurn());
	}
	
	private void vetoRejected()
	{
		choosingActsPrimeMinister = true;
		sendChooseActsRequestToPrimeMinister();
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
	
	private void passAct()
	{
		if(currentActs == null || currentActs.length == 0) return;
		Act lastAct = currentActs[0];
		if(lastAct == Act.LUSTRATION) passedLustrationActs++;
		else if(lastAct == Act.ANTILUSTRATION) passedAntilustrationActs++;
		else return;
		
		broadcastActPassed();
		checkForWin();
		if(gameEnd) return;
		
		if(lastAct == Act.ANTILUSTRATION) antilustrationActPassed();
		else addActionAndReturnImmediately(new GameActionNextTurn());
	}
	
	private void checkForWin()
	{
		if(didMinistersWinByActs()) ministersWin(WinCause.ACTS_PASSED);
		else if(didCollaboratorsWinByActs()) collaboratorsWin(WinCause.ACTS_PASSED);
	}
	
	private void ministersWin(WinCause cause)
	{
		broadcastMinistersWin(cause);
		gameEnd();
	}
	
	private void collaboratorsWin(WinCause cause)
	{
		broadcastCollaboratorsWin(cause);
		gameEnd();
	}
	
	private void gameEnd()
	{
		gameEnd = true;
		if(onGameEndListener != null) onGameEndListener.onGameEnd();
	}
	
	private void antilustrationActPassed()
	{
		if(canPresidentCheckPlayer()) letPresidentCheckPlayer();
		else if(canPresidentCheckPlayerOrActs()) letPresidentCheckPlayerOrActs();
		else if(canPresidentChoosePresident()) letPresidentChooseNewPresident();
		else if(canPresidentLustratePlayer()) letPresidentLustratePlayer();
	}
	
	private void letPresidentCheckPlayer()
	{
		checkingPlayerByPresident = true;
		broadcastPresidentCheckingPlayer();
		sendPlayerCheckRequestToPresident();
	}
	
	boolean checkPlayerByPresident(Player sender, String checkedPlayer)
	{
		if(!checkingPlayerByPresident || sender != president) return false;
		Player player = getPlayerByName(checkedPlayer);
		if(player == null || player.wasChecked()) return false;
		
		checkingPlayerByPresident = false;
		player.setChecked();
		
		sendPlayerCheckingResultToPresident(player.isCollaborator() || player.isBolek() ? 1 : 0);
		broadcastPresidentCheckedPlayerMessage(player);
		addActionAndReturnImmediately(new GameActionNextTurn());
		return true;
	}
	
	private void letPresidentCheckPlayerOrActs()
	{
		checkingPlayerOrActsByPresident = true;
		broadcastPresidentCheckingPlayerOrActsMessage();
		sendPlayerOrActsCheckingChooseRequestToPresident();
	}
	
	boolean choosePlayerOrActsCheckingByPresident(Player sender, int choice)
	{
		if(!checkingPlayerOrActsByPresident || sender != president || !(choice == 0 || choice == 1)) return false;
		
		checkingPlayerOrActsByPresident = false;
		if(choice == 0) letPresidentCheckPlayer();
		else checkActsByPresident();
		return true;
	}
	
	private void checkActsByPresident()
	{
		Act[] acts = new Act[] { incomingActs.peek(), incomingActs.peek(), incomingActs.peek() };
		sendActsCheckingResultMessageToPresident(acts);
		broadcastPresidentCheckedActsMessage();
		addActionAndReturnImmediately(new GameActionNextTurn());
	}
	
	private void letPresidentChooseNewPresident()
	{
		choosingPresident = true;
		broadcastPresidentChoosingPresidentMessage();
		sendChoosePresidentRequestToPresident();
	}
	
	boolean choosePresident(Player sender, String president)
	{
		if(!choosingPresident || sender != this.president) return false;
		Player player = getPlayerByName(president);
		if(player == null) return false;
		
		choosingPresident = false;
		assignPresidentPosition(player);
		extraordinaryPresident = true;
		doNotChangePresident = true;
		
		addActionAndReturnImmediately(new GameActionNextTurn());
		return true;
	}
	
	private void letPresidentLustratePlayer()
	{
		lustratingByPresident = true;
		broadcastPresidentLustratingMessage();
		sendLustrationRequestToPresident();
	}
	
	boolean lustrateByPresident(Player sender, String playerName)
	{
		if(!lustratingByPresident || sender != president) return false;
		Player player = getPlayerByName(playerName);
		if(player == null || player == president) return false;
		
		lustratingByPresident = false;
		sendYouAreLustratedMessage(player);
		//Wysłać powiadomienie o wyjściu z gry do wyeliminowanego
		removePlayer(player);
		
		broadcastPresidentLustratedMessage(player);
		if(player.isBolek()) ministersWin(WinCause.BOLEK);
		else addActionAndReturnImmediately(new GameActionNextTurn());
		return true;
	}
	
	
	private boolean isBolekCausingCollaboratorsWin()
	{
		return passedAntilustrationActs >= 3;
	}
	
	private boolean isVetoApplicable()
	{
		return passedAntilustrationActs >= 5;
	}
	
	private boolean didMinistersWinByActs()
	{
		return passedLustrationActs >= 5;
	}
	
	private boolean didCollaboratorsWinByActs()
	{
		return passedAntilustrationActs >= 6;
	}
	
	private boolean canPresidentCheckPlayer()
	{
		return passedAntilustrationActs == 1;
	}
	
	private boolean canPresidentCheckPlayerOrActs()
	{
		return passedAntilustrationActs == 2;
	}
	
	private boolean canPresidentChoosePresident()
	{
		return passedAntilustrationActs == 3;
	}
	
	private boolean canPresidentLustratePlayer()
	{
		return passedAntilustrationActs == 4 || passedAntilustrationActs == 5;
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
		Predicate<Player> filter = players.size() > 6 ? Player::isCollaborator :
														p -> p.isCollaborator() || p.isBolek();
		Stream<Player> targetPlayers = players.stream().filter(filter);
		
		Stream<Player> collaborators = players.stream().filter(Player::isCollaborator);
		Player bolek = players.stream().filter(Player::isBolek).findAny().orElse(null);
		if(bolek == null) return;
		
		targetPlayers.forEach(p -> p.sendCollaboratorsRevealmentMessage(collaborators, bolek));
	}
	
	private void broadcastStackRefill()
	{
		players.forEach(p -> p.sendStackRefillMessage(Act.getTotalActsCount()));
	}
	
	private void broadcastPresidentAssignment()
	{
		players.forEach(p -> p.sendPresidentAssignmentMessage(president));
	}
	
	private void sendPrimeMinisterChooseRequest()
	{
		Stream<Player> candidates = players.stream().filter(this::canPlayerBePrimeMinister);
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
	
	private void broadcastPollIndexChange()
	{
		players.forEach(p -> p.sendPollIndexChangeMessage(pollIndex));
	}
	
	private void broadcastRandomActPassed()
	{
		players.forEach(Player::sendRandomActMessage);
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
	
	private void sendChooseActsOrVetoRequestToPrimeMinister()
	{
		primeMinister.sendChooseActsOrVetoRequestToPrimeMinister(currentActs);
	}
	
	private void broadcastPrimeMinisterChoosingActs()
	{
		players.stream().filter(p -> p != primeMinister).forEach(Player::sendPrimeMinisterChoosingActsMessage);
	}
	
	private void broadcastVetoRequest()
	{
		players.forEach(Player::sendVetoRequest);
	}
	
	private void broadcastVetoResponse(boolean accepted)
	{
		players.forEach(p -> p.sendVetoResponseMessage(accepted));
	}
	
	private void broadcastActPassed()
	{
		players.forEach(p -> p.sendActPassedMessage(passedLustrationActs, passedAntilustrationActs));
	}
	
	private void broadcastMinistersWin(WinCause cause)
	{
		players.forEach(p -> {
			if(p.getRole() == Role.MINISTER) p.sendWinMessage(cause);
			else p.sendLossMessage(cause);
			p.reset();
		});
	}
	
	private void broadcastCollaboratorsWin(WinCause cause)
	{
		players.forEach(p -> {
			if(p.getRole() != Role.MINISTER) p.sendWinMessage(cause);
			else p.sendLossMessage(cause);
			p.reset();
		});
	}
	
	private void broadcastPresidentCheckingPlayer()
	{
		players.forEach(Player::sendPresidentCheckingPlayerMessage);
	}
	
	private void sendPlayerCheckRequestToPresident()
	{
		Stream<Player> checkablePlayers = players.stream().filter(p -> !p.wasChecked());
		president.sendPlayerCheckRequestToPresident(checkablePlayers);
	}
	
	private void sendPlayerCheckingResultToPresident(int result)
	{
		president.sendPlayerCheckingResultToPresident(result);
	}
	
	private void broadcastPresidentCheckedPlayerMessage(Player checkedPlayer)
	{
		players.forEach(p -> p.sendPresidentCheckedPlayerMessage(checkedPlayer));
	}
	
	private void broadcastPresidentCheckingPlayerOrActsMessage()
	{
		players.forEach(Player::sendPresidentCheckingPlayerOrActsMessage);
	}
	
	private void sendPlayerOrActsCheckingChooseRequestToPresident()
	{
		president.sendPlayerOrActsCheckingChooseRequestToPresident();
	}
	
	private void sendActsCheckingResultMessageToPresident(Act[] acts)
	{
		president.sendActsCheckingResultMessageToPresident(acts);
	}
	
	private void broadcastPresidentCheckedActsMessage()
	{
		players.forEach(Player::sendPresidentCheckedActsMessage);
	}
	
	private void broadcastPresidentChoosingPresidentMessage()
	{
		players.forEach(Player::sendPresidentChoosingPresidentMessage);
	}
	
	private void sendChoosePresidentRequestToPresident()
	{
		president.sendChoosePresidentRequestToPresident();
	}
	
	private void broadcastPresidentLustratingMessage()
	{
		players.forEach(Player::sendPresidentLustratingMessage);
	}
	
	private void sendLustrationRequestToPresident()
	{
		president.sendLustrationRequestToPresident();
	}
	
	private void sendYouAreLustratedMessage(Player lustrated)
	{
		lustrated.sendYouAreLustratedMessage();
	}
	
	private void broadcastPresidentLustratedMessage(Player player)
	{
		players.forEach(p -> p.sendPresidentLustratedMessage(player, player.isBolek()));
	}
	
	public void executeActions()
	{
		while(actionsQueue.hasUnprocessedActions())
		{
			GameAction action = actionsQueue.peekAction();
			Object result = action.execute(this);
			actionsQueue.setResult(action, result);
		}
	}
	
	public <R> R addActionAndWaitForResult(GameAction<R> action)
	{
		if(action == null) return null;
		actionsQueue.addAction(action);
		
		do Thread.yield();
		while(!actionsQueue.isResultSetForAction(action));
		
		Object result = actionsQueue.getResult(action);
		actionsQueue.removeAction(action);
		return (R) result;
	}
	
	public void addActionAndReturnImmediately(GameAction action)
	{
		if(action != null) actionsQueue.addAction(action);
	}
	
	//TDOD Użyć tej metody
	public void setOnGameEndListener(OnGameEndListener onGameEndListener)
	{
		this.onGameEndListener = onGameEndListener;
	}
}