import java.util.HashSet;

public class PathfindCustom extends Pathfind { // Shitty pathfinding class I used for testing, you can play with the G and H values for interesting results
	
	public PathfindCustom(NodeBox startNode, NodeBox endNode) {
	
		super(startNode, endNode, 0);
		
	}
	
	public void searchForPath() {
		
		NodeBox currentNode;
		
		do {
			
			synchronized(this) {
			
				try {
					
					while(pause) {
						
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
				
				if (neighbor.box.getFlag() != Box.flags.QUEUED && neighbor.box.getFlag() != Box.flags.SEARCHED && !neighbor.box.isFullBarrier() && isBoxInAllowedBoxes(neighbor.box)) {
					
					double g = neighbor.box.euclideanDistance(startNode.box);
					double h = neighbor.box.euclideanDistance(endNode.box);
					addNodeToOpen(neighbor, g*VisualizationBase.gModifier + h*VisualizationBase.hModifier);
					
				}
				
			}
			
		}
		
		while (!open.isEmpty() && !isExpandedCounterExceeded() && running);//Window.box_X_Count*Window.box_Y_Count);
		
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
