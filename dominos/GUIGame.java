package dominos;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * GUI game of dominos. Normal game of dominos just with a GUI and not console
 * based. Extends Application to start a javafx GUI program
 *
 * @author Logan Nunno
 */
public class GUIGame extends Application {
    //What Domino is currently selected
    private static int clickedBone = -1;
    //The Boneyard all players are using
    private static Boneyard boneyard = new Boneyard();
    private static ComputerPlayer computerPlayer = new ComputerPlayer(boneyard);
    private static Player human = new Player(boneyard);
    //The board that all players are using
    private static PlayArea board = new PlayArea();
    //The main screen of the GUI as a Border Pane
    private static BorderPane root = new BorderPane();

    /**
     * Main method to start the JavaFX GUI program
     *
     * @param args - not used in this program
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start of the GUI.
     * will update the players tray, update the labels
     * and show the stage in order to start the GUI elements
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will not be
     *              primary stages.
     */
    public void start(Stage stage) {
        updateTray();
        updateLabels();
        stage.setMinHeight(350);
        stage.setMinWidth(1500);
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Builds the HBox to hold a single domino
     * builds a new Domino canvas to represent a domino on the screen
     *
     * @param domino domino that is being build
     * @return a new HBox holding a domino
     */
    private HBox buildDomino(Domino domino) {
        int rectangleSize = 50;
        return new HBox(new DominoCanvas(rectangleSize * 2, rectangleSize, domino));
    }

    /**
     * A method that takes in a HBox and an index and makes that index clickable
     * it will then update all things that have to do with that HBox
     *
     * @param tray  the tray of dominos that is being updated
     * @param index index that is now clickable and was clicked
     */
    private void updateClickable(HBox tray, int index) {
        tray.getChildren().get(index).setOnMouseClicked(event -> {
            clickedBone = index;
            updateLabels();
            updateTray();
            updatePlayArea();
        });
    }

    /**
     * Updates the tray on the screen
     * will make all dominos in the try clickable
     * will center the tray and add it to the bottom of the Boarder pane
     */
    private void updateTray() {
        HBox tray = new HBox();
        for (int i = 0; i < human.getHand().size(); i++) {
            tray.getChildren().add(buildDomino(human.getHand().get(i)));
            updateClickable(tray, i);
        }
        tray.setAlignment(Pos.CENTER);
        tray.setSpacing(2);
        root.setBottom(tray);
        root.setBottom(tray);
    }

    /**
     * Updates the labels at the top of the screen
     * one label is telling the user how many dominos are in the boneyard
     * the other is telling the user how many dominos the computer has in its hand
     */
    private void updateLabels() {
        Label boneyardLabel = new Label("Boneyard contains " +
                boneyard.getBonesCount() + " dominos");
        boneyardLabel.setFont(new Font("Arial", 30));
        Label computerLabel = new Label("Computer has " +
                computerPlayer.getNumberOfBonesInHand() + " dominos");
        computerLabel.setFont(new Font("Arial", 30));
        VBox labels = new VBox(boneyardLabel, computerLabel);
        labels.setAlignment(Pos.CENTER);
        root.setTop(labels);
    }

    /**
     * Updates the play area. when clicked it will play a domino based on the side
     * of the screen that was clicked if it is valid. if not a valid move then a
     * popup will, and you will have to select a new domino. if a domino can be
     * flipped then it will ask the user if they want to flip the domino in a
     * different popup
     */
    private void updatePlayArea() {
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER);
        HBox bottomRow = new HBox();
        bottomRow.setAlignment(Pos.CENTER);
        bottomRow.getChildren().add(new Rectangle(50, 50, Color.TRANSPARENT));
        for (int i = 0; i < board.getBoardSize(); i += 2) {
            topRow.getChildren().add(buildDomino(board.getBoard().get(i)));

        }
        for (int i = 1; i < board.getBoardSize(); i += 2) {
            bottomRow.getChildren().add(buildDomino(board.getBoard().get(i)));
        }
        VBox playArea = new VBox(topRow, bottomRow);
        playArea.setAlignment(Pos.CENTER);
        root.setCenter(playArea);
        playArea.setOnMouseClicked(event -> {
            boolean gameOver = false;
            if (clickedBone != -1) {
                boolean isFlipped = false;
                if (event.getX() > 750) {
                    if (human.canBePlayedEitherSide(human.getHand().get(clickedBone)
                            , board.getRightSide()) &&
                            human.getHand().get(clickedBone).getRightSide() !=
                                    human.getHand().get(clickedBone).getLeftSide()
                            && board.getBoardSize() != 0) {
                        isFlipped = isFlipped();
                    }

                    if (human.playDomino(human.getHand().get(clickedBone),
                            board, false, isFlipped)) {
                        gameOver = isGameOver(gameOver);

                    } else {
                        showInvalidMove();
                    }
                } else {
                    if (human.canBePlayedEitherSide(human.getHand().get(clickedBone)
                            , board.getLeftSide()) && human.getHand().get(clickedBone)
                            .getRightSide() != human.getHand().get(clickedBone)
                            .getLeftSide() && board.getBoardSize() != 0) {
                        isFlipped = isFlipped();
                    }
                    if (human.playDomino(human.getHand().get(clickedBone), board,
                            true, isFlipped)) {
                        gameOver = isGameOver(gameOver);
                    } else {
                        showInvalidMove();
                    }
                }
                clickedBone = -1;
                if (!gameOver) {
                    updateLabels();
                    updateTray();
                    updatePlayArea();
                }
            }
        });

    }

    /**
     * will ask the user in a popup if they want to flip the domino they are
     * trying to play
     *
     * @return true if yes, false if no
     */
    private static boolean isFlipped() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Do you want to flip the Domino?");
        return alert.showAndWait().get().getButtonData().toString().equals("OK_DONE");
    }

    /**
     * checks to see if the game is over after the human makes a move
     * then the computer will take its move and then the human will draw cards
     * if it needs to
     *
     * @param gameOver if the game is over or not
     * @return if the game is now over or not
     */
    private boolean isGameOver(boolean gameOver) {
        gameOver = checkEnding("Human", gameOver);
        if (!computerPlayer.playDomino(board, boneyard)) {
            gameOver = checkEnding("Human", gameOver);
        }
        gameOver = checkEnding("Computer", gameOver);
        while (!human.hasValidMove(board) && boneyard.hasBones()) {
            human.tryDrawing(board, boneyard);
        }
        gameOver = checkEnding("Computer", gameOver);
        return gameOver;
    }

    /**
     * Checks the ending of the game
     * when the game ends it will check who the winner based on the number of dots
     * that each player has. whoever has the least amount of dots will have a
     * label shown on screen that they win. if they have the same number of dots
     * then the last person who played is the winner
     *
     * @param winner   String representing the name of the last person who played
     * @param gameOver if a winner as already been selected
     * @return if someone has won the game
     */
    private boolean checkEnding(String winner, boolean gameOver) {
        if (!boneyard.hasBones() && (!(human.hasValidMove(board) && computerPlayer.hasValidMove(board))) && !gameOver) {
            root.getChildren().clear();
            Label gameOverLabel = new Label("Game Over");
            gameOverLabel.setFont(new Font("Arial", 30));
            Label computerLabel = new Label("Computer Wins");
            computerLabel.setFont(new Font("Arial", 30));
            Label humanLabel = new Label("Human Wins");
            humanLabel.setFont(new Font("Arial", 30));
            VBox labels = new VBox(gameOverLabel);
            labels.setAlignment(Pos.CENTER);
            if (human.getNumberOfDots() > computerPlayer.getNumberOfDots()) {
                labels.getChildren().add(computerLabel);
            } else if (human.getNumberOfDots() < computerPlayer.getNumberOfDots()) {
                labels.getChildren().add(humanLabel);
            } else if (human.getNumberOfDots() == computerPlayer.getNumberOfDots()) {
                if (winner.equals("Human")) {
                    labels.getChildren().add(humanLabel);
                } else {
                    labels.getChildren().add(computerLabel);
                }

            }
            root.setCenter(labels);
            return true;
        }
        return gameOver;

    }

    /**
     *
     */
    private void showInvalidMove() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Move");
        alert.showAndWait();
    }


}
