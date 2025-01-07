/**
 * Class for the None card
 * This card is only used when the play area is empty
 * The card will act as a wild card
 * Extends The card class
 *
 * @author Logan Nunno
 */
public class None extends Card {
    /**
     * Constructor for the None card
     * All it does is call the super constructor with the color of wild
     */
    public None() {
        super(Color.WILD);
    }

    /**
     * This method does nothing within the none card, but it does override the cards doAction method
     *
     * @param game The current game state
     */
    @Override
    public void doAction(Game game) {
    }

    /**
     * Will alway return true because any card can be played on the none card like the wild
     *
     * @param other the card to be played
     * @return always return true
     */
    @Override
    public boolean matchValue(Card other) {
        return true;
    }

    /**
     * The string representation of the card witch is nothing
     *
     * @return returns a blank string to represent nothing
     */
    @Override
    public String strRep() {
        return "";
    }
}
