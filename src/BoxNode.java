import java.util.HashSet;

public class BoxNode extends Node { // Wrapper class for boxes that contains a reference to a parent node, used to traverse backwards once the endBox has been located

	protected Box box;
	protected BoxNode parentNode;
	
	public BoxNode(Box current_Box, BoxNode parent_Node) {
		
		super(parent_Node);
		this.box = current_Box;
		this.parentNode = parent_Node;
		
	}
	
	public HashSet<BoxNode> findNeighboringNodes() { // simply converts valid boxes to valid nodes
		
		HashSet<Box> neighboringBoxes = box.findNeighboringBoxes(1);
		HashSet<BoxNode> neighboringNodes = new HashSet<BoxNode>();
		
		for (Box neighboringBox : neighboringBoxes) {
			
			neighboringNodes.add(new BoxNode(neighboringBox, this));
			
		}
		
		return neighboringNodes;
		
	}
	
	public void setParent(BoxNode node) {
		
		this.parentNode = node;
		
	}
	
	@Override
	public boolean equals(Object o) {
		
		BoxNode node = (BoxNode) o;
		
		if (this.box.equals(node.box)) {// && this.parentNode.box.equals(node.parentNode.box) && this.pathCost == node.pathCost) {
			
			return true;
			
		}
		
		return false;
		
	}
	
}
