package PathfindingAlgorithms;

import NodeSystem.INode;
import RegionSystem.Region;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

import static Settings.WindowSettings.SLEEP_TIMER;
import static Settings.WindowSettings.VISUALIZATION_GUI;

public class PathfindRegion extends Thread {

	protected INode startNode;
	protected INode endNode;
	protected double expandedCounter;
	protected final double EXPANDED_COUNTER_HARD_CAP = 100000;
	protected boolean running = true;
	protected boolean pause = false;
	protected boolean pathFound = false;
	protected BestPathComparator comparator = new BestPathComparator();
	protected PriorityQueue<INode> open = new PriorityQueue<>(10, comparator);
	protected PriorityQueue<INode> closed = new PriorityQueue<>(10, comparator);
	protected INode endOfPath = null;
	
	public PathfindRegion(INode startNode, INode endNode) {
	
		this.startNode = startNode;
		this.endNode = endNode;
		addNodeToOpen(startNode, startNode.getG() + startNode.distanceFrom(endNode));
		startNode.setG(0);
		
	}
	
	static class BestPathComparator implements Comparator<INode> {

		public int compare(INode node, INode node2) {

			return Double.compare(node.getF(), node2.getF());
			
		}
		
	}
	
	@Override
	public final void run() {
		
		VISUALIZATION_GUI.setRunButtonState(pause);
		searchForPath();
		
	}
	
	
	
	public void searchForPath() {

		INode currentNode;
		
		do {
			
			synchronized (this) {
			
				try {
					
					while (pause) {
						
						this.wait();
						
					}
					
					if (SLEEP_TIMER > 0) {
					
						sleep(SLEEP_TIMER);
						
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
			HashSet<INode> neighboringNodes = currentNode.findNeighboringNodes();
			expandedCounter++;
			
			for (INode neighbor : neighboringNodes) {
				
				if (!closed.contains(neighbor)) {
					
					double tentitive_g = currentNode.getG() + currentNode.distanceFrom(neighbor);
					
					if (!open.contains(neighbor) | tentitive_g < neighbor.getG()) {
						
						neighbor.setParent(currentNode);
						neighbor.setG(tentitive_g);
						
						if (!open.contains(neighbor)) {
							
							addNodeToOpen(neighbor, neighbor.getG() + neighbor.distanceFrom(endNode));
							//VisualizationBase.VISUALIZATION_WINDOW.registerChange(neighbor.region, 5000, new Color(255, 0, 0, 125));
							
						}
						
					}
					
				}
				
			}
			
		}
		
		while (!open.isEmpty() && !isExpandedCounterExceeded() && running);
		
		if (currentNode.equals(endNode)) {
			
			pathFound = true;
			endOfPath = currentNode;
			System.out.println("PathfindingAlgorithms.Path found through regions!");
			//VisualizationBase.VISUALIZATION_GUI.setPathLengthCounter(regions.size());
			
		} else if (isExpandedCounterExceeded()) {
			
			System.out.println("RegionSystem.Region path could not be produced as counter expanded past the hard cap.");
			
		} else {
			
			System.out.println("RegionSystem.Region path could not be produced as there are no more regionNodes to be searched.");
			
		}
		
		VISUALIZATION_GUI.setRunButtonState(true);
		running = false;
		
	}
	
	public final void togglePause() {
		
		synchronized(this) {
			
			if (pause) {
			
				pause = false;
				VISUALIZATION_GUI.setRunButtonState(pause);
				this.notify();
				
			} else {
				
				pause = true;
				VISUALIZATION_GUI.setRunButtonState(pause);
				
			}
			
		}
			
	}
	
	protected final boolean isRunning() {
		
		return running;
		
	}
	
	public final void end() {
		
		running = false;
		
	}
	
	protected final void addNodeToOpen(INode node, double f) {
		
		node.setF(f);
		open.add(node);
		
	}
	
	protected final void addNodeToClosed(INode node) {
		
		closed.add(node);
	
	}
	
	protected final void returnPath(INode endNode) {

		INode currentNode = endOfPath;
		
		do {
			
			currentNode = currentNode.getParent();
			
		}
		
		while (!currentNode.equals(startNode));
		
	}
	
	protected synchronized final boolean isPathFound() {
		
		try {
			
			notifyAll();
			
		} catch (IllegalMonitorStateException e) {}
		
		return pathFound;
		
	}
	
	public final INode path() {
		
		if (pathFound) {
			
			return endNode;
			
		}
		
		return null;
		
	}
	
	public synchronized final HashSet<Region> regionsAlongPath() {
		
		HashSet<Region> regionsList = new HashSet<>();
		
		if (!isPathFound()) {
		
			try {
				
				wait();
				
			} catch (InterruptedException e) {}
			
		}

		regionsList.add((Region) startNode.getObject());
		INode currentNode = endOfPath;
		
		if (currentNode == null) {
			
			return regionsList;
			
		}
		
		do {
			
			regionsList.add((Region) currentNode.getObject());
			currentNode = currentNode.getParent();
				
		}
			
		while (!currentNode.equals(startNode));
		
		return regionsList;
		
	}
	
	protected final double getExpandedCounter() {
		
		return expandedCounter;
		
	}
	
	protected final boolean isExpandedCounterExceeded() {
		
		return getExpandedCounter() > EXPANDED_COUNTER_HARD_CAP;
		
	}
	
}
