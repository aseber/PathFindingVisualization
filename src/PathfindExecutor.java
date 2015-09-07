import java.awt.Color;
import java.util.HashSet;


public class PathfindExecutor implements Runnable { // Simple class that allows me to move the processing to a new thread so the UI doesn't lag.
													// Also lets me test the algorithms speed
	
	Pathfind pathfinder;
	
	public void run() {

		Box.flags[] flags = {Box.flags.SEARCHED, Box.flags.SHORTEST_PATH, Box.flags.QUEUED};
		VisualizationBase.VISUALIZATION_WINDOW.clearBoxFieldFlags(flags);
		long startTime = System.currentTimeMillis();
		
		if (Box.beginningAndEndExist()) {
			
			PathfindRegion regionPathfind = null;
			HashSet<Box> boxesAlongRegionPath = null;
			
			if (VisualizationBase.hierarchicalPathfinding) {
				
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
			
			if (VisualizationBase.CURRENT_ALGORITHM == VisualizationBase.ASTAR) {
				
				pathfinder = new PathfindAStar(new NodeBox(Box.startBox, null), new NodeBox(Box.endBox, null));
				
			} else if (VisualizationBase.CURRENT_ALGORITHM == VisualizationBase.DIJKSTRA) {
			
				pathfinder = new PathfindDijkstra(new NodeBox(Box.startBox, null), new NodeBox(Box.endBox, null));
				
			} else if (VisualizationBase.CURRENT_ALGORITHM == VisualizationBase.CUSTOM) {
				
				pathfinder = new PathfindCustom(new NodeBox(Box.startBox, null), new NodeBox(Box.endBox, null));
				
			}
			
			if (VisualizationBase.hierarchicalPathfinding) {
				
				pathfinder.setAvailableRegion(boxesAlongRegionPath);
				
				if (regionPathfind.isPathFound()) {
					
					pathfinder.start();
					pathfinder.waitForFinish();
					long endTime = System.currentTimeMillis();
					VisualizationBase.VISUALIZATION_GUI.setRunTimeCounter(endTime - startTime);
					
				}
				
				else {
					
					System.out.println("Pathfind not attempted as region pathfinding did not find a result.");
					
				}
				
				return;
				
			}
			
			pathfinder.start();
			pathfinder.waitForFinish();
			long endTime = System.currentTimeMillis();
			VisualizationBase.VISUALIZATION_GUI.setRunTimeCounter(endTime - startTime);
			
			//VisualizationBase.VISUALIZATION_WINDOW.repaintAll();
			
		}
		
	}

}
