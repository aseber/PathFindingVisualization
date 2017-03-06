package NodeSystem;

import BoxSystem.Box;

import java.util.HashSet;

public class NodeBox extends Node { // Wrapper class for boxes that contains a reference to a parent node, used to traverse backwards once the endBox has been located

	public NodeBox(Box box, NodeBox node) {

        super(box, node);

	}
	
	public HashSet<INode> findNeighboringNodes() { // simply converts valid boxes to valid nodes

        System.out.println("NOTE! NodeBox::findNeighboringNodes() called and is currently broken!");
        /*Box box = (Box) getObject();
		HashSet<Box> neighboringBoxes = .findNeighboringBoxes(1);
		HashSet<INode> neighboringNodes = new HashSet<>();
		
		for (Box neighboringBox : neighboringBoxes) {
			
			neighboringNodes.add(new NodeBox(neighboringBox, this));
			
		}
		
		return neighboringNodes;*/
		return new HashSet<>();
		
	}

}
