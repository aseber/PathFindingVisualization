import BoxSystem.Box;
import GUI.VisualizationGUI;
import GUI.VisualizationWindow;
import PathfindingAlgorithms.Pathfind;
import RegionSystem.Region;

import javax.swing.JFrame;

import static Settings.WindowSettings.VISUALIZATION_GUI;

public class VisualizationBase {



	
	public static void main(String[] args) {

		Box.initializeStaticVariables();
		Region.initializeStaticVariables();
		VISUALIZATION_GUI = new VisualizationGUI();
		VISUALIZATION_GUI.setTitle("Pathfinding Visualization");
		VISUALIZATION_GUI.setVisible(true);
		VISUALIZATION_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		// Final problem is the RegionSystem.Region.fillRegionDebugColor doesnt work, not sure if it fails due to multiple region creation at the same time OR because of bad draw code

	}

}
