import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class BallPath {

    public static void main(String[] args) {
        String path = getPathToFile();

    }


    private static String getPathToFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter path to the file with input data: ");
        String path = scanner.next();
        scanner.close();
        return path;
    }

    private static List<String> readFile(String path) {
        try {
            return Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Failed to read data from file. Check the specified file path.");
            return null;
        }
    }
}
