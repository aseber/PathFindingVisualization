import javax.swing.JFrame;

public class VisualizationBase {

	public static final String ASTAR = "ASTAR";
	public static final String DIJKSTRA = "DIJKSTRA";
	public static final String CUSTOM = "CUSTOM";
	
	protected static VisualizationWindow VISUALIZATION_WINDOW;
	protected static VisualizationGUI VISUALIZATION_GUI;
	public static double gModifier = 0.5;
	public static double hModifier = 0.5;
	public static int sleepTimer = 0;
	public static double weight = 1.0;
	public static double weightModifier = 1.5;
	public static int ROW_COLUMN_COUNT = 100;
	public static int boxXYSize = 6;
	public static int regionSize = 10;
	public static boolean hierarchicalPathfinding = true;
	public static String CURRENT_ALGORITHM = ASTAR;
	
	public static void main(String[] args) {

		Box.initializeStaticVariables();
		Region.initializeStaticVariables();
		VISUALIZATION_GUI = new VisualizationGUI();
		VISUALIZATION_GUI.setTitle("Pathfinding Visualization");
		VISUALIZATION_GUI.setVisible(true);
		VISUALIZATION_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		// Final problem is the Region.fillRegionDebugColor doesnt work, not sure if it fails due to multiple region creation at the same time OR because of bad draw code

	}

}
