import java.util.ArrayList;
import java.util.List;

/**
 * The skip card skips a player while playing the game of UNUS
 * You can skip either the next player or a specific player
 * The skip card only has a color
 * Skip class extends the Card class
 *
 * @author Logan Nunno
 */
public final class Skip extends Card {
    /**
     * Constructor for the Skip card
     * Takes in a color
     * Calls the super constructor on the card Color
     *
     * @param cardColor The color of the skip card
     */
    public Skip(Color cardColor) {
        super(cardColor);
    }

    /**
     * Skip can skip any player except for the player who played it.
     * This function accomplishes the following:
     * - Prompts the user who they would like to skip with the following message:
     * "Who would you like to skip? (n)ext or (s)pecific user?"
     * - If the answer is "n" then the next player is skipped
     * - If the answer is "s" then a specific player is skipped
     * - The user must then be prompted with the following prompt:
     * "Please choose from the following numbers: $playerNumbers"
     * where playerNumbers are all the indices of players other than the current player seperated by spaces
     * - You must loop until they give a valid index, if they fail output the following message:
     * "$playerNumber is not valid."
     * where playerNumber is the number they input
     * - If they give an index that is not a number then output the following message and loop again:
     * "$n not an int, please try again."
     * where n is the index they input
     * - You must loop until they give you a valid command, if they fail output the following message:
     * "$answer is not a recognized command, please try again."
     *
     * @param game The current game state
     */
    @Override
    public void doAction(Game game) {
        String playerToChoose = "";
        List<Integer> playerNumbers = new ArrayList<>();
        for (int i = 0; i < game.getNumPlayers(); i++) {
            if (i != game.getPlayers().getCurIndex()) {
                playerNumbers.add(i);
                playerToChoose += i + " ";
            }
        }
        boolean validAnswer = false;
        String playerCommand = game.interact("Who would you like to skip? (n)ext or (s)pecific user?");
        while (!validAnswer) {
            if (playerCommand.length() == 0) {
                playerCommand = game.interact(playerCommand + " is not a recognized command, please try again.");
            } else if (playerCommand.charAt(0) == 'n') {
                if (game.getPlayers().getCurIndex() + 1 >= game.getNumPlayers() && game.getPlayers().getDir() == 1) {
                    game.getPlayers().skip(0);
                    validAnswer = true;
                } else if (game.getPlayers().getCurIndex() - 1 == -1 && game.getPlayers().getDir() == -1) {
                    game.getPlayers().skip(game.getNumPlayers() - 1);
                    validAnswer = true;
                } else {
                    game.getPlayers().skip(game.getPlayers().getCurIndex() + game.getPlayers().getDir());
                    validAnswer = true;
                }
            } else if (playerCommand.charAt(0) == 's') {
                boolean validIndex = false;
                do {
                    String playerString = game.interact("Please choose from the following numbers: "
                            + playerToChoose);
                    try {
                        int playerNumber = Integer.parseInt(playerString);
                        boolean containsNumber = false;
                        for (int current : playerNumbers) {
                            if (current == playerNumber) {
                                containsNumber = true;
                                validIndex = true;
                            }
                        }
                        if (containsNumber) {
                            validIndex = true;
                            validAnswer = true;
                            game.getPlayers().skip(playerNumber);
                        } else {
                            System.out.println(playerNumber + " is not valid.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(playerString + " not an int, please try again.");
                    }
                } while (!validIndex);

            } else {
                playerCommand = game.interact(playerCommand + " is not a recognized command, please try again.");
            }
        }

    }

    /**
     * Checks to see if the other card is a skip card
     *
     * @param other Card to check if it is a skip card
     * @return if the other card is a skip or not
     */
    @Override
    public boolean matchValue(Card other) {
        return other instanceof Skip;
    }

    /**
     * Returns the string value of the Skip card
     * will be used in the toString method for the card class
     * will be on the right hand of the card to show the player what kind of card it is in the console
     *
     * @return The String representation of the Skip card
     */
    @Override
    public String strRep() {
        return "S";
    }
}
