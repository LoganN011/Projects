package scrabble;

/**
 * A Class that is used to represent a space on board of scrabble
 *
 * @author Logan Nunno
 */
public class BoardSquare {
    private int wordMultiplier;
    private int letterMultiplier;
    private Tile letter;
    private int row;
    private int col;

    /**
     * Constructor for a Board square
     *
     * @param row              row where square is located
     * @param col              col where square is located
     * @param letter           letter that is in that location if any
     * @param wordMultiplier   multiplier for the word in that location
     * @param letterMultiplier multiplier for the letter in that location
     */
    public BoardSquare(int row, int col, char letter, int wordMultiplier,
                       int letterMultiplier) {
        this.row = row;
        this.col = col;
        this.letter = new Tile(letter);
        this.wordMultiplier = wordMultiplier;
        this.letterMultiplier = letterMultiplier;
    }

    /**
     * Constructor that will make a copy of a given board square
     * @param boardSquare square that will be copied
     */
    public BoardSquare(BoardSquare boardSquare) {
        this.row = boardSquare.row;
        this.col = boardSquare.col;
        this.letter = boardSquare.letter;
        this.wordMultiplier = boardSquare.wordMultiplier;
        this.letterMultiplier = boardSquare.letterMultiplier;
    }

    /**
     * gets the row of the current square
     * @return the row as an int
     */
    public int getRow() {
        return row;
    }

    /**
     * gets the column of the current square
     * @return the column as an int
     */
    public int getCol() {
        return col;
    }

    /**
     * gets the word multiplier for the current square
     * @return the word multiplier as an int
     */
    public int getWordMultiplier() {
        return wordMultiplier;
    }

    /**
     * gets the letter multiplier for the current square
     *
     * @return the letter multiplier as an int
     */
    public int getLetterMultiplier() {
        return letterMultiplier;
    }

    /**
     * gets the letter of the current square
     * @return the letter as a char of the current square
     */
    public char getLetter() {
        return letter.getLetter();
    }

    /**
     * gets the tile of the current square
     *
     * @return The Tile of the current square
     */
    public Tile getTile() {
        return letter;
    }

    /**
     * Checks to see if the current square has a letter
     * @return if there is a letter
     */
    public boolean hasLetter() {
        return letter.getLetter() != ' ';
    }

    /**
     * sets the letter for the current square and sets all multipliers to 1
     * @param letter the letter that is being added to the square
     */
    public void setLetter(char letter) {
        this.letter = new Tile(letter);
        letterMultiplier = 1;
        wordMultiplier = 1;
    }

    /**
     * String representation of the current board square
     *
     * If the square is unoccupied, the two characters will represent the word
     * and letter multipliers (if any) or just a period if there is no multiplier
     * (that is, the multiplier is one).
     *
     * If the square has a tile on it, the two characters will be a blank and the letter
     * of the tile. A lowercase letter represents an ordinary tile and an uppercase letter
     * represents a blank tile being used as that letter
     *
     * @return A string representation of a board square
     */
    public String toString(){
        String output="";
        if (letter.getLetter() == ' ') {
            if (wordMultiplier==1){
                output+='.';
            }
            else{
                output+=wordMultiplier;
            }
            if (letterMultiplier==1){
                output+='.';
            }
            else {
                output+=letterMultiplier;
            }
        }
        else{
            output=" "+letter;
        }
        return output;
    }

    /**
     * Get the score for the current board square
     * @return the score of the current square as an int
     */
    public int getScore() {
        return letter.getScore();
    }

    /**
     * checks to see if two squares are equal
     * @param other square being compared
     * @return if the two squares are the same
     */
    public boolean equals(BoardSquare other) {
        return this.getLetter() == other.getLetter() &&
                this.getLetterMultiplier() == other.getLetterMultiplier() &&
                this.getWordMultiplier() == other.getWordMultiplier();
    }
}
