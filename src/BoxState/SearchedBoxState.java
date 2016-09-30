package BoxState;

import BoxSystem.Box;

import java.awt.*;

/**
 * Created by asebe on 9/28/2016.
 */
public class SearchedBoxState extends BoxState {

    private Color searchedColor = new Color(200, 0, 0);

    public Color getColor(Box context) {

        return searchedColor;

    }

    public void resetSoftState(Box context) {

        context.setState(STANDARD_STATE);

    }

    public String toString() {

        return "Searched";

    }

}
