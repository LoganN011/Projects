package dominos;

import java.util.ArrayList;

/**
 * A computer player extends the Player class
 * Only has one method the play domino method
 *
 * @author Logan Nunno
 */
public class ComputerPlayer extends Player {

    private ArrayList<Domino> hand = super.getHand();

    /**
     * Computer player constructor
     * calls the super constructor of the player class
     *
     * @param boneyard the bone yard that all players are currently playing with
     */
    public ComputerPlayer(Boneyard boneyard) {
        super(boneyard);
    }

    /**
     * Plays any available domino onto the board
     * if no domino in the hand is a valid move then it will draw cards until
     * it either the boneyard is empty or there is now a valid move
     * once a there exists at least one valid move loops from until it finds the
     * domino that can be played and plays it onto the board
     *
     * @param board    the current play area of the game
     * @param boneyard the bone yard that the players are using
     * @return if a card is played or not
     */
    public boolean playDomino(PlayArea board, Boneyard boneyard) {
        while (!hasValidMove(board) && boneyard.hasBones()) {
            super.tryDrawing(board, boneyard);

        }
        if (hasValidMove(board)) {
            for (int i = 0; i < hand.size(); i++) {
                if (super.isValidMove(hand.get(i), board.getLeftSide())) {
                    playDomino(hand.get(i), board, true, false);
                    return true;
                } else if (super.isValidMove(hand.get(i), board.getRightSide())) {
                    playDomino(hand.get(i), board, false, false);
                    return true;
                }
            }
        }
        return false;
    }

}
