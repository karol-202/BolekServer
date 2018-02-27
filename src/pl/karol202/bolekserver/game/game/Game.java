package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.Looper;
import pl.karol202.bolekserver.game.Target;
import pl.karol202.bolekserver.server.Utils;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Game implements Target
{
	public enum UserChoosingError
	{
		OK, INVALID_USER, ERROR
	}
	
	private static final int MIN_PLAYERS = 2;
	
	private Looper looper;
	private List<Player> players;
	private int initialPlayersAmount;
	private boolean gameEnd;
	private boolean nextTurn;
	
	private Player president;
	private Player primeMinister;
	private boolean extraordinaryPresident;
	private boolean doNotChangePresident;
	private Player nextPresident;
	
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
	private boolean vetoApplicable;
	private boolean vetoing;
	private Act[] currentActs;
	
	private boolean checkingPlayerByPresident;
	private boolean checkingPlayerOrActsByPresident;
	private boolean choosingPresident;
	private boolean lustratingByPresident;
	
	private GameListener gameListener;
	
	public Game(Looper looper, List<Player> players)
	{
		this.looper = looper;
		this.players = new ArrayList<>(players);
		this.players.forEach(p -> p.init(this));
		this.initialPlayersAmount = players.size();
		this.incomingActs = new Stack<>();
	}
	
	void startGame()
	{
		broadcastGameStart();
		sendPlayersUpdatedMessage();
		assignRoles();
		assignPresidentPosition(getRandomPlayer());
		refillIncomingActsStack();
		letPresidentChoosePrimeMinister();
	}
	
	private void assignRoles()
	{
		Player bolek = getRandomPlayerWithoutRole();
		if(bolek != null) bolek.assignRole(Role.BOLEK);
		
		getRandomPlayersWithoutRole().limit(Role.getNumberOfCollaborators(initialPlayersAmount))
									 .forEach(p -> p.assignRole(Role.COLLABORATOR));
		
		getRandomPlayersWithoutRole().forEach(p -> p.assignRole(Role.MINISTER));
		
		sendRoleAssignmentMessages();
		sendCollaboratorsRevealmentMessagesToCollaborators();
	}
	
	void nextTurn()
	{
		if(gameEnd) return;
		nextTurn = false;
		
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
		if(!extraordinaryPresident || nextPresident == null) assignPresidentPosition(getNextPlayer(president));
		else assignPresidentPosition(nextPresident);
		extraordinaryPresident = false;
		nextPresident = null;
	}
	
	private void assignPresidentPosition(Player president)
	{
		previousPresident = this.president;
		this.president = president;
		broadcastPresidentAssignment();
	}
	
	private void endPrimeMinisterTerm()
	{
		if(primeMinister == null) return;
		this.previousPrimeMinister = primeMinister;
		primeMinister = null;
		broadcastPrimeMinisterAssignment();
	}
	
	private void letPresidentChoosePrimeMinister()
	{
		choosingPrimeMinister = true;
		sendPrimeMinisterChooseRequest(false);
	}
	
	private boolean canPlayerBePrimeMinister(Player player)
	{
		return player != president && player != previousPresident && player != previousPrimeMinister;
	}
	
	UserChoosingError choosePrimeMinister(Player sender, String name)
	{
		if(!choosingPrimeMinister || sender != president) return UserChoosingError.ERROR;
		Player player = getPlayerByName(name);
		if(player == null || !canPlayerBePrimeMinister(player)) return UserChoosingError.INVALID_USER;
		
		choosingPrimeMinister = false;
		votingOnPrimeMinister = true;
		votedPrimeMinister = player;
		votes = new HashMap<>();
		
		broadcastPrimeMinisterChosen(player);
		broadcastPrimeMinisterVotingRequest();
		return UserChoosingError.OK;
	}
	
	boolean voteOnPrimeMinister(Player sender, boolean vote)
	{
		if(!votingOnPrimeMinister || votes.containsKey(sender)) return false;
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
		float requiredVotes = votes.size() / 2f;
		boolean passed = upvotes > requiredVotes;
		
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
		int previousPollIndex = pollIndex;
		pollIndex = 0;
		if(previousPollIndex != pollIndex) broadcastPollIndexChange();
	}
	
	private void primeMinisterNotChosen()
	{
		incrementPollIndex();
		callNextTurn();
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
		passAct();
		if(!gameEnd) refillIncomingActsStack();
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
		vetoApplicable = isVetoApplicable();
		
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
		vetoApplicable = false;
		currentActs = null;
		return true;
	}
	
	boolean requestVetoByPrimeMinister(Player sender)
	{
		if(!choosingActsPrimeMinister || sender != primeMinister || !vetoApplicable) return false;
		choosingActsPrimeMinister = false;
		vetoApplicable = false;
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
		callNextTurn();
	}
	
	private void vetoRejected()
	{
		choosingActsPrimeMinister = true;
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
		else callNextTurn();
	}
	
	private void checkForWin()
	{
		if(didMinistersWinByActs()) ministersWin(WinCause.ACTS_PASSED);
		else if(didCollaboratorsWinByActs()) collaboratorsWin(WinCause.ACTS_PASSED);
	}
	
	private void ministersWin(WinCause cause)
	{
		broadcastMinistersWin(cause);
		sendCollaboratorsRevealmentMessagesToAll();
		broadcastGameExitedMessage();
		gameEnd();
	}
	
	private void collaboratorsWin(WinCause cause)
	{
		broadcastCollaboratorsWin(cause);
		sendCollaboratorsRevealmentMessagesToAll();
		broadcastGameExitedMessage();
		gameEnd();
	}
	
	private void gameEnd()
	{
		gameEnd = true;
		if(gameListener != null) gameListener.onGameEnd();
	}
	
	private void antilustrationActPassed()
	{
		if(canPresidentCheckPlayer()) letPresidentCheckPlayer();
		else if(canPresidentCheckPlayerOrActs()) letPresidentCheckPlayerOrActs();
		else if(canPresidentChoosePresident()) letPresidentChooseNewPresident();
		else if(canPresidentLustratePlayer()) letPresidentLustratePlayer();
		else callNextTurn();
	}
	
	private void letPresidentCheckPlayer()
	{
		checkingPlayerByPresident = true;
		broadcastPresidentCheckingPlayer();
		sendPlayerCheckRequestToPresident(false);
	}
	
	UserChoosingError checkPlayerByPresident(Player sender, String checkedPlayer)
	{
		if(!checkingPlayerByPresident || sender != president) return UserChoosingError.ERROR;
		Player player = getPlayerByName(checkedPlayer);
		if(player == null || player.wasChecked() || player == president) return UserChoosingError.INVALID_USER;
		
		checkingPlayerByPresident = false;
		player.setChecked();
		
		sendPlayerCheckingResultToPresident(player.isCollaborator() || player.isBolek() ? 1 : 0);
		broadcastPresidentCheckedPlayerMessage(player);
		callNextTurn();
		return UserChoosingError.OK;
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
		Act[] acts = new Act[] { incomingActs.get(incomingActs.size() - 1), incomingActs.get(incomingActs.size() - 2), incomingActs.get(incomingActs.size() - 3) };
		sendActsCheckingResultMessageToPresident(acts);
		broadcastPresidentCheckedActsMessage();
		callNextTurn();
	}
	
	private void letPresidentChooseNewPresident()
	{
		choosingPresident = true;
		broadcastPresidentChoosingPresidentMessage();
		sendChoosePresidentRequestToPresident(false);
	}
	
	UserChoosingError choosePresident(Player sender, String president)
	{
		if(!choosingPresident || sender != this.president) return UserChoosingError.ERROR;
		Player player = getPlayerByName(president);
		if(player == null || player == this.president) return UserChoosingError.INVALID_USER;
		
		choosingPresident = false;
		
		extraordinaryPresident = true;
		doNotChangePresident = true;
		nextPresident = getNextPlayer(this.president);
		assignPresidentPosition(player);
		
		callNextTurn();
		return UserChoosingError.OK;
	}
	
	private void letPresidentLustratePlayer()
	{
		lustratingByPresident = true;
		broadcastPresidentLustratingMessage();
		sendLustrationRequestToPresident(false);
	}
	
	UserChoosingError lustrateByPresident(Player sender, String playerName)
	{
		if(!lustratingByPresident || sender != president) return UserChoosingError.ERROR;
		Player player = getPlayerByName(playerName);
		if(player == null || player == president) return UserChoosingError.INVALID_USER;
		
		lustratingByPresident = false;
		sendYouAreLustratedMessage(player);
		sendGameExitedMessage(player);
		broadcastPresidentLustratedMessage(player);
		removePlayer(player);
		
		if(player.isBolek()) ministersWin(WinCause.BOLEK);
		else callNextTurn();
		return UserChoosingError.OK;
	}
	
	void exitGame(Player player)
	{
		if(player == null || !players.contains(player)) return;
		removePlayer(player);
		sendGameExitedMessage(player);
	}
	
	private void removePlayer(Player player)
	{
		if(player == previousPresident) previousPresident = null;
		if(player == previousPrimeMinister) previousPrimeMinister = null;
		if(player == nextPresident) nextPresident = getNextPlayer(nextPresident);
		
		players.remove(player);
		
		if(choosingPrimeMinister) sendPrimeMinisterChooseRequest(true);
		else if(checkingPlayerByPresident) sendPlayerCheckRequestToPresident(true);
		else if(choosingPresident) sendChoosePresidentRequestToPresident(true);
		else if(lustratingByPresident) sendLustrationRequestToPresident(true);
		
		sendPlayersUpdatedMessage();
		
		if(player == president) resetTurn();
		if(votingOnPrimeMinister)
		{
			if(player == votedPrimeMinister) resetTurn();
			else
			{
				votes.remove(player);
				checkIfVotingIsEnded();
			}
		}
		else if(choosingActsPresident && player == primeMinister) resetTurn();
		else if(choosingActsPrimeMinister && player == primeMinister) resetTurn();
		
		gameListener.onPlayerLeaveGame(player);
		if(players.size() < MIN_PLAYERS) tooFewPlayers();
	}
	
	private void resetTurn()
	{
		resetState();
		callNextTurn();
	}
	
	private void resetState()
	{
		choosingPrimeMinister = false;
		votingOnPrimeMinister = false;
		votedPrimeMinister = null;
		votes = null;
		
		choosingActsPresident = false;
		choosingActsPrimeMinister = false;
		vetoing = false;
		currentActs = null;
		
		checkingPlayerByPresident = false;
		checkingPlayerOrActsByPresident = false;
		choosingPresident = false;
		lustratingByPresident = false;
	}
	
	private void tooFewPlayers()
	{
		broadcastTooFewPlayersMessage();
		gameEnd();
	}
	
	private void callNextTurn()
	{
		if(nextTurn) return;
		nextTurn = true;
		addActionAndReturnImmediately(new GameActionNextTurn());
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
		return passedAntilustrationActs == 1;// && initialPlayersAmount > 8;
	}
	
	private boolean canPresidentCheckPlayerOrActs()
	{
		return passedAntilustrationActs == 2;// && initialPlayersAmount > 6;
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
		int playerId = players.indexOf(player);
		if(++playerId >= players.size()) playerId = 0;
		return players.get(playerId);
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
	
	private void sendCollaboratorsRevealmentMessagesToCollaborators()
	{
		Predicate<Player> filter = players.size() > 6 ? Player::isCollaborator :
														p -> p.isCollaborator() || p.isBolek();
		Stream<Player> targetPlayers = players.stream().filter(filter);
		
		sendCollaboratorsRevealmentMessagesTo(targetPlayers);
	}
	
	private void sendCollaboratorsRevealmentMessagesToAll()
	{
		sendCollaboratorsRevealmentMessagesTo(players.stream());
	}
	
	private void sendCollaboratorsRevealmentMessagesTo(Stream<Player> targetPlayers)
	{
		Supplier<Stream<Player>> collaboratorsSupplier = () -> players.stream().filter(Player::isCollaborator);
		Player bolek = players.stream().filter(Player::isBolek).findAny().orElse(null);
		if(bolek == null) return;
		
		targetPlayers.forEach(p -> p.sendCollaboratorsRevealmentMessage(collaboratorsSupplier.get(), bolek));
	}
	
	private void broadcastStackRefill()
	{
		players.forEach(p -> p.sendStackRefillMessage(Act.getTotalActsCount()));
	}
	
	private void broadcastPresidentAssignment()
	{
		players.forEach(p -> p.sendPresidentAssignmentMessage(president));
	}
	
	private void sendPrimeMinisterChooseRequest(boolean update)
	{
		Stream<Player> candidates = players.stream().filter(this::canPlayerBePrimeMinister);
		president.sendPrimeMinisterChooseRequest(update, candidates);
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
		Supplier<Stream<Player>> upvotersSupplier = () -> votes.entrySet().stream().filter(Map.Entry::getValue)
																				   .map(Map.Entry::getKey);
		players.forEach(p -> p.sendVotingResultMessage(upvotersSupplier.get(), votes.size(), passed));
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
		players.stream().filter(p -> p != president).forEach(Player::sendPresidentCheckingPlayerMessage);
	}
	
	private void sendPlayerCheckRequestToPresident(boolean update)
	{
		Stream<Player> checkablePlayers = players.stream().filter(p -> !p.wasChecked() && p != president);
		president.sendPlayerCheckRequestToPresident(update, checkablePlayers);
	}
	
	private void sendPlayerCheckingResultToPresident(int result)
	{
		president.sendPlayerCheckingResultToPresident(result);
	}
	
	private void broadcastPresidentCheckedPlayerMessage(Player checkedPlayer)
	{
		players.stream().filter(p -> p != president).forEach(p -> p.sendPresidentCheckedPlayerMessage(checkedPlayer));
	}
	
	private void broadcastPresidentCheckingPlayerOrActsMessage()
	{
		players.stream().filter(p -> p != president).forEach(Player::sendPresidentCheckingPlayerOrActsMessage);
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
		players.stream().filter(p -> p != president).forEach(Player::sendPresidentCheckedActsMessage);
	}
	
	private void broadcastPresidentChoosingPresidentMessage()
	{
		players.stream().filter(p -> p != president).forEach(Player::sendPresidentChoosingPresidentMessage);
	}
	
	private void sendChoosePresidentRequestToPresident(boolean update)
	{
		Stream<Player> availablePlayers = players.stream().filter(p -> p != president);
		president.sendChoosePresidentRequestToPresident(update, availablePlayers);
	}
	
	private void broadcastPresidentLustratingMessage()
	{
		players.stream().filter(p -> p != president).forEach(Player::sendPresidentLustratingMessage);
	}
	
	private void sendLustrationRequestToPresident(boolean update)
	{
		Stream<Player> availablePlayers = players.stream().filter(p -> p != president);
		president.sendLustrationRequestToPresident(update, availablePlayers);
	}
	
	private void sendYouAreLustratedMessage(Player lustrated)
	{
		lustrated.sendYouAreLustratedMessage();
	}
	
	private void broadcastPresidentLustratedMessage(Player player)
	{
		players.stream().filter(p -> p != player).forEach(p -> p.sendPresidentLustratedMessage(player, player.isBolek()));
	}
	
	private void broadcastGameExitedMessage()
	{
		players.forEach(Player::sendGameExitedMessage);
	}
	
	private void sendGameExitedMessage(Player player)
	{
		player.sendGameExitedMessage();
	}
	
	private void sendPlayersUpdatedMessage()
	{
		Supplier<Stream<Player>> playersSupplier = () -> players.stream();
		players.forEach(p -> p.sendPlayersUpdatedMessage(playersSupplier.get()));
	}
	
	private void broadcastTooFewPlayersMessage()
	{
		players.forEach(Player::sendTooFewPlayers);
	}
	
	public <R> R addActionAndWaitForResult(GameAction<R> action)
	{
		return looper.addActionAndWaitForResult(action, this);
	}
	
	public void addActionAndReturnImmediately(GameAction<?> action)
	{
		looper.addActionAndReturnImmediately(action, this);
	}
	
	public void setGameListener(GameListener gameListener)
	{
		this.gameListener = gameListener;
	}
}