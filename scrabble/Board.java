package scrabble;


import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A representation of the scrabble game board
 * has a size and a 2d array of board squares
 * also has all methods to determine if a move is valid
 *
 * @author Logan Nunno
 */
public class Board {
    private int size;
    private BoardSquare[][] board;


    /**
     * board constructor that just takes a size and nothing else
     * @param size size of the board
     */
    public Board(int size) {
        this.size = size;
        board = new BoardSquare[size][size];
    }

    /**
     * Board constructor to make a copy of any board with the same multipliers
     * and the same tiles played on it
     * @param board 2d array of board squares that is going to be copied
     */
    public Board(BoardSquare[][] board) {
        this(board.length);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.board[i][j] = new BoardSquare(board[i][j]);
            }
        }
    }

    /**
     * adds a provided letter to a provided row and column
     * @param letter letter that will be added to a board
     * @param row row where it will be added
     * @param col column where it will be added
     */
    public void addLetter(char letter, int row, int col) {
        board[row][col].setLetter(letter);
    }

    /**
     * add a letter to any provided position on the board
     * @param letter letter that will be added
     * @param pos Point where it will be added
     */
    public void addLetter(char letter, Point pos) {
        board[pos.y][pos.x].setLetter(letter);
    }

    /**
     * Updates a position on the board based on the square number provided and
     * the line for the multiplier or the letter on the board
     *
     * @param position place where the board will be updated
     * @param line     string of either a single letter or multiplier that is used
     *                 to update the board
     */
    public void updatePosition(int position, String line) {
        if (line.length() == 1) {
            board[position / size][position % size] =
                    new BoardSquare(position / size, position % size,
                            line.charAt(0), 1, 1);
        } else {
            int wordMult;
            int letterMult;
            if (line.charAt(0) == '.') {
                wordMult = 1;
            } else {
                wordMult = Integer.parseInt(line.charAt(0) + "");
            }
            if (line.charAt(1) == '.') {
                letterMult = 1;
            } else {
                letterMult = Integer.parseInt(line.charAt(1) + "");
            }

            board[position / size][position % size] =
                    new BoardSquare(position / size, position % size,
                            ' ', wordMult, letterMult);
        }

    }

    /**
     * prints the board to the console
     */
    public void printBoard() {
        System.out.print(this);
    }

    /**
     * a string representation of the board
     * a single letter means there is a letter on the board in that spot
     * two dots means that there is no multipliers in that location
     * a number then a dot means there is a word multiplier of that value in that spot
     * a dot then a number means there is a letter multiplier of that value in that spot
     * @return a string representing the board
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                s += board[i][j].toString();
                if (j != board[i].length - 1) {
                    s += " ";
                }
            }
            s += "\n";
        }
        return s;
    }

    /**
     * gets the board as a 2d array of board squares
     * @return the board as a 2d array of board squares
     */
    public BoardSquare[][] getBoard() {
        return board;
    }

    /**
     * reads a single board in from standard input based on the size provided
     * @param sc the scanner used to read in the board
     * @return the board that was read in from standard input
     */
    public static Board readInputBoard(Scanner sc) {
        int numberOfSpaces = -1;
        Board board;
        int size = sc.nextInt();
        board = new Board(size);
        numberOfSpaces++;
        for (int i = 0; i < size * size; i++) {
            board.updatePosition(numberOfSpaces++, sc.next());
        }
        return board;
    }

    /**
     * reads in two boards from standard input and prints them back out to the
     * user it also compares the town boards and prints how they are different and
     * if a move was legal and the score that move is worth
     * @param dictionary list of valid words that are used to find if a move
     *                   is legal or not
     * @param sc scanner used to read in the boards
     */
    public static void getPairOfBoards(Dictionary dictionary, Scanner sc) {
        Board orginalBoard = readInputBoard(sc);
        Board resultBoard = readInputBoard(sc);
        System.out.println("original board:");
        orginalBoard.printBoard();
        System.out.println("result board:");
        resultBoard.printBoard();
        System.out.println(orginalBoard.compareBoards(resultBoard, dictionary));
    }

    /**
     * Method to compute the score of a move if it was a legal move
     * will return the score of the move or -1 if the move was not valid
     * @param result the board after the move was made
     * @param dictionary list of valid words that are used to find if a move
     *      *            is legal or not
     * @return the score of the move or -1 if not a valid move
     */
    public int scoreBoard(Board result, Dictionary dictionary) {
        ArrayList<BoardSquare> differences = this.findAllDifferences(result);
        if (!differences.isEmpty() && isLegalPlay(differences, dictionary, result)) {
            if (this.findAllDifferences(result).size() == 7) {
                return ScoreChecker.checkScore(differences, dictionary, this, 50)
                        + getScoreForConnections(differences, dictionary, result);
            } else {

                return ScoreChecker.checkScore(differences, dictionary, this, 0)
                        + getScoreForConnections(differences, dictionary, result);
            }
        }
        return -1;
    }

    /**
     * Compares two boards and returns a string that says how they are different
     * and how much the move is worth and if the play is a legal move
     * or if they cannot be compared it will say that
     * if a move has 7 tiles played then a bonus of 50 will be added to the score
     * @param board other board that is being compared
     @param dictionary list of valid words that are used to find if a move
      *                   is legal or not
      * @return a string representing how the boards are different
     */
    public String compareBoards(Board board, Dictionary dictionary) {
        if (this.size != board.size) {
            return "boards are not the same size\nplay is not legal\n";
        }
        ArrayList<BoardSquare> differences = this.findAllDifferences(board);
        if (differences.isEmpty()) {
            return "play is empty\nplay is not legal\n";
        }
        if (differences.size() == 7) {
            return scoreMove(differences, dictionary, board, 50);
        }

        return scoreMove(differences, dictionary, board, 0);
    }

    /**
     * Method to compute the score of two boards and return a string representing
     * the difference of the two boards and how much the play was worth
     *
     * @param differences the different board square between the two boards
     * @param dictionary  list of valid words that are used to find if a move
     *                    is legal or not
     * @param board       the other board after the move was made
     * @param bonus       extra score that will be added if the move is 7 tiles played
     * @return a string representing the score and if a move is legal or not
     */
    public String scoreMove(ArrayList<BoardSquare> differences,
                            Dictionary dictionary, Board board, int bonus) {
        String result = "";
        if (!isIncompatibleBoards(differences)) {
            result += "play is " + differencesString(differences);
            result = result.substring(0, result.length() - 2);
            if (isLegalPlay(differences, dictionary, board)) {
                result += "\nplay is legal\n";
                result += "score is " + (ScoreChecker.checkScore(differences,
                        dictionary, this, bonus) +
                        getScoreForConnections(differences, dictionary, board));
            } else {
                result += "\nplay is not legal";
            }
        } else {
            for (BoardSquare square : differences) {
                if (!square.hasLetter()) {
                    if (!board.hasLetter(square.getRow(), square.getCol())
                            && this.hasLetter(square.getRow(), square.getCol())) {
                        result += "Incompatible boards: tile removed at ("
                                + square.getRow() + ", " + square.getCol() + ")";
                    } else if (!this.getBoard()[square.getRow()]
                            [square.getCol()].equals(square)) {
                        result += "Incompatible boards: multiplier mismatch at ("
                                + square.getRow() + ", " + square.getCol() + ")";
                    }
                }
            }
        }

        return result + "\n";
    }

    /**
     * Checks if a play is legal based on the differences between the board and the
     * dictionary. to be legal it must have a move made on it, must be played in
     * a single line, must be connected to another word on the board, the
     * connection word must be in the word list, must have a tile in the center of
     * the board, and the word that was played must be in the dictionary
     * @param differences the list of differences between the two boards
     * @param dictionary list of valid words that are used to find if a move
     *                   is legal or not
     * @param board the board after the play was made
     * @return if the play is legal or not
     */
    public boolean isLegalPlay(ArrayList<BoardSquare> differences, Dictionary dictionary, Board board) {
        if (differences.isEmpty()) {
            return false;
        }
        boolean isSplitPlay = isSplitPlay(differences, board);
        if (isSingleLinePlay(differences)) {
            completeWordPlay(differences, board);
        } else {
            return false;
        }
        if (!isSplitPlay && buildingPlay(differences, board)) {

        } else if (!board.hasConnection(differences) && !isEmpty() && !isSplitPlay) {
            return false;
        }
        if (ScoreChecker.checkScore(differences, dictionary, this, 0) == -1) {
            return false;
        } else if (!board.hasLetter(size / 2, size / 2)) {
            return false;
        } else if (getScoreForConnections(differences, dictionary, board) == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks to see if the boards are incompatible
     * to be incompatible there was a change in something about the board
     * like the multiplier or a letter was removed
     * @param differences list of board square that are different
     * @return if the boards are compatible or not
     */
    private boolean isIncompatibleBoards(ArrayList<BoardSquare> differences) {
        for (BoardSquare square : differences) {
            if (!square.hasLetter()) {
                return true;
            }
        }
        return false;
    }

    /**
     * a string representation of the differences between two boards
     * will be in the form of the letter and will say what row and column the letter was
     * played at
     * @param differences list of differences between two boards
     * @return a string representation of the differences
     */
    public String differencesString(ArrayList<BoardSquare> differences) {
        String result = "";
        for (int i = 0; i < differences.size(); i++) {
            result += differences.get(i).getLetter() + " at (" +
                    differences.get(i).getRow() + ", " +
                    differences.get(i).getCol() + "), ";
        }
        return result;
    }

    /**
     * Method to find all the differences between two boards
     * @param board other board that is being compared to
     * @return a list of board squares that are different
     */
    public ArrayList<BoardSquare> findAllDifferences(Board board) {
        ArrayList<BoardSquare> differentSquares = new ArrayList<>();
        for (int i = 0; i < this.getBoard().length; i++) {
            for (int j = 0; j < this.getBoard()[i].length; j++) {
                if (!this.getBoard()[i][j].equals(board.getBoard()[i][j])) {
                    differentSquares.add(board.getBoard()[i][j]);
                }
            }
        }
        return differentSquares;
    }

    /**
     * checks to see any of the differences between two boards are connected
     * to another word on the board. This means that it is checking to see if
     * the move made has a connection to another as required by the rules of scrabble
     * @param differences a list of difference between two boards
     * @return if the move has a connection to another word on the boards
     */
    private boolean hasConnection(ArrayList<BoardSquare> differences) {
        boolean result = false;
        for (int i = 0; i < differences.size(); i++) {
            if (hasConnection(differences.get(i), differences)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * checks to see if a single board square is next to another word on the board
     *
     * @param square square that is being checked
     * @param differences list of differences between the two boards
     * @return if the square is connected to a word on the board
     */
    public boolean hasConnection(BoardSquare square, ArrayList<BoardSquare> differences) {
        int row = square.getRow();
        int col = square.getCol();
        if (hasConnectionDirection(row + 1, col, differences)) {
            return true;
        } else if (hasConnectionDirection(row, col + 1, differences)) {
            return true;
        } else if (hasConnectionDirection(row - 1, col, differences)) {
            return true;
        } else if (hasConnectionDirection(row, col - 1, differences)) {
            return true;
        }
        return false;
    }

    /**
     * checks to see if the provided row and column has a letter and not in a new play
     * to be in a that means that it must have been on the original board not be
     * in the difference
     * @param row row that is being checked to see if it has a letter
     * @param col column that is being checked to see if it has a letter
     * @param differences the differences between two boards
     * @return if a specific row and column has a letter as is not in the new play
     */
    public boolean hasConnectionDirection(int row, int col, ArrayList<BoardSquare> differences) {
        return inBounds(row, col) && this.hasLetter(row, col) &&
                !inNewPlay(differences, row, col);
    }

    /**
     * checks to see if a provided row and column is in the new play
     * to be in the new play it has to be in the differences
     * @param differences the list of differences between the two boards
     * @param row row that is being checked
     * @param col column that is being checked
     * @return if it is in the new play or not
     */
    private boolean inNewPlay(ArrayList<BoardSquare> differences, int row, int col) {
        for (BoardSquare square : differences) {
            if (square.getRow() == row && square.getCol() == col) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks to see if the provided row and column are with the board
     * @param row the row that is being checked
     * @param col the column that is being checked
     * @return if it is with the board or not
     */
    public boolean inBounds(int row, int col) {
        return (row >= 0 && row < size) && (col >= 0 && col < size);
    }

    /**
     * checks to see if the board is empty of letters or not
     * @return if the board has any letters
     */
    public boolean isEmpty() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].hasLetter()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Give the play that was made find all the spaces where letters were
     * played that are connected to another word on the original board
     * @param differences differences between two boards
     * @return a list of board squares that are connected to another word
     */
    private ArrayList<BoardSquare> findAllConnections(ArrayList<BoardSquare> differences) {
        ArrayList<BoardSquare> connections = new ArrayList<>();
        for (int i = 0; i < differences.size(); i++) {
            if (hasConnection(differences.get(i), differences)) {
                connections.add(differences.get(i));
            }
        }
        return connections;
    }

    /**
     * a method to check if a provided row and column has a letter
     * @param row row that is being checked
     * @param col column that is being checked
     * @return if there is letter in the provided position
     */
    public boolean hasLetter(int row, int col) {
        return (inBounds(row, col) && this.getBoard()[row][col].hasLetter());
    }

    /**
     * Finds the word that the square is connected to
     *
     * @param square      square that is being checked
     * @param differences differences between two boards
     * @param result      board that is result of a play
     * @return the connecting word as an array list of board squares
     */
    public ArrayList<BoardSquare> findConnectionWord(
            BoardSquare square, ArrayList<BoardSquare> differences, Board result) {
        //Will break if the connection is in the middle of a word :(
        int row = square.getRow();
        int col = square.getCol();
        ArrayList<BoardSquare> connections = new ArrayList<>();
        if (hasConnectionDirection(row - 1, col, differences)) {
            int startRow = square.getRow() - 1;
            findConnectionBackwards(differences, result, startRow,
                    col, connections, false);
        } else if (hasConnectionDirection(row, col - 1, differences)) {
            int startCol = square.getCol() - 1;
            findConnectionBackwards(differences, result, row,
                    startCol, connections, true);
        } else if (hasConnectionDirection(row + 1, col, differences)) {
            int startRow = row + 1;
            connections.add(square);
            findConnectionForwards(differences, result, startRow,
                    col, connections, false);
        } else if (hasConnectionDirection(row, col + 1, differences)) {
            int startCol = col + 1;
            connections.add(square);
            findConnectionForwards(differences, result, row,
                    startCol, connections, true);
        }
        return connections;

    }

    /**
     * method to find the connecting word forwards
     *
     * @param differences differences between two boards
     * @param result      board that is result of a play
     * @param row         starting row
     * @param col         staring column
     * @param connections connections that the word has
     * @param isCol       if it is moving forwards in the columns or not
     */
    private void findConnectionForwards(ArrayList<BoardSquare> differences,
                                        Board result, int row, int col,
                                        ArrayList<BoardSquare> connections,
                                        boolean isCol) {
        while (hasConnectionDirection(row, col, differences) ||
                result.hasLetter(row, col)) {
            connections.add(result.getBoard()[row][col]);
            if (isCol) {
                col++;
            } else {
                row++;
            }
        }
    }

    /**
     * method to find the connecting word backwards
     *
     * @param differences differences between two boards
     * @param result      board that is result of a play
     * @param row         starting row
     * @param col         staring column
     * @param connections connections that the word has
     * @param isCol       if it is moving backwards in the columns or not
     */
    public void findConnectionBackwards(ArrayList<BoardSquare> differences,
                                        Board result, int row, int col,
                                        ArrayList<BoardSquare> connections,
                                        boolean isCol) {
        while (hasConnectionDirection(row, col, differences) ||
                result.hasLetter(row, col)) {
            if (isCol) {
                col--;
            } else {
                row--;
            }

        }
        if (isCol) {
            col++;
        } else {
            row++;
        }
        while (result.hasLetter(row, col)) {
            connections.add(result.getBoard()[row][col]);
            if (isCol) {
                col++;
            } else {
                row++;
            }
        }
    }

    /**
     * gets the score for all of the connections that a play has
     *
     * @param differences the differences between two board
     * @param dictionary  list of valid words that are used to find if a move
     *                    is legal or not
     * @param result      board that is result of a play
     * @return score of the connecting words
     */
    public int getScoreForConnections(ArrayList<BoardSquare> differences,
                                      Dictionary dictionary, Board result) {
        int score = 0;
        ArrayList<BoardSquare> connections = findAllConnections(differences);
        for (BoardSquare square : connections) {
            //maybe delete the second check 
            if (!this.hasLetter(square.getRow(), square.getCol())) {
                if (ScoreChecker.checkScore(findConnectionWord(square,
                        differences, result), dictionary, this, 0) != -1) {
                    score += ScoreChecker.checkScore(findConnectionWord(square,
                            differences, result), dictionary, this, 0);
                } else {
                    return -1;
                }
            }
        }
        return score;
    }

    /**
     * checks to see if a move was made in one single line
     * @param differences move that was made on the board based on the board squares
     * @return if the play was made in a single line or not
     */
    private boolean isSingleLinePlay(ArrayList<BoardSquare> differences) {
        for (int i = 0; i < differences.size() - 1; i++) {
            if (Math.abs(differences.get(i).getCol() - differences.get(i + 1).getCol()) > 1) {
                return false;
            }
            if (Math.abs(differences.get(i).getRow() - differences.get(i + 1).getRow()) > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks to see if the move was a split play. to be a split play the
     * play must be have a gap in the letters originally played connected by letters
     * that were on the original board. Also fills in the differences with the letters
     * that connect them.
     * @param differences move that was made on the board based on the board squares
     * @param result board that is result of a play
     * @return if it is a split play or not
     */
    private boolean isSplitPlay(ArrayList<BoardSquare> differences, Board result) {
        int col = differences.getFirst().getCol();
        int row = differences.getFirst().getRow();
        for (int i = 0; i < differences.size() - 1; i++) {
            if (Math.abs(differences.get(i).getCol() -
                    differences.get(i + 1).getCol()) > 1) {
                return getSplitPlay(differences, result, row, col, true);
            }
            if (Math.abs(differences.get(i).getRow() -
                    differences.get(i + 1).getRow()) > 1) {
                return getSplitPlay(differences, result, row, col, false);
            }
        }
        return false;
    }

    /**
     * gets the whole word that was played when there is split play. fills
     * in the gaps between the differences
     *
     * @param differences move that was made on the board based on the board squares
     * @param result      board that is result of a play
     * @param row         staring row of the play
     * @param col         starting column of the play
     * @param isCol       if the play is moving in the columns or not
     * @return if it is a split play
     */
    private boolean getSplitPlay(ArrayList<BoardSquare> differences,
                                 Board result, int row, int col,
                                 boolean isCol) {
        int index = 0;
        while (inBounds(row, col) && result.hasLetter(row, col)) {
            if (!differences.contains(result.getBoard()[row][col])) {
                differences.add(index, result.getBoard()[row][col]);
            }
            index++;
            if (isCol) {
                col++;
            } else {
                row++;
            }
        }
        return true;
    }

    /**
     * A building play is a play that is added to the end or beginning of another
     * word that is on the board. will add the whole word that is played to the
     * differences array to be scored
     * @param differences move that was made on the board based on the board squares
     * @param result board that is result of a play
     * @return if it is a building play or not
     */
    private boolean buildingPlay(ArrayList<BoardSquare> differences, Board result) {
        ArrayList<BoardSquare> connections = new ArrayList<>();
        int row = differences.getFirst().getRow();
        int col = differences.getFirst().getCol();
        if (hasConnectionDirection(row - 1, col, differences) &&
                hasConnectionDirection(row, col - 1, differences)) {
            col--;
            while (inBounds(row, col) && board[row][col].hasLetter()) {
                differences.addFirst(result.getBoard()[row][col]);
                col--;
            }
            return true;
        } else {
            for (BoardSquare square : differences) {
                connections = findConnectionWord(square, differences, result);
                if (!connections.isEmpty()) {
                    break;
                }
            }
            for (BoardSquare square : differences) {
                if (!connections.contains(square)) {
                    return false;
                }
            }
            differences.clear();
            differences.addAll(connections);
            return !connections.isEmpty();
        }
    }

    /**
     * Completes a word that was played. if a word is played and is connected
     * to another word by the end of it. it will add all of those letters
     * to the play to be scored as one word
     * @param differences move that was made on the board based on the board squares
     * @param result board that is result of a play
     */
    private void completeWordPlay(ArrayList<BoardSquare> differences, Board result) {
        if (differences.size() >= 2) {
            int row = differences.get(0).getRow();
            int col = differences.get(0).getCol();
            int rowChange = Math.abs(row - differences.get(1).getRow());
            int colChange = Math.abs(col - differences.get(1).getCol());
            ArrayList<BoardSquare> connections = new ArrayList<>();
            while (inBounds(row, col) && result.hasLetter(row, col)) {
                row -= rowChange;
                col -= colChange;
            }
            row += rowChange;
            col += colChange;
            while (inBounds(row, col) && result.getBoard()[row][col].hasLetter()) {
                connections.add(result.getBoard()[row][col]);
                row += rowChange;
                col += colChange;
            }
            differences.clear();
            differences.addAll(connections);
        }

    }


}
