package dominos;

import java.util.LinkedList;

/**
 * A board of the current game
 * Has a list that represents the current state of the game
 * @author Logan Nunno
 */
public class PlayArea {
    private LinkedList<Domino> board = new LinkedList<>();

    /**
     * get the current number of dominos on the board
     *
     * @return the size of the board as an int
     */
    public int getBoardSize() {
        return board.size();
    }

    /**
     * gets the board
     * @return a linked list that holds dominos
     */
    public LinkedList<Domino> getBoard() {
        return board;
    }

    /**
     * gets the number on the left most side of the play area
     * @return a number representing the available number that can be played on
     * the left side
     */
    public int getLeftSide() {
        if (board.isEmpty()) {
            return 0;
        }
        return board.getFirst().getLeftSide();
    }

    /**
     * gets the number on the right most side of the play area
     * @return a number representing the available number that can be played on
     * the right side
     */
    public int getRightSide() {
        if (board.isEmpty()) {
            return 0;
        }
        return board.getLast().getRightSide();
    }

    /**
     * adds the provided domino to the left side of the play area
     * @param domino domino that will be added
     */
    public void playLeftSide(Domino domino) {
        board.addFirst(domino);
    }

    /**
     * adds the provided domino to the right side of the play area
     * @param domino domino that will be added
     */
    public void playRightSide(Domino domino) {
        board.addLast(domino);
    }

    /**
     * A string version of the board. All dominos will form two parallel rows
     * shifted by half a domino
     * @return a string representation of the current play area
     */
    public String toString() {
        String topRow = "";
        String bottomRow = "";
        for (int i = 0; i < board.size(); i += 2) {
            topRow += board.get(i).toString();
        }
        for (int i = 1; i < board.size(); i += 2) {
            bottomRow += board.get(i).toString();
        }
        return topRow + "\n  " + bottomRow;
    }


}
