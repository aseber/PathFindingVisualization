package BoxState;

import BoxSystem.Box;

import java.awt.*;

/**
 * Created by asebe on 9/28/2016.
 */
public class BarrierBoxState extends BoxState {

    private Color barrierColor = new Color(10, 10, 10);

    public Color getColor(Box context) {

        return barrierColor;

    }

    public void resetSoftState(Box context) {}

    public String toString() {

        return "Barrier";

    }

}
