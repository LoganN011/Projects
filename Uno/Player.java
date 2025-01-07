import java.util.ArrayList;

/**
 * The player class is the container for all the info needed to play UNUS
 * The player has the following info:
 * name - The name of the player held in a string but is the index they are in the game.
 * game - the current game state
 * hand - The current hand of the player. the cards that they are to use while playing UNUS
 *
 * @author Logan Nunno
 */
public class Player {
    private final String name;
    private final Game game;
    private final Hand hand;

    /**
     * Constructor for the player
     * sets the name to the variable that is passed in of the same name
     * also sets the game to the variable that is passed in of the same name
     * creates a new hand for the player as an array list
     *
     * @param name the name of the player. The number of the player they are in the game held in a string
     * @param game the current game state
     */
    public Player(String name, Game game) {
        this.name = name;
        this.game = game;
        this.hand = new Hand(new ArrayList<>());
    }

    /**
     * This function does the following:
     * - Attempts to draw num number of cards
     * - If a EmptyDeckException is caught then the play area
     * must be shuffled into the deck. Note this a function of game class
     * - Adds each drawn card to hand
     *
     * @param num Number of cards to be drawn
     */
    public void drawCards(int num) {
        int numsLeft = num;
        for (int i = 0; i < num; i++) {
            try {
                hand.addCard(game.getDeck().drawCard());
                numsLeft--;
            } catch (Deck.EmptyDeckException e) {
                game.shufflePlayAreaIntoDeck();
                drawCards(numsLeft);
            }

        }


    }

    /**
     * Performs IO to figure out what moves the user
     * wants to make. It does this as follows:
     * - Loops until the user has successfully played a card
     * - Prints out "Play area:\n"
     * - Prints out the top card
     * - Checks to see if the hand has any matches against the top card
     * - If it does not then print: "Your hand had no matches, a card was drawn."
     * - Then draw 1 card
     * - Then prints "Hand:\n"
     * - Then prints out the hand
     * - If the hand still has no matches then print: "Your hand still has no matches your turn is being passed"
     * and ends the turn
     * - Otherwise it asks the user: "Which card would you like to play?" using the game::interact function
     * - The code loops until the user successfully answers this question, the three criteria are:
     * - A valid int, if not print:
     * "$cardNumStr is not a valid integer, please try again."
     * where cardNumStr is the user input
     * - A valid match, if not print:
     * "Card $cardNumStr cannot currently be played, please try again."
     * where cardNumStr is the user input
     * - A valid index, if not print:
     * "$cardNumStr is not a valid index, please try again."
     */
    public void takeTurn() {
        boolean validPlay = false;
        System.out.print("Play area:\n");
        System.out.print(game.getTopCard());
        System.out.print("Hand:\n");
        System.out.println(hand);
        if (!hand.noMatches(game.getTopCard())) {
            System.out.println("Your hand had no matches, a card was drawn.");
            System.out.print("Hand:\n");
            drawCards(1);
            System.out.println(hand);
            if (!hand.noMatches(game.getTopCard())) {
                System.out.println("Your hand still has no matches your turn is being passed");
                validPlay = true;
            }
        }
        System.out.println();
        while (!validPlay) {
            String cardNumStr = game.interact("Which card would you like to play?");
            try {
                if (!(Integer.parseInt(cardNumStr) >= 0) || !(Integer.parseInt(cardNumStr)
                        <= hand.numCardsRemaining() - 1)) {
                    System.out.println(cardNumStr + " is not a valid index, please try again.");
                } else {
                    hand.playCard(game, Integer.parseInt(cardNumStr));
                    validPlay = true;
                }
            } catch (NumberFormatException e) {
                System.out.println(cardNumStr + " is not a valid integer, please try again.");
            } catch (Card.CannotPlayCardException e) {
                System.out.println("Card " + cardNumStr + " cannot currently be played, please try again.");
            }
        }


    }

    /**
     * A check to see if the current players hand is empty
     * checks the size of the hand by calling the numCardsRemaining method defined inside the hand class
     *
     * @return a boolean if the current players hand is empty or not
     */
    public boolean emptyHand() {
        return hand.numCardsRemaining() == 0;
    }

    /**
     * The to string method for the name of the player
     * Will return only the name of the player as a string
     * The name of the player is the number index they are in the list of players held as a string
     *
     * @return The name of the player as a string
     */
    @Override
    public String toString() {
        return name;
    }
}
