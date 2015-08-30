import java.util.Comparator; // A-Star pathfinding implementation for regions
import java.util.HashSet;
import java.util.PriorityQueue;

public class PathfindRegion extends Thread {

	protected RegionNode startNode;
	protected RegionNode endNode;
	protected double expandedCounter;
	protected final double EXPANDED_COUNTER_HARD_CAP = 100000;
	protected boolean running = true;
	protected boolean pause = false;
	protected boolean pathFound = false;
	protected BestPathComparator comparator = new BestPathComparator();
	protected PriorityQueue<RegionNode> open = new PriorityQueue<RegionNode>(10, comparator);
	protected PriorityQueue<RegionNode> closed = new PriorityQueue<RegionNode>(10, comparator);
	protected RegionNode endOfPath = null;
	
	public PathfindRegion(RegionNode startNode, RegionNode endNode) {
	
		this.startNode = startNode;
		this.endNode = endNode;
		addNodeToOpen(startNode, startNode.getG() + MyUtils.euclideanDistance(startNode.region.getCenter(), endNode.region.getCenter()));
		startNode.setG(0);
		
	}
	
	static class BestPathComparator implements Comparator<Node> {
		
		public int compare(Node node, Node node2) {
			
			return Double.compare(node.getF(), node2.getF());
			
		}
		
	}
	
	@Override
	public final void run() {
		
		VisualizationBase.VISUALIZATION_GUI.setRunButtonState(pause);
		searchForPath();
		
	}
	
	
	
	public void searchForPath() {
		
		RegionNode currentNode;
		
		do {
			
			synchronized (this) {
			
				try {
					
					while (pause) {
						
						this.wait();
						
					}
					
					if (VisualizationBase.sleepTimer > 0) {
					
						sleep(VisualizationBase.sleepTimer);
						
					}
					
				} catch (InterruptedException e) {}
				
			}
			
			currentNode = open.poll();
			
			if (currentNode.equals(endNode)) {
				
				break;
				
			}
			
			addNodeToClosed(currentNode);
			//VisualizationBase.VISUALIZATION_GUI.setOpenCounter(open.size());
			//VisualizationBase.VISUALIZATION_GUI.setClosedCounter(closed.size());
			HashSet<RegionNode> neighboringNodes = currentNode.findNeighboringNodes();
			expandedCounter++;
			
			for (RegionNode neighbor : neighboringNodes) {
				
				if (!closed.contains(neighbor)) {
					
					double tentitive_g = currentNode.getG() + MyUtils.euclideanDistance(currentNode.region.getCenter(), neighbor.region.getCenter());
					
					if (!open.contains(neighbor) | tentitive_g < neighbor.getG()) {
						
						neighbor.setParent(currentNode);
						neighbor.setG(tentitive_g);
						
						if (!open.contains(neighbor)) {
							
							addNodeToOpen(neighbor, neighbor.getG() + MyUtils.euclideanDistance(neighbor.region.getCenter(), endNode.region.getCenter()));
							//VisualizationBase.VISUALIZATION_WINDOW.registerChange(neighbor.region, 5000, new Color(255, 0, 0, 125));
							
						}
						
					}
					
				}
				
			}
			
		}
		
		while (!open.isEmpty() && !isExpandedCounterExceeded() && running);
		
		if (currentNode.region.equals(endNode.region)) {
			
			pathFound = true;
			endOfPath = currentNode;
			System.out.println("Path found through regions!");
			//VisualizationBase.VISUALIZATION_GUI.setPathLengthCounter(regions.size());
			
		} else if (isExpandedCounterExceeded()) {
			
			System.out.println("Region path could not be produced as counter expanded past the hard cap.");
			
		} else {
			
			System.out.println("Region path could not be produced as there are no more regionNodes to be searched.");
			
		}
		
		VisualizationBase.VISUALIZATION_GUI.setRunButtonState(true);
		running = false;
		
	}
	
	public final void togglePause() {
		
		synchronized(this) {
			
			if (pause) {
			
				pause = false;
				VisualizationBase.VISUALIZATION_GUI.setRunButtonState(pause);
				this.notify();
				
			} else {
				
				pause = true;
				VisualizationBase.VISUALIZATION_GUI.setRunButtonState(pause);
				
			}
			
		}
			
	}
	
	protected final boolean isRunning() {
		
		return running;
		
	}
	
	public final void end() {
		
		running = false;
		
	}
	
	protected final void addNodeToOpen(RegionNode node, double f) {
		
		node.setF(f);
		open.add(node);
		
	}
	
	protected final void addNodeToClosed(RegionNode node) {
		
		closed.add(node);
	
	}
	
	protected final void returnPath(Node endNode) {
		
		RegionNode currentNode = endOfPath;
		
		do {
			
			currentNode = currentNode.parentNode;
			
		}
		
		while (!currentNode.region.equals(startNode.region));
		
	}
	
	protected synchronized final boolean isPathFound() {
		
		try {
			
			notifyAll();
			
		} catch (IllegalMonitorStateException e) {}
		
		return pathFound;
		
	}
	
	public final Node path() {
		
		if (pathFound) {
			
			return endNode;
			
		}
		
		return null;
		
	}
	
	public synchronized final HashSet<Region> regionsAlongPath() {
		
		HashSet<Region> regionsList = new HashSet<Region>();
		
		if (!isPathFound()) {
		
			try {
				
				wait();
				
			} catch (InterruptedException e) {}
			
		}

		regionsList.add(startNode.region);
		RegionNode currentNode = endOfPath;
		
		if (currentNode == null) {
			
			return regionsList;
			
		}
		
		do {
			
			regionsList.add(currentNode.region);
			currentNode = currentNode.parentNode;
				
		}
			
		while (!currentNode.region.equals(startNode.region));
		
		return regionsList;
		
	}
	
	protected final double getExpandedCounter() {
		
		return expandedCounter;
		
	}
	
	protected final boolean isExpandedCounterExceeded() {
		
		return getExpandedCounter() > EXPANDED_COUNTER_HARD_CAP;
		
	}
	
}
