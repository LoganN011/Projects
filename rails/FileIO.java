package smart.rails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to read in from a file
 * Could be used for output in future
 *
 * @author Cameron Fox and Logan Nunno
 */
public class FileIO {

    /**
     * This is the method to read in a file.
     * It is being saved as a List<String> so we can access each part as needed.
     *
     * @param fileName name of file that is being read in
     */
    public static List<String> readFile(String fileName) {
        try {
            return Files.readAllLines(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
