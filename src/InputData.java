import java.awt.*;

/**
 * Represents an object that stores input data
 */
public class InputData {

    /* Width of the game field */
    private final int fieldWidth;

    /* Height of the game field */
    private final int fieldHeight;

    /* Coordinates of the start cell */
    private final Point startPoint;

    /* Coordinates of the end cell */
    private final Point endPoint;

    /* Game field */
    private final Cell[][] gameField;

    /**
     * InputData class constructor
     *
     * @param fieldWidth  Width of the game field
     * @param fieldHeight Height of the game field
     * @param startPoint  Coordinates of the start cell
     * @param endPoint    Coordinates of the end cell
     * @param gameField   Game field
     */
    public InputData(int fieldWidth, int fieldHeight, Point startPoint, Point endPoint, Cell[][] gameField) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.gameField = gameField;
    }

    /**
     * Return value of width game field
     *
     * @return Width value
     */
    public int getFieldWidth() {
        return fieldWidth;
    }

    /**
     * Return value of height game field
     *
     * @return Height value
     */
    public int getFieldHeight() {
        return fieldHeight;
    }

    /**
     * Return coordinates of the start cell
     *
     * @return Start cell coordinates
     */
    public Point getStartPoint() {
        return startPoint;
    }

    /**
     * Return coordinates of the finish cell
     *
     * @return Finish cell coordinates
     */
    public Point getEndPoint() {
        return endPoint;
    }

    /**
     * Return game field
     *
     * @return Game field value
     */
    public Cell[][] getGameField() {
        return gameField;
    }
}
