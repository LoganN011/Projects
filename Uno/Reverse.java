/**
 * The reverse card changes the direction during the game of UNUS
 * It only has a color
 * Reverse extends the Card class
 *
 * @author Logan Nunno
 */
public final class Reverse extends Card {
    /**
     * Constructor for the Reverse card
     * class the super constructor on the card color
     *
     * @param cardColor the given color of the reverse card
     */
    public Reverse(Color cardColor) {
        super(cardColor);
    }

    /**
     * This function calls the reverse function
     * on the UnusIterator
     * reverse the direction of the game so the next player is the one before rather than the one after
     *
     * @param game the current game state
     */
    @Override
    public void doAction(Game game) {
        game.getPlayers().reverse();

    }

    /**
     * Checks to see if the other card is a reverse so that it can be played on
     * the reverse card
     *
     * @param other the card to check to see if it is a reverse card
     * @return if other is a reverse or not
     */
    @Override
    public boolean matchValue(Card other) {
        return other instanceof Reverse;
    }

    /**
     * Returns the string value of the reverse card
     * will be used in the toString method for the card class
     * will be on the right hand of the card to show the player what kind of card it is in the console
     *
     * @return The String representation of the reverse card
     */
    @Override
    public String strRep() {
        return "Rev";
    }
}
