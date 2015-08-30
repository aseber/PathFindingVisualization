import java.util.HashSet;

public class RegionNode extends Node { // A boxNode but adapted for regions

	protected Region region;
	protected RegionNode parentNode;
	
	public RegionNode(Region current_Region, RegionNode parent_Node) {
		
		super(parent_Node);
		this.region = current_Region;
		this.parentNode = parent_Node;
		
	}
	
	public HashSet<RegionNode> findNeighboringNodes() { // simply converts valid boxes to valid nodes
		
		HashSet<Region> neighboringRegions = region.getNeighboringRegions();
		HashSet<RegionNode> neighboringNodes = new HashSet<RegionNode>();
		
		for (Region neighboringBox : neighboringRegions) {
			
			neighboringNodes.add(new RegionNode(neighboringBox, this));
			
		}
		
		return neighboringNodes;
		
	}
	
	public void setParent(RegionNode node) {
		
		super.setParent(node);
		
	}
	
	@Override
	public boolean equals(Object o) {
		
		RegionNode node = (RegionNode) o;
		
		if (this.region.equals(node.region)) {// && this.parentNode.box.equals(node.parentNode.box) && this.pathCost == node.pathCost) {
			
			return true;
			
		}
		
		return false;
		
	}
	
}
