import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Box {

	// The box is the smallest unit on the graph. Each box has four neighbors, corresponding to the four cardinal directions
	// A box can have a weight between 0.0 and 1.0, where 0.0 is considered "open" and 1.0 a "full barrier", values inbetween
	// are considered weighted and change the way A-Star and Dijkstras algorithm rate each box
	
	static protected Box selectedBox;
	static protected Box startBox;
	static protected Box endBox;
	
	protected Point physicalPosition = new Point();
	protected Point windowPosition = new Point();
	private double weight = 0.0;
	private Color color = DEFAULT_COLOR;
	private boolean selected = false;
	private int flag = BOX_STANDARD_FLAG;
	private HashSet<Edge> edges = new HashSet<Edge>();
	private Region region = null;
	
	private static Box[][] boxMap;
	
	static final int BOX_STANDARD_FLAG = 0;
	static final int BOX_SEARCHED_FLAG = 1;
	static final int BOX_PARTIAL_BARRIER_FLAG = 2;
	static final int BOX_FULL_BARRIER_FLAG = 3;
	static final int BOX_SHORTEST_PATH_FLAG = 4;
	static final int BOX_START_FLAG = 5;
	static final int BOX_END_FLAG = 6;
	static final int BOX_QUEUED_FLAG = 7;
	static final Color DEFAULT_COLOR = new Color(180, 40, 220);
	static final Color STANDARD_COLOR = new Color(200, 200, 200);
	static final Color BARRIER_COLOR = new Color(10, 10, 10);
	static final Color SEARCHED_COLOR = new Color(200, 0, 0);
	static final Color SHORTEST_PATH_COLOR = new Color(0, 0, 255);
	static final Color START_COLOR = new Color(0, 0, 0);
	static final Color END_COLOR = new Color(255, 255, 255);
	static final Color BOX_QUEUED_COLOR = new Color(155, 155, 155);
	static final Color SELECTED_COLOR = new Color(100, 100, 255);
	
	static final int TOP_LEFT = 0;
	static final int TOP_RIGHT = 1;
	static final int BOTTOM_LEFT = 2;
	static final int BOTTOM_RIGHT = 3;
	
	static final byte DIRECTION_DOWN = 0;
	static final byte DIRECTION_RIGHT = 1;
	
	public Box(int x, int y, int window_X_Index, int window_Y_Index, int flag) {
		
		this.physicalPosition.setLocation(x, y);
		this.windowPosition.setLocation(window_X_Index, window_Y_Index);
		boxMap[window_X_Index][window_Y_Index] = this;
		setWeight(0.0);
		setFlag(flag);
		edgeInitialization();
		
	}
	
	public static void initializeStaticVariables() {
		
		boxMap = new Box[VisualizationBase.ROW_COLUMN_COUNT][VisualizationBase.ROW_COLUMN_COUNT];
		
	}
	
	public void setPosition(int x, int y) {
		
		this.physicalPosition.setLocation(x, y);
		edgeInitialization();
		
	}
	
	public void setWeight(double newWeight) {
		
		weight = Math.max(0.0, Math.min(1.0, newWeight));
		int value = (int) (200 - 150*weight);
		Color color = new Color(value, value, value);
		setColor(color);
		
		if (weight == 0.0) {
			
			setFlag(BOX_STANDARD_FLAG);
			
		}
		
		else if (weight == 1.0) {
			
			setFlag(BOX_FULL_BARRIER_FLAG);
			
		}
		
		else {
			
			setFlag(BOX_PARTIAL_BARRIER_FLAG);
			
		}
		
		VisualizationBase.VISUALIZATION_WINDOW.repaint(this);
		
		
	}
	
	public void setRegion(Region r) {
		
		region = r;
		
	}
	
	public Region getRegion() {
		
		return region;
		
	}
	
	public void clearRegion() {
		
		region = null;
		
	}
	
	public double getWeight() {
		
		return weight;
		
	}
	
	public static void createBoxField() {		
		
		for (int row = 0; row < VisualizationBase.ROW_COLUMN_COUNT; row++) {
			
			for (int column = 0; column < VisualizationBase.ROW_COLUMN_COUNT; column++) {
			
				int x = (int) (row*(VisualizationBase.boxXYSize));
				int y = (int) (column*(VisualizationBase.boxXYSize));
				new Box(x, y, row, column, Box.BOX_STANDARD_FLAG);
			
			}
		
		}
		
	}
	
	public void delete() {
		
		edges = null;
		clearRegion();
		
	}
	
	public static void removeAllBoxes() {
		
		for (Box currentBox : getAllBoxes()) {
			
			currentBox.delete();
			
		}
		
		initializeStaticVariables();
		
	}
	
	public synchronized static Box getBoxFromIndex(Point p1) {
		
		return boxMap[p1.x][p1.y];
		
	}
	
	public synchronized static Box getBoxFromIndex(int x, int y) {
		
		return boxMap[x][y];
		
	}
	
	public static void resetStartBox() {
		
		if (startBox != null) {
		
			Box oldStartBox = startBox;
			startBox = null;
			oldStartBox.setFlag(BOX_STANDARD_FLAG);
			
		}
		
	}
	
	public static void resetEndBox() {
		
		if (endBox != null) {
		
			Box oldEndBox = endBox;
			endBox = null;
			oldEndBox.setFlag(BOX_STANDARD_FLAG);
			
		}
		
	}
	
	public void edgeInitialization() {
		
		byte[][] offset = {{TOP_LEFT, DIRECTION_RIGHT}, {TOP_LEFT, DIRECTION_DOWN}, {TOP_RIGHT, DIRECTION_DOWN}, {BOTTOM_LEFT, DIRECTION_RIGHT}}; // Use HashMap but done wrong?
		
		Point point = null;
		Edge edge = null;
		byte length = 1;
		
		for (int i = 0; i < 4; i++) {
			
			
			point = getPhysicalCorner(offset[i][0]);
			edge = new Edge(this, point, offset[i][1], length);
			edges.add(edge);
			
		}
		
	}
	
	public HashSet<Edge> getEdges() {
		
		return edges;
		
	}
	
	public Point getPhysicalCorner(int cornerFlag) {
		
		Point point = new Point();
		
		if (cornerFlag == TOP_LEFT) {
			
			return physicalPosition;
			
		}
		
		else if (cornerFlag == TOP_RIGHT) {
			
			point.setLocation(physicalPosition.getX() + VisualizationBase.boxXYSize, physicalPosition.getY() + 0);
			return point;
			
		}
		
		else if (cornerFlag == BOTTOM_LEFT) {
			
			point.setLocation(physicalPosition.getX() + 0, physicalPosition.getY() + VisualizationBase.boxXYSize);
			return point;
			
		}
		
		else if (cornerFlag == BOTTOM_RIGHT) {
			
			point.setLocation(physicalPosition.getX() + VisualizationBase.boxXYSize, physicalPosition.getY() + VisualizationBase.boxXYSize);
			return point;
			
		}
		
		return null;
		
	}
	
	public Point getCenterPoint() {
		
		return new Point((int) (physicalPosition.x + Math.floor((double) VisualizationBase.boxXYSize/2.0)), (int) (physicalPosition.y + (Math.floor((double) VisualizationBase.boxXYSize/2.0))));
		
	}
	
	public Color getActiveColor() {
		
		if (selected) {
			
			return SELECTED_COLOR;
			
		} 
		
		else if (this.flag == BOX_PARTIAL_BARRIER_FLAG || this.flag == BOX_SEARCHED_FLAG) {
		
			return color;
			
		}
		
		else {
		
			switch(this.getFlag()) {
				
				case BOX_STANDARD_FLAG:
					
					return STANDARD_COLOR;
			
				case BOX_FULL_BARRIER_FLAG:
				
					return BARRIER_COLOR;
				
				case BOX_START_FLAG:
					
					return START_COLOR;
				
				case BOX_END_FLAG:
					
					return END_COLOR;
					
				case BOX_SHORTEST_PATH_FLAG:
						
					return SHORTEST_PATH_COLOR;
						
				case BOX_QUEUED_FLAG:
						
					return BOX_QUEUED_COLOR;
				
			}
			
		}
		
		return DEFAULT_COLOR;
		
	}
	
	public void setColor(Color color) {
		
		this.color = color;
		
	}
	
	public int getFlag() {
		
		return flag;
		
	}
	
	public void drawBox(Graphics g, boolean drawBorder) {
		
		int x = (int) this.physicalPosition.getX();
		int y = (int) this.physicalPosition.getY();
		int sizeX = (int) VisualizationBase.boxXYSize;
		int sizeY = (int) VisualizationBase.boxXYSize;
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
		int sizeX = (int) VisualizationBase.boxXYSize;
		int sizeY = (int) VisualizationBase.boxXYSize;
		
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
	
	public static HashSet<Box> boxesInPhysicalSquare(Point p1, Point p2) { // Used to find each regions initial boxes
		
		Point newP1 = new Point(findClosestIndex(p1.x), findClosestIndex(p1.y));
		Point newP2 = new Point(findClosestIndex(p2.x), findClosestIndex(p2.y));
		
		return boxesInIndicesSquare(newP1, newP2);
		
	}
	
	public static HashSet<Box> boxesInPhysicalSquare(int p1x, int p1y, int p2x, int p2y) {
		
		Point newP1 = new Point(findClosestIndex(p1x), findClosestIndex(p1y));
		Point newP2 = new Point(findClosestIndex(p2x), findClosestIndex(p2y));
		
		return boxesInIndicesSquare(newP1, newP2);
		
	}
	
	public static HashSet<Box> boxesInIndicesSquare(int p1x, int p1y, int p2x, int p2y) {
		
		Point newP1 = new Point(p1x, p1y);
		Point newP2 = new Point(p2x, p2y);
		
		return boxesInIndicesSquare(newP1, newP2);
		
	}
	
	public static HashSet<Box> boxesInIndicesSquare(Point p1, Point p2) {
		
		HashSet<Box> boxSet = new HashSet<Box>();
		
		Point newP1 = new Point(MyUtils.clampInt(VisualizationBase.ROW_COLUMN_COUNT - 1, p1.x, 0), MyUtils.clampInt(VisualizationBase.ROW_COLUMN_COUNT - 1, p1.y, 0));
		Point newP2 = new Point(MyUtils.clampInt(VisualizationBase.ROW_COLUMN_COUNT - 1, p2.x, 0), MyUtils.clampInt(VisualizationBase.ROW_COLUMN_COUNT - 1, p2.y, 0));
		
		 if (newP1.getX() > newP2.getX()) { // Implies we have a box with the wrong corners, we need to calculate the standard corners now.
			
			double oldX = newP1.getX();
			newP1.setLocation(newP2.getX(), newP1.getY());
			newP2.setLocation(oldX, newP2.getY());
			
		}
		
		if (newP1.getY() > newP2.getY()) { // Implies we have a box with the wrong corners, we need to calculate the standard corners now. 
			
			double oldY = newP1.getY();
			newP1.setLocation(newP1.getX(), newP2.getY());
			newP2.setLocation(newP2.getX(), oldY);
			
		}
		
		for (int x = (int) newP1.getX(); x <= newP2.getX(); x++) {
			
			for (int y = (int) newP1.getY(); y <= newP2.getY(); y++) {
				
				boxSet.add(getBoxFromIndex(x, y));
				
			}
			
		}
		
		return boxSet;
		
	}
	
	public static HashSet<Box> boxesInCircle(Box initialBox, double radius) { // Used to draw larger swaths 
		
		HashSet<Box> boxSet = new HashSet<Box>();
		Point center = initialBox.getCenterPoint();
		double size = VisualizationBase.boxXYSize;
		double radiusSq = Math.pow(radius, 2.0);
		
		HashSet<Box> roughIntersectingBoxes = initialBox.findNeighboringBoxes((int) Math.ceil(radius/(size - 1)));
		
		for (Box currentBox : roughIntersectingBoxes) {
			
			Point boxPos = currentBox.getCenterPoint();
			double distance = boxPos.distanceSq(center);
			
			if (distance <= radiusSq) {
				
				boxSet.add(currentBox);
				
			}
			
		}
		
		boxSet.add(initialBox);
		return boxSet;
		
	}
	
	public static HashSet<Box> boxesBetweenPoints(Point p1, Point p2, double radius) { // Used for interpolating between two points and finding the boxes that lie on that line
		
		HashSet<Box> intersectingBoxesList = new HashSet<Box>();
		int xDiff = (int) (p2.getX() - p1.getX()); // Can extend this to make it skip tp xPos that is in next box.
		int yDiff = (int) (p2.getY() - p1.getY());
		int accuracy = 1000;
		int xPos = 0;
		Box boxOnPath;
		
		while (xPos <= accuracy) {
			
			int x = (int) (xPos*xDiff/accuracy + p1.getX());
			int y = (int) (xPos*yDiff/accuracy + p1.getY());
			boxOnPath = getBoxFromPosition(x, y);				
			intersectingBoxesList.add(boxOnPath);
			xPos++;
			
		}
		
		HashSet<Box> newIntersectingBoxesList = new HashSet<Box>();
		
		for (Box box : intersectingBoxesList) {
			
			newIntersectingBoxesList.addAll(boxesInCircle(box, radius));
			
		}
		
		return newIntersectingBoxesList;
		
	}
	
	public static void setFlags(HashSet<Box> boxes, int flag) {
		
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
		
		VisualizationBase.VISUALIZATION_WINDOW.repaint(this);
		
	}
	
	private void setUnselected() {
		
		this.selected = false;
		VisualizationBase.VISUALIZATION_WINDOW.repaint(this);
		
	}
	
	public synchronized static HashSet<Box> getAllBoxes() {
		
		HashSet<Box> boxesList = new HashSet<Box>(100);
		
		for (Box[] bigArray : boxMap) {
			
			for (Box currentBox : bigArray) {
				
				if (currentBox != null) {
					
					boxesList.add(currentBox);
					
				}
				
			}
			
		}

		return boxesList;
		
	}
	
	public static Box getBoxFromPosition(int x, int y) {
		
		int boxX = findClosestIndex(x);
		int boxY = findClosestIndex(y);
		return getBoxFromIndex(boxX, boxY);
		
	}
	
	public static Box getBoxFromPosition(Point p1) {
		
		return getBoxFromPosition(p1.x, p1.y);
		
	}
	
	public static int findClosestIndex(int pos) {
		
		int count = VisualizationBase.ROW_COLUMN_COUNT;
		double interval = VisualizationBase.boxXYSize;
		
		return MyUtils.clampInt(count - 1, (int) Math.floor(pos/interval), 0);
		
	}
	
	public void setFlag(int newFlag) {
		
		if (this.getFlag() == newFlag) {
			
			return;
			
		}
		
		if (isFullBarrier()) {
			
			this.flag = newFlag;
			Point regionWindowPosition = new Point(Region.findClosestIndex(getCenterPoint().x), Region.findClosestIndex(getCenterPoint().y));
			Region.rebuildRegionFromIndex(regionWindowPosition);
			
		}
		
		if (newFlag == BOX_START_FLAG) {
			
			resetStartBox();
			this.flag = BOX_START_FLAG;
			startBox = this;
			checkBeginningAndEndState();
			
		}
		
		else if (newFlag == BOX_END_FLAG) {
			
			resetEndBox();
			this.flag = BOX_END_FLAG;
			endBox = this;
			checkBeginningAndEndState();
			
		}
		
		else if (newFlag == BOX_FULL_BARRIER_FLAG) {
			
			weight = 1.0;
			this.flag = newFlag;
			Point regionWindowPosition = new Point(Region.findClosestIndex(getCenterPoint().x), Region.findClosestIndex(getCenterPoint().y));
			Region.rebuildRegionFromIndex(regionWindowPosition);
			
		}
		
		else if (newFlag == BOX_PARTIAL_BARRIER_FLAG) {
			
			int value = (int) (200 - weight*150);
			color = new Color(value, value, value);
			
		}
		
		else if (newFlag == BOX_STANDARD_FLAG) {
			
			weight = 0.0;
			
		}
		
		if (this != startBox && this != endBox) {
		
			this.flag = newFlag;
			
		}
		
		VisualizationBase.VISUALIZATION_WINDOW.repaint(this);
		
	}
	
	public static boolean beginningAndEndExist() {
		
		if (startBox != null && endBox != null) {
			
			return true;
			
		}
		
		return false;
		
	}
	
	public static void checkBeginningAndEndState() {
		
		VisualizationBase.VISUALIZATION_GUI.setRunButtonEnabledState(beginningAndEndExist());
		
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
				
				if (edge_X >= 0 && edge_X < VisualizationBase.ROW_COLUMN_COUNT && edge_Y >= 0 && edge_Y < VisualizationBase.ROW_COLUMN_COUNT) {
					
					Box currentNeighbor = boxMap[edge_X][edge_Y];
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
		
		if (flag == BOX_FULL_BARRIER_FLAG) {
			
			return true;
			
		}
		
		return false;
		
	}
	
	public boolean isPartialBarrier() {
		
		if (flag == BOX_PARTIAL_BARRIER_FLAG) {
			
			return true;
			
		}
		
		return false;
		
	}
	
	public double euclideanDistance(Box box) {
		
		return MyUtils.euclideanDistance(this.windowPosition, box.windowPosition);
		
	}
	
	public double euclideanDistanceSquared(Box box) {
		
		return MyUtils.euclideanDistanceSquared(this.windowPosition, box.windowPosition);
		
	}
	
	public double manhattanDistance(Box box) {
		
		return MyUtils.manhattanDistance(this.windowPosition, box.windowPosition);
		
	}
	
	@Override
	public boolean equals(Object o) {
		
		Box box = (Box) o;
		
		if (this.windowPosition.getX() == box.windowPosition.getX() && this.windowPosition.getY() == box.windowPosition.getY()) {
			
			return true;
			
		}
		
		return false;
		
	}

	
	public String toString() {
		
		return "Box[" + windowPosition.getX() + ", " + windowPosition.getY() + "]";
		
	}
	
	public String flagToString(int flag) {
		
		switch (flag) {
		
			case BOX_STANDARD_FLAG:
				return "Standard (" + weight + ")";
		
			case BOX_FULL_BARRIER_FLAG:
				return "Barrier (" + weight + ")";
				
			case BOX_PARTIAL_BARRIER_FLAG:
				return "Partial Barrier (" + weight + ")";
				
			case BOX_SEARCHED_FLAG:
				return "Searched";
			
			case BOX_SHORTEST_PATH_FLAG:
				return "Shortest Path";
			
			case BOX_START_FLAG:
				return "Start";
				
			case BOX_END_FLAG:
				return "End";
				
			case BOX_QUEUED_FLAG:
				return "Queued";
				
			default:
				return null;
			
		}
		
	}
	
}
