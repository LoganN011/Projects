package dominos;

import java.util.ArrayList;

/**
 * A Player is a Human that has a majority of the logic for making valid
 * moves and checking logic that has to do with a game of dominos
 *
 * @author Logan Nunno
 */
public class Player {

    private ArrayList<Domino> hand = new ArrayList<>();
    private static final int HAND_SIZE = 7;

    /**
     * Constructor for a new player
     * Shares a boneyard with all other players and computers
     *
     * @param boneyard boneyard that player will draw from
     */
    public Player(Boneyard boneyard) {
        buildHand(boneyard);
    }

    /**
     * Builds the players hand to have a specific number of dominos in their
     * hand at the start of the game default is 7
     * @param boneyard boneyard where dominos will be drawn from
     */
    private void buildHand(Boneyard boneyard) {
        for (int i = 0; i < HAND_SIZE; i++) {
            hand.add(boneyard.drawDomino());
        }
    }

    /**
     * Gets the total number of dots of the player
     * Sums both the right and left side of each domino currently in the players
     * hand
     * @return the number of dots in the players hand
     */
    public int getNumberOfDots() {
        int count = 0;
        for (Domino domino : hand) {
            count += domino.getLeftSide();
            count += domino.getRightSide();
        }
        return count;
    }

    /**
     * Gets a string representation of the players current hand
     * @return a string of the players hand
     */
    public String printHand() {
        return hand.toString();
    }

    /**
     * Gets the players current hand as a list
     * @return an Array list of dominos
     */
    public ArrayList<Domino> getHand() {
        return hand;
    }

    /**
     * gets the number of dominos in the players hand
     * @return the number of dominos in the hand
     */
    public int getNumberOfBonesInHand() {
        return hand.size();
    }

    /**
     * Checks if a domino is valid to be played give the number it is trying to
     * be played next to
     *
     * @param domino         domino that is trying to be played
     * @param availableSpace number that domino could be played next to
     * @return if a move is valid or invalid
     */
    protected boolean isValidMove(Domino domino, int availableSpace) {
        if (availableSpace == 0) {
            return true;
        } else if (domino.getRightSide() == availableSpace
                || domino.getLeftSide() == availableSpace) {
            return true;
        }
        return domino.getLeftSide() == 0 || domino.getRightSide() == 0;
    }

    /**
     * Checks if a domino can be played either rotated or not rotated
     * @param domino1 domino that is being checked
     * @param availableSpace space domino is trying to be played
     * @return if a domino can be played on either side
     */
    protected boolean canBePlayedEitherSide(Domino domino1, int availableSpace) {
        if (isValidMove(domino1, availableSpace)) {
            return availableSpace == 0 ||
                    (domino1.getRightSide() == availableSpace
                            && domino1.getLeftSide() == 0) ||
                    (domino1.getLeftSide() == availableSpace
                            && domino1.getRightSide() == 0);
        }
        return false;
    }

    /**
     * Checks if the player has a valid move
     *
     * @param board board where player want to make a move
     * @return if a valid move exists
     */
    public boolean hasValidMove(PlayArea board) {
        for (Domino current : hand) {
            if (isValidMove(current, board.getLeftSide()) ||
                    isValidMove(current, board.getRightSide())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to draw a card from the bone yard if the player does not have
     * a valid move a nd the bone yard has dominos
     * @param board board where play is playing the game
     * @param boneyard place where dominos are being drawn from
     */
    public void tryDrawing(PlayArea board, Boneyard boneyard) {
        //TODO:Make GUI version maybe
        if (!hasValidMove(board) && boneyard.hasBones()) {
            hand.add(boneyard.drawDomino());
            System.out.println("DRAWING CARD");
        }
    }

    /**
     * Checks that if a domino is flipped then it will still be a valid move to
     * be played
     * @param domino domino that is being checked
     * @param board board where domino is being played
     * @param isLeft if a domino is being played on the left side
     * @return if a domino can be flipped and still be played
     */
    public boolean canBeFlipped(Domino domino, PlayArea board, boolean isLeft) {
        domino.flipDomino();
        if (isLeft) {
            if (domino.getRightSide() == board.getLeftSide() ||
                    (domino.getRightSide() == 0 || board.getLeftSide() == 0)) {
                return true;
            }
        } else {
            if (domino.getLeftSide() == board.getRightSide() ||
                    (domino.getLeftSide() == 0 || board.getRightSide() == 0)) {
                return true;
            }
        }
        domino.flipDomino();
        return false;
    }

    /**
     * Attempts to play a domino on the board on one side of the board and if
     * it being played flipped
     * @param domino domino being played
     * @param board board where domino is trying to be played
     * @param isLeft if the domino is being played on the left side or not
     * @param isFlipped is the domino being played flipped
     * @return if a domino was played on the board or not
     */
    public boolean playDomino(Domino domino, PlayArea board, boolean isLeft,
                              boolean isFlipped) {
        if (isLeft) {
            if (!isFlipped && (domino.getRightSide() == board.getLeftSide() ||
                    (domino.getRightSide() == 0 || board.getLeftSide() == 0))) {
                board.playLeftSide(domino);
                hand.remove(domino);
                return true;
            } else if (canBeFlipped(domino, board, isLeft)) {
                board.playLeftSide(domino);
                hand.remove(domino);
                return true;
            }
        } else {
            if (!isFlipped && (domino.getLeftSide() == board.getRightSide() ||
                    (domino.getLeftSide() == 0 || board.getRightSide() == 0))) {
                board.playRightSide(domino);
                hand.remove(domino);
                return true;
            } else if (canBeFlipped(domino, board, isLeft)) {
                board.playRightSide(domino);
                hand.remove(domino);
                return true;
            }
        }
        return false;

    }


}
