package BoxSystem;

import BoxState.BoxState;
import Utilities.MyUtils;

import java.awt.*;
import java.util.HashSet;

import static Settings.WindowSettings.BOX_XY_SIZE;
import static Settings.WindowSettings.ROW_COLUMN_COUNT;

public class BoxGrid {

    private Point gridSize;
    private Box[][] boxMap;

    public BoxGrid(Point gridSize) {

        this.gridSize = gridSize;
        boxMap = new Box[gridSize.x][gridSize.y];
        initializeField();

    }

    public synchronized Box getBoxFromIndex(Point point) {

        return getBoxFromIndex(point.x, point.y);

    }

    public Box getBoxFromPosition(int x, int y) {

        int boxX = findClosestIndex(x);
        int boxY = findClosestIndex(y);
        return getBoxFromIndex(boxX, boxY);

    }

    public Box getBoxFromPosition(Point p1) {

        return getBoxFromPosition(p1.x, p1.y);

    }

    public void removeAllBoxes() {

        for (Box currentBox : getAllBoxes()) {

            currentBox.delete();

        }

    }

    public HashSet<Box> boxesInPhysicalSquare(Point p1, Point p2) { // Used to find each regions initial boxes

        Point newP1 = new Point(findClosestIndex(p1.x), findClosestIndex(p1.y));
        Point newP2 = new Point(findClosestIndex(p2.x), findClosestIndex(p2.y));

        return boxesInIndicesSquare(newP1, newP2);

    }

    public HashSet<Box> boxesInPhysicalSquare(int p1x, int p1y, int p2x, int p2y) {

        Point newP1 = new Point(findClosestIndex(p1x), findClosestIndex(p1y));
        Point newP2 = new Point(findClosestIndex(p2x), findClosestIndex(p2y));

        return boxesInIndicesSquare(newP1, newP2);

    }

    public HashSet<Box> boxesInIndicesSquare(Point p1, Point p2) {

        HashSet<Box> boxSet = new HashSet<Box>();

        Point newP1 = new Point(MyUtils.clampInt(ROW_COLUMN_COUNT - 1, p1.x, 0), MyUtils.clampInt(ROW_COLUMN_COUNT - 1, p1.y, 0));
        Point newP2 = new Point(MyUtils.clampInt(ROW_COLUMN_COUNT - 1, p2.x, 0), MyUtils.clampInt(ROW_COLUMN_COUNT - 1, p2.y, 0));

        if (newP1.getX() > newP2.getX()) { // Implies we have a box with the wrong corners, we need to calculate the standard corners now.

            double oldX = newP1.getX();
            newP1.setLocation(newP2.getX(), newP1.getY());
            newP2.setLocation(oldX, newP2.getY());

        }

        if (newP1.getY() > newP2.getY()) { // Implies we have a box with the wrong corners, we need to calculate the standard corners now.

            double oldY = newP1.getY();
            newP1.setLocation(newP1.getX(), newP2.getY());
            newP2.setLocation(newP2.getX(), oldY);

        }

        for (int x = (int) newP1.getX(); x <= newP2.getX(); x++) {

            for (int y = (int) newP1.getY(); y <= newP2.getY(); y++) {

                boxSet.add(getBoxFromIndex(x, y));

            }

        }

        return boxSet;

    }

    public HashSet<Box> boxesInIndicesSquare(int p1x, int p1y, int p2x, int p2y) {

        Point newP1 = new Point(p1x, p1y);
        Point newP2 = new Point(p2x, p2y);

        return boxesInIndicesSquare(newP1, newP2);

    }

    public HashSet<Box> boxesInCircle(Box initialBox, double radius) { // Used to draw larger swaths

        HashSet<Box> boxSet = new HashSet<Box>();
        Point center = initialBox.getCenterPoint();
        double size = BOX_XY_SIZE;
        double radiusSq = Math.pow(radius, 2.0);

        HashSet<Box> roughIntersectingBoxes = initialBox.findNeighboringBoxes((int) Math.ceil(radius/(size - 1)));

        for (Box currentBox : roughIntersectingBoxes) {

            Point boxPos = currentBox.getCenterPoint();
            double distance = boxPos.distanceSq(center);

            if (distance <= radiusSq) {

                boxSet.add(currentBox);

            }

        }

        boxSet.add(initialBox);
        return boxSet;

    }

    public HashSet<Box> boxesBetweenPoints(Point p1, Point p2, double radius) { // Used for interpolating between two points and finding the boxes that lie on that line

        HashSet<Box> intersectingBoxesList = new HashSet<Box>();
        int xDiff = (int) (p2.getX() - p1.getX()); // Can extend this to make it skip tp xPos that is in next box.
        int yDiff = (int) (p2.getY() - p1.getY());
        int accuracy = 1000;
        int xPos = 0;
        Box boxOnPath;

        while (xPos <= accuracy) {

            int x = (int) (xPos*xDiff/accuracy + p1.getX());
            int y = (int) (xPos*yDiff/accuracy + p1.getY());
            boxOnPath = getBoxFromPosition(x, y);
            intersectingBoxesList.add(boxOnPath);
            xPos++;

        }

        HashSet<Box> newIntersectingBoxesList = new HashSet<Box>();

        for (Box box : intersectingBoxesList) {

            newIntersectingBoxesList.addAll(boxesInCircle(box, radius));

        }

        return newIntersectingBoxesList;

    }

    public synchronized HashSet<Box> getAllBoxes() {

        HashSet<Box> boxesList = new HashSet<Box>(100);

        for (Box[] bigArray : boxMap) {

            for (Box currentBox : bigArray) {

                if (currentBox != null) {

                    boxesList.add(currentBox);

                }

            }

        }

        return boxesList;

    }

    public static int findClosestIndex(int pos) {

        int count = ROW_COLUMN_COUNT;
        double interval = BOX_XY_SIZE;

        return MyUtils.clampInt(count - 1, (int) Math.floor(pos/interval), 0);

    }

    public synchronized Box getBoxFromIndex(int x, int y) {

        return boxMap[x][y];

    }

    private void initializeField() {

        for (int row = 0; row < gridSize.x; row++) {

            for (int column = 0; column < gridSize.y; column++) {

                int x = row*(6);
                int y = column*(6);
                Box box = new Box(x, y, row, column, BoxState.STANDARD_STATE);
                boxMap[row][column] = box;

            }

        }

    }

}
