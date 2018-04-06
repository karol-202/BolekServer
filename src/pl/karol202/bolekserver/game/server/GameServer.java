package pl.karol202.bolekserver.game.server;

import pl.karol202.bolekserver.game.ErrorReference;
import pl.karol202.bolekserver.game.Looper;
import pl.karol202.bolekserver.game.Target;
import pl.karol202.bolekserver.game.game.Game;
import pl.karol202.bolekserver.game.game.GameActionStartGame;
import pl.karol202.bolekserver.game.game.GameListener;
import pl.karol202.bolekserver.game.game.Player;
import pl.karol202.bolekserver.server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameServer implements Target, GameListener
{
	public enum UserAddingError
	{
		INVALID_NAME, TOO_MANY_USERS, USERNAME_BUSY
	}
	
	private static final int MAX_USERNAME_LENGTH = 20;
	private static final int MIN_USERS = 2;
	private static final int MAX_USERS = 10;
	
	private Looper looper;
	private String name;
	private int serverCode;
	private List<User> users;
	private Game game;
	private List<Message> messages;
	
	private ServerListener serverListener;
	
	public GameServer(Looper looper, String name, int serverCode)
	{
		this.looper = looper;
		this.name = name;
		this.serverCode = serverCode;
		this.users = new ArrayList<>();
		this.messages = new ArrayList<>();
	}
	
	boolean addNewUser(User user, ErrorReference<UserAddingError> error)
	{
		String username = user.getName();
		if(username == null || username.isEmpty() || username.length() > MAX_USERNAME_LENGTH)
		{
			error.setError(UserAddingError.INVALID_NAME);
			return false;
		}
		if(users.size() >= MAX_USERS)
		{
			error.setError(UserAddingError.TOO_MANY_USERS);
			return false;
		}
		if(isUsernameUsed(username))
		{
			error.setError(UserAddingError.USERNAME_BUSY);
			return false;
		}
		
		users.add(user);
		
		sendLoggedInMessage(user);
		broadcastUsersUpdate();
		sendServerStatus(user);
		sendAllMessages(user);
		
		Server.LOGGER.info("User " + username + " joined server " + serverCode);
		return true;
	}
	
	boolean removeUser(User user)
	{
		if(!users.contains(user)) return false;
		users.remove(user);
		
		broadcastUsersUpdate();
		if(users.isEmpty()) serverListener.onServerEmpty();
		checkForReadiness();
		
		Server.LOGGER.info("User " + user.getName() + " leaved server " + serverCode);
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
		game = new Game(looper, players);
		game.setGameListener(this);
		game.addActionAndReturnImmediately(new GameActionStartGame());
		
		Server.LOGGER.info("Game started on server " + serverCode);
	}
	
	@Override
	public void onGameEnd()
	{
		game = null;
		broadcastUsersUpdate();
		broadcastServerStatus();
		
		Server.LOGGER.info("Game ended on server " + serverCode);
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
		users.stream().filter(u -> u != sender).forEach(u -> u.sendMessage(sender, message, true));
	}
	
	private void sendAllMessages(User user)
	{
		messages.forEach(m -> user.sendMessage(m.getSender(), m.getMessage(), false));
	}
	
	public <R> R addActionAndWaitForResult(ServerAction<R> action)
	{
		return looper.addActionAndWaitForResult(action, this);
	}
	
	public void addActionAndReturnImmediately(ServerAction<?> action)
	{
		looper.addActionAndReturnImmediately(action, this);
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getServerCode()
	{
		return serverCode;
	}
	
	public void setServerListener(ServerListener serverListener)
	{
		this.serverListener = serverListener;
	}
}