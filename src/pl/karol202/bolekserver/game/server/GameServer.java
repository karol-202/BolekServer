package pl.karol202.bolekserver.game.server;

import pl.karol202.bolekserver.game.ActionsQueue;
import pl.karol202.bolekserver.game.ErrorReference;
import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionStartGame;
import pl.karol202.bolekserver.game.game.GameListener;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameServer implements GameListener
{
	public enum UserAddingError
	{
		INVALID_NAME, TOO_MANY_USERS, USERNAME_BUSY
	}
	
	private static final int MAX_USERNAME_LENGTH = 20;
	private static final int MIN_USERS = 2;
	private static final int MAX_USERS = 10;
	
	private String name;
	private int serverCode;
	private List<User> users;
	private Game game;
	private boolean shouldExist;
	private List<Message> messages;
	
	private ActionsQueue<ServerAction> actionsQueue;
	
	public GameServer(String name, int serverCode)
	{
		this.name = name;
		this.serverCode = serverCode;
		this.users = new ArrayList<>();
		this.shouldExist = true;
		this.messages = new ArrayList<>();
		
		this.actionsQueue = new ActionsQueue<>();
	}
	
	User addNewUser(String username, Connection connection, ErrorReference<UserAddingError> error)
	{
		if(username == null || username.isEmpty() || username.length() > MAX_USERNAME_LENGTH)
		{
			error.setError(UserAddingError.INVALID_NAME);
			return null;
		}
		if(users.size() >= MAX_USERS)
		{
			error.setError(UserAddingError.TOO_MANY_USERS);
			return null;
		}
		if(isUsernameUsed(username))
		{
			error.setError(UserAddingError.USERNAME_BUSY);
			return null;
		}
		
		if(!shouldExist) shouldExist = true;
		
		User user = new User(username, connection);
		users.add(user);
		
		sendLoggedInMessage(user);
		broadcastUsersUpdate();
		sendServerStatus(user);
		sendAllMessages(user);
		return user;
	}
	
	boolean removeUser(User user)
	{
		if(!users.contains(user)) return false;
		users.remove(user);
		
		broadcastUsersUpdate();
		if(users.isEmpty()) shouldExist = false;
		return true;
	}
	
	void setUserReady(User user)
	{
		if(!users.contains(user)) return;
		user.setReady(true);
		broadcastUsersUpdate();
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
		game.setGameListener(this);
		game.addActionAndReturnImmediately(new GameActionStartGame());
	}
	
	@Override
	public void onGameEnd()
	{
		game = null;
		broadcastUsersUpdate();
		broadcastServerStatus();
	}
	
	@Override
	public void onPlayerLeaveGame(Player player)
	{
		sendUsersUpdate(player.getUser());
		sendServerStatus(player.getUser());
	}
	
	void sendMessage(User sender, String message)
	{
		if(!users.contains(sender)) return;
		messages.add(new Message(sender, message));
		broadcastMessage(sender, message);
	}
	
	
	private boolean isUsernameUsed(String username)
	{
		return users.stream().anyMatch(u -> u.getName().equals(username));
	}
	
	private void sendLoggedInMessage(User user)
	{
		user.sendLoggedInMessage(name, serverCode);
	}
	
	private void broadcastUsersUpdate()
	{
		users.forEach(this::sendUsersUpdate);
	}
	
	private void sendUsersUpdate(User user)
	{
		user.sendUsersListMessage(users.stream());
	}
	
	private void broadcastServerStatus()
	{
		users.forEach(this::sendServerStatus);
	}
	
	private void sendServerStatus(User user)
	{
		user.sendServerStatus(game == null);
	}
	
	private void broadcastMessage(User sender, String message)
	{
		users.stream().filter(u -> u != sender).forEach(u -> u.sendMessage(sender, message));
	}
	
	private void sendAllMessages(User user)
	{
		messages.forEach(m -> user.sendMessage(m.getSender(), m.getMessage()));
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
		actionsQueue.addAction(action, false);
		
		do Thread.yield();
		while(!actionsQueue.isResultSetForAction(action));
		
		Object result = actionsQueue.getResult(action);
		actionsQueue.removeAction(action);
		return (R) result;
	}
	
	public void addActionAndReturnImmediately(ServerAction action)
	{
		if(action != null) actionsQueue.addAction(action, true);
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