package dominos;

import java.util.Scanner;

/**
 * Main console version of the game.
 * Has all game logic for ending of the game and for taking console input from
 * the user.
 *
 * @author Logan Nunno
 */
public class ConsoleGame {

    /**
     * Main game loop of the console version of the game of dominos
     * Will start the game. The human play will always go first.
     * Takes input from the human and asks what domino they want to play. If it
     * is not the first move of the game it will then ask what side they wish to
     * play the domino. If that side allows for the domino to be rotated then it
     * will ask if they want to flip the domino. Once the human play has made
     * their turn the computer player will go next. The check for the end of the
     * game is after each player makes their move. Once the game end the number
     * of dots is used to find the winner. if they have the same number of dots
     * then the last player to make a move wins
     * @param args - command line arguments not used in this file
     */
    public static void main(String[] args) {
        Boneyard boneyard = new Boneyard();
        ComputerPlayer computerPlayer = new ComputerPlayer(boneyard);
        Player human = new Player(boneyard);
        Player[] players = {human, computerPlayer};
        PlayArea board = new PlayArea();
        Scanner input = new Scanner(System.in);
        int turnIndex = 0;
        System.out.println("Dominos!");
        do {
            System.out.println("Computer has " +
                    computerPlayer.getNumberOfBonesInHand() + " dominos");
            System.out.println("Boneyard contains " + boneyard.getBonesCount() +
                    " dominos");

            System.out.println(board);

            if (players[turnIndex] instanceof ComputerPlayer) {
                System.out.println("Computer's turn");

                if (!computerPlayer.playDomino(board, boneyard)) {
                    turnIndex++;
                    break;
                }

            } else {
                while (!human.hasValidMove(board) && boneyard.hasBones()) {
                    human.tryDrawing(board, boneyard);
                }
                if (!human.hasValidMove(board) && !boneyard.hasBones()) {
                    turnIndex++;
                    break;
                }
                System.out.println("Tray: " + human.printHand());
                System.out.println("Human's turn");

                String sideOption;
                String isFlipped = "";
                while (true) {
                    System.out.println("What domino do you want to play?");
                    int index;
                    while (true) {
                        while (!input.hasNextInt()) {
                            System.out.println("Try again: ");
                            input.next();
                        }
                        index = input.nextInt() - 1;
                        if (index >= 0 && index <=
                                human.getNumberOfBonesInHand() - 1) {
                            break;
                        }
                        System.out.println("Try again: ");
                    }
                    if (board.getBoardSize() != 0) {
                        System.out.println("Left or Right? (l/r)");
                        sideOption = getInput(input, "l", "r");
                        if (sideOption.contains("r")) {
                            if (human.canBePlayedEitherSide(
                                    human.getHand().get(index),
                                    board.getRightSide())) {
                                System.out.println("Do you want to rotate" +
                                        " the domino? (y/n)");
                                isFlipped = getInput(input, "y", "n");
                            }

                            if ((isFlipped.contains("n") || isFlipped.isEmpty())
                                    && human.playDomino(human.getHand().get(index),
                                    board, false, false)) {
                                System.out.println("Playing on the Right side");
                                break;
                            } else if ((isFlipped.contains("y") ||
                                    isFlipped.isEmpty()) &&
                                    human.playDomino(human.getHand().get(index),
                                            board, false, true)) {
                                System.out.println("Playing on the Right side");
                                break;
                            } else {
                                System.out.println("Not a real move!!!" +
                                        "\nTry again: ");

                            }
                        } else if (sideOption.contains("l")) {
                            if (human.canBePlayedEitherSide(human.getHand()
                                    .get(index), board.getLeftSide())) {
                                System.out.println("Do you want to rotate" +
                                        " the domino? (y/n)");
                                isFlipped = getInput(input, "y", "n");
                            }
                            if ((isFlipped.contains("n") || isFlipped.isEmpty())
                                    && human.playDomino(human.getHand().get(index),
                                    board, true, false)) {
                                System.out.println("Playing on the Left side");
                                break;
                            } else if ((isFlipped.contains("y") ||
                                    isFlipped.isEmpty()) &&
                                    human.playDomino(human.getHand().get(index),
                                            board, true, true)) {
                                System.out.println("Playing on the Left side");
                                break;
                            } else {
                                System.out.println("Not a real move!!!" +
                                        "\nTry again: ");
                            }
                        }
                    } else {
                        human.playDomino(human.getHand().get(index),
                                board, false, false);
                        break;
                    }
                }
            }
            turnIndex++;
            if (turnIndex == players.length) {
                turnIndex = 0;
            }
            System.out.println();
        } while (boneyard.hasBones());
        System.out.println("\nGAME OVER!");
        if (human.getNumberOfDots() > computerPlayer.getNumberOfDots()) {
            System.out.println("COMPUTER WINS!!!");
        } else if (human.getNumberOfDots() < computerPlayer.getNumberOfDots()) {
            System.out.println("HUMAN WINS!!!");
        } else if (human.getNumberOfDots() == computerPlayer.getNumberOfDots()) {
            if (turnIndex == 1) {
                System.out.println("HUMAN WINS!!!");
            } else {
                System.out.println("COMPUTER WINS!!!");
            }

        }

    }

    /**
     * Gets the input from the user. Will run until the user enters a valid option
     * The checks that the input contains one of the options but not both
     *
     * @param input a scanner to get the input from the console
     * @param option1 option one for the user to select
     * @param option2 option two for the user to select
     * @return the option that the user selected
     */
    private static String getInput(Scanner input, String option1,
                                   String option2) {
        String optionSelection;
        while (true) {
            optionSelection = input.next().toLowerCase();
            if (optionSelection.contains(option1) ^ optionSelection.contains(
                    option2)) {
                break;
            }
            System.out.println("Try again: ");
        }
        return optionSelection;
    }


}
