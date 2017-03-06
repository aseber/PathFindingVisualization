package BoxState;

import BoxSystem.Box;

import java.awt.*;

/**
 * Created by asebe on 9/28/2016.
 */
public class StandardBoxState extends BoxState {

    private Color standardColor = new Color(200, 200, 200);

    public Color getColor(Box context) {

        if (context.getWeight() == 0) {

            return standardColor;

        }

        double weight = context.getWeight();
        int value = (int) (200 - 150 * weight);
        return new Color(value, value, value);

    }

    public void resetSoftState(Box context) {}

    public String toString() {

        return "Standard";

    }

}
