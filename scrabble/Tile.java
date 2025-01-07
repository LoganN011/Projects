package scrabble;

/**
 * A class that represents a tile in a game of scrabble
 * Each tile has a letter and a score based on that letter
 *
 * @author Logan Nunno
 */
public class Tile {
    private char letter;
    private int score;

    /**
     * Constructor that takes in both a letter and a score to build a new tile
     *
     * @param letter letter that will be in the tile
     * @param score the score for that letter
     */
    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    /**
     * Constructor that takes in a letter and builds a tile that has that letter
     * and the score based on the letter value for the standard scrabble scoring
     *
     * @param letter letter that the tile represents
     */
    public Tile(char letter) {
        this(letter, checkLetter(letter));
    }

    /**
     * Get the current letter of the tile as char
     * @return the letter as a char
     */
    public char getLetter() {
        return letter;
    }

    /**
     * Gets the score that the tile is worth
     * @return the score as an int
     */
    public int getScore() {
        return score;
    }

    /**
     * A string representation of the Tile
     * @return a string version of the letter
     */
    public String toString() {
        return letter + "";
    }

    /**
     * Compares two tiles to see if they are equal
     * to be equal they must has the same letter and the same score
     * @param tile other tiles that is being compared
     * @return if they are equal as a boolean
     */
    public boolean equals(Tile tile) {
        return letter == tile.letter && getScore() == tile.getScore();
    }

    /**
     * gets the score of for a given char based on the standard scrabble scoring
     *
     * @param a char that is being checked
     * @return the score for the letter as an int
     */
    private static int checkLetter(char a) {
        switch (a) {
            case 'a', 'e', 'i', 'o', 'n', 'r', 't', 'l', 's', 'u' -> {
                return 1;
            }
            case 'd', 'g' -> {
                return 2;
            }
            case 'b', 'c', 'm', 'p' -> {
                return 3;
            }
            case 'f', 'h', 'v', 'w', 'y' -> {
                return 4;
            }
            case 'k' -> {
                return 5;
            }
            case 'j', 'x' -> {
                return 8;
            }
            case 'q', 'z' -> {
                return 10;
            }
            default -> {
                return 0;
            }
        }
    }
}
