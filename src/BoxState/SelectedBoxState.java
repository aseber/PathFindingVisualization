package BoxState;

import BoxSystem.Box;

import java.awt.*;

/**
 * Created by asebe on 9/28/2016.
 */
public class SelectedBoxState extends BoxState {

    private Color selectedColor = new Color(100, 100, 255);

    public Color getColor(Box context) {

        return selectedColor;

    }

    public void resetSoftState(Box context) {}

    public String toString() {

        return "Selected";

    }

}
