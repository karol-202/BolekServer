package pl.karol202.bolekserver.game;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ActionsQueue<A extends Action>
{
	private class ActionAndResult
	{
		private A action;
		private boolean removeImmediately;
		private Object result;
		private boolean processed;
		
		ActionAndResult(A action, boolean removeImmediately)
		{
			this.action = action;
			this.removeImmediately = removeImmediately;
		}
		
		Object getResult()
		{
			return result;
		}
		
		void setResult(Object result)
		{
			this.result = result;
			this.processed = true;
		}
		
		boolean isProcessed()
		{
			return processed;
		}
	}
	
	private ConcurrentLinkedQueue<ActionAndResult> queue;
	
	public ActionsQueue()
	{
		queue = new ConcurrentLinkedQueue<>();
	}
	
	public void addAction(A action, boolean removeImmediately)
	{
		queue.add(new ActionAndResult(action, removeImmediately));
	}
	
	public void removeAction(A action)
	{
		ActionAndResult aar = null;
		for(ActionAndResult actionAndResult : queue)
			if(actionAndResult.action == action) aar = actionAndResult;
		if(aar != null) queue.remove(aar);
	}
	
	public A peekAction()
	{
		ActionAndResult action = queue.peek();
		queue.remove(action);
		if(!action.removeImmediately) queue.add(action);
		return action.action;
	}
	
	public boolean hasUnprocessedActions()
	{
		for(ActionAndResult actionAndResult : queue)
			if(!actionAndResult.isProcessed()) return true;
		return false;
	}
	
	public boolean isResultSetForAction(A action)
	{
		for(ActionAndResult actionAndResult : queue)
			if(actionAndResult.action == action) return actionAndResult.isProcessed();
		return false;
	}
	
	public Object getResult(A action)
	{
		for(ActionAndResult actionAndResult : queue)
			if(actionAndResult.action == action) return actionAndResult.getResult();
		return null;
	}
	
	public void setResult(A action, Object result)
	{
		for(ActionAndResult actionAndResult : queue)
			if(actionAndResult.action == action) actionAndResult.setResult(result);
	}
}