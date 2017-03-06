package BoxSystem;

import BoxState.BoxState;
import BoxState.IBoxState;
import NodeSystem.IDistance;
//import RegionSystem.Region;
import Utilities.MyUtils;

import java.awt.*;
import java.util.*;

import static BoxState.BoxState.BARRIER_STATE;
import static BoxState.BoxState.STANDARD_STATE;
import static Settings.WindowSettings.*;

public class Box extends Observable implements IDistance {

	// The box is the smallest unit on the graph. Each box has four neighbors, corresponding to the four cardinal directions
	// A box can have a weight between 0.0 and 1.0, where 0.0 is considered "open" and 1.0 a "full barrier", values inbetween
	// are considered weighted and change the way A-Star and Dijkstras algorithm rate each box
	
	public enum corners {
		
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT

    }
	
	static public Box startBox;
	static public Box endBox;

	public IBoxState boxState;
	public Point physicalPosition = new Point();
	public Point windowPosition = new Point();
	private double weight = 0.0;
	private HashSet<Edge> edges = new HashSet<>();
//	private Region region = null;
	
	public Box(int x, int y, int window_X_Index, int window_Y_Index, IBoxState state) {
		
		this.physicalPosition.setLocation(x, y);
		this.windowPosition.setLocation(window_X_Index, window_Y_Index);
        setState(state);
        setWeight(0.0);
		edgeInitialization();

	}

	public Point getWindowPosition() {

        return new Point(windowPosition);

    }

	public void setPosition(int x, int y) {
		
		this.physicalPosition.setLocation(x, y);
		edgeInitialization();
		
	}

	public void setState(IBoxState state) {

		boxState = state;
        setChanged();
        notifyObservers();

	}

	public IBoxState getState() {

		return boxState;

	}

	private void setWeight(double newWeight) {

		weight = MyUtils.clampDouble(newWeight, 1.0, 0.0);

        if (getState() == BoxState.STANDARD_STATE || getState() == BoxState.BARRIER_STATE) {

            if (weight == 1.0) {

                setState(BARRIER_STATE);

            } else {

                setState(STANDARD_STATE);

            }

        }

        setChanged();
        notifyObservers();

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
		
		return new Point((int) (physicalPosition.x + Math.floor((double) BOX_XY_SIZE / 2.0)), (int) (physicalPosition.y + (Math.floor((double) BOX_XY_SIZE / 2.0))));
		
	}
	
	private Color getActiveColor() {
		
        return this.getFlag().getColor(this);
		
	}
	
	public void drawBox(Graphics g, boolean drawBorder) {
		
		drawBox(g, drawBorder, getActiveColor());

	}

	public void drawBox(Graphics g, boolean drawBorder, Color color) {

        int x = (int) this.physicalPosition.getX();
        int y = (int) this.physicalPosition.getY();
        int sizeX = BOX_XY_SIZE;
        int sizeY = BOX_XY_SIZE;

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
