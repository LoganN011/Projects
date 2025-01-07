package scrabble;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Display class that extends Application to launch a javaFx screen
 * Used to play the game of scrabble in a GUI
 * has all the required objects for a game of scrabble
 *
 * @author Logan Nunno
 */
public class Display extends Application {
    private Bag bag;
    private Board board;
    private static final int SQUARE_SIZE = 30;
    private ComputerPlayer computerPlayer;
    private HumanPlayer humanPlayer;
    private GridPane grid;
    private HBox tray;
    private BorderPane root;
    private Dictionary dictionary = new Dictionary("resources/sowpods.txt");

    /**
     * Constructor for making a display
     * builds all of the required objects for a game of scrabble including a
     * bag, a board, a computer player, and a human player
     */
    public Display() {
        bag = new Bag();
        try {
            board = Board.readInputBoard(
                    new Scanner(new File("resources/scrabble_board.txt")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        humanPlayer = new HumanPlayer(bag, board, dictionary);
        computerPlayer = new ComputerPlayer(bag, board, dictionary);

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        tray = new HBox();
        tray.setAlignment(Pos.CENTER);
        root = new BorderPane();
    }

    /**
     * Main method that starts the javaFx program
     * @param args command line arguments not used
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the GUI by building the board and updating the labels on the
     * screen
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage){
        stage.setTitle("Scrabble");
        stage.setMinHeight(700);
        stage.setMinWidth(700);
        updateBoard();
        setScoreLabel();
        updateHumanTray(humanPlayer.tray);
        refreshScreen();
        stage.setScene(new Scene(this.root));
        stage.show();
    }

    /**
     * refreshes all aspects of the root of the display
     */
    private void refreshScreen() {
        this.root.getChildren().clear();
        setScoreLabel();
        this.root.setCenter(this.grid);
        this.root.setBottom(this.tray);
    }

    /**
     * Sets score labels based on each players score
     * Also adds the button to submit a play for the human
     */
    private void setScoreLabel() {
        VBox info = new VBox();
        info.setSpacing(10);
        Label humanScoreLabel = new Label();
        humanScoreLabel.setFont(new Font("Arial", 30));
        humanScoreLabel.setAlignment(Pos.CENTER);
        humanScoreLabel.setText("Human: " + humanPlayer.getScore());
        Label computerScoreLabel = new Label();
        computerScoreLabel.setFont(new Font("Arial", 30));
        computerScoreLabel.setAlignment(Pos.CENTER);
        computerScoreLabel.setText("Computer: " + computerPlayer.getScore());
        Button takeTurnButton = getButton();
        info.getChildren().addAll(humanScoreLabel, computerScoreLabel,
                takeTurnButton);
        this.root.setRight(info);
    }

    /**
     * Makes a button for the human to submit a play
     *
     * @return The button that will be played
     */
    private Button getButton() {
        Button takeTurnButton = new Button("Take turn");
        takeTurnButton.setOnMouseClicked(event -> {
            Board temp = this.humanPlayer.takeTurn();
            if (temp != null) {
                computerPlayer.setBoard(temp);
                board = new Board(temp.getBoard());
                isGameOver(false, false);
                isGameOver(true, false);
                this.board = computerPlayer.takeTurn();
                this.humanPlayer.setBoard(this.board);
                isGameOver(true, true);
            } else {
                Alert invalidMoveWarning = new Alert(Alert.AlertType.WARNING);
                invalidMoveWarning.setTitle("Invalid Move");
                invalidMoveWarning.setHeaderText("Invalid Move! Try again.");
                invalidMoveWarning.showAndWait();
            }
            updateBoard();
            setScoreLabel();
            updateHumanTray(humanPlayer.tray);
        });
        return takeTurnButton;
    }

    /**
     * Builds a tile and sets its mouse event if it is able to be clicked
     * to be click it must be in the tray
     * @param tile tile that is being represented in graphics
     * @param isClickable if the tile can be clicked on
     * @return the tile as a stack pane
     */
    private StackPane buildTile(Tile tile, boolean isClickable) {
        StackPane tilePane = new StackPane();
        Rectangle rectangle = new Rectangle(SQUARE_SIZE,
                SQUARE_SIZE, Color.BURLYWOOD);
        Label label = new Label(tile.toString());
        label.setMaxSize(SQUARE_SIZE, SQUARE_SIZE);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Arial", FontWeight.BOLD, SQUARE_SIZE - 10));
        label.setMaxSize(SQUARE_SIZE, SQUARE_SIZE);
        label.setAlignment(Pos.CENTER);
        tilePane.getChildren().addAll(rectangle, label);
        tilePane.setMaxSize(SQUARE_SIZE, SQUARE_SIZE);
        if (isClickable) {
            tilePane.setOnMouseClicked(event -> {
                this.humanPlayer.updateSelectedTile(tile);
                updateBoard();
                updateHumanTray(this.humanPlayer.tray);
                refreshScreen();
            });
        }
        tilePane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return tilePane;
    }

    /**
     * Builds a board square graphic based on the provided board square object
     * The base color for the tile is brown then the different color represent the
     * multipliers
     * if it is a double word it will be pink
     * if it is a triple word it will be red
     * if it is a double letter it will be light blue
     * if it is a triple letter it will be dark blue
     *
     * @param current the square that is being build into a graphic version
     * @return the stack pane representing the square
     */
    private StackPane buildBoardSquare(BoardSquare current) {
        StackPane square = new StackPane();
        Color squareColor = Color.BISQUE;
        switch (current.getWordMultiplier()) {
            case 2:
                squareColor = Color.PINK;
                break;
            case 3:
                squareColor = Color.RED;
        }

        switch (current.getLetterMultiplier()) {
            case 2:
                squareColor = Color.LIGHTBLUE;
                break;
            case 3:
                squareColor = Color.DARKBLUE;
                break;
        }
        square.getChildren().add(new Rectangle(SQUARE_SIZE, SQUARE_SIZE, squareColor));
        square.setOnMouseClicked(event -> {
            this.humanPlayer.updateSelectedSquare(current);
            updateBoard();
            updateHumanTray(this.humanPlayer.tray);
            refreshScreen();
        });
        square.setBorder(new Border(new BorderStroke(Color.GREY,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        return square;
    }

    /**
     * Updates the tray on the screen for the human
     *
     * @param tray The array list representing the players tray
     */
    private void updateHumanTray(ArrayList<Tile> tray) {
        this.tray.getChildren().clear();
        for (Tile tile : tray) {
            this.tray.getChildren().add(buildTile(tile, true));
        }
        this.tray.setAlignment(Pos.CENTER);
        this.tray.setSpacing(2);
    }

    /**
     * Updates the Grid pane that represents the board
     */
    private void updateBoard() {
        this.grid.getChildren().clear();
        BoardSquare[][] temp = this.humanPlayer.getMidMoveBoard().getBoard();
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                if (this.humanPlayer.getMidMoveBoard().hasLetter(i, j)) {
                    this.grid.add(buildTile(new Tile(temp[i][j].getLetter()),
                            false), j, i);
                } else {
                    this.grid.add(buildBoardSquare(temp[i][j]), j, i);
                }
            }
        }
        this.grid.setBackground(new Background(new BackgroundFill(Color.GREY,
                null, null)));
    }

    /**
     * Checks to see if the game is over
     *
     * @param hasMoveCheck boolean to see if we are checking if a player as a move
     * @param humanCheck boolean to see if we are checking the humans play or not
     */
    private void isGameOver(boolean hasMoveCheck, boolean humanCheck) {

        Solver moveChecker = new Solver(dictionary, board, humanPlayer.convertTray());
        moveChecker.findAllOptions();

        if (!hasMoveCheck && !bag.hasTilesLeft() &&
                ((humanPlayer.getNumberOfTilesInHand() == 0
                        || computerPlayer.getNumberOfTilesInHand() == 0))) {
            showGameOverMessage();
        } else if (humanCheck &&
                !board.isLegalPlay(board.findAllDifferences(moveChecker
                        .getBestMove()), dictionary, moveChecker.getBestMove())) {
            showGameOverMessage();

        } else if (!humanCheck &&
                !board.isLegalPlay(board.findAllDifferences(computerPlayer.
                        getBestMove()), dictionary, computerPlayer.getBestMove())) {
            showGameOverMessage();
        }
    }

    /**
     * Shows a pop up for when the game is over and who will when
     */
    private void showGameOverMessage() {
        Alert gameOver = new Alert(Alert.AlertType.INFORMATION);
        gameOver.setTitle("Game Over");
        if (humanPlayer.getScore() > computerPlayer.getScore()) {
            gameOver.setHeaderText("You win!");
        } else {
            gameOver.setHeaderText("You lose!");
        }
        gameOver.showAndWait();
    }


}
