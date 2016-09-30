package BoxState;

import BoxSystem.Box;

import java.awt.*;

/**
 * Created by asebe on 9/28/2016.
 */
public class QueuedBoxState extends BoxState {

    private Color queuedColor = new Color(155, 155, 155);

    public Color getColor(Box context) {

        return queuedColor;

    }

    public void resetSoftState(Box context) {

        context.setState(STANDARD_STATE);

    }

    public String toString() {

        return "Queued";

    }

}
