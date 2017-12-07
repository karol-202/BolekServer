package pl.karol202.bolekserver.game.server;

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
		
		private A getAction()
		{
			return action;
		}
		
		private Object getResult()
		{
			return result;
		}
		
		private void setResult(Object result)
		{
			this.result = result;
		}
	}
	
	private ConcurrentLinkedQueue<ActionAndResult> queue;
	
	ActionsQueue()
	{
		queue = new ConcurrentLinkedQueue<>();
	}
	
	public void addAction(A action)
	{
		queue.add(new ActionAndResult(action));
	}
	
	public Object getResult(A action)
	{
		for(ActionAndResult actionAndResult : queue)
			if(actionAndResult.action == action) return actionAndResult.result;
		return null;
	}
}