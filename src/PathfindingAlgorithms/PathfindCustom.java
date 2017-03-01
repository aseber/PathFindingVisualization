package PathfindingAlgorithms;

import BoxSystem.Box;
import NodeSystem.INode;

import java.util.HashSet;

import static BoxState.BoxState.QUEUED_BOX_STATE;
import static BoxState.BoxState.SEARCHED_BOX_STATE;
import static BoxState.BoxState.SHORTEST_PATH_BOX_STATE;
import static Settings.AlgorithmSettings.CUSTOM_G_MODIFIER;
import static Settings.AlgorithmSettings.CUSTOM_H_MODIFIER;
import static Settings.WindowSettings.VISUALIZATION_GUI;

public class PathfindCustom extends Pathfind { // Shitty pathfinding class I used for testing, you can play with the G and H values for interesting results
	
	public PathfindCustom(INode startNode, INode endNode) {
	
		super(startNode, endNode, 0);
		
	}
	
	public void searchForPath() {

		INode currentNode;
		
		synchronized(this) {
		
			do {

				checkWait();
			
				currentNode = open.poll();

                if (currentNode.equals(endNode)) {
					
					break;
					
				}
				
				addNodeToClosed(currentNode);
				VISUALIZATION_GUI.setOpenCounter(open.size());
				VISUALIZATION_GUI.setClosedCounter(closed.size());
				HashSet<INode> neighboringNodes = currentNode.findNeighboringNodes();
				expandedCounter++;
				
				for (INode neighbor : neighboringNodes) {

					Box neighborBox = (Box) neighbor.getObject();

					if (neighborBox.getFlag() != QUEUED_BOX_STATE && neighborBox.getFlag() != SEARCHED_BOX_STATE && !neighborBox.isFullBarrier() && isBoxInAllowedBoxes(neighborBox)) {
						
						double g = neighbor.distanceFrom(startNode);
						double h = neighbor.distanceFrom(endNode);
						addNodeToOpen(neighbor, g* CUSTOM_G_MODIFIER + h* CUSTOM_H_MODIFIER);
						
					}
					
				}
				
			}
			
			while (!open.isEmpty() && !isExpandedCounterExceeded() && running);//Window.box_X_Count*Window.box_Y_Count);
		
			if (currentNode.equals(endNode)) {
				
				pathFound = true;
				endOfPath = currentNode;
				System.out.println("PathfindingAlgorithms.Path found! Retracing our steps and highlighting the path.");
				HashSet<Box> boxes = boxesAlongPath();
				Box.setFlags(boxes, SHORTEST_PATH_BOX_STATE);
				VISUALIZATION_GUI.setPathLengthCounter(boxes.size());
				
			} else if (isExpandedCounterExceeded()) {
				
				System.out.println("PathfindingAlgorithms.Path could not be produced as counter expanded past the hard cap.");
				
			} else {
				
				System.out.println("PathfindingAlgorithms.Path could not be produced as there are no more nodes to be searched.");
				
			}
			
			VISUALIZATION_GUI.setRunButtonState(true);
			running = false;
			
		}
		
	}
	
}
