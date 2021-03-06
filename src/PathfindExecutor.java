import java.awt.Color;
import java.util.HashSet;


public class PathfindExecutor implements Runnable { // Simple class that allows me to move the processing to a new thread so the UI doesn't lag.
													// Also lets me test the algorithms speed
	
	private Pathfind pathfinder;
	private Thread thread;
	private long startTime;
	
	public void run() {

		synchronized (this) {
		
			while (true) {
				
				if (isPathFinderRunning()) {
					
					pathfinder.waitForFinish();
					long endTime = System.currentTimeMillis();
					VisualizationBase.VISUALIZATION_GUI.setRunTimeCounter(endTime - startTime);
					
				}
				
				try {
					
					wait(100);
					
				} catch (InterruptedException e) {}
				
			}
			
		}
		
	}

	public void startPathfinding() {
		
		Box.flags[] flags = {Box.flags.SEARCHED, Box.flags.SHORTEST_PATH, Box.flags.QUEUED};
		VisualizationBase.VISUALIZATION_WINDOW.clearBoxFieldFlags(flags);
		startTime = System.currentTimeMillis();
		
		if (Box.beginningAndEndExist()) {
			
			PathfindRegion regionPathfind = null;
			HashSet<Box> boxesAlongRegionPath = null;
			
			if (VisualizationBase.HIERARCHICAL_PATHFINDING) {
				
				regionPathfind = new PathfindRegion(new NodeRegion(Box.startBox.getRegion(), null), new NodeRegion(Box.endBox.getRegion(), null));
				regionPathfind.start();
				HashSet<Region> regionsOnPath = regionPathfind.regionsAlongPath();
				HashSet<Region> expandedRegionsOnPath = new HashSet<Region>();
				
				for (Region currentRegion : regionsOnPath) {
					
					expandedRegionsOnPath.addAll(currentRegion.getNeighboringRegions());
					expandedRegionsOnPath.add(currentRegion);
					
				}
				
				boxesAlongRegionPath = new HashSet<Box>();
				
				for (Region currentRegion : expandedRegionsOnPath) {
				
					VisualizationBase.VISUALIZATION_WINDOW.registerChange(currentRegion, 2000, new Color(0, 0, 255, 125));
					boxesAlongRegionPath.addAll(currentRegion.getBoxes());
					
				}
				
			}
			
			//long intermediateTime = System.currentTimeMillis();
			//System.out.println(intermediateTime - startTime + " ms");
			
			pathfinder = VisualizationBase.CURRENT_ALGORITHM.pathfind(new NodeBox(Box.startBox, null), new NodeBox(Box.endBox, null));
			
			if (VisualizationBase.HIERARCHICAL_PATHFINDING) {
				
				pathfinder.setAvailableRegion(boxesAlongRegionPath);
				
				if (regionPathfind.isPathFound()) {
					
					thread = new Thread(pathfinder);
					thread.start();
					
					synchronized (this) {
						
						this.notify();
						
					}
					
					
				}
				
				else {
					
					System.out.println("Pathfind not attempted as region pathfinding did not find a result.");
					
				}
				
				return;
				
			}
			
			thread = new Thread(pathfinder);
			thread.start();

			synchronized (this) {
				
				this.notify();
				
			}
			
		}
		
	}
	
	public void togglePause() {
		
		if (isPathFinderRunning()) {
			
			pathfinder.togglePause();
			
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void endPathfinding() {
		
		if (isPathFinderRunning()) {
			
			thread.stop();
			pathfinder = null;
			thread = null;
			
		}
		
	}
	
	public boolean isPathFinderRunning() {
		
		if (pathfinder != null) {
			
			return pathfinder.isRunning();
			
		}
		
		return false;
		
	}
	
}
