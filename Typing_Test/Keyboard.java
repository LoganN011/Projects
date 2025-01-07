import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class used to creat the key that will be shown on the screen
 * it has a full list of key codes that looks like a normal american QWERTY keyboard
 * when a key is click it will change color for 100 milliseconds
 * each row of keys is held in a Hbox and those Hboxs are then placed in a Vbox to be place at the bottom of the
 * screen in the main class
 *
 * @author Logan Nunno
 */
public class Keyboard {
    // 2 Dimensional list representing the rows of keys on the keyboard
    // Letter keys only
    private final List<List<KeyCode>> keyCodes;
    // Map that is used to access the keys JavaFX representation
    private final Map<KeyCode, WordBox> keyCodeToWordBox;
    // JavaFX control that represents the keyboard on the screen
    private final VBox keyboard;
    // Color that the keys are by default
    private static final Color from = Color.color(0.9, 0.9, 0.9);
    // Color that the keys become when pressed
    private static final Color to = Color.color(0.3, 0.3, 0.8);

    /**
     * the constructor for the keyboard
     * it calls two methods to make the make keyboard
     * it also initializes the keyCodeToWordBox to a new HashMap
     *
     * @param width   the width of the keyboard
     * @param height  the height of the keyboard
     * @param spacing the space between the keys
     */
    public Keyboard(double width, double height, double spacing) {
        keyCodes = initializeKeys();
        keyCodeToWordBox = new HashMap<>();

        keyboard = initializeKeyboard(width, height, keyCodes, spacing);
    }

    /**
     * Gets the current keyboard that is held in a Vbox
     *
     * @return the current keyboard
     */
    public VBox getKeyboard() {
        return keyboard;
    }

    /**
     * First checks if the given keyCode exists in the keyCodeToWordBox.
     * If it does then it starts a FillTransition
     * (https://openjfx.io/javadoc/18/javafx.graphics/javafx/animation/FillTransition.html)
     * to go from the from color to the to color.
     * It will cycle twice to go back to the original color
     * It will then play the FillTransition
     * If the keyCode does not exist then it does nothing.
     *
     * @param keyCode KeyCode to lookup in the map and flash
     */
    public void startFillTransition(KeyCode keyCode) {
        if (keyCodeToWordBox.containsKey(keyCode)) {
            FillTransition ft = new FillTransition(Duration.millis(100),
                    this.keyCodeToWordBox.get(keyCode).getRect(), from, to);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.play();
        }
    }

    /**
     * Simply creates the 2D list that represents the keyboard.
     * Each row is an element of the outer list and each inner list
     * contains all the letter keys in that row. Only contains
     * 3 rows. All letters are uppercase.
     *
     * @return 2D list representing the letters on the keyboard
     */
    private List<List<KeyCode>> initializeKeys() {
        //list of letters for each row on the keyboard
        String letterList = "QWERTYUIOP\nASDFGHJKL\nZXCVBNM\n";
        //list of keys for the whole keyboard
        List<List<KeyCode>> keyList = new ArrayList<>();
        //List of keys for each row of the keyboard. will be emptied for each row
        List<KeyCode> rowOfKeys = new ArrayList<>();
        //Loop to look at each letter in the list of letter String and then add them to the row
        //If it is a new line charter it will add the row to the key list
        for (int i = 0; i < letterList.length(); i++) {
            if (letterList.charAt(i) != '\n') {
                for (KeyCode current : KeyCode.values()) {
                    if (current.isLetterKey() && current.getChar().charAt(0) == letterList.charAt(i)) {
                        rowOfKeys.add(current);
                    }
                }
            } else {
                List<KeyCode> temp = new ArrayList<>();
                temp.addAll(rowOfKeys);
                keyList.add(temp);
                rowOfKeys.clear();
            }
        }
        return keyList;
    }

    /**
     * Creates the JavaFX control that visualized the keyboard on the screen
     * Also initializes the keyCodeToWordBox map as it goes.
     * It deduces the size of each key using the 2D list and the
     * width parameter. Then creates a VBox and sets its width/height
     * and centers it. Then loops over the 2D list and creates JavaFX
     * controls, WordBox, to represent each key and adds them to HBoxes.
     * The adds the row HBox to the VBox. It also adds the WordBox to the
     * map. Then it moves on to the next row.
     *
     * @param width    Width of the screen
     * @param height   Height of the screen
     * @param keyCodes 2D list that holds all the letters on the keyboard
     * @param spacing  Space between each key
     * @return JavaFX control that visualizes the keyboard on the screen
     */
    private VBox initializeKeyboard(double width, double height, List<List<KeyCode>> keyCodes, double spacing) {
        VBox keyboard = new VBox(spacing);
        keyboard.setMaxSize(width, height);

        for (List<KeyCode> inner : keyCodes) {
            HBox row = new HBox(spacing);
            row.setAlignment(Pos.CENTER);
            for (KeyCode current : inner) {
                WordBox temp = new WordBox(width / 20, current.getName(), from);
                temp.getRect().setArcHeight(width / 35);
                temp.getRect().setArcWidth(width / 35);
                row.getChildren().add(temp.getWordBox());
                keyCodeToWordBox.put(current, temp);
            }
            keyboard.getChildren().add(row);
        }
        return keyboard;
    }
}