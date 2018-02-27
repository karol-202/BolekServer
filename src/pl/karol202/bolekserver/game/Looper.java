package pl.karol202.bolekserver.game;

public class Looper
{
	private ActionsQueue actionsQueue;
	private boolean suspend;
	
	public Looper()
	{
		this.actionsQueue = new ActionsQueue();
		this.suspend = false;
	}
	
	public void run()
	{
		try
		{
			while(!suspend) executeActions();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private void executeActions() throws InterruptedException
	{
		while(actionsQueue.hasUnprocessedActions())
			executeAction(actionsQueue.peekActionAndTargetIfUnprocessed());
		Thread.sleep(10);
	}
	
	private <A extends Action<T, ?>, T extends Target> void executeAction(ActionsQueue.ActionAndTarget<A, T> actionAndTarget)
	{
		if(actionAndTarget == null) return;
		A action = actionAndTarget.getAction();
		T target = actionAndTarget.getTarget();
		Object result = action.execute(target);
		actionsQueue.setResult(action, result);
	}
	
	public void suspend()
	{
		suspend = true;
	}
	
	@SuppressWarnings("unchecked")
	public <A extends Action<T, R>, T extends Target, R> R addActionAndWaitForResult(A action, T target)
	{
		if(action == null) return null;
		actionsQueue.addAction(action, target, false);
		
		do Thread.yield();
		while(!actionsQueue.isResultSetForAction(action));
		
		Object result = actionsQueue.getResult(action);
		actionsQueue.removeAction(action);
		return (R) result;
	}
	
	public <A extends Action<T, R>, T extends Target, R> void addActionAndReturnImmediately(A action, T target)
	{
		if(action != null) actionsQueue.addAction(action, target, true);
	}
}
