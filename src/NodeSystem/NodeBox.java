package NodeSystem;

import BoxSystem.Box;
import Utilities.MyUtils;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.HashSet;

public class NodeBox extends Node { // Wrapper class for boxes that contains a reference to a parent node, used to traverse backwards once the endBox has been located

	public NodeBox(Box box, NodeBox node) {

        super(box, node);

	}
	
	public HashSet<INode> findNeighboringNodes() { // simply converts valid boxes to valid nodes

        Box box = (Box) getObject();
		HashSet<Box> neighboringBoxes = box.findNeighboringBoxes(1);
		HashSet<INode> neighboringNodes = new HashSet<>();
		
		for (Box neighboringBox : neighboringBoxes) {
			
			neighboringNodes.add(new NodeBox(neighboringBox, this));
			
		}
		
		return neighboringNodes;
		
	}

}
