package scrabble;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is used to find the best move possible based on the provided board
 * and the provided tray
 *
 * @author Logan Nunno
 */
public class Solver {
    private Dictionary dictionary;
    private Board board;
    private ArrayList<Character> tray;
    private Direction direction;
    private Board bestMove;
    private String bestWord;

    /**
     * An enum used to determine the direction of the move that is trying to
     * be played
     */
    private enum Direction {
        ACROSS,
        DOWN
    }

    /**
     * A constructor to make a new solver
     * @param dictionary the dictionary of valid words
     * @param board the board that a move will be played on
     * @param tray the tray that we are using to find the best move
     */
    public Solver(Dictionary dictionary, Board board, ArrayList<Character> tray) {
        this.dictionary = dictionary;
        this.board = board;
        bestMove = new Board(board.getBoard());
        this.tray = tray;
    }

    /**
     * move the current position to the position before it
     * to be before that mean that if the direction is down then it will
     * be one row up and if it across then it will be one column left
     * @param pos the position that is moving
     */
    private void before(Point pos) {
        //x is col
        //y is row
        if (direction == Direction.DOWN) {
            pos.setLocation(pos.x, pos.y - 1);
        } else {
            pos.setLocation(pos.x - 1, pos.y);
        }
    }

    /**
     * move the current position to the position after it
     * to be after that mean that if the direction is down then it will
     * be one row down and if it across then it will be one column right
     * @param pos the position that is moving
     */
    private void after(Point pos) {
        if (direction == Direction.DOWN) {
            pos.setLocation(pos.x, pos.y + 1);
        } else {
            pos.setLocation(pos.x + 1, pos.y);
        }
    }

    /**
     * gets the best move the solver has found
     * @return the board of the best move
     */
    public Board getBestMove() {
        return bestMove;
    }

    /**
     * A method to find all Anchors squares on the board
     * an anchor is a square that has a connection if a letter is placed there
     * used to reduce the time it takes to find a valid word
     * if nothing is on the board then the center square will be an anchor
     *
     * @return the List of board squares that are anchors
     */
    private ArrayList<BoardSquare> findAnchors() {
        ArrayList<BoardSquare> anchors = new ArrayList<>();
        for (BoardSquare[] row : board.getBoard()) {
            for (BoardSquare square : row) {
                if (!square.hasLetter() &&
                        board.hasConnection(square, new ArrayList<>())) {
                    anchors.add(square);
                }
            }
        }
        if (anchors.isEmpty()) {
            anchors.add(board.getBoard()[board.getBoard().length / 2]
                    [board.getBoard().length / 2]);
        }
        return anchors;
    }

    /**
     * Method to check if a move that was found is legal and place it on the board
     * in the desired location. If the play that was found is legal then it will
     * check to see if it is a higher scoring move than the one that is currently saved
     * if it is then it will be the new highest scoring move
     *
     * @param word      real word that was found
     * @param lastPoint last position of the word ie where the word will end
     */
    private void legalMove(String word, Point lastPoint) {
        Board copy = new Board(board.getBoard());
        Point playPos = new Point(lastPoint.x, lastPoint.y);
        int wordIndex = word.length() - 1;
        while (wordIndex >= 0 && board.inBounds(lastPoint.y, lastPoint.x)) {
            copy.addLetter(word.charAt(wordIndex), playPos);
            wordIndex--;
            before(playPos);
        }
        ArrayList<BoardSquare> differences = board.findAllDifferences(copy);
        if (board.isLegalPlay(differences, dictionary, copy)) {
            if (board.scoreBoard(bestMove, dictionary) < board.scoreBoard(copy, dictionary)) {
                bestMove = copy;
                bestWord = word;
            }
        }
    }

    /**
     * Method that is used to find the best possible word that can come before a
     * provided position. It will check based on the number of spaces before
     * an anchor what words can go there. it does this by first checking the provided spot
     * it then checks to see that we are not going past the limit. it then looks
     * at all the current children to see if we have that letter in our tray.
     * if we do then it calls itself and keeps going.
     *
     * @param partialWord part of a word that is being checked and added to
     * @param currentNode the current node in the word tree where we are looking
     *                    for children
     * @param nextPos     the next position that we at
     * @param limit       how many spaces we can go before the node before we go out
     *                    of bounds of the board
     */
    private void beforePart(String partialWord, TrieNode currentNode, Point nextPos, int limit) {
        afterPart(partialWord, currentNode, nextPos, false);
        if (limit > 0) {
            TrieNode[] children = currentNode.children();
            for (int i = 0; i < children.length; i++) {
                TrieNode child = children[i];
                if (child != null && tray.contains((char) ('a' + i))) {
                    tray.remove(tray.indexOf((char) ('a' + i)));
                    beforePart(partialWord + (char) ('a' + i),
                            child, nextPos, limit - 1);
                    tray.add((char) ('a' + i));
                }
                if (child != null && tray.contains('*')) {
                    tray.remove(tray.indexOf('*'));
                    beforePart(partialWord + (char) (('a' + i) - 32),
                            child, nextPos, limit - 1);
                    tray.add('*');
                }

            }
        }
    }

    /**
     * Recursive method that is used to find a word based on the current location
     * and words that are in the tray. It does this by checking that the location
     * is with the bounds of the board. If it is then it checks to see a letter
     * is in that location on the board. If there is a letter than it will check
     * that their exists some word with that part of a word. and move forwards and
     * adds that letter to the partial word and calls its self.
     * if there is not a letter in that space on the board it will check each possible
     * child to see if we have that letter in our tray. if we do then it will move
     * forward and add the letter to the partial word and call itself.
     * The base case is that if the current partial word is a valid word and there
     * is not a letter next to us and the anchor has been filled then we call the legal
     * move method.
     * after each recursive call it move backwards and adds the letter it placed into
     * the partial word back onto the tray.
     *
     * @param partialWord  part of a word that is being checked and added to
     * @param currentNode  the current node in the word tree where we are looking
     *                     for children
     * @param nextPos      the next position that we at
     * @param anchorFilled if the anchor that we are checking has been filled yet
     */
    private void afterPart(String partialWord, TrieNode currentNode,
                           Point nextPos, boolean anchorFilled) {
        if (currentNode.isWord() && !board.hasLetter(nextPos.y, nextPos.x)
                && anchorFilled) {
            before(nextPos);
            legalMove(partialWord, nextPos);
            after(nextPos);
        }
        if (board.inBounds(nextPos.y, nextPos.x)) {
            if (!board.hasLetter(nextPos.y, nextPos.x)) {
                TrieNode[] children = currentNode.children();
                for (int i = 0; i < children.length; i++) {
                    TrieNode child = children[i];
                    if (child != null && tray.contains((char) ('a' + i))) {
                        tray.remove(tray.indexOf((char) ('a' + i)));
                        after(nextPos);
                        afterPart(partialWord + (char) ('a' + i),
                                child, nextPos, true);
                        before(nextPos);
                        tray.add((char) ('a' + i));
                    }
                    if (child != null && tray.contains('*')) {
                        tray.remove(tray.indexOf('*'));
                        after(nextPos);
                        afterPart(partialWord + (char) (('a' + i) - 32),
                                child, nextPos, true);
                        before(nextPos);
                        tray.add('*');
                    }
                }
            } else {
                char existingLetter =
                        board.getBoard()[nextPos.y][nextPos.x].getLetter();
                if (currentNode.getChild(existingLetter) != null) {
                    after(nextPos);
                    afterPart(partialWord + existingLetter,
                            currentNode.getChild(existingLetter), nextPos, true);
                    before(nextPos);
                }
            }
        }
    }

    /**
     * A method that will find all options of move that can be made
     * it does this by look each direction and every anchor position and calls
     * methods to look at the words that can go there.
     */
    public void findAllOptions() {
        for (Direction direction : Direction.values()) {
            this.direction = direction;
            ArrayList<BoardSquare> anchors = findAnchors();
            for (BoardSquare anchor : anchors) {
                ArrayList<BoardSquare> connections = new ArrayList<>();
                Point anchorPos = new Point(anchor.getCol(), anchor.getRow());
                before(anchorPos);
                if (board.hasLetter(anchorPos.y, anchorPos.x)) {
                    board.findConnectionBackwards(new ArrayList<>(), board,
                            anchor.getRow(), anchor.getCol() - 1, connections, true);
                    String partialWord = "";
                    for (BoardSquare connection : connections) {
                        partialWord += connection.getLetter();
                    }
                    TrieNode current = dictionary.getWords().lookUp(partialWord);
                    if (current != null) {
                        afterPart(partialWord, current, new Point(anchor.getCol(),
                                anchor.getRow()), false);
                    }
                } else {
                    int limit = 0;
                    Point temp = new Point(anchor.getCol(), anchor.getRow());
                    before(temp);
                    while (board.inBounds(temp.y, temp.x) && !board.hasLetter(temp.y, temp.x)) {
                        limit++;
                        before(temp);
                    }
                    beforePart("", dictionary.getWords().lookUp(""),
                            new Point(anchor.getCol(), anchor.getRow()), limit);
                }
            }
        }
    }


    /**
     * reads the board and the tray that will be used in the solver
     * from standard input
     *
     * @param sc   scanner used to read from the console
     * @param tray an array of chars that is used to represent the tray
     * @return the board that was read in from the console
     */
    private static Board readInputBoard(Scanner sc, ArrayList<Character> tray) {
        Board board = Board.readInputBoard(sc);
        String line = sc.next();
        for (char c : line.toCharArray()) {
            tray.add(c);
        }
        return board;
    }

    /**
     * Main method that is used to read in and unlimited amount of boards and trays
     * and output the best possible move that can be made. There are required command
     * line arguments of the file path of the list of words that will be counted as
     * valid words
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a file name");
        } else {
            Dictionary dictionary = new Dictionary(args[0]);
            Scanner sc = new Scanner(System.in);
            while (sc.hasNext()) {
                ArrayList<Character> tray = new ArrayList<>();
                Solver test = new Solver(dictionary, readInputBoard(sc, tray), tray);
                System.out.println("Input Board:");
                test.board.printBoard();
                System.out.print("Tray: ");
                for (char c : tray) {
                    System.out.print(c);
                }
                test.findAllOptions();
                System.out.println("\nSolution " + test.bestWord + " has " +
                        test.board.scoreBoard(test.bestMove, dictionary) +
                        " points\nSolution Board:");
                test.getBestMove().printBoard();
                System.out.println();
            }
        }
    }
}
