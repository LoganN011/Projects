import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is a helper class that will read in a file and
 * then return a list of each of the lines in that file
 * This is how we are getting all the given words in the words.txt file
 *
 * @author Logan Nunno
 */
public class Utils {
    /**
     * A method to read each word in the file and add it to a list to be returned
     *
     * @param path the path of the file stored in the system
     * @return a list of all words
     * @throws FileNotFoundException if file cannot be found with provided path
     */
    public static List<String> readWords(String path) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(path));
        List<String> words = new ArrayList<>();

        while (sc.hasNextLine()) {
            words.add(sc.nextLine());
        }

        return words;
    }
}
