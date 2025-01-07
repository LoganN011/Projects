import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * A class that will be used to hold both the letters of the keyboard and
 * the words that will be shown on the screen
 * A word box is a stack pane that has a rectangle and a label on top
 * Extends the Node class
 *
 * @author Logan Nunno
 */
public class WordBox extends Node {
    private final StackPane wordBox;
    private final Rectangle rect;
    private final String word;

    /**
     * the constructor for the word box class
     * It will make the required stack pane that is made up of:
     * A label with the given word
     * The text is 2 less than the provided size
     * A box of the provided color
     * and size of the given size
     * The text is on top and the rectangle is on the bottom
     *
     * @param size  the size of the rectangle
     * @param word  the word to be shown in the wordbox
     * @param color the color of the rectangle
     */
    public WordBox(double size, String word, Color color) {
        wordBox = new StackPane();
        rect = new Rectangle(size, size, color);
        this.word = word.toUpperCase();
        Label text = new Label(this.word);
        text.setFont(new Font(size - 2));
        wordBox.getChildren().addAll(rect, text);
    }

    /**
     * returns the given word box as a stack pane
     *
     * @return the current word box
     */
    public StackPane getWordBox() {
        return wordBox;
    }

    /**
     * returns the current rectangle of the current word box
     *
     * @return the current rectangle
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * returns the current word in the word box
     *
     * @return the current word in the word box
     */
    public String getWord() {
        return word;
    }
}
