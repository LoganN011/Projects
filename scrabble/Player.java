package scrabble;

import java.util.ArrayList;

/**
 * The player class is an abstract class that has the basic method for a player
 * to take a turn in a game of scrabble. This includes building their tray, and
 * updating their tray after a move.
 *
 * @author Logan Nunno
 */
public abstract class Player {
    private static final int HAND_SIZE = 7;
    protected ArrayList<Tile> tray = new ArrayList<>();
    protected Board board;
    protected Bag bag;
    protected int score = 0;
    protected Dictionary dictionary;

    /**
     * method used to build a players tray
     * it will draw tiles from the bag until the hand is full
     * or the bag does not have any tiles left
     */
    protected void buildTray() {
        while (tray.size() < HAND_SIZE && bag.hasTilesLeft()) {
            tray.add(bag.drawTile());
        }
    }

    /**
     * sets the players board to a new board with a new by making
     * a copy of the one that was passed in
     *
     * @param board board that is being made a copy of and
     */
    protected void setBoard(Board board) {
        this.board = new Board(board.getBoard());
    }

    /**
     * gets the number of tiles in the players hand currently
     * @return the number of tiles in the hand as an int
     */
    public int getNumberOfTilesInHand() {
        return tray.size();
    }

    /**
     * based on the tiles played on the board the player will have those tiles
     * remove and new ones drawn from the bag of tiles
     * @param differences move that was made on the board
     */
    protected void updateTray(ArrayList<BoardSquare> differences) {
        for (BoardSquare square : differences) {
            for (Tile tile : tray) {
                if (square.getTile().equals(tile)) {
                    tray.remove(tile);
                    break;
                }
            }
        }
        buildTray();
    }

    /**
     * gets the current score of the player
     *
     * @return the current score as an int
     */
    public int getScore() {
        return score;
    }

    /**
     * converts the tray of tiles to an array of Character to be used in a solver
     *
     * @return an ArrayList of Character representing the tray
     */
    protected ArrayList<Character> convertTray() {
        ArrayList<Character> tray = new ArrayList<>();
        for (Tile current : this.tray) {
            tray.add(current.getLetter());
        }
        return tray;
    }

    /**
     * Method that will return the output of the players turn as a board
     * The move must be valid and will update all bookkeeping including
     * the score and the tray
     *
     * @return the output board of a players turn
     */
    protected abstract Board takeTurn();
}
