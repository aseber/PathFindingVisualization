package BoxSystem;

import BoxState.IBoxState;
import NodeSystem.IDistance;
//import RegionSystem.Region;
import Utilities.MyUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import static BoxState.BoxState.BARRIER_STATE;
import static BoxState.BoxState.STANDARD_STATE;
import static Settings.WindowSettings.*;

public class Box implements IDistance {

	// The box is the smallest unit on the graph. Each box has four neighbors, corresponding to the four cardinal directions
	// A box can have a weight between 0.0 and 1.0, where 0.0 is considered "open" and 1.0 a "full barrier", values inbetween
	// are considered weighted and change the way A-Star and Dijkstras algorithm rate each box
	
	public enum corners {
		
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT

    }
	
	private static Box selectedBox;
	static public Box startBox;
	static public Box endBox;

	public IBoxState boxState;
	public Point physicalPosition = new Point();
	private Point windowPosition = new Point();
	private double weight = 0.0;
	private boolean selected = false;
	private HashSet<Edge> edges = new HashSet<>();
//	private Region region = null;
	
	public Box(int x, int y, int window_X_Index, int window_Y_Index, IBoxState state) {
		
		this.physicalPosition.setLocation(x, y);
		this.windowPosition.setLocation(window_X_Index, window_Y_Index);
//		boxMap[window_X_Index][window_Y_Index] = this;
		setWeight(0.0);
        setState(state);
		edgeInitialization();
		
	}
	
	public void setPosition(int x, int y) {
		
		this.physicalPosition.setLocation(x, y);
		edgeInitialization();
		
	}

	public void setState(IBoxState state) {

		boxState = state;

	}

	public IBoxState getState() {

		return boxState;

	}

	private void setWeight(double newWeight) {
		
		weight = Math.max(0.0, Math.min(1.0, newWeight));
//		VISUALIZATION_WINDOW.repaint(this);
		
	}
	
//	public void setRegion(Region region) {
		
//		region = region;
		
//	}
	
//	public Region getRegion() {
		
//		return region;
		
//	}
	
//	public void clearRegion() {
		
//		region = null;
		
//	}
	
	public double getWeight() {
		
		return weight;
		
	}
	
	public void delete() {
		
		edges = null;
//		clearRegion();
		
	}
	
	public static void resetStartBox() {
		
		if (startBox != null) {
		
			Box oldStartBox = startBox;
			startBox = null;
			oldStartBox.setFlag(STANDARD_STATE);
			
		}
		
	}
	
	public static void resetEndBox() {
		
		if (endBox != null) {
		
			Box oldEndBox = endBox;
			endBox = null;
			oldEndBox.setFlag(STANDARD_STATE);
			
		}
		
	}
	
	private void edgeInitialization() {
		
		Object[][] offset = {{corners.TOP_LEFT, Edge.directions.RIGHT}, {corners.TOP_LEFT, Edge.directions.DOWN}, {corners.TOP_RIGHT, Edge.directions.DOWN}, {corners.BOTTOM_LEFT, Edge.directions.RIGHT}}; // Use HashMap but done wrong?
		
		Point point = null;
		Edge edge = null;
		byte length = 1;
		
		for (int i = 0; i < 4; i++) {
			
			point = getPhysicalCorner((corners) offset[i][0]);
			edge = new Edge(this, point, (Edge.directions) offset[i][1], length);
			edges.add(edge);
			
		}
		
	}
	
	public HashSet<Edge> getEdges() {
		
		return edges;
		
	}
	
	private Point getPhysicalCorner(corners cornerFlag) {
		
		Point point = new Point();
		
		if (cornerFlag == corners.TOP_LEFT) {
			
			return physicalPosition;
			
		}
		
		else if (cornerFlag == corners.TOP_RIGHT) {
			
			point.setLocation(physicalPosition.getX() + BOX_XY_SIZE, physicalPosition.getY() + 0);
			return point;
			
		}
		
		else if (cornerFlag == corners.BOTTOM_LEFT) {
			
			point.setLocation(physicalPosition.getX() + 0, physicalPosition.getY() + BOX_XY_SIZE);
			return point;
			
		}
		
		else if (cornerFlag == corners.BOTTOM_RIGHT) {
			
			point.setLocation(physicalPosition.getX() + BOX_XY_SIZE, physicalPosition.getY() + BOX_XY_SIZE);
			return point;
			
		}
		
		return null;
		
	}
	
	public Point getCenterPoint() {
		
		return new Point((int) (physicalPosition.x + Math.floor((double) BOX_XY_SIZE/2.0)), (int) (physicalPosition.y + (Math.floor((double) BOX_XY_SIZE/2.0))));
		
	}
	
	private Color getActiveColor() {
		
//		if (selected) {
			
//			return getFlag().SELECTED.getColor();

//		}
		
		/*else if (this.flag == flags.PARTIAL_BARRIER || this.flag == flags.SEARCHED) {
		
			return color;

		}*/
		
//		else {
		
			return this.getFlag().getColor(this);
			
//		}
		
	}
	
	public void drawBox(Graphics g, boolean drawBorder) {
		
		int x = (int) this.physicalPosition.getX();
		int y = (int) this.physicalPosition.getY();
		int sizeX = BOX_XY_SIZE;
		int sizeY = BOX_XY_SIZE;
		Color color = this.getActiveColor();
		
		g.setColor(color);
		g.fillRect(x, y, sizeX, sizeY);
		
		if (drawBorder) {
		
			g.setColor(Color.BLACK);
			g.drawRect(x, y, sizeX, sizeY);
			
		}
		
		/*Graphics2D g2d = (Graphics2D) g; // Interesting code but slow to run
		
		BufferedImage boxImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		Graphics2D boxGraphic = boxImage.createGraphics();
		Rectangle wholeRectangle = new Rectangle(0, 0, 10, 10);
		Rectangle drawRectangle = new Rectangle(0, 0, 10, 10);
		Rectangle fillRectangle = new Rectangle(1, 1, 9, 9);
		
		boxGraphic.setColor(Color.WHITE);
		boxGraphic.fill(wholeRectangle);
		boxGraphic.setColor(Color.BLACK);
		boxGraphic.draw(drawRectangle);
		boxGraphic.setColor(currentBox.getActiveColor());
		boxGraphic.fill(fillRectangle);
		
		AffineTransform t = new AffineTransform();
		t.translate(currentBox.x, currentBox.y);
		t.scale(currentBox.sizeX/10, currentBox.sizeY/10);
		g2d.drawImage(boxImage, t, null);*/

	}
	
	public void drawBox(Graphics g, boolean drawBorder, Color color, AlphaComposite composite) {
		
		int x = (int) this.physicalPosition.getX();
		int y = (int) this.physicalPosition.getY();
		int sizeX = BOX_XY_SIZE;
		int sizeY = BOX_XY_SIZE;
		
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(color);
		g2.setComposite(composite);
		
		g.setColor(color);
		g.fillRect(x, y, sizeX, sizeY);
		
		if (drawBorder) {
		
			g.setColor(Color.BLACK);
			g.drawRect(x, y, sizeX, sizeY);
			
		}
		
	}
	
	public static HashSet<Box> floodFill(Box startBox, HashSet<Box> allowedBoxes) { // Used to find regions known as "islands"
		
		HashSet<Box> filledBoxes = new HashSet<Box>();
		HashSet<Box> nodesToVisit = new HashSet<Box>();
		Iterator<Box> iterator = null;
		nodesToVisit.add(startBox);
		
		while (!nodesToVisit.isEmpty()) {
			
			iterator = nodesToVisit.iterator(); 
			Box currentBox = iterator.next();
			iterator.remove();
			iterator = null;
			filledBoxes.add(currentBox);
			HashSet<Box> neighbors = currentBox.findNeighboringBoxes(1);
			
			for (Box neighbor : neighbors) {
				
				if (!filledBoxes.contains(neighbor) && allowedBoxes.contains(neighbor)) {
					
					nodesToVisit.add(neighbor);
					
				}
				
			}
			
		}
		
		return filledBoxes;
		
	}
	
	public static void setFlags(HashSet<Box> boxes, IBoxState flag) {
		
		for (Box box : boxes) {
			
			box.setFlag(flag);
			
		}
		
	}
	
	public static void setWeights(HashSet<Box> boxes, double weight) {
		
		for (Box box : boxes) {
			
			box.setWeight(weight);
			
		}
		
	}
	
	public void setSelected() {
		
		if (selectedBox != this) {
			
			this.selected = true;
			
			try {
				
				selectedBox.setUnselected();
				
			} catch (NullPointerException e) {}
			
			selectedBox = this;
			
		}
		
		VISUALIZATION_WINDOW.repaint(this);
		
	}
	
	private void setUnselected() {
		
		this.selected = false;
		VISUALIZATION_WINDOW.repaint(this);
		
	}
	
	public IBoxState getFlag() {

	    return boxState;

    }

	public void setFlag(IBoxState newFlag) {

		this.boxState = newFlag;

	}

		/*if (this.getFlag() == newFlag) {
			
			return;
			
		}
		
		if (isFullBarrier()) {
			
			this.flag = newFlag;
			Point regionWindowPosition = new Point(Region.findClosestIndex(getCenterPoint().x), Region.findClosestIndex(getCenterPoint().y));
			Region.rebuildRegionFromIndex(regionWindowPosition);
			
		}
		
		if (newFlag == flags.START) {
			
			resetStartBox();
			this.flag = flags.START;
			startBox = this;
			checkBeginningAndEndState();
			
		}
		
		else if (newFlag == flags.END) {
			
			resetEndBox();
			this.flag = flags.END;
			endBox = this;
			checkBeginningAndEndState();
			
		}
		
		else if (newFlag == flags.FULL_BARRIER) {
			
			weight = 1.0;
			this.flag = newFlag;
			Point regionWindowPosition = new Point(Region.findClosestIndex(getCenterPoint().x), Region.findClosestIndex(getCenterPoint().y));
			Region.rebuildRegionFromIndex(regionWindowPosition);
			
		}
		
		else if (newFlag == flags.PARTIAL_BARRIER) {
			
			int value = (int) (200 - weight*150);
			color = new Color(value, value, value);
			
		}
		
		else if (newFlag == flags.STANDARD) {
			
			weight = 0.0;
			
		}
		
		if (this != startBox && this != endBox) {
		
			this.flag = newFlag;
			
		}
		
		VISUALIZATION_WINDOW.repaint(this);
		
	}*/
	
	public static boolean beginningAndEndExist() {

        return startBox != null && endBox != null;

    }
	
	public static void checkBeginningAndEndState() {
		
		VISUALIZATION_GUI.setRunButtonEnabledState(beginningAndEndExist());
		
	}
	
	public synchronized HashSet<Box> findNeighboringBoxes(int order) { // Nth order neighbors, I hate how this was implemented but it works, sort of
		
		//long startTimeMilli = System.currentTimeMillis();
		//long startTimeNano = System.nanoTime();
		
		HashSet<Box> neighbors = new HashSet<Box>();
		ArrayList<HashSet<Box>> expansion = new ArrayList<HashSet<Box>>();
		
		int[][] offsetArray = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
		
		if (order >= 1) {
		
			for (int i = 0; i < 4; i++) {
				
				int edge_X = (int) windowPosition.getX() + offsetArray[i][0];
				int edge_Y = (int) windowPosition.getY() + offsetArray[i][1];
				
				if (edge_X >= 0 && edge_X < ROW_COLUMN_COUNT && edge_Y >= 0 && edge_Y < ROW_COLUMN_COUNT) {
					
//					Box currentNeighbor = boxMap[edge_X][edge_Y];
//					neighbors.add(currentNeighbor);
					
//					if (order > 1) {
					
//						expansion.add(currentNeighbor.findNeighboringBoxes(order - 1, this));
					
//					}
					
				}
				
			}
		
		}
		
		for (HashSet<Box> set : expansion) {
			
			neighbors.addAll(set);
			
		}
		
		//System.out.println("Delta time: " + (System.currentTimeMillis() - startTimeMilli) + " ms");
		//System.out.println("Delta time: " + (System.nanoTime() - startTimeNano) + " ns");
		
		return neighbors;
		
	}
	
	private synchronized HashSet<Box> findNeighboringBoxes(int order, Box parentBox) {
		
		HashSet<Box> neighbors = new HashSet<Box>();
		ArrayList<HashSet<Box>> expansion = new ArrayList<HashSet<Box>>();
		
		if (order >= 1) {
			
			HashSet<Box> neighborBoxes = this.findNeighboringBoxes(1);
			
			for (Box currentNeighbor : neighborBoxes) {
				
				if (currentNeighbor != parentBox) {
					
					neighbors.add(currentNeighbor);
					
					if (order > 1) {
						
						expansion.add(currentNeighbor.findNeighboringBoxes(order - 1, this));
					
					}
					
				}
				
			}
			
		}
		
		for (HashSet<Box> set : expansion) {
			
			neighbors.addAll(set);
			
		}
		
		return neighbors;
		
	}
	
	public boolean isFullBarrier() {

        return getFlag() == BARRIER_STATE;

    }
	
	public boolean isPartialBarrier() {

        return getFlag() == BARRIER_STATE;

    }
	
	public double distanceFrom(IDistance distance) {

		if (distance instanceof Box) {

		    Box box = (Box) distance;
            return MyUtils.euclideanDistance(this.windowPosition, box.windowPosition);

        }

        throw new IllegalArgumentException("Cannot compare distance of unlike objects");

	}
	
	@Override
	public boolean equals(Object o) {
		
		Box box = (Box) o;

        return this.windowPosition.getX() == box.windowPosition.getX() && this.windowPosition.getY() == box.windowPosition.getY();

    }

	
	public String toString() {
		
		return "BoxSystem.Box[" + windowPosition.getX() + ", " + windowPosition.getY() + "]";
		
	}
	
	public String flagToString(int flag) {
		
		return this.getFlag().toString();
		
	}
	
}
