package pl.karol202.bolekserver.game.server;

import pl.karol202.bolekserver.game.ActionsQueue;
import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionStartGame;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameServer
{
	private static final int MIN_USERS = 5;
	private static final int MAX_USERS = 10;
	
	private String name;
	private int serverCode;
	private List<User> users;
	private Game game;
	private boolean shouldExist;
	
	private ActionsQueue<ServerAction> actionsQueue;
	
	public GameServer(String name, int serverCode)
	{
		this.name = name;
		this.serverCode = serverCode;
		this.users = new ArrayList<>();
		this.shouldExist = true;
		
		this.actionsQueue = new ActionsQueue<>();
	}
	
	User addNewUser(String username, Connection connection)
	{
		if(username == null || username.isEmpty() || connection == null ||
				users.size() >= MAX_USERS || isUsernameUsed(username))
		{
			shouldExist = false;
			return null;
		}
		
		if(!shouldExist) shouldExist = true;
		
		User user = new User(username, connection);
		users.add(user);
		
		broadcastUsersUpdate();
		sendServerStatus(user);
		return user;
	}
	
	boolean removeUser(User user)
	{
		if(!users.contains(user)) return false;
		broadcastUsersUpdate();
		if(users.isEmpty()) shouldExist = false;
		
		users.remove(user);
		return true;
	}
	
	void setUserReady(User user)
	{
		if(!users.contains(user)) return;
		user.setReady(true);
		broadcastUserReadiness(user.getName());
		checkForReadiness();
	}

	private void checkForReadiness()
	{
		if(users.size() < MIN_USERS) return;
		for(User user : users) if(!user.isReady()) return;
		startGame();
	}
	
	private void startGame()
	{
		users.forEach(u -> u.setReady(false));
		List<Player> players = users.stream().map(u -> new Player(u, u.getAdapter())).collect(Collectors.toList());
		game = new Game(players);
		game.setOnGameEndListener(this::onGameEnded);
		game.addActionAndReturnImmediately(new GameActionStartGame());
	}
	
	private void onGameEnded()
	{
		game = null;
		broadcastServerStatus();
	}
	
	
	private boolean isUsernameUsed(String username)
	{
		return users.stream().anyMatch(u -> u.getName().equals(username));
	}
	
	private void broadcastUsersUpdate()
	{
		users.forEach(u -> u.sendUsersListMessage(users.stream()));
	}
	
	private void broadcastUserReadiness(String username)
	{
		users.forEach(u -> u.sendUserReadinessMessage(username));
	}
	
	private void broadcastServerStatus()
	{
		users.forEach(this::sendServerStatus);
	}
	
	private void sendServerStatus(User user)
	{
		user.sendServerStatus(game == null);
	}
	
	public void executeActions()
	{
		while(actionsQueue.hasUnprocessedActions())
		{
			ServerAction action = actionsQueue.peekAction();
			Object result = action.execute(this);
			actionsQueue.setResult(action, result);
		}
		if(game != null) game.executeActions();
	}
	
	public <R> R addActionAndWaitForResult(ServerAction<R> action)
	{
		if(action == null) return null;
		actionsQueue.addAction(action);
		
		do Thread.yield();
		while(!actionsQueue.isResultSetForAction(action));
		
		Object result = actionsQueue.getResult(action);
		actionsQueue.removeAction(action);
		return (R) result;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getServerCode()
	{
		return serverCode;
	}
	
	public boolean shouldLongerExist()
	{
		return shouldExist;
	}
}