package PathfindingAlgorithms;

import BoxSystem.Box;
import NodeSystem.INode;

import java.awt.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

import static BoxState.BoxState.QUEUED_BOX_STATE;
import static BoxState.BoxState.SEARCHED_BOX_STATE;
import static BoxState.BoxState.SHORTEST_PATH_BOX_STATE;
import static Settings.WindowSettings.SLEEP_TIMER;
import static Settings.WindowSettings.VISUALIZATION_GUI;

public abstract class Pathfind implements Runnable { // Abstract class that all of the pathfinding algorithms are based on

	public enum algorithms {
		
		ASTAR ("A-Star") {Pathfind pathfind(INode startNode, INode endNode) {return new PathfindAStar(startNode, endNode);}},
		DIJKSTRA ("Dijkstra") {Pathfind pathfind(INode startNode, INode endNode) {return new PathfindDijkstra(startNode, endNode);}},
		CUSTOM ("Custom") {Pathfind pathfind(INode startNode, INode endNode) {return new PathfindAStar(startNode, endNode);}};
		
		private final String name;
		
		algorithms(String newName) {
			
			name = newName;
			
		}
		
		abstract Pathfind pathfind(INode startNode, INode endNode);
		
		public String toString() {
			
			return name;
			
		}
		
	}

    protected INode startNode;
	protected INode endNode;
	protected double expandedCounter;
	protected final double EXPANDED_COUNTER_HARD_CAP = 100000;
	protected boolean running = false;
	protected boolean pause = false;
	protected boolean pathFound = false;
	protected HashSet<Box> allowedBoxes;
	protected BestPathComparator comparator = new BestPathComparator();
	protected PriorityQueue<INode> open = new PriorityQueue<>(10, comparator);
	protected PriorityQueue<INode> closed = new PriorityQueue<>(10, comparator);
	protected INode endOfPath = null;
	
	public Pathfind(INode startNode, INode endNode, double f) {
		
		this.startNode = startNode;
		this.endNode = endNode;
		addNodeToOpen(startNode, f);
		
	}
	
	//static abstract class BestPathComparator implements Comparator<NodeSystem.Node> {};
	
	static class BestPathComparator implements Comparator<INode> {
		
		public int compare(INode node, INode node2) {
			
			return Double.compare(node.getF(), node2.getF());
			
		}
		
	}
	
	public final void run() {
		
		VISUALIZATION_GUI.setRunButtonState(pause);
		running = true;
		searchForPath();
		
	}
	
	public final void checkWait() {
		
		try {
			
			while (pause) {
				
				this.wait(); // We can pause the simulation now!
				
			}
			
			if (SLEEP_TIMER > 0) {
			
				wait(SLEEP_TIMER); // Sleep timer
				
			}
			
		} catch (InterruptedException e) {}
		
	}
	
	public abstract void searchForPath();
	
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
		Box box = (Box) node.getObject();
		box.setFlag(QUEUED_BOX_STATE);
		
	}
	
	public final boolean isBoxInAllowedBoxes(Box box) {
		
		if (allowedBoxes == null) {
			
			return true;
			
		}
		
		if (allowedBoxes.isEmpty()) {
			
			return true;
			
		}
		
		return allowedBoxes.contains(box);
		
	}
	
	protected final void addNodeToClosed(INode node) {

        Box box = (Box) node.getObject();

		closed.add(node);
		
		double f = node.distanceFrom(endNode)*0.85;
		double fStandard = startNode.distanceFrom(endNode);
		int red = (int) (Math.round(Math.max(0, Math.min(200, 200*(f/fStandard))))*(1 - box.getWeight()));
		int green = (int) ((200 - red)*(1 - box.getWeight()));
		
//		box.setColor(new Color(red, green, 0));
		
		box.setFlag(SEARCHED_BOX_STATE);
		
	}
	
	protected final void returnPath(INode endNode) {
		
		INode currentNode = endOfPath;
        Box currentBox = (Box) currentNode.getObject();
		
		do {

            currentBox.setFlag(SHORTEST_PATH_BOX_STATE);
			currentNode = currentNode.getParent();
            currentBox = (Box) currentNode.getObject();
			
		}
		
		while (!currentBox.equals(startNode));
		
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
	
	public final HashSet<Box> boxesAlongPath() {
		
		HashSet<Box> boxesList = new HashSet<>();
		
		if (isPathFound()) {
		
			INode currentNode = endOfPath;
			
			do {
				
				boxesList.add((Box) currentNode.getObject());
				currentNode = currentNode.getParent();
				
			}
			
			while (!currentNode.equals(startNode));
		
		}

		return boxesList;
		
	}
	
	public void setAvailableRegion(HashSet<Box> boxes) {
		
		if (!isRunning()) {
		
			allowedBoxes = boxes;
		
		}
		
	}
	
	public synchronized final void waitForFinish() {
		
		if (!isPathFound()) {
			
			try {
				
				wait();
				
			} catch (InterruptedException e) {}
			
		}
		
	}
	
	public synchronized final HashSet<Box> boxesAlongPathBinding() {
		
		HashSet<Box> boxesList = new HashSet<>();
		
		if (!isPathFound()) {
		
			try {
				
				wait();
				
			} catch (InterruptedException e) {}
			
		}
			
		INode currentNode = endOfPath;
			
		do {
				
			boxesList.add((Box) currentNode.getObject());
			currentNode = currentNode.getParent();
				
		}
			
		while (!currentNode.equals(startNode));
		
		return boxesList;
		
	}
	
	protected final double getExpandedCounter() {
		
		return expandedCounter;
		
	}
	
	protected final boolean isExpandedCounterExceeded() {
		
		return getExpandedCounter() > EXPANDED_COUNTER_HARD_CAP;
		
	}
	
}
