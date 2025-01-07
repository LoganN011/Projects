import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;


/**
 * The main class is where all the JavaFx controls and setup will be
 * It will draw the keyboard on the scree
 * It will also make the word pane in the center of the screen
 * it also adds multiple labels and other controls this includes:
 * a stop button to the game and show the WPM
 * a slider to control the speed of words spawning
 * a slider to control how fast words disapper
 * a label to show the current score of the game
 * a label to show the current letter that have been typed
 * This class extends Application and that is how to start a javaFx program
 *
 * @author Logan Nunno
 */
public class Main extends Application {
    /**
     * the main method to launch args and show the javaFx program on to the screen
     *
     * @param args command line argument to be ignored
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Setups up all the JavaFX GUI controls and creates instances of
     * all the helper classes.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception will throw if the file provided to word cannot be found and the program will not be started
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Always make sure to set the title of the window
        primaryStage.setTitle("Key Shooter");
        // Width/height variables so that we can mess with the size of the window
        double width = 600;
        double height = 600;
        // BorderPane (https://openjfx.io/javadoc/18/javafx.graphics/javafx/scene/layout/BorderPane.html)
        // Provides the basis which we basis the rest of the GUI on
        BorderPane window = new BorderPane();
        // VBox for the top part of the GUI
        VBox topVBox = new VBox(5);
        topVBox.setAlignment(Pos.CENTER);
        // Label which displays the score
        Label scoreLabel = new Label("0");
        scoreLabel.setFont(new Font(40));
        // make the stop button and set the color to red and change the font to Comic Sans MS
        Button stop = new Button("STOP");
        stop.setTextFill(Paint.valueOf("black"));
        stop.setBackground(Background.fill(Paint.valueOf("red")));
        stop.setBorder(Border.stroke(Paint.valueOf("black")));
        stop.setStyle("-fx-font-family: 'Comic Sans MS'");
        stop.setStyle("-fx-font-weight: bold");
        // make the spawn speed slider and show the different tick marks and labels
        Slider spawnSpeed = new Slider(0.5, 2, 1);
        spawnSpeed.setShowTickLabels(true);
        spawnSpeed.setMajorTickUnit(.5);
        spawnSpeed.setSnapToTicks(true);
        spawnSpeed.setShowTickMarks(true);
        // make the disapper speed slider and show the different tick marks and labels
        Slider disapperSpeed = new Slider(0.5, 2, 1);
        disapperSpeed.setShowTickLabels(true);
        disapperSpeed.setMajorTickUnit(.5);
        disapperSpeed.setSnapToTicks(true);
        //make labels that are placed in front of the different slider
        Label disapperLabel = new Label("Disapper: ");
        disapperLabel.setFont(new Font(20));
        Label spawnLabel = new Label("Spawn: ");
        spawnLabel.setFont(new Font(20));
        //add all the controls to a Hbox to be added to a Vbox and show on the screen
        HBox controls = new HBox(disapperLabel, disapperSpeed, stop, spawnLabel, spawnSpeed);
        //sets the spacing to 10
        controls.setSpacing(10);
        //sets the alignment to the center of the screen
        controls.setAlignment(Pos.CENTER);
        // Label which displays the currently typed letters
        Label typedLabel = new Label();
        typedLabel.setFont(new Font(40));
        // Add them all to the VBox
        topVBox.getChildren().addAll(scoreLabel, typedLabel, controls);
        // Put them in the top of the BorderPane
        window.setTop(topVBox);
        // Create an instance of our helper Words class
        Words words = new Words("./docs/words.txt", width, (height * 3) / 4,
                scoreLabel, typedLabel, disapperSpeed);
        // Put it in the middle of the BorderPane
        window.setCenter(words.getWordsPane());
        // Create a VBox for the keyboard
        VBox keyBoardWindow = new VBox(10);
        // Create an instance of our helper class Keyboard
        Keyboard keyboard = new Keyboard(width, height / 4, 10);
        // Add a horizontal line above the keyboard to create clear seperation
        keyBoardWindow.getChildren().addAll(new Separator(Orientation.HORIZONTAL), keyboard.getKeyboard());
        // Put it in the bottom of the BorderPane
        window.setBottom(keyBoardWindow);
        // Create the scene
        Scene scene = new Scene(window, width, height);
        // The scene is the best place to capture keyboard input
        // First get the KeyCode of the event
        // Then start the fill transition, which blinks the key
        // Then add it to the typed letters
        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            keyboard.startFillTransition(keyCode);
            words.addTypedLetter(keyCode);
        });
        // Set the scene
        primaryStage.setScene(scene);
        // Showtime!
        primaryStage.show();
        //Counter to count how many times the animation timer is called should be 100 times a second
        final long[] counter = {0};
        //Animation timer used to add words to the screen after 3 second(3000 milliseconds)
        AnimationTimer timer = new AnimationTimer() {
            Duration lastUpdate = Duration.of(0, ChronoUnit.NANOS);

            /**
             * After 3 second a new word will be added to screen
             * it will update a local variable that count when it was last updated
             * @param now
             *            The timestamp of the current frame given in nanoseconds. This
             *            value will be the same for all {@code AnimationTimers} called
             *            during one frame.
             */
            @Override
            public void handle(long now) {
                counter[0]++;
                Duration nowDur = Duration.of(now, ChronoUnit.NANOS);
                if (nowDur.minus(lastUpdate).toMillis() >= 3000 / spawnSpeed.getValue()) {
                    lastUpdate = nowDur;
                    words.createWord();

                }
            }


        };
        //stars the animation timer
        timer.start();

        //Sets what the stop button does when it is clicked
        stop.setOnAction(event -> {
            //stops the animation timer so no new words will spawn
            timer.stop();
            //Clears the window of all the words that were on the screen
            window.setCenter(new BorderPane());

            //This does the amount of seconds
            //  ((counter[0]-200)/100)

            // A decimal format object to format the number, so it only has 2 places after the decimal
            DecimalFormat df = new DecimalFormat("0.00");
            //gets the number on screen and then multiples it by 60 it, so it can be divided by the number of seconds
            // and that will be the average number of words per minute
            double WPM = ((double) (words.getScore()) * 60) / ((double) ((counter[0] -200) / 100));
            //Makes a new label to show the average words per min and shows it on the window
            Label WPMLabel = new Label("Words Per Min= " + df.format(WPM));
            WPMLabel.setFont(new Font(40));
            topVBox.getChildren().clear();
            topVBox.getChildren().add(WPMLabel);
            window.setTop(topVBox);
        });
    }
}
