package Settings;

import PathfindingAlgorithms.Pathfind;

/**
 * Created by Austin Seber2 on 9/26/2016.
 */
public class AlgorithmSettings {

    public static double CUSTOM_G_MODIFIER = 0.5;
    public static double CUSTOM_H_MODIFIER = 0.5;
    public static double WEIGHT = 1.0;
    public static double WEIGHT_MODIFIER = 1.5;

    public static boolean HIERARCHICAL_PATHFINDING = true;
    public static Pathfind.algorithms CURRENT_ALGORITHM = Pathfind.algorithms.ASTAR;

}
