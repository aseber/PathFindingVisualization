package RegionSystem;

import BoxSystem.Box;
import BoxSystem.BoxGrid;
import BoxSystem.Edge;
import NodeSystem.IDistance;
import Utilities.MyUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import static Settings.WindowSettings.*;

/*

public class Region implements IDistance {

	// A region is a collection of boxes in a N x N grid. Regions are defined by fixed points in space, and can be split in half if "barriers" break it up. In this case,
	// two regions will occupy the same fixed space as parts of the total N x N area. Regions can have an infinite number of edges, and neighbors are determined by 
	// first finding all region edges in each region, and comparing it to a master list of all edges and finding collisions.
	
	private Point physicalPosition = new Point();
	private Point windowPosition = new Point();
	private HashSet<Box> boxes = new HashSet<>();
	private HashSet<Edge> edges = new HashSet<>();
	private Point centerPosition = new Point();
	private HashSet<Region> neighboringRegions = new HashSet<Region>();

	private BoxGrid boxGrid = new BoxGrid();

	private static ArrayList<Edge> edgeList;
	private static RegionArrayList[][] regionMap;

	public Region(HashSet<Box> inputBoxes, Point phyPos, Point winPos) {
		
		if (inputBoxes.size() > Math.pow(REGION_SIZE, 2.0)) {
			
			throw new ArrayIndexOutOfBoundsException("Tried to fit too many boxes in a region. Change the maximum region size.");
			
		}
		
		physicalPosition = phyPos;
		windowPosition = winPos;
		
		for (Box currentBox : inputBoxes) {
			
			boxes.add(currentBox);
			centerPosition.setLocation(centerPosition.x + currentBox.physicalPosition.x, centerPosition.y + currentBox.physicalPosition.y);
			currentBox.setRegion(this);
			
		}
		
		centerPosition.setLocation(Math.round(centerPosition.x/boxes.size()), Math.round(centerPosition.y/boxes.size()));
		initializeEdges();		
		findNeighboringRegions();
		
		for (Region currentRegion : getNeighboringRegions()) {
			
			currentRegion.addNeighbor(this);
			
		}
		
		RegionArrayList thisRegion = regionMap[winPos.x][winPos.y];
		
		if (thisRegion == null) {
			
			thisRegion = new RegionArrayList();
			
		}
		
		thisRegion.add(this);
		regionMap[winPos.x][winPos.y] = thisRegion; // Necessary if null to begin with
		VISUALIZATION_WINDOW.registerChange(this, 1000, new Color(255, 255, 0, 125));
			
	}
	
	public static void initializeStaticVariables() {
		
		int size = (int) Math.ceil(((double) ROW_COLUMN_COUNT)/((double) REGION_SIZE));
		edgeList = new ArrayList<Edge>();
		regionMap = new RegionArrayList[size][size];
		
	}
	
	public Point getPhysicalPosition() {
		
		return physicalPosition;
		
	}
	
	public Point getCenter() {
		
		return centerPosition;
		
	}

	public double distanceFrom(IDistance distance) {

		if (distance instanceof Region) {

			Region region = (Region) distance;
			return MyUtils.euclideanDistance(this.getCenter(), region.getCenter());

		}

		throw new IllegalArgumentException("Cannot compare distance of unlike objects");

	}

	public void fillRegionDebugColor(Graphics g, Color color, AlphaComposite composite) { // Broken
		
		for (Box box : boxes) {
			
			box.drawBox(g, false, color, composite);
			
		}
		
		VISUALIZATION_WINDOW.repaint();
		
	}
	
	public void fillRegionXYDebugColor(Graphics g, Color color, AlphaComposite composite) {
		
		Point p1 = new Point(this.getPhysicalPosition());
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(color);
		g2.setComposite(composite);
		
		g.setColor(color);
		g.fillRect(p1.x, p1.y, BOX_XY_SIZE* REGION_SIZE, BOX_XY_SIZE* REGION_SIZE);
		
		VISUALIZATION_WINDOW.repaint();
		
	}
	
	public void drawRegionBorderDebugColor(Graphics g, BasicStroke brush, Color color) {
		
		Graphics2D g2 = (Graphics2D) g;
			
		for (Edge currentEdge : edges) {
				
			Point p1 = currentEdge.getStartPoint();
			Point p2 = currentEdge.getEndPoint();
			g2.setColor(color);
			g2.setStroke(brush);
			g2.drawLine(p1.x, p1.y, p2.x, p2.y);
				
		}
		
		VISUALIZATION_WINDOW.repaint();
		
	}
	
	public Point getWindowPosition() {
		
		return windowPosition;
		
	}
	
	public static int findClosestIndex(int pos) {
		
		int count = regionMap.length;
		double interval = REGION_SIZE* BOX_XY_SIZE;
		
		return MyUtils.clampInt(count - 1, (int) Math.floor(((double) pos)/interval), 0);
		
	}
	
	public static RegionArrayList getRegionsFromIndex(Point p1) {
		
		return regionMap[p1.x][p1.y];
		
	}
	
	public static RegionArrayList getRegionsFromIndex(int x, int y) {
		
		return regionMap[x][y];
		
	}
	
	public static RegionArrayList getRegionsFromPosition(int x, int y) {
		
		int boxX = findClosestIndex(x);
		int boxY = findClosestIndex(y);
		return getRegionsFromIndex(boxX, boxY);
		
	}
	
	public static RegionArrayList getRegionsFromPosition(Point p1) {
		
		return getRegionsFromPosition(p1.x, p1.y);
		
	}
	
	public synchronized void delete() {

		Iterator<Edge> edgeListIterator = edgeList.iterator();
		
		while (!edges.isEmpty() && edgeListIterator.hasNext()) { 
		
			Edge currentEdge = edgeListIterator.next();
			
			if (currentEdge.getBox().getRegion() == this) {
				
				edgeListIterator.remove();
				edges.remove(currentEdge);
				
			}
			
		}
		
		for (Box box : boxes) {
			
			box.clearRegion();
			
		}
		
		for (Region region : getNeighboringRegions()) {
			
			region.removeNeighbor(this);
			
		}
		
	}
	
	public synchronized void removeNeighbor(Region region) {
		
		neighboringRegions.remove(region);
		
	}
	
	public synchronized void addNeighbor(Region region) {
		
		neighboringRegions.add(region);
		
	}
	
	public synchronized static void deleteRegionsFromIndex(int x, int y) {
		
		RegionArrayList list = getRegionsFromIndex(x, y);
		
		while (!list.isEmpty()) {
			
			list.remove(0).delete();
			
		}
		
	}
	
	public synchronized static void deleteRegionsFromIndex(Point p1) {
		
		deleteRegionsFromIndex(p1.x, p1.y);
		
	}
	
	public HashSet<Edge> getEdges() { // Used to paint the edges of this particular region a certain color.
		
		return edges;
		
	}
	
	public HashSet<Box> getBoxes() {
		
		return boxes;
		
	}
	
	private synchronized void initializeEdges() { // We iterate through each boxes edges and keep only ones that are found one time.
													// If an edge is found twice, it indicates that the edge is internal to the region and not part of the regions edge
		
		for (Box currentBox : boxes) {
			
			for (Edge currentEdge : currentBox.getEdges()) {
				
				if (edges.contains(currentEdge)) {
					
					edges.remove(currentEdge);
					continue;
					
				}
				
				edges.add(currentEdge);
				
			}
			
		}
		
		edgeList.addAll(edges);
		
	}
	
	//public 
	
	public synchronized static void createRegionFromIndex(int x, int y) {
		
		Point p1 = new Point(x* REGION_SIZE, y* REGION_SIZE);
		Point p2 = new Point((x + 1)* REGION_SIZE - 1, (y + 1)* REGION_SIZE - 1);
		HashSet<Box> availableBoxes = Box.boxesInIndicesSquare(p1, p2);
		HashSet<Box> barrierBoxes = new HashSet<Box>();
				
		for (Box currentBox : availableBoxes) {
			
			if (currentBox.isFullBarrier()) {
					
				barrierBoxes.add(currentBox);
				
			}
				
		}
		
		availableBoxes.removeAll(barrierBoxes);
		Iterator<Box> iterator = availableBoxes.iterator();
				
		while (iterator.hasNext()) {
					
			Point physicalLocation = new Point(p1.x* BOX_XY_SIZE, p1.y* BOX_XY_SIZE);
			Point windowLocation = new Point(x, y);
			HashSet<Box> island = Box.floodFill(iterator.next(), availableBoxes);
			new Region(island, physicalLocation, windowLocation);
			iterator = null;
			availableBoxes.removeAll(island);
			iterator = availableBoxes.iterator();
		
		}
		
	}
	
	public synchronized static void createRegionFromIndex(Point p1) {
		
		createRegionFromIndex(p1.x, p1.y);
		
	}
	
	public synchronized static void createRegionField() {
		
		int regionXYSize = (int) Math.ceil(((double) ROW_COLUMN_COUNT)/((double) REGION_SIZE));
		
		for (int row = 0; row < regionXYSize; row++) {
			
			for (int column = 0; column < regionXYSize; column++) {
				
				createRegionFromIndex(row, column);
				
			}
			
		}
		
	}
	
	public synchronized static void rebuildRegionFromIndex(int x, int y) {
		
		deleteRegionsFromIndex(x, y);
		createRegionFromIndex(x, y);
		
	}
	
	public synchronized static void rebuildRegionFromIndex(Point p1) {
		
		rebuildRegionFromIndex(p1.x, p1.y);
		
	}
	
	public synchronized HashSet<Region> getNeighboringRegions() {
		
		return neighboringRegions;
		
	}
	
	private synchronized void findNeighboringRegions() { // This code originally ran in 36 ms, dropped it to 6 and now 1 ms thanks to HashSet.contains(o)
	
		HashSet<Region> neighbors = new HashSet<Region>();
		
		for (Edge foreignEdge : edgeList) {
			
			if (edges.contains(foreignEdge)) {
				
				neighbors.add(foreignEdge.getBox().getRegion());
				
			}
			
		}

		neighbors.remove(this);
		
		neighboringRegions = neighbors;
		
	}
	
	public synchronized static void removeAllRegions() {
		
		for (RegionArrayList[] bigList : regionMap) {
			
			for (RegionArrayList smallList : bigList) {
				
				if (smallList != null) {
				
					for (Region region : smallList) {
						
						region.delete();
						
					}
				
				}
				
			}
			
		}
		
		initializeStaticVariables();
		
	}
	
	public boolean regionContains(Box box) {
		
		return boxes.contains(box);
		
	}
	
	public String toString() {
		
		return "Pos: [" + windowPosition.x + ", " + windowPosition.y + "] | Size = " + boxes.size();
		
	}

}

*/
