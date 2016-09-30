package BoxState;

import BoxSystem.Box;

import java.awt.*;

/**
 * Created by asebe on 9/28/2016.
 */
public class AlongShortestPathBoxState extends BoxState {

    private Color shortestPathColor = new Color(0, 0, 255);

    public Color getColor(Box context) {

        return shortestPathColor;

    }

    public void resetSoftState(Box context) {

        context.setState(STANDARD_STATE);

    }

    public String toString() {

        return "Shortest Path";

    }

}
