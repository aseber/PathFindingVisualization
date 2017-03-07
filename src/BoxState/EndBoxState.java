package BoxState;

import BoxSystem.Box;

import java.awt.*;

/**
 * Created by asebe on 9/28/2016.
 */
public class EndBoxState extends BoxState {

    private Color endColor = new Color(255, 255, 255);

    public Color getColor(Box context) {

        return endColor;

    }

    public void resetSoftState(Box context) {}

    public String toString() {

        return "End";

    }

}