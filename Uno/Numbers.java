/**
 * The Numbers class is the basic UNUS card
 * It is has both a color and number
 * It extends the Card class
 * the number of the card can only be between 0-9
 *
 * @author Logan Nunno
 */
public class Numbers extends Card {
    private final int cardNumber;

    /**
     * Constructor for the Numbers card
     * Takes both a color and a number as an int
     * The number can only be 0-9
     * if the number is not between 0-9 it throws an IllegalArgumentException
     * The color calls the super constructor
     *
     * @param color      The color of the card
     * @param cardNumber The number of the card, between 0-9
     */
    public Numbers(Color color, int cardNumber) {
        super(color);

        if (!(cardNumber >= 0 && cardNumber <= 9)) {
            throw new IllegalArgumentException(cardNumber + " must be between [0,9]");
        }

        this.cardNumber = cardNumber;
    }

    /**
     * Getter for the number of the card
     *
     * @return The card number as an int
     */
    public int getN() {
        return cardNumber;
    }

    /**
     * This method does nothing because the numbers card does not affect
     * the next player or change the game in any meaningful way
     *
     * @param game The current game state
     */
    @Override
    public void doAction(Game game) {
    }

    /**
     * Checks to see if the two cards or both of type number
     * if they are then we return if the numbers of the cards match
     *
     * @param other card to be compared with the current card
     * @return if the two cards are both matching numbers
     */
    @Override
    public boolean matchValue(Card other) {
        if (other instanceof Numbers o) {
            return getN() == o.getN();
        } else {
            return false;
        }
    }

    /**
     * The string representation of the numbers card
     * to be used in other to string methods for the inside of the card
     *
     * @return The card number in a string
     */
    @Override
    public String strRep() {
        return Integer.toString(cardNumber);
    }
}