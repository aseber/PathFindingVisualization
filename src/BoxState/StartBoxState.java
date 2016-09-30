package BoxState;

import BoxSystem.Box;

import java.awt.*;

/**
 * Created by asebe on 9/28/2016.
 */
public class StartBoxState extends BoxState {

    private Color startColor = new Color(0, 0, 0);

    public Color getColor(Box context) {

        return startColor;

    }

    public void resetSoftState(Box context) {}

    public String toString() {

        return "Start";

    }

}
