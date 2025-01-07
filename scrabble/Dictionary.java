package scrabble;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A dictionary is a file that is used to read in and store the words that are used
 * to play the game of scrabble
 *
 * @author Logan Nunno
 */
public class Dictionary {

    private TrieNode words = new TrieNode();

    /**
     * The constructor for the dictonary that takes in a path and builds and
     * Trie based on all of the words that are inside of the file that was
     * provided into the path
     *
     * @param path the file path for the words that will be added to the dictionary
     */
    public Dictionary(String path) {

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.toLowerCase().trim());
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Gets the root of the Trie for the dictionary
     *
     * @return the root of the trie
     */
    public TrieNode getWords() {
        return words;
    }
}
