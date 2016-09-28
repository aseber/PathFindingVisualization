package PathfindingAlgorithms;

import BoxSystem.Box;
import NodeSystem.INode;
import NodeSystem.Node;
import NodeSystem.NodeBox;

import java.util.HashSet;

import static Settings.AlgorithmSettings.WEIGHT_MODIFIER;
import static Settings.WindowSettings.VISUALIZATION_GUI;

public class PathfindAStar extends Pathfind {
	
	public PathfindAStar(INode startNode, INode endNode) {
	
		super(startNode, endNode, startNode.getG() + startNode.getObject().distanceFrom(endNode.getObject()));
		startNode.setG(0);
		
	}
	
	// A-Star implementation of pathfinding
	
	public void searchForPath() {

        INode currentNode;
		
		synchronized (this) {
		
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

				    Box currentBox = (Box) currentNode.getObject();
				    Box neighborBox = (Box) neighbor.getObject();

					if (neighborBox.getFlag() != Box.flags.SEARCHED && !neighborBox.isFullBarrier() && isBoxInAllowedBoxes(neighborBox)) {
						
						double tentitive_g = currentNode.getG() + neighbor.distanceFrom(currentNode)*(1 + WEIGHT_MODIFIER*neighborBox.getWeight());
						
						if (neighborBox.getFlag() != Box.flags.QUEUED | tentitive_g < neighbor.getG()) {
							
							neighbor.setParent(currentNode);
							neighbor.setG(tentitive_g);
							
							if (neighborBox.getFlag() != Box.flags.QUEUED) {
								
								addNodeToOpen(neighbor, neighbor.getG() + neighbor.distanceFrom(endNode));
								
							}
							
						}
						
					}
					
				}
				
			}
			
			while (!open.isEmpty() && !isExpandedCounterExceeded() && running);
			
			if (currentNode.equals(endNode)) {
				
				pathFound = true;
				endOfPath = currentNode;
				System.out.println("PathfindingAlgorithms.Path found! Retracing our steps and highlighting the path.");
				HashSet<Box> boxes = boxesAlongPath();
				Box.setFlags(boxes, Box.flags.SHORTEST_PATH);
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
