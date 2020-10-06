import java.awt.*;

/**
 * It is an object of a cell that stores data about the location, whether it was previously visited, whether it has a ball
 */
public class Cell {

    /* Coordinates of the cell in game field */
    private final Point coordinates;

    /* Is cell visited before */
    private boolean isVisited;

    /* Is cell has a ball */
    private final boolean hasBall;

    /* Distance index from start cell */
    private Integer distanceIndex;

    /* The step to take from the current cell to reach the final cell */
    private char stepToFinalPoint;

    /**
     * Cell class constructor
     *
     * @param coordinates Cell coordinate
     * @param hasBall     Is cell has ball
     */
    public Cell(Point coordinates, boolean hasBall) {
        this.coordinates = coordinates;
        this.hasBall = hasBall;
        isVisited = false;
        stepToFinalPoint = hasBall ? 'O' : '1';
    }

    /**
     * Set visit value for the cell
     *
     * @param visited New visit value
     */
    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    /**
     * Return visit value for the current cell
     *
     * @return Visit value
     */
    public boolean isVisited() {
        return isVisited;
    }

    /**
     * Return is cell has a ball
     *
     * @return Is cell has a ball
     */
    public boolean isHasBall() {
        return hasBall;
    }

    /**
     * Return distance index for the current cell
     *
     * @return Distance index value
     */
    public Integer getDistanceIndex() {
        return distanceIndex;
    }

    /**
     * Set distance index value for the current cell
     *
     * @param distanceIndex Distance value
     */
    public void setDistanceIndex(Integer distanceIndex) {
        this.distanceIndex = distanceIndex;
    }

    /**
     * Return coordinates of the cell
     *
     * @return Cell coordinates
     */
    public Point getCoordinates() {
        return coordinates;
    }

    /**
     * Return step value for the current cell
     *
     * @return Step value
     */
    public char getStepToFinalPoint() {
        return stepToFinalPoint;
    }

    /**
     * Set step value for the current cell
     *
     * @param stepToFinalPoint New step value
     */
    public void setStepToFinalPoint(char stepToFinalPoint) {
        this.stepToFinalPoint = stepToFinalPoint;
    }
}
