package BoxState;

import java.awt.*;

/**
 * Created by asebe on 9/28/2016.
 */
public class StandardBoxState implements IBoxState {

    private Color standardColor = new Color(200, 200, 200);

    public Color getColor() {

        return standardColor;

    }

    public void setWeight(double weight) {



    }

    public String toString() {

        return "Standard";

    }

}
