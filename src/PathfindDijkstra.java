import java.util.HashSet;

public class PathfindDijkstra extends Pathfind {
	
	// Implementation for Dijkstra's algorithm. Code is nearly identical to A-Star but disregards the cost heuristic for the current box to the end box
	
	public PathfindDijkstra(NodeBox startNode, NodeBox endNode) {
	
		super(startNode, endNode, 0);
		startNode.setG(0);
		
	}
	
	public void searchForPath() {
		
		NodeBox currentNode;
		
		do {
			
			synchronized (this) {
			
				try {
					
					while (pause) {
						
						this.wait();
						
					}
					
					if (VisualizationBase.sleepTimer > 0) {
					
						sleep(VisualizationBase.sleepTimer);
						
					}
					
				} catch (InterruptedException e) {}
				
			}
			
			currentNode = open.poll();
			
			if (currentNode.box.equals(endNode.box)) {
				
				break;
				
			}
			
			addNodeToClosed(currentNode);
			VisualizationBase.VISUALIZATION_GUI.setOpenCounter(open.size());
			VisualizationBase.VISUALIZATION_GUI.setClosedCounter(closed.size());
			HashSet<NodeBox> neighboringNodes = currentNode.findNeighboringNodes();
			expandedCounter++;
			
			for (NodeBox neighbor : neighboringNodes) {
				
				if (neighbor.box.getFlag() != Box.flags.SEARCHED && !neighbor.box.isFullBarrier() && isBoxInAllowedBoxes(neighbor.box)) {
					
					double tentitive_g = currentNode.getG() + neighbor.box.euclideanDistance(currentNode.box)*(1 + VisualizationBase.weightModifier*neighbor.box.getWeight());
					
					if (neighbor.box.getFlag() != Box.flags.QUEUED | tentitive_g < neighbor.getG()) {
						
						neighbor.setParent(currentNode);
						neighbor.setG(tentitive_g);
						
						if (neighbor.box.getFlag() != Box.flags.QUEUED) {
							
							addNodeToOpen(neighbor, neighbor.box.euclideanDistance(startNode.box));
							
						}
						
					}
					
				}
				
			}
			
		}
		
		while (!open.isEmpty() && !isExpandedCounterExceeded() && running);
		
		if (currentNode.box.equals(endNode.box)) {
			
			pathFound = true;
			endOfPath = currentNode;
			System.out.println("Path found! Retracing our steps and highlighting the path.");
			HashSet<Box> boxes = boxesAlongPath();
			Box.setFlags(boxes, Box.flags.SHORTEST_PATH);
			VisualizationBase.VISUALIZATION_GUI.setPathLengthCounter(boxes.size());
			
			
		} else if (isExpandedCounterExceeded()) {
			
			System.out.println("Path could not be produced as counter expanded past the hard cap.");
			
		} else {
			
			System.out.println("Path could not be produced as there are no more nodes to be searched.");
			
		}
		
		VisualizationBase.VISUALIZATION_GUI.setRunButtonState(true);
		running = false;
		
	}

}
