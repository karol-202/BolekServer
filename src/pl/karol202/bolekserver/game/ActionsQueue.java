package pl.karol202.bolekserver.game;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ActionsQueue<A extends Action>
{
	private class ActionAndResult
	{
		private A action;
		private Object result;
		
		ActionAndResult(A action)
		{
			this.action = action;
		}
	}
	
	private ConcurrentLinkedQueue<ActionAndResult> queue;
	
	public ActionsQueue()
	{
		queue = new ConcurrentLinkedQueue<>();
	}
	
	public void addAction(A action)
	{
		queue.add(new ActionAndResult(action));
	}
	
	public A pollAction()
	{
		return queue.poll().action;
	}
	
	public boolean isEmpty()
	{
		return queue.isEmpty();
	}
	
	public Object getResult(A action)
	{
		for(ActionAndResult actionAndResult : queue)
			if(actionAndResult.action == action) return actionAndResult.result;
		return null;
	}
	
	public void setResult(A action, Object result)
	{
		for(ActionAndResult actionAndResult : queue)
			if(actionAndResult.action == action) actionAndResult.result = result;
	}
}