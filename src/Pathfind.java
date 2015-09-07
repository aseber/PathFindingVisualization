import java.awt.Color;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public abstract class Pathfind extends Thread { // Abstract class that all of the pathfinding algorithms are based on

	protected NodeBox startNode;
	protected NodeBox endNode;
	protected double expandedCounter;
	protected final double EXPANDED_COUNTER_HARD_CAP = 100000;
	protected boolean running = false;
	protected boolean pause = false;
	protected boolean pathFound = false;
	protected HashSet<Box> allowedBoxes;
	protected BestPathComparator comparator = new BestPathComparator();
	protected PriorityQueue<NodeBox> open = new PriorityQueue<NodeBox>(10, comparator);
	protected PriorityQueue<NodeBox> closed = new PriorityQueue<NodeBox>(10, comparator);
	protected NodeBox endOfPath = null;
	
	public Pathfind(NodeBox startNode, NodeBox endNode, double f) {
		
		this.startNode = startNode;
		this.endNode = endNode;
		addNodeToOpen(startNode, f);
		
	}
	
	//static abstract class BestPathComparator implements Comparator<Node> {};
	
	static class BestPathComparator implements Comparator<Node> {
		
		public int compare(Node node, Node node2) {
			
			return Double.compare(node.getF(), node2.getF());
			
		}
		
	}
	
	@Override
	public final void run() {
		
		VisualizationBase.VISUALIZATION_GUI.setRunButtonState(pause);
		running = true;
		searchForPath();
		
	}
	
	public abstract void searchForPath();
	
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
	
	protected final void addNodeToOpen(NodeBox node, double f) {
		
		node.setF(f);
		open.add(node);
		node.box.setFlag(Box.flags.QUEUED);
		
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
	
	protected final void addNodeToClosed(NodeBox node) {
		
		closed.add(node);
		
		double f = node.box.euclideanDistance(endNode.box)*0.85;
		double fStandard = startNode.box.euclideanDistance(endNode.box);
		int red = (int) (Math.round(Math.max(0, Math.min(200, 200*(f/fStandard))))*(1 - node.box.getWeight()));
		int green = (int) ((200 - red)*(1 - node.box.getWeight()));
		
		node.box.setColor(new Color(red, green, 0));
		
		node.box.setFlag(Box.flags.SEARCHED);
		
	}
	
	protected final void returnPath(NodeBox endNode) {
		
		NodeBox currentNode = endOfPath;
		
		do {
			
			currentNode.box.setFlag(Box.flags.SHORTEST_PATH);
			currentNode = currentNode.parentNode;
			
		}
		
		while (!currentNode.box.equals(startNode.box));
		
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
	
	public final HashSet<Box> boxesAlongPath() {
		
		HashSet<Box> boxesList = new HashSet<Box>();
		
		if (isPathFound()) {
		
			NodeBox currentNode = endOfPath;
			
			do {
				
				boxesList.add(currentNode.box);
				currentNode = currentNode.parentNode;
				
			}
			
			while (!currentNode.box.equals(startNode.box));
		
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
		
		HashSet<Box> boxesList = new HashSet<Box>();
		
		if (!isPathFound()) {
		
			try {
				
				wait();
				
			} catch (InterruptedException e) {}
			
		}
			
		NodeBox currentNode = endOfPath;
			
		do {
				
			boxesList.add(currentNode.box);
			currentNode = currentNode.parentNode;
				
		}
			
		while (!currentNode.box.equals(startNode.box));
		
		return boxesList;
		
	}
	
	protected final double getExpandedCounter() {
		
		return expandedCounter;
		
	}
	
	protected final boolean isExpandedCounterExceeded() {
		
		return getExpandedCounter() > EXPANDED_COUNTER_HARD_CAP;
		
	}
	
}
