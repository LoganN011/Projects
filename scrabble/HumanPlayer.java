package scrabble;

import javafx.scene.control.TextInputDialog;

/**
 * A Human player is a player that is controlled by the user
 * has each step of the players move will be updated until it is played in pull
 * where it will update all aspects of the human player
 * extends the Abstract player class
 *
 * @author Logan Nunno
 */
public class HumanPlayer extends Player {
    private Board midMoveBoard;
    private Tile selectedTile;
    private BoardSquare selectedSquare;

    /**
     * Human player constructor
     *
     * @param bag shared bag of tiles of all users
     * @param board state of the board and where tiles will be played
     * @param dictionary the list of words that are valid stored as a Trie
     */
    public HumanPlayer(Bag bag, Board board, Dictionary dictionary) {
        this.bag = bag;
        this.board = board;
        this.dictionary = dictionary;
        buildTray();
        midMoveBoard = new Board(board.getBoard());
    }


    /**
     * Method that will return the output of the players turn as a board
     * The move must be valid and will update all bookkeeping including
     * the score and the tray
     * <p>
     * updates the board and resets the mid move board if the move was valid
     * also resets the selected tile and selected square on the board
     * if the move was not valid then all played tiles will return to the tray
     * and the mid move board will be reset and no board will be returned
     *
     * @return the output board of a players turn
     */
    @Override
    public Board takeTurn() {
        if (board.isLegalPlay(board.findAllDifferences(midMoveBoard), dictionary, midMoveBoard)) {
            score += board.scoreBoard(midMoveBoard, dictionary);
            board = new Board(midMoveBoard.getBoard());
            selectedSquare = null;
            selectedTile = null;
            buildTray();
            return board;
        } else {
            for (BoardSquare square : board.findAllDifferences(midMoveBoard)) {
                if (Character.isUpperCase(square.getLetter()) || !Character.isAlphabetic(square.getLetter())) {
                    tray.add(new Tile('*'));
                } else {
                    tray.add(new Tile(square.getLetter()));
                }
            }
            midMoveBoard = new Board(board.getBoard());
            return null;
        }
    }

    /**
     * sets the board of the player and also updates the mid move board
     * overrides the player classes method of the same name
     * @param board board that is being made a copy of and
     */
    public void setBoard(Board board) {
        super.setBoard(board);
        midMoveBoard = new Board(board.getBoard());
    }

    /**
     * updates the selected tile that will be played in the future on the board
     * calls method to check if the tile can be played on to the board
     * @param tile tile that is being saved
     */
    public void updateSelectedTile(Tile tile) {
        selectedTile = tile;
        addToBoard();
    }

    /**
     * gets the mid move board of the player
     *
     * @return the mid move board of the same size as the board with added tiles
     */
    public Board getMidMoveBoard() {
        return midMoveBoard;
    }

    /**
     * if both a tile and square on the board is selected then the tile will
     * be added to the board it will also remove it from the tray and reset
     * the variables to save them will be reset
     */
    private void addToBoard() {
        if (selectedTile != null && selectedSquare != null) {
            if (selectedTile.getLetter() == '*') {
                String input;
                do {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Human Player");
                    dialog.setHeaderText("Human Player");
                    dialog.setContentText("Please enter the letter you wish to play");
                    input = dialog.showAndWait().orElse(null);
                } while (input == null || input.length() != 1 || !Character.isLetter(input.charAt(0)));
                selectedTile = new Tile(input.trim().toUpperCase().charAt(0));
                System.out.println(input);
                    for (Tile current : tray) {
                        if (current.getLetter() == '*') {
                            tray.remove(current);
                            break;
                        }
                    }
            }
            midMoveBoard.addLetter(selectedTile.getLetter(),
                    selectedSquare.getRow(), selectedSquare.getCol());
            for (Tile current : tray) {
                if (current.equals(selectedTile)) {
                    tray.remove(current);
                    break;
                }
            }
            selectedSquare = null;
            selectedTile = null;
        }
    }

    /**
     * updates the selected square where a tile wants to be played
     * if a tile is also selected it will play in that position
     * @param square space on the board that was selected
     */
    public void updateSelectedSquare(BoardSquare square) {
        selectedSquare = square;
        addToBoard();
    }
}
