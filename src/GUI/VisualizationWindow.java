package GUI;

import BoxState.IBoxState;
import BoxSystem.Box;
import BoxSystem.BoxGrid;
//import EventHandler.RegionEvent;
//import EventHandler.RegionEventDriver;
import NodeSystem.NodeBox;
import PathfindingAlgorithms.Pathfind;
import PathfindingAlgorithms.PathfindAStar;
import PathfindingAlgorithms.PathfindExecutor;
//import RegionSystem.Region;
import Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import static BoxState.BoxState.*;
import static Settings.AlgorithmSettings.WEIGHT;
import static Settings.WindowSettings.*;

public class VisualizationWindow extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
	
	private static final long serialVersionUID = -453107461013635372L;
	int WINDOW_WIDTH, WINDOW_HEIGHT;
	static boolean mouse1Down, mouse3Down, shiftDown, spaceDown;
	Point mouse = new Point();
	Point oldMouse = new Point();
	boolean drawDebugInformation = false;
	boolean drawFunInformation = false;
	int drawSize = 1;
	private BoxGrid boxGrid;
	private BufferedImage image, regionChangeImage;
//	RegionEventDriver driver = new RegionEventDriver();
	PathfindExecutor executor = new PathfindExecutor();
	
	public VisualizationWindow() {
		
		new Thread(executor).start();
		
		setFocusable(true);
		addComponentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseWheelListener(this);
		setIgnoreRepaint(true);
		setOpaque(true);
		setBackground(Color.MAGENTA);
		
		WINDOW_WIDTH = getWidth();
		WINDOW_HEIGHT = getHeight();
	
		int sizeX = ROW_COLUMN_COUNT* BOX_XY_SIZE + 1;
		int sizeY = ROW_COLUMN_COUNT* BOX_XY_SIZE + 1;

		boxGrid = new BoxGrid(new Point(ROW_COLUMN_COUNT, ROW_COLUMN_COUNT));

		Graphics g;
		
		image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
		g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, sizeX, sizeY);
		
		regionChangeImage = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
		g = regionChangeImage.getGraphics();
		g.setColor(new Color(255, 255, 255, 0));
		g.fillRect(0, 0, sizeX, sizeY);
		
	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		g.setColor(this.getBackground());
		g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		int mouseX = mouse.x;
		int mouseY = mouse.y;
		
		//g.drawLine(0, mouseX, mouseX, WINDOW_HEIGHT);
		//g.drawLine(mouseY, 0, WINDOW_WIDTH, mouseY);
		
		g.drawImage(image, 0, 0, null);					// Draw the image containing all of the boxes
		g.drawImage(regionChangeImage, 0, 0, null);		// Draw the regionChanges on top of the boxes
		
		g.setColor(Color.BLACK);
		g.drawArc(mouseX - drawSize, mouseY - drawSize, drawSize*2, drawSize*2, 0, 360);
		
		if (drawDebugInformation) {
			
			Box box = boxGrid.getBoxFromPosition(mouse);
			
//			Region region = box.getRegion();
			
			/*if (region != null) {
			
				HashSet<Region> neighboringRegions = region.getNeighboringRegions();
				
				for (Region neighborRegion : neighboringRegions) {	// Drawing border regions in a magenta color
					
					if (neighborRegion != null) {
					
						neighborRegion.drawRegionBorderDebugColor(g, new BasicStroke(3), new Color(255, 0, 255, 125));
						
					}
					
				}
				
				region.drawRegionBorderDebugColor(g, new BasicStroke(3), new Color(0, 255, 255, 125));	// draw this region in a cyan color
				
			}*/
			
			g.setColor(Color.BLACK);
			g.drawLine((int) oldMouse.getX(), (int) oldMouse.getY(), mouseX, mouseY);	
			g.setColor(Color.RED);
			g.drawLine(mouseX, 0, mouseX, WINDOW_HEIGHT);
			g.drawLine(0, mouseY, WINDOW_WIDTH, mouseY);
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, ROW_COLUMN_COUNT* BOX_XY_SIZE + 1, 15);
			g.setColor(Color.WHITE);
			String display1 = "Mouse: [" + mouseX + ", " + mouseY + "]";
//			String display2 = "RegionSystem.Region: [" + Region.findClosestIndex(mouseX) + ", " + Region.findClosestIndex(mouseY) + "]";
			String display3 = "BoxSystem.Box: [" + boxGrid.findClosestIndex(mouseX) + ", " + boxGrid.findClosestIndex(mouseY) + "]";
			String display4 = "BoxSystem.Box flag: " + box.getFlag();
			String display5 = "BoxSystem.Box weight: " + box.getWeight();
			int offsetX = 5;
			g.drawString(display1, offsetX, 12);
			offsetX += g.getFontMetrics().stringWidth(display1) + 5;
//			g.drawString(display2, offsetX, 12);
//			offsetX += g.getFontMetrics().stringWidth(display2) + 5;
			g.drawString(display3, offsetX, 12);
			offsetX += g.getFontMetrics().stringWidth(display3) + 5;
			g.drawString(display4, offsetX, 12);
			offsetX += g.getFontMetrics().stringWidth(display4) + 5;
			g.drawString(display5, offsetX, 12);
			
		}
		
		oldMouse.setLocation(mouseX, mouseY);
		
	}
	
	public void setDrawSize(int value) {
		
		drawSize = value;
		repaint();
		
	}
	
	public void setWeight(double value) {
		
		WEIGHT = MyUtils.clampDouble(1.0, value/1000, 0.0);
		VISUALIZATION_GUI.setWeightSlider((int) (WEIGHT*1000));
		VISUALIZATION_GUI.setWeightValue(WEIGHT);
		
	}
	
	public void repaint(Box box) { // Draw each box out on the image graphics, only repaint when each box changes flags

		box.drawBox(image.getGraphics(), true);
		repaint();
		
	}
	
	public void repaint(HashSet<Box> boxes) {
		
		for (Box box : boxes) {
			
			repaint(box);
			
		}
		
	}
	
	public void repaintAll() {
		
		repaint(boxGrid.getAllBoxes());
		
	}
	
	/*public void drawBox(Graphics g, BoxSystem.Box currentBox, Color color, boolean drawWhiteBackground) {
		
		int x = (int) currentBox.physicalPosition.getX();
		int y = (int) currentBox.physicalPosition.getY();
		int sizeX = (int) VisualizationBase.boxXYSize;
		int sizeY = (int) VisualizationBase.boxXYSize;
		
		if (drawWhiteBackground) {
			
			g.setColor(Color.WHITE);
			g.fillRect(x, y, sizeX, sizeY);
		
		}
		
		g.setColor(color);
		g.fillRect(x, y, sizeX, sizeY);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, sizeX, sizeY);
		
	}*/
	
/*	public void registerChange(Region region, int time, Color color) {
		
		driver.runEvent(new RegionEvent(region, regionChangeImage.getGraphics(), color), time); // RegionSystem.Region flashing box system
		
	}
	
	public void removeBoxRegionField() {
		
		Region.removeAllRegions();
        boxGrid.removeAllBoxes();
		
	}
	
	public void createBoxRegionField() {

		Region.createRegionField();
		repaintAll();
		
	}*/
	
	public void clearBoxFieldFlag(IBoxState flag) {
		
		/*if (flag == Box.flags.STANDARD) {return;}
		if (flag == Box.flags.START) {Box.resetStartBox(); return;}
		if (flag == Box.flags.END) {Box.resetEndBox();  return;}
		
		for (Box box : Box.getAllBoxes()) {
			
			if (box.getFlag() == flag) {
				
				if (flag != Box.flags.PARTIAL_BARRIER) {
					
					if (box.getWeight() == 0.0 || box.getWeight() == 1.0) {
					
						box.setFlag(Box.flags.STANDARD);
						
					}
					
					else if (box.getWeight() != 1.0) {
						
						box.setFlag(Box.flags.PARTIAL_BARRIER);
						
					}
					
				}
				
				else {
					
					box.setFlag(Box.flags.STANDARD);
					
				}
				
			}
			
		}*/
		
	}
	
	public void clearBoxFieldFlags(IBoxState[] flags) {
		
		for (IBoxState flag : flags) {
			
			clearBoxFieldFlag(flag);
			
		}
		
	}
	
	public void setWindowSize(Dimension d) {
		
		int sizeX = ROW_COLUMN_COUNT* BOX_XY_SIZE + 1;
		int sizeY = ROW_COLUMN_COUNT* BOX_XY_SIZE + 1;
		image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
		regionChangeImage = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
		Dimension newDimension = new Dimension((int) (d.getWidth() + 17), (int) (d.getHeight() + 124));
		this.setSize(d);
		VISUALIZATION_GUI.setSize(newDimension);
		Graphics g = image.getGraphics();
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		g = regionChangeImage.getGraphics();
		g.setColor(new Color(255, 255, 255, 0));
		g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
	}
	
	public void getMouseInWindow(boolean clamp) {

		int x = (int)(MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().getX());
		int y = (int)(MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().getY());
		
		if (clamp) {
		
			x = MyUtils.clampInt(WINDOW_WIDTH, x, 0);
			y = MyUtils.clampInt(WINDOW_HEIGHT, y, 0);
			
		}
		
		mouse.setLocation(x, y);
		
	}
	
	public void mousePressed(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			
			mouse1Down = true;
			
		}
		
		else if (e.getButton() == MouseEvent.BUTTON3) {
			
			mouse3Down = true;
			
		}
		
	}
	
	public void mouseReleased(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			
			mouse1Down = false;
			
		}
		
		else if (e.getButton() == MouseEvent.BUTTON3) {
			
			mouse3Down = false;
			
		}
		
	}
	
	public void mouseClicked(MouseEvent e) {
		
		int mouseX = e.getX();
		int mouseY = e.getY();
			
		Box box = boxGrid.getBoxFromPosition(mouseX, mouseY);
		HashSet<Box> boxes = boxGrid.boxesInCircle(box, drawSize);
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			
			if (shiftDown) {
				
				box.setFlag(START_BOX_STATE);
				
			}
			
			else {
				
				Box.setWeights(boxes, WEIGHT);
				
			}
			
		}
		
		if (e.getButton() == MouseEvent.BUTTON3) {
			
			if (shiftDown) {
				
				box.setFlag(END_BOX_STATE);
				
			}
			
			else {
				
				Box.setFlags(boxes, STANDARD_STATE);
				
			}
			
		}
		
	}
	
	public void mouseMoved(MouseEvent e) {
		
		int mouseX = e.getX();
		int mouseY = e.getY();
		mouse.setLocation(MyUtils.clampInt(WINDOW_WIDTH, mouseX, 0), MyUtils.clampInt(WINDOW_HEIGHT, mouseY, 0));
		
		Box box = boxGrid.getBoxFromPosition(mouseX, mouseY);
		box.setSelected();
		
		Box oldBox = boxGrid.getBoxFromPosition(oldMouse);
		
		if (drawFunInformation) {
		
			if (box != oldBox) {
			
				IBoxState[] flags = {SEARCHED_BOX_STATE, SHORTEST_PATH_BOX_STATE, QUEUED_BOX_STATE};
				clearBoxFieldFlags(flags);
				
				Pathfind path = new PathfindAStar(new NodeBox(boxGrid.getBoxFromIndex(ROW_COLUMN_COUNT/2, ROW_COLUMN_COUNT/2), null), new NodeBox(boxGrid.getBoxFromPosition(mouseX, mouseY), null));
				path.run();
				
			}
		
		}
		
	}
	
	public void mouseDragged(MouseEvent e) { // This should draw line between last two points and all boxes that intersect it are colored.
		
		int mouseX = e.getX();
		int mouseY = e.getY();
		mouse.setLocation(MyUtils.clampInt(WINDOW_WIDTH, mouseX, 0), MyUtils.clampInt(WINDOW_HEIGHT, mouseY, 0));
			
		HashSet<Box> boxesList = new HashSet<Box>();
		Box box = boxGrid.getBoxFromPosition(mouseX, mouseY);
		Box oldBox = boxGrid.getBoxFromPosition(oldMouse);
		
		boxesList.add(box);
		
		if (box != oldBox) { // Far enough apart that we care about interpolating between the points.
			
			HashSet<Box> boxes = boxGrid.boxesBetweenPoints(new Point(mouseX, mouseY), oldMouse, drawSize);
			
			for (Box currentBox : boxes) {
			
				boxesList.add(currentBox);
				
			}
			
		}
		
		if (mouse1Down) {
			
			Box.setWeights(boxesList, WEIGHT);
			
			box.setSelected();
		
		}
		
		else if (mouse3Down) {
		
			for (Box currentBox : boxesList) {
				
				currentBox.setFlag(STANDARD_STATE);
				
			}

			box.setSelected();
			
		}
		
	}
	
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			
			shiftDown = true;
			
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			
			spaceDown = true;
			
			try {
				
				System.out.println("Test");
				System.out.println(executor.isPathFinderRunning());
				
				if (executor.isPathFinderRunning()) {
					
					executor.togglePause();
					
				} else {
					
					runPathfinder();
					
				}
				
			} catch (NullPointerException e2) {
				
				runPathfinder();
				
			}
			
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			
			executor.endPathfinding();
				
			IBoxState[] flags = {SEARCHED_BOX_STATE, SHORTEST_PATH_BOX_STATE, QUEUED_BOX_STATE};
			clearBoxFieldFlags(flags);
			
		}
		
	}
	
	public void runPathfinder() {
		
		executor.startPathfinding();
		
	}
	
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_ALT) {
			
			drawDebugInformation = !drawDebugInformation; // Must show the currently selected block and highlight it
			repaint();
			e.consume();
			
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			
			shiftDown = false;
			
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			
			spaceDown = false;
		
		}
		
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			
			drawFunInformation = !drawFunInformation;
			
		}
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			
			setDrawSize(MyUtils.clampInt(50, drawSize + 5, 1));
			
		}
		
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				
			setDrawSize(MyUtils.clampInt(50, drawSize - 5, 1));
			
			
		}
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			
			setWeight(MyUtils.clampInt(1000, (int) (WEIGHT*1000 + 50), 0));
			
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			
			setWeight(MyUtils.clampInt(1000, (int) (WEIGHT*1000 - 50), 0));
			
		}
		
	}
	
	public void componentResized(ComponentEvent e) {
		
		WINDOW_WIDTH = e.getComponent().getWidth();
		WINDOW_HEIGHT = e.getComponent().getHeight();
		
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}

	public void mouseWheelMoved(MouseWheelEvent e) {

		if (!shiftDown) {
			
			setDrawSize(MyUtils.clampInt(50, drawSize - e.getWheelRotation(), 1));
			
		}
		
		else {
		
			setWeight(MyUtils.clampInt(1000, (int) (WEIGHT*1000 - e.getWheelRotation()*15), 0));
			
		}
		
	}
	
}