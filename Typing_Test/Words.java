import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.PathTransition;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * The words class is used to both draw the words on the screen,
 * and it is used to delete the words and update the score if the word is correct
 * all the current words are help as both their word box version and Strings of the word
 * all  the current words are shown in the wordsPane that is created and used in main
 *
 * @author Logan Nunno
 */
public class Words {
    // Pane (https://openjfx.io/javadoc/18/javafx.graphics/javafx/scene/layout/Pane.html)
    // which represents the floating words part of the game
    private final Pane wordsPane;
    // List of all available words
    private final List<String> words;
    // List of all JavaFX floating words currently on the screen
    private final List<WordBox> activeWords;
    // List of all keys that have been pressed since the last correct word
    private final List<KeyCode> typed;
    // JavaFX Label which shows the score on the screen
    private final Label scoreLabel;
    // JavaFX slider that effects the speed of words being removed
    private final Slider disapperSpeed;
    // Keeps track of the number of correct words
    private int score = 0;
    // JavaFX Label which shows what the user has typed since the last correct word
    private final Label typedLabel;
    // Width/height of the screen
    private final double width;
    private final double height;

    /**
     * The constructor for the words class
     * It does multiple things:
     * It first sets the wordsPane to a new pane
     * then sets the PrefWidth and PrefHeight to the provided width and height
     * then sets the words list by calling the read Words method from Utils wit the provided path
     * then sets active words to a new array list
     * then sets typed to a new array list
     * It then saves the score label, typed label, and disapper speed, to the class variables
     * it also saves the width and the height
     *
     * @param path          the path to the words file
     * @param width         the width of the words pane
     * @param height        the height of the words pane
     * @param scoreLabel    the label for the current score of the game
     * @param typedLabel    the current typed letters in the game
     * @param disapperSpeed the slider for the speed that words disapper
     * @throws FileNotFoundException if the file path could not be found
     */
    public Words(String path, double width, double height,
                 Label scoreLabel, Label typedLabel, Slider disapperSpeed) throws FileNotFoundException {
        wordsPane = new Pane();
        wordsPane.setPrefWidth(width);
        wordsPane.setPrefHeight(height);

        this.words = Utils.readWords(path);

        activeWords = new ArrayList<>();
        typed = new ArrayList<>();

        this.scoreLabel = scoreLabel;
        this.typedLabel = typedLabel;
        this.disapperSpeed = disapperSpeed;

        this.width = width;
        this.height = height;
    }

    /**
     * gets the pane of the words class
     *
     * @return the current words pane
     */
    public Pane getWordsPane() {
        return wordsPane;
    }


    /**
     * Creates a random floating word.
     * Chooses a random word from the list of words.
     * Then chooses a starting point on anywhere on the screen.
     * Then creates an animation
     * that moves the WordBox from its starting point to a random ending on the edge of the screen
     * point over 10 seconds.
     * once it reaches the end the word box will be removed from the screen and
     * removed from the active words list
     */
    public void createWord() {
        // Picks a random word
        String word = words.get((int) (Math.random() * words.size()));

        //creates an X\Y Pos on a random edge of the screen
        int xPos;
        int yPos;
        int side = (int) (Math.random() * 4) + 1;

        switch (side) {
            case 1 -> {
                xPos = 55;
                yPos = (int) (Math.random() * (height - 175));
            }
            case 2 -> {
                xPos = (int) width - 55;
                yPos = (int) (Math.random() * (height - 175));
            }
            case 3 -> {
                yPos = 0;
                xPos = (int) (Math.random() * (width - 55)) + 55;

            }
            default -> {
                yPos = (int) height - 175;
                xPos = (int) (Math.random() * (width - 55)) + 55;
            }
        }

        //Makes a word box to be added to the active words list and the words pane
        WordBox temp = new WordBox(30, word, Color.TRANSPARENT);
        // Adds the words to the screen on the word pane
        this.wordsPane.getChildren().add(temp.getWordBox());
        //Adds the word box to the active words list
        activeWords.add(temp);
        //The animation of what the words do on the screen
        Animation animation = moveAround(temp, xPos, yPos);
        //Sets what the animation does when the animation is done
        animation.setOnFinished(event -> {
            //removes the word from the active words list and the screen
            int index = activeWords.indexOf(temp);
            if (index != -1) {
                this.wordsPane.getChildren().remove(activeWords.get(index).getWordBox());
                activeWords.remove(index);
            }
        });
        animation.play();

    }

    /**
     * Adds the keyCode to typed if it is a letter key.
     * Removes the first element of typed if it is the backspace key.
     * Either way it checks for a correct word and updates the typedLabel.
     *
     * @param keyCode KeyCode to add to the state
     */
    public void addTypedLetter(KeyCode keyCode) {
        String output = "";
        //If it is a backSpace it deletes the most recently type letter on the screen and from the type list
        if (KeyCode.BACK_SPACE.equals(keyCode) && typed.size() != 0) {
            typed.remove(typed.size() - 1);
            //else if it is a letter it is added to the screen in the typed label
        } else if (keyCode.isLetterKey()) {
            typed.add(keyCode);
        }
        //updates the screen and checks to see if the word is on the screen
        for (KeyCode current : typed) {
            output += current;
        }
        if (checkForCorrectWord(output)) {
            output = "";
        }
        typedLabel.setText(output);


    }

    /**
     * Checks if the given String is equal to any of the currently
     * active words. If it is then it updates the score and scoreLabel.
     * It also removes the wordBox and clears the typed list.
     *
     * @param s Word to check
     */
    private boolean checkForCorrectWord(String s) {
        int index = 0;
        for (WordBox current : activeWords) {
            if (current.getWord().equals(s)) {
                score++;
                scoreLabel.setText(String.valueOf(score));
                removeWord(index);
                typed.clear();
                return true;
            }
            index++;
        }
        return false;
    }

    /**
     * A method to make a random path for words to be moved around on the screen
     * There are 4 options of the different paths that words can travel from
     * 1 - words can take QuadCurve path
     * 2 - words can take an Arc path
     * 3 - words can take a CubicCurve path
     * 4 - words can take a straight line
     * THIS IS THE EXTRA CREDIT ATTEMPT
     * if the words go off the screen that is because of the different paths not required
     * the default straight line path will not go off the screen but
     * paths 2 and 3 might go off the screen
     * The animation will default to take 10 second(10000 milliseconds)
     * it will be divided by the disapper speed slider value to either make it disapper faster or slower
     *
     * @param word      the word that will be animated
     * @param startingX the starting X position of the word
     * @param startingY the starting Y position of the word
     * @return the animation path that the word will take
     */
    private Animation moveAround(WordBox word, int startingX, int startingY) {
        PathTransition pathTransition = new PathTransition();


        Path path = new Path();

        //Makes a random POS for the words to end on a random side of the screen
        int xEnding;
        int yEnding;
        int side = (int) (Math.random() * 4) + 1;

        switch (side) {
            case 1 -> {
                xEnding = 55;
                yEnding = (int) (Math.random() * (height - 175));
            }
            case 2 -> {
                xEnding = (int) width - 55;
                yEnding = (int) (Math.random() * (height - 175));
            }
            case 3 -> {
                yEnding = 0;
                xEnding = (int) (Math.random() * (width - 55)) + 55;

            }
            default -> {
                yEnding = (int) height - 175;
                xEnding = (int) (Math.random() * (width - 55)) + 55;
            }
        }

        int pathNumber = (int) (Math.random() * 4) + 1;

        switch (pathNumber) {
            case 1 -> {
                //Works
                // A Quadratic Bézier
                path.getElements().add(new MoveTo(startingX, startingY));
                path.getElements().add(new QuadCurveTo(width / 2, height / 2, xEnding, yEnding));
            }

            case 2 -> {
                //Works
                // Arc
                path.getElements().add(new MoveTo(startingX, startingY));
                path.getElements().add(new ArcTo(width, height, 5, xEnding, yEnding,
                        false, false));
            }
            case 3 -> {
                //Works
                // A Cubic Bézier curve
                path.getElements().add(new MoveTo(startingX, startingY));
                path.getElements().add(new CubicCurveTo(width / 2 + 50, height / 2 + 50,
                        width / 2 - 50, height / 2 - 50, xEnding, yEnding));
            }
            default -> {
                //Works
                // default straight line
                path.getElements().add(new MoveTo(startingX, startingY));
                path.getElements().add(new LineTo(xEnding, yEnding));
            }
        }
        pathTransition.setDuration(Duration.millis(10000 / disapperSpeed.getValue()));
        pathTransition.setNode(word.getWordBox());
        pathTransition.setPath(path);
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(false);
        return pathTransition;
    }

    /**
     * removes a word from the word pane and the active words at the provided index
     * EXTRA CREDIT:
     * It also flashes the color of the word before it gets removed
     *
     * @param index the index that will be removed
     */
    public void removeWord(int index) {
        FillTransition ft = new FillTransition(Duration.millis(100), activeWords.get(index).getRect(), Color.RED,
                Color.GOLD);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.setOnFinished(event -> {
            this.wordsPane.getChildren().remove(activeWords.get(index).getWordBox());
            activeWords.remove(index);
        });
        ft.play();
    }

    /**
     * Gets the score of the current game
     *
     * @return the score of the game
     */
    public int getScore() {
        return score;
    }
}
