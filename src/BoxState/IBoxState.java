package BoxState;

import BoxSystem.Box;

import java.awt.*;

/**
 * Created by asebe on 9/28/2016.
 */
public interface IBoxState {

    Color getColor(Box context);
    String toString();
    void resetSoftState(Box context);
    void resetHardState(Box context);

}
