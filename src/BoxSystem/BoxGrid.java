package BoxSystem;

import BoxState.BoxState;

import java.awt.*;

import static Settings.WindowSettings.ROW_COLUMN_COUNT;

public class BoxGrid {

    private Point gridSize;
    private Box[][] boxMap;

    public BoxGrid(Point gridSize) {

        gridSize = gridSize;
        boxMap = new Box[gridSize.x][gridSize.y];
        initializeField();

    }

    public synchronized Box getBoxFromIndex(Point point) {

        return getBoxFromIndex(point.x, point.y);

    }

    public synchronized Box getBoxFromIndex(int x, int y) {

        return boxMap[x][y];

    }

    private void initializeField() {

        for (int row = 0; row < gridSize.x; row++) {

            for (int column = 0; column < gridSize.y; column++) {

                int x = row*(6);
                int y = column*(6);
                new Box(x, y, row, column, BoxState.STANDARD_STATE);

            }

        }

    }

}
