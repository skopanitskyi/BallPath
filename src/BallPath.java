import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Processes incoming data and builds the shortest route to the final point, if possible.
 */
public class BallPath {

    /**
     * The main method of the program
     * @param args Program start arguments
     */
    public static void main(String[] args) {
        List<String> inputData = getInputData();
        InputData data = processInputData(inputData);
        setRoutFromStartPoint(data.getGameField(), data.getStartPoint(), data.getEndPoint());
        checkRoute(data);
    }

    /**
     * Requests input data from the user until it receives it
     * @return Processed input data
     */
    private static List<String> getInputData() {
        Scanner scanner = new Scanner(System.in);
        String filePath = getPathToFile(scanner);
        List<String> data = readFile(filePath);

        while (data == null) {
            filePath = getPathToFile(scanner);
            data = readFile(filePath);
        }
        scanner.close();
        return data;
    }

    /**
     * Asks the user for the path to the file where the input data is stored
     * @param scanner The scanner object that reads data from the console
     * @return Path to the data file
     */
    private static String getPathToFile(Scanner scanner) {
        System.out.println("Enter path to the file with input data: ");
        return scanner.nextLine();
    }

    /**
     * Reads data from a file. If they can be read, returns data.
     * If the data could not be read, it displays an error message and returns null
     * @param path Path to the data file
     * @return Read files or null
     */
    private static List<String> readFile(String path) {
        try {
            return Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Failed to read data from file. Check the specified file path.");
            return null;
        }
    }

    /**
     * Creates an Input Data object that stores all data from the read file
     * @param data Read data
     * @return Input Data object
     */
    private static InputData processInputData(List<String> data) {
        int fieldWidth = Integer.parseInt(data.remove(0));
        int fieldHeight = Integer.parseInt(data.remove(0));
        Point startPoint = getPointFrom(data.remove(0));
        Point endPoint = getPointFrom(data.remove(0));
        Cell[][] gameField = fillGameField(data, fieldWidth, fieldHeight);
        return new InputData(fieldWidth, fieldHeight, startPoint, endPoint, gameField);
    }

    /**
     * Creates a Point object from a string record of coordinates
     * @param data Contains coordinates as a string
     * @return Point object
     */
    private static Point getPointFrom(String data) {
        String dataWithoutBrackets = data.replaceAll("[()]", "");
        String numbersData = dataWithoutBrackets.replaceAll(",", " ");
        String[] numbers = numbersData.split("\\s");
        int x = Integer.parseInt(numbers[0]) - 1;
        int y = Integer.parseInt(numbers[1]) - 1;
        return new Point(x, y);
    }

    /**
     * Creates a two-dimensional array of cells that represents the playing field
     * @param data Contains data about the playing field
     * @param width Width of the playing field
     * @param height Height of the playing field
     * @return A playing field that contains information about the location of balls
     */
    private static Cell[][] fillGameField(List<String> data, int width, int height) {
        Cell[][] gameField = new Cell[height][width];

        List<String> linesWithData = data.stream().filter(line -> line.matches("[01\\s]+"))
                                                  .map(line -> line.replaceAll("\\s+", ""))
                                                  .collect(Collectors.toList());

        for (int y = 0; y < linesWithData.size(); y++) {
            char[] chars = linesWithData.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                boolean hasBall = chars[x] == '1';
                gameField[y][x] = new Cell(new Point(x, y), hasBall);
            }
        }
        return gameField;
    }

    /**
     * Plots a route from the starting cell for each field cell, or until it reaches the final field. Each cell stores
     * a distance index if it does not contain a ball that helps you get from the current cell to the starting position.
     * @param gameField Game field
     * @param start Coordinates of the starting cell
     * @param end Coordinates of the final cell
     */
    private static void setRoutFromStartPoint(Cell[][] gameField, Point start, Point end) {
        Queue<Cell> queue = new LinkedList<>();
        Cell startCell = gameField[start.y][start.x];
        startCell.setVisited(true);
        startCell.setDistanceIndex(0);
        queue.add(startCell);

        boolean isEndPoint = queue.peek().getCoordinates().equals(end);

        while (!queue.isEmpty() && !isEndPoint) {

            Cell cell = getNeighborCell(gameField, queue.peek().getCoordinates());
            isEndPoint = queue.peek().getCoordinates().equals(end);

            if (cell == null) {
                queue.poll();
            } else {
                cell.setVisited(true);
                cell.setDistanceIndex(1 + queue.peek().getDistanceIndex());
                queue.add(cell);
            }
        }
    }

    /**
     * Returns a neighbor cell that does not contain a ball and has not been passed before. Used when building a
     * route to the final cell
     * @param gameField  Game field
     * @param coordinates Coordinates of the current cell
     * @return Neighbor cell if present, or null
     */
    private static Cell getNeighborCell(Cell[][] gameField, Point coordinates) {
        int x = coordinates.x;
        int y = coordinates.y;

        if (x > 0 && !gameField[y][x - 1].isVisited() && !gameField[y][x - 1].isHasBall()) {
            return gameField[y][x - 1];
        }

        if (x + 1 < gameField[y].length && !gameField[y][x + 1].isVisited() && !gameField[y][x + 1].isHasBall()) {
            return gameField[y][x + 1];
        }

        if (y > 0 && !gameField[y - 1][x].isVisited() && !gameField[y - 1][x].isHasBall()) {
            return gameField[y - 1][x];
        }

        if (y + 1 < gameField.length && !gameField[y + 1][x].isVisited() && !gameField[y + 1][x].isHasBall()) {
            return gameField[y + 1][x];
        }

        return null;
    }

    /**
     * Find the shortest route from the final cell to the starting cell using the distance index.
     * The route is laid out on the playing field using the stepToFinalPoint variable
     * @param gameField Game field
     * @param endPoint Coordinates of the final cell
     * @return The order of steps that need to take to go from the starting cell to the final
     */
    private static String getPathFromFinishToStartPoint(Cell[][] gameField, Point endPoint) {
        StringBuilder builder = new StringBuilder();
        Cell currentCell = gameField[endPoint.y][endPoint.x];
        currentCell.setStepToFinalPoint('F');

        while (currentCell.getDistanceIndex() != 0) {
            currentCell = getCellForPath(gameField, currentCell.getDistanceIndex(), currentCell.getCoordinates());
            if (currentCell.getDistanceIndex() != 0) {
                builder.append(currentCell.getStepToFinalPoint()).append(" ,");
            } else {
                builder.append(currentCell.getStepToFinalPoint());
            }
        }
        return builder.reverse().toString();
    }

    /**
     * Finds a neighboring cell that has the distance index value one less. This allows to build the shortest route.
     * @param gameField Game field
     * @param distance Distance index value of the current cell
     * @param coordinates Coordinates of the current cell
     * @return Neighbor cell
     */
    private static Cell getCellForPath(Cell[][] gameField, int distance, Point coordinates) {

        int x = coordinates.x;
        int y = coordinates.y;

        if (x > 0 && !gameField[y][x - 1].isHasBall() && gameField[y][x - 1].getDistanceIndex() == distance - 1) {
            gameField[y][x - 1].setStepToFinalPoint('R');
            return gameField[y][x - 1];
        }

        if (x + 1 < gameField[y].length && !gameField[y][x + 1].isHasBall() && gameField[y][x + 1].getDistanceIndex() == distance - 1) {
            gameField[y][x + 1].setStepToFinalPoint('L');
            return gameField[y][x + 1];
        }

        if (y > 0 && !gameField[y - 1][x].isHasBall() && gameField[y - 1][x].getDistanceIndex() == distance - 1) {
            gameField[y - 1][x].setStepToFinalPoint('D');
            return gameField[y - 1][x];
        }

        if (y + 1 < gameField.length && !gameField[y + 1][x].isHasBall() && gameField[y + 1][x].getDistanceIndex() == distance - 1) {
            gameField[y + 1][x].setStepToFinalPoint('U');
            return gameField[y + 1][x];
        }
        return null;
    }

    /**
     * Checks whether it is possible to set a route from the starting cell to the final one
     * @param data Input data
     */
    private static void checkRoute(InputData data) {
        Point endPoint = data.getEndPoint();
        boolean hasPathToFinishPoint = data.getGameField()[endPoint.y][endPoint.x].getDistanceIndex() != null;

        if (hasPathToFinishPoint) {
            Cell[][] gameField = data.getGameField();
            String path = getPathFromFinishToStartPoint(data.getGameField(), data.getEndPoint());
            displayResults(gameField, path, endPoint);
        } else {
            System.out.println("There is no path");
        }
    }

    /**
     * Displays the result of the program, and the shortest route to the cell
     * @param gameField GameField
     * @param path The order of steps that need to take to go from the starting cell to the final
     * @param endPoint Coordinates of the final cell
     */
    private static void displayResults(Cell[][] gameField, String path, Point endPoint) {

        for (Cell[] cells : gameField) {
            for (Cell cell : cells) {
                System.out.print(cell.getStepToFinalPoint());
                System.out.print("     ");
            }
            System.out.println();
        }
        System.out.println("End point path: " + path);
        System.out.println("Number of steps: " + gameField[endPoint.y][endPoint.x].getDistanceIndex());
    }
}
