import javax.swing.JFrame;

public class VisualizationBase {
	
	protected static VisualizationWindow VISUALIZATION_WINDOW;
	protected static VisualizationGUI VISUALIZATION_GUI;
	public static double CUSTOM_G_MODIFIER = 0.5;
	public static double CUSTOM_H_MODIFIER = 0.5;
	public static int SLEEP_TIMER = 0;
	public static double WEIGHT = 1.0;
	public static double WEIGHT_MODIFIER = 1.5;
	public static int ROW_COLUMN_COUNT = 100;
	public static int BOX_XY_SIZE = 6;
	public static int REGION_SIZE = 10;
	public static boolean HIERARCHICAL_PATHFINDING = true;
	public static Pathfind.algorithms CURRENT_ALGORITHM = Pathfind.algorithms.ASTAR;
	
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
