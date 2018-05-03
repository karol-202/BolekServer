package pl.karol202.bolekserver.game.game;

import pl.karol202.bolekserver.game.Looper;
import pl.karol202.bolekserver.game.Target;
import pl.karol202.bolekserver.server.Utils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game implements Target
{
	public enum UserChoosingError
	{
		OK, INVALID_USER, ERROR
	}
	
	private static final int MIN_PLAYERS = 2;
	
	private Looper looper;
	private List<Consumer<EventListener>> events;
	private List<Player> players;
	private List<Player> initialPlayers;
	private List<Spectator> spectators;
	private boolean secretImages;
	private boolean debug;
	private boolean gameEnd;
	private boolean nextTurn;
	
	private Player president;
	private Player primeMinister;
	private boolean extraordinaryPresident;
	private boolean doNotChangePresident;
	private Player nextPresident;
	private boolean presidentNotChosen;
	
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
	private boolean checkingActsByPresident;
	private boolean choosingPresident;
	private boolean lustratingByPresident;
	
	private GameListener gameListener;
	
	public Game(Looper looper, List<Player> players, boolean secretImages, boolean debug)
	{
		this.looper = looper;
		this.events = new ArrayList<>();
		this.players = new ArrayList<>(players);
		this.players.forEach(p -> p.init(this));
		this.initialPlayers = new ArrayList<>(players);
		this.spectators = new ArrayList<>();
		this.secretImages = secretImages;
		this.debug = debug;
		this.incomingActs = new Stack<>();
	}
	
	void startGame()
	{
		onGameStart();
		assignRoles();
		assignPresidentPosition(getRandomPlayer());
		refillIncomingActsStack();
		letPresidentChoosePrimeMinister();
	}
	
	private void assignRoles()
	{
		Player bolek = getRandomPlayerWithoutRole();
		if(bolek != null) bolek.assignRole(Role.BOLEK);
		
		getRandomPlayersWithoutRole().limit(Role.getNumberOfCollaborators(initialPlayers.size()))
									 .forEach(p -> p.assignRole(Role.COLLABORATOR));
		
		getRandomPlayersWithoutRole().forEach(p -> p.assignRole(Role.MINISTER));
		
		onRolesAssign();
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
		for(int i = 0; i < Act.LUSTRATION.getCount() - passedLustrationActs; i++) incomingActs.push(Act.LUSTRATION);
		for(int i = 0; i < Act.ANTILUSTRATION.getCount() - passedAntilustrationActs; i++) incomingActs.push(Act.ANTILUSTRATION);
		Collections.shuffle(incomingActs);
		
		onStackRefill();
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
		if(!presidentNotChosen) previousPresident = this.president;
		presidentNotChosen = false;
		
		this.president = president;
		onPresidentAssignment();
	}
	
	private void endPrimeMinisterTerm()
	{
		if(primeMinister == null) return;
		this.previousPrimeMinister = primeMinister;
		primeMinister = null;
		onPrimeMinisterAssignment();
	}
	
	private void letPresidentChoosePrimeMinister()
	{
		choosingPrimeMinister = true;
		onPresidentChoosingPrimeMinister(false);
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
		
		onPrimeMinisterChosen();
		return UserChoosingError.OK;
	}
	
	boolean voteOnPrimeMinister(Player sender, boolean vote)
	{
		if(!votingOnPrimeMinister || sender == null || votes.containsKey(sender)) return false;
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
		float requiredVotes = (votes.size() / 2f) + 0.5f;
		boolean passed = upvotes >= requiredVotes;
		
		onVotingResult(passed);
		if(passed) primeMinisterChosen();
		else primeMinisterNotChosen();
		endVoting();
	}
	
	private void primeMinisterChosen()
	{
		assignPrimeMinisterPosition(votedPrimeMinister);
		resetPollIndex();
		if(primeMinister.isBolek() && isBolekCausingCollaboratorsWin()) win(false, WinCause.BOLEK);
		else letPresidentChooseActs();
	}
	
	private void assignPrimeMinisterPosition(Player primeMinister)
	{
		this.primeMinister = primeMinister;
		onPrimeMinisterAssignment();
	}
	
	private void resetPollIndex()
	{
		int previousPollIndex = pollIndex;
		pollIndex = 0;
		if(previousPollIndex != pollIndex) onPollIndexChange();
	}
	
	private void primeMinisterNotChosen()
	{
		incrementPollIndex();
		presidentNotChosen = true;
		callNextTurn();
	}
	
	private void incrementPollIndex()
	{
		pollIndex++;
		onPollIndexChange();
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
		onRandomActPass();
		currentActs = new Act[] { incomingActs.pop() };
		passAct(true);
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
		onPresidentChoosingActs();
	}
	
	boolean dismissActByPresident(Player sender, Act act)
	{
		if(!choosingActsPresident || sender != president || !dismissAct(act)) return false;
		choosingActsPresident = false;
		choosingActsPrimeMinister = true;
		vetoApplicable = isVetoApplicable();
		
		onPrimeMinisterChoosingActs();
		return true;
	}
	
	boolean dismissActByPrimeMinister(Player sender, Act act)
	{
		if(!choosingActsPrimeMinister || sender != primeMinister || !dismissAct(act) || currentActs.length != 1) return false;
		passAct(false);
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
		
		onVetoRequest();
		return true;
	}
	
	boolean respondOnVeto(Player sender, boolean accept)
	{
		if(!vetoing || sender != president) return false;
		vetoing = false;
		
		onVetoResponse(accept);
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
	
	private void passAct(boolean random)
	{
		if(currentActs == null || currentActs.length == 0) return;
		Act lastAct = currentActs[0];
		if(lastAct == Act.LUSTRATION) passedLustrationActs++;
		else if(lastAct == Act.ANTILUSTRATION) passedAntilustrationActs++;
		else return;
		
		onActPassed();
		checkForWin();
		if(gameEnd) return;
		
		if(lastAct == Act.ANTILUSTRATION && !random) antilustrationActPassed();
		else callNextTurn();
	}
	
	private void checkForWin()
	{
		if(didMinistersWinByActs()) win(true, WinCause.ACTS_PASSED);
		else if(didCollaboratorsWinByActs()) win(false, WinCause.ACTS_PASSED);
	}
	
	private void win(boolean ministers, WinCause cause)
	{
		onWin(ministers, cause);
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
		else if(canPresidentCheckActs()) letPresidentCheckActs();
		else if(canPresidentChoosePresident()) letPresidentChooseNewPresident();
		else if(canPresidentLustratePlayer()) letPresidentLustratePlayer();
		else callNextTurn();
	}
	
	private void letPresidentCheckPlayer()
	{
		checkingPlayerByPresident = true;
		onPresidentCheckingPlayer(false);
	}
	
	UserChoosingError checkPlayerByPresident(Player sender, String checkedPlayer)
	{
		if(!checkingPlayerByPresident || sender != president) return UserChoosingError.ERROR;
		Player player = getPlayerByName(checkedPlayer);
		if(player == null || player.wasChecked() || player == president) return UserChoosingError.INVALID_USER;
		
		checkingPlayerByPresident = false;
		player.setChecked();
		
		onPresidentCheckedPlayer(player, player.isCollaborator() || player.isBolek() ? 1 : 0);
		callNextTurn();
		return UserChoosingError.OK;
	}
	
	private void letPresidentCheckPlayerOrActs()
	{
		checkingPlayerOrActsByPresident = true;
		onPresidentCheckingPlayerOrActs();
	}
	
	boolean choosePlayerOrActsCheckingByPresident(Player sender, int choice)
	{
		if(!checkingPlayerOrActsByPresident || sender != president || !(choice == 0 || choice == 1)) return false;
		
		checkingPlayerOrActsByPresident = false;
		if(choice == 0) letPresidentCheckPlayer();
		else checkActsByPresident();
		return true;
	}
	
	private void letPresidentCheckActs()
	{
		checkingActsByPresident = true;
		onPresidentCheckingActs();
	}
	
	boolean checkActsByPresident(Player sender)
	{
		if(!checkingActsByPresident || sender != president) return false;
		
		//Resetting checkingActsByPresident moved to checkActsByPresident()
		checkActsByPresident();
		return true;
	}
	
	private void checkActsByPresident()
	{
		checkingActsByPresident = false;
		
		Act[] acts = new Act[] { incomingActs.get(incomingActs.size() - 1), incomingActs.get(incomingActs.size() - 2), incomingActs.get(incomingActs.size() - 3) };
		onPresidentCheckedActs(acts);
		callNextTurn();
	}

	private void letPresidentChooseNewPresident()
	{
		choosingPresident = true;
		onPresidentChoosingPresident(false);
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
		onPresidentLustratingPlayer(false);
	}
	
	UserChoosingError lustrateByPresident(Player sender, String playerName)
	{
		if(!lustratingByPresident || sender != president) return UserChoosingError.ERROR;
		Player player = getPlayerByName(playerName);
		if(player == null || player == president) return UserChoosingError.INVALID_USER;
		
		lustratingByPresident = false;
		onPresidentLustratedPlayer(player);
		removePlayer(player);
		
		if(player.isBolek()) win(true, WinCause.BOLEK);
		else callNextTurn();
		return UserChoosingError.OK;
	}
	
	void exitGame(Player player)
	{
		if(player == null || !players.contains(player)) return;
		removePlayer(player);
	}
	
	private void removePlayer(Player player)
	{
		if(player == previousPresident) previousPresident = null;
		if(player == previousPrimeMinister) previousPrimeMinister = null;
		if(player == nextPresident) nextPresident = getNextPlayer(nextPresident);
		
		players.remove(player);
		
		if(choosingPrimeMinister) onPresidentChoosingPrimeMinister(true);
		else if(checkingPlayerByPresident) onPresidentCheckingPlayer(true);
		else if(choosingPresident) onPresidentChoosingPresident(true);
		else if(lustratingByPresident) onPresidentLustratingPlayer(true);
		
		onGameExited(player);
		
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
		onTooFewPlayers();
		gameEnd();
	}
	
	private void callNextTurn()
	{
		if(nextTurn) return;
		nextTurn = true;
		addActionAndReturnImmediately(new GameActionNextTurn());
	}
	
	void spectate(Spectator spectator)
	{
		if(spectator == null || spectators.contains(spectator)) return;
		spectator.init(this);
		spectators.add(spectator);
		
		onSpectatingStart(spectator);
		for(Consumer<EventListener> event : events) event.accept(spectator);
	}
	
	void stopSpectating(Spectator spectator)
	{
		if(!spectators.contains(spectator)) return;
		spectators.remove(spectator);
		spectator.reset();
	}
	
	
	private boolean doesBolekKnowCollaborators()
	{
		return initialPlayers.size() <= 6;
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
		return passedAntilustrationActs == 1 && (initialPlayers.size() > 8 || debug);
	}
	
	private boolean canPresidentCheckPlayerOrActs()
	{
		return passedAntilustrationActs == 2 && (initialPlayers.size() > 6 || debug);
	}

	private boolean canPresidentCheckActs()
	{
		return passedAntilustrationActs == 2 && (initialPlayers.size() <= 6 && !debug);
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
	
	private void onGameStart()
	{
		List<Player> players = new ArrayList<>(this.players);
		fireEvent(el -> el.onGameStart(players, secretImages));
	}
	
	private void onRolesAssign()
	{
		List<Player> ministers = initialPlayers.stream().filter(Player::isMinister).collect(Collectors.toList());
		List<Player> collaborators = initialPlayers.stream().filter(Player::isCollaborator).collect(Collectors.toList());
		Player bolek = initialPlayers.stream().filter(Player::isBolek).findAny().orElse(null);
		if(bolek == null) return;
		
		fireEvent(el -> el.onRolesAssignment(ministers, collaborators, bolek, doesBolekKnowCollaborators()));
	}
	
	private void onStackRefill()
	{
		fireEvent(el -> el.onStackRefill(Act.getTotalActsCount()));
	}
	
	private void onPresidentAssignment()
	{
		Player president = this.president;
		fireEvent(el -> el.onPresidentAssignment(president));
	}
	
	private void onPrimeMinisterAssignment()
	{
		Player primeMinister = this.primeMinister;
		fireEvent(el -> el.onPrimeMinisterAssignment(primeMinister));
	}
	
	private void onPresidentChoosingPrimeMinister(boolean update)
	{
		Player president = this.president;
		List<Player> candidates = players.stream().filter(this::canPlayerBePrimeMinister).collect(Collectors.toList());
		fireEvent(el -> el.onPresidentChoosingPrimeMinister(president, update, candidates));
	}
	
	private void onPrimeMinisterChosen()
	{
		Player votedPrimeMinister = this.votedPrimeMinister;
		fireEvent(el -> el.onPrimeMinisterChosen(votedPrimeMinister));
	}
	
	private void onVotingResult(boolean passed)
	{
		List<Player> upvoters = votes.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey)
				.collect(Collectors.toList());
		int totalVotes = votes.size();
		fireEvent(el -> el.onVotingResult(upvoters, totalVotes, passed));
	}
	
	private void onPollIndexChange()
	{
		int pollIndex = this.pollIndex;
		fireEvent(el -> el.onPollIndexChange(pollIndex));
	}
	
	private void onRandomActPass()
	{
		fireEvent(EventListener::onRandomActPass);
	}
	
	private void onPresidentChoosingActs()
	{
		Player president = this.president;
		fireEvent(el -> el.onPresidentChoosingActs(president, currentActs));
		//Passing currentActs is not dangerous because it's used only at the moment of event's firing
	}
	
	private void onPrimeMinisterChoosingActs()
	{
		Player primeMinister = this.primeMinister;
		boolean vetoApplicable = isVetoApplicable();
		fireEvent(el -> el.onPrimeMinisterChoosingActs(primeMinister, currentActs, vetoApplicable));
		//Passing currentActs is not dangerous because it's used only at the moment of event's firing
	}
	
	private void onVetoRequest()
	{
		fireEvent(EventListener::onVetoRequest);
	}
	
	private void onVetoResponse(boolean accepted)
	{
		fireEvent(el -> el.onVetoResponse(accepted));
	}
	
	private void onActPassed()
	{
		int passedLustrationActs = this.passedLustrationActs;
		int passedAntilustrationActs = this.passedAntilustrationActs;
		fireEvent(el -> el.onActPassed(passedLustrationActs, passedAntilustrationActs));
	}
	
	private void onWin(boolean ministersWin, WinCause cause)
	{
		List<Player> ministers = initialPlayers.stream().filter(Player::isMinister).collect(Collectors.toList());
		List<Player> collaborators = initialPlayers.stream().filter(Player::isCollaborator).collect(Collectors.toList());
		Player bolek = initialPlayers.stream().filter(Player::isBolek).findAny().orElse(null);
		if(bolek == null) return;
		
		fireEvent(el -> el.onWin(ministersWin, cause, ministers, collaborators, bolek));
	}
	
	private void onPresidentCheckingPlayer(boolean update)
	{
		Player president = this.president;
		List<Player> checkablePlayers = players.stream().filter(p -> !p.wasChecked() && p != president).collect(Collectors.toList());
		fireEvent(el -> el.onPresidentCheckingPlayer(president, update, checkablePlayers));
	}
	
	private void onPresidentCheckedPlayer(Player checkedPlayer, int result)
	{
		Player president = this.president;
		fireEvent(el -> el.onPresidentCheckedPlayer(president, checkedPlayer, result));
	}
	
	private void onPresidentCheckingPlayerOrActs()
	{
		Player president = this.president;
		fireEvent(el -> el.onPresidentCheckingPlayerOrActs(president));
	}
	
	private void onPresidentCheckingActs()
	{
		Player president = this.president;
		fireEvent(el -> {
			if(el == president)
			{
				if(!el.onPresidentCheckingActs(president)) checkActsByPresident();//For backward compatibility
			}
			else el.onPresidentCheckingActs(president);
		});
	}
	
	private void onPresidentCheckedActs(Act[] acts)
	{
		Player president = this.president;
		fireEvent(el -> el.onPresidentCheckedActs(president, acts));
	}
	
	private void onPresidentChoosingPresident(boolean update)
	{
		Player president = this.president;
		List<Player> availablePlayers = players.stream().filter(p -> p != president).collect(Collectors.toList());
		fireEvent(el -> el.onPresidentChoosingPresident(president, update, availablePlayers));
	}
	
	private void onPresidentLustratingPlayer(boolean update)
	{
		Player president = this.president;
		List<Player> availablePlayers = players.stream().filter(p -> p != president).collect(Collectors.toList());
		fireEvent(el -> el.onPresidentLustratingPlayer(president, update, availablePlayers));
	}
	
	private void onPresidentLustratedPlayer(Player lustratedPlayer)
	{
		fireEvent(el -> el.onPresidentLustratedPlayer(lustratedPlayer));
	}
	
	private void onGameExited(Player player)
	{
		List<Player> allPlayers = new ArrayList<>(this.players);
		List<Player> ministers = initialPlayers.stream().filter(Player::isMinister).collect(Collectors.toList());
		List<Player> collaborators = initialPlayers.stream().filter(Player::isCollaborator).collect(Collectors.toList());
		Player bolek = initialPlayers.stream().filter(Player::isBolek).findAny().orElse(null);
		if(bolek == null) return;
		
		fireEvent(el -> el.onGameExited(player, allPlayers, ministers, collaborators, bolek, doesBolekKnowCollaborators()), false);
	}
	
	private void onTooFewPlayers()
	{
		fireEvent(EventListener::onTooFewPlayers);
	}
	
	private void onSpectatingStart(Spectator spectator)
	{
		List<Player> allPlayers = new ArrayList<>(this.players);
		List<Player> ministers = initialPlayers.stream().filter(Player::isMinister).collect(Collectors.toList());
		List<Player> collaborators = initialPlayers.stream().filter(Player::isCollaborator).collect(Collectors.toList());
		Player bolek = initialPlayers.stream().filter(Player::isBolek).findAny().orElse(null);
		if(bolek == null) return;
		
		fireEvent(el -> el.onSpectatingStart(spectator, allPlayers, secretImages, ministers, collaborators, bolek, doesBolekKnowCollaborators()));
	}
	
	private void fireEvent(Consumer<EventListener> event)
	{
		fireEvent(event, true);
	}
	
	private void fireEvent(Consumer<EventListener> event, boolean log)
	{
		if(log) events.add(event);
		players.forEach(event);
		spectators.forEach(event);
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