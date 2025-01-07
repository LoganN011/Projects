package scrabble;

import java.util.Scanner;

/**
 * Class that is used to compare an unlimited about of boards and compute
 * the score of the boards if they are valid moves
 *
 * @author Logan Nunno
 */
public class Controller {
    /**
     * Main method to start the program
     * Must have a command line argument that is the directory that is being used
     * in the rules for computing the score of the two boards
     *
     * @param args command line arguments used to enter a name of a file
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a file name");
        } else {
            Dictionary dictionary = new Dictionary(args[0]);
            //System.out.println(dictionary.getWords().isWord("at"));
            Scanner sc = new Scanner(System.in);
            while (sc.hasNext()) {
                Board.getPairOfBoards(dictionary, sc);
            }
        }
    }

}
