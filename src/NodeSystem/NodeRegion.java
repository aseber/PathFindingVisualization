package NodeSystem;

import RegionSystem.Region;

import java.util.HashSet;

public class NodeRegion extends Node { // A boxNode but adapted for regions

	public NodeRegion(Region region, NodeRegion node) {
		
		super(region, node);

	}
	
	public HashSet<INode> findNeighboringNodes() { // simply converts valid boxes to valid nodes

        Region region = (Region) getObject();
		HashSet<Region> neighboringRegions = region.getNeighboringRegions();
		HashSet<INode> neighboringNodes = new HashSet<>();

		for (Region neighboringBox : neighboringRegions) {
			
			neighboringNodes.add(new NodeRegion(neighboringBox, this));
			
		}
		
		return neighboringNodes;
		
	}
	
}
