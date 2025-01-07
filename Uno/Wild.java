/**
 * The Wild card is a card that allows any card to be played on top of it.
 * It is of color wild from the card class
 * extends the Card class
 *
 * @author Logan Nunno
 */
public class Wild extends Card {
    /**
     * Constructor for the Wild class
     * Calls the super constructor for card color
     *
     * @param cardColor The Color of the wild card (should be color.Wild)
     */
    public Wild(Color cardColor) {
        super(cardColor);
    }

    /**
     * do action does not do anything for wild because it does not affect to game or players around it
     *
     * @param game current game state
     */
    @Override
    public void doAction(Game game) {
    }

    /**
     * Checks to see if a card can be played on wild
     * will always be true because anything can be played on a wild card
     *
     * @param other card to be checked if it can be played
     * @return always will be true
     */
    @Override
    public boolean matchValue(Card other) {
        return true;
    }

    /**
     * Will return the String representation of the wild card
     * Will return the letter "W" to be used in the toString method for the Card class
     *
     * @return the String representation of the wild card
     */
    @Override
    public String strRep() {
        return "W";
    }
}
