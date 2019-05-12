package ws.refcursor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WsThreadPoolExecutor extends ThreadPoolExecutor {
	
	boolean isError = false;
	
	List<String> errors = new ArrayList<String>(0);
	
	List results = new ArrayList(0);
	
	public boolean isError() {
		return isError;
	}

	public List<String> getErrors() {
		return errors;
	}

	public List getResults() {
		return results;
	}

	public WsThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		
		if (t == null && r instanceof Future<?>) {
		       try {
		         Object result = ((Future<?>) r).get();
		         
		         if(result!=null && result instanceof List){
		        	 results.addAll((List)result);
		         }
		         
		       } catch (CancellationException ce) {
		           t = ce;
		       } catch (ExecutionException ee) {
		           t = ee.getCause();
		       } catch (InterruptedException ie) {
		           Thread.currentThread().interrupt(); // ignore/reset
		       }
		     }
		     if (t != null){
		    	 isError = true;
		    	 errors.add(t.getMessage());
		     }

	}
	
	

}
