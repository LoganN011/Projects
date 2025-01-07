package scrabble;

/**
 * A computer player is a player that automatically takes a move based on the
 * state of the board and the tray using a solver to find the best move possible
 *
 * @author Logan Nunno
 */
public class ComputerPlayer extends Player {
    private Solver solver;

    /**
     * constructor for a computer player
     * builds all standard things used in a game of scrabble and also a solver
     *
     * @param bag        shared bag of tiles of all users
     * @param board      state of the board and where tiles will be played
     * @param dictionary the list of words that are valid stored as a Trie
     */
    public ComputerPlayer(Bag bag, Board board, Dictionary dictionary) {
        this.bag = bag;
        this.board = board;
        this.dictionary = dictionary;
        buildTray();
        solver = new Solver(dictionary, board, convertTray());
    }

    /**
     * Sets the board of the computer player and also makes a new solver
     * with the updated board
     * @param board board that is being made a copy of and
     */
    public void setBoard(Board board) {
        super.setBoard(board);
        solver = new Solver(dictionary, board, convertTray());
    }

    /**
     * Method that will return the output of the players turn as a board
     * The move must be valid and will update all bookkeeping including
     * the score and the tray
     * <p>
     * uses the solver to find the best move possible based on the state of
     * the game and takes that move. The move will always be legal and will
     * update all bookkeeping of the computer player based on the move take
     * @return the output board of a players turn
     */
    @Override
    public Board takeTurn() {
        solver.findAllOptions();
        Board temp = solver.getBestMove();
        score += board.scoreBoard(temp, dictionary);
        updateTray(board.findAllDifferences(temp));
        board = new Board(temp.getBoard());
        //board.printBoard();
        return board;
    }

    /**
     * Method that will find the best move and will return
     * the board that represents that move
     *
     * @return the board that has the next best move on it
     */
    public Board getBestMove() {
        solver.findAllOptions();
        return solver.getBestMove();
    }

}
