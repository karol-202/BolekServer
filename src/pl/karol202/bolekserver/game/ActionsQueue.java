package pl.karol202.bolekserver.game;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ActionsQueue
{
	class ActionAndTarget<A extends Action<T, ?>, T extends Target>
	{
		private A action;
		private T target;
		
		private ActionAndTarget(A action, T target)
		{
			this.action = action;
			this.target = target;
		}
		
		A getAction()
		{
			return action;
		}
		
		T getTarget()
		{
			return target;
		}
	}
	
	private class ActionData<A extends Action<T, R>, T extends Target, R>
	{
		private ActionAndTarget<A, T> actionAndTarget;
		private boolean removeImmediately;
		private R result;
		private boolean processed;
		
		private ActionData(A action, T target, boolean removeImmediately)
		{
			this.actionAndTarget = new ActionAndTarget<>(action, target);
			this.removeImmediately = removeImmediately;
		}
		
		ActionAndTarget<A, T> getActionAndTarget()
		{
			return actionAndTarget;
		}
		
		A getAction()
		{
			return actionAndTarget.action;
		}
		
		R getResult()
		{
			return result;
		}
		
		void setResult(R result)
		{
			this.result = result;
			this.processed = true;
		}
		
		boolean isProcessed()
		{
			return processed;
		}
	}
	
	private ConcurrentLinkedQueue<ActionData> queue;
	
	public ActionsQueue()
	{
		queue = new ConcurrentLinkedQueue<>();
	}
	
	public <A extends Action<T, R>, T extends Target, R> void addAction(A action, T target, boolean removeImmediately)
	{
		queue.add(new ActionData<>(action, target, removeImmediately));
	}
	
	public void removeAction(Action action)
	{
		ActionData aar = null;
		for(ActionData actionData : queue)
			if(actionData.getAction() == action) aar = actionData;
		if(aar != null) queue.remove(aar);
	}
	
	@SuppressWarnings("unchecked")
	public <A extends Action<T, R>, T extends Target, R> ActionAndTarget<A, T> peekActionAndTargetIfUnprocessed()
	{
		ActionData<A, T, R> action = queue.peek();
		queue.remove(action);
		if(!action.removeImmediately) queue.add(action);
		if(action.isProcessed()) return null;
		return action.getActionAndTarget();
	}
	
	public boolean hasUnprocessedActions()
	{
		for(ActionData actionData : queue)
			if(!actionData.isProcessed()) return true;
		return false;
	}
	
	public boolean isResultSetForAction(Action action)
	{
		for(ActionData actionData : queue)
			if(actionData.getAction() == action) return actionData.isProcessed();
		return false;
	}
	
	public Object getResult(Action action)
	{
		for(ActionData actionData : queue)
			if(actionData.getAction() == action) return actionData.getResult();
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void setResult(Action action, Object result)
	{
		for(ActionData actionData : queue)
			if(actionData.getAction() == action) actionData.setResult(result);
	}
}