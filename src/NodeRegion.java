import java.util.HashSet;

public class NodeRegion extends Node { // A boxNode but adapted for regions

	protected Region region;
	protected NodeRegion parentNode;
	
	public NodeRegion(Region current_Region, NodeRegion parent_Node) {
		
		super(parent_Node);
		this.region = current_Region;
		this.parentNode = parent_Node;
		
	}
	
	public HashSet<NodeRegion> findNeighboringNodes() { // simply converts valid boxes to valid nodes
		
		HashSet<Region> neighboringRegions = region.getNeighboringRegions();
		HashSet<NodeRegion> neighboringNodes = new HashSet<NodeRegion>();
		
		for (Region neighboringBox : neighboringRegions) {
			
			neighboringNodes.add(new NodeRegion(neighboringBox, this));
			
		}
		
		return neighboringNodes;
		
	}
	
	public void setParent(NodeRegion node) {
		
		super.setParent(node);
		
	}
	
	@Override
	public boolean equals(Object o) {
		
		NodeRegion node = (NodeRegion) o;
		
		if (this.region.equals(node.region)) {// && this.parentNode.box.equals(node.parentNode.box) && this.pathCost == node.pathCost) {
			
			return true;
			
		}
		
		return false;
		
	}
	
}
