package NodeSystem;

import BoxSystem.Box;

import java.util.HashSet;

public class NodeBox extends Node { // Wrapper class for boxes that contains a reference to a parent node, used to traverse backwards once the endBox has been located

	public Box box;
	public NodeBox parentNode;
	
	public NodeBox(Box current_Box, NodeBox parent_Node) {
		
		super(parent_Node);
		this.box = current_Box;
		this.parentNode = parent_Node;
		
	}
	
	public HashSet<NodeBox> findNeighboringNodes() { // simply converts valid boxes to valid nodes
		
		HashSet<Box> neighboringBoxes = box.findNeighboringBoxes(1);
		HashSet<NodeBox> neighboringNodes = new HashSet<NodeBox>();
		
		for (Box neighboringBox : neighboringBoxes) {
			
			neighboringNodes.add(new NodeBox(neighboringBox, this));
			
		}
		
		return neighboringNodes;
		
	}
	
	public void setParent(NodeBox node) {
		
		this.parentNode = node;
		
	}
	
	@Override
	public boolean equals(Object o) {
		
		NodeBox node = (NodeBox) o;
		
		if (this.box.equals(node.box)) {// && this.parentNode.box.equals(node.parentNode.box) && this.pathCost == node.pathCost) {
			
			return true;
			
		}
		
		return false;
		
	}
	
}
