package BoxState;

import BoxSystem.Box;

import java.awt.*;

/**
 * Created by asebe on 9/30/2016.
 */
public abstract class BoxState implements IBoxState {

    public static final IBoxState BARRIER_STATE = new BarrierBoxState();
    public static final IBoxState END_BOX_STATE = new EndBoxState();
    public static final IBoxState SEARCHED_BOX_STATE = new SearchedBoxState();
    public static final IBoxState SELECTED_BOX_STATE = new SelectedBoxState();
    public static final IBoxState STANDARD_STATE = new StandardBoxState();
    public static final IBoxState START_BOX_STATE = new StartBoxState();
    public static final IBoxState QUEUED_BOX_STATE = new QueuedBoxState();

    public final void setWeight(double weight, Box context) {

        weight = Math.max(0.0, Math.min(1.0, weight));

        if (weight > 1.0) {

            context.setState(STANDARD_STATE);

        }

        else {

            context.setState(BARRIER_STATE);

        }

    }

    public final void resetHardState(Box context) {

        context.setState(STANDARD_STATE);

    }

    public abstract Color getColor(Box context);
    public abstract String toString();
    public abstract void resetSoftState(Box context);

}
