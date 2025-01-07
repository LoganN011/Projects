package dominos;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A Domino canvas is used for a GUI representation of any domino
 * extends the javafx canvas class
 */
public class DominoCanvas extends Canvas {
    Domino domino;

    /**
     * The constructor for the domino canvas will draw a canvas that looks like
     * a domino and have the correct number of dots per side
     *
     * @param width  width of the canvas
     * @param height height of the canvas
     * @param domino domino that will be represented as a GUI element
     */
    public DominoCanvas(int width, int height, Domino domino) {
        super(width, height);
        this.domino = domino;
        drawDomino();
    }

    /**
     * Will draw the outline of the domino with curved corners, and a line down
     * the middle to separate the domino into a right and left side. Then draws
     * all the dots that are required on each side of the domino
     */
    private void drawDomino() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        gc.setFill(Color.BLACK);
        gc.fillRoundRect(getWidth() / 2, 0, 1, getHeight(), 0, 0);
        drawLeftDots(getWidth(), 0, domino.getLeftSide());
        drawRightDots(getWidth(), 50, domino.getRightSide());
    }

    //1:Center
    //2:Top right and bottom left
    //3:Center top right and bottom (1 and 2)
    //4: all corners
    //5: all corners and center (1 and 4)
    //6: top and bottom with 3 dots each (4 + two in between them)

    /**
     * draws a single dot on the domino based on the given values
     *
     * @param x          horizontal position of where the dot will do drawn
     * @param y          vertical position of where the dot will do drawn
     * @param shiftValue how shifted the dot will be
     */
    private void drawDot(double x, double y, int shiftValue) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.fillOval(x - 2.5 + shiftValue, y - 2.5, 5, 5);
    }

    /**
     * Will draw dots on the left side of the current domino canvas
     * @param width size of the canvas
     * @param shiftValue how far shifted it is to the right
     * @param numDots the number of dots to be drawn
     */
    private void drawLeftDots(double width, int shiftValue, int numDots) {
        switch (numDots) {
            case 1 -> drawDot(width / 4, getHeight() / 2, shiftValue);
            case 2 -> {
                drawDot(width / 8, getHeight() / 4, shiftValue);
                drawDot((width / 8) * 3, (getHeight() / 4) * 3, shiftValue);
            }
            case 3 -> {
                drawDot(width / 4, getHeight() / 2, shiftValue);
                drawDot(width / 8, getHeight() / 4, shiftValue);
                drawDot((width / 8) * 3, (getHeight() / 4) * 3, shiftValue);
            }
            case 4 -> drawCorners(width, shiftValue);
            case 5 -> {
                drawDot(width / 4, getHeight() / 2, shiftValue);
                drawCorners(width, shiftValue);
            }
            case 6 -> {
                drawCorners(width, shiftValue);
                drawDot(width / 4, (getHeight() / 4) * 3, shiftValue);
                drawDot(width / 4, getHeight() / 4, shiftValue);
            }
        }

    }

    /**
     * Draws the conner dots of a domino
     *
     * @param width      size of the canvas
     * @param shiftValue how far it is shifted to the right
     */
    private void drawCorners(double width, int shiftValue) {
        drawDot(width / 8, getHeight() / 4, shiftValue);
        drawDot((width / 8) * 3, (getHeight() / 4) * 3, shiftValue);
        drawDot(width / 8, (getHeight() / 4) * 3, shiftValue);
        drawDot((width / 8) * 3, getHeight() / 4, shiftValue);
    }

    /**
     * Draws dots on the right side of the canvas
     * @param width size of the canvas
     * @param shiftValue how far shifted the dots are to the right
     * @param numDots number of dots to be drawn on the right
     */
    private void drawRightDots(double width, int shiftValue, int numDots) {
        drawLeftDots(width, shiftValue, numDots);
    }


}