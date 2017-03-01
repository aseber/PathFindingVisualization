package EventHandler;

import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*

public class RegionEventDriver { // class that drives the region flashing thing

	private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
	@SuppressWarnings("rawtypes")
	private static HashMap<RegionEvent, Future> eventMap = new HashMap<RegionEvent, Future>();

	public RegionEventDriver() {
		
		executor.setMaximumPoolSize(5);
		
	}
	
	public void runEvent(RegionEvent event, int time) {
				
		synchronized(eventMap) {
			
			if (!eventMap.containsKey(event)) {
				
				eventMap.put(event, executor.schedule(event, time, TimeUnit.MILLISECONDS));
				event.drawBeginning();
				
			} else {
				
				eventMap.remove(event).cancel(true);
				eventMap.put(event, executor.schedule(event, time, TimeUnit.MILLISECONDS));
				
			}
			
		}
		
	}
	
	public static void removeEvent(RegionEvent event) {
		
		synchronized(eventMap) {
			
			eventMap.remove(event);
			
		}
		
	}
	
	public static int size() {
		
		synchronized(eventMap) {
			
			return eventMap.size();
			
		}
		
	}
	
}

*/