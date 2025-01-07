package dominos;

/**
 * A Domino has a right and a left side
 * each domino has a max number of dots per side
 * by default the max num of dots is 6
 * @author Logan Nunno
 */
public class Domino {

    private int leftSide;
    private int rightSide;
    //put back to 6. at 4 for testing
    private static final int MAX_NUM_DOTS = 6;

    /**
     * The domino constructor
     * Takes in two ints for representing the number of dots on that side
     *
     * @param leftSide  Number of dots on the left
     * @param rightSide Number of dots on the right
     */
    public Domino(int leftSide, int rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    /**
     * Gets the number of dots on the left side of the domino
     * @return number of dots on the left side as an int
     */
    public int getLeftSide() {
        return leftSide;
    }

    /**
     * Gets the number of dots on the right side of the domino
     * @return number of dots on the right side as an int
     */
    public int getRightSide() {
        return rightSide;
    }

    /**
     * Gets the maximum number of dots for any domino
     * is a static method
     * @return the max number of dots per side of a domino
     */
    public static int getMaxNumDots() {
        return MAX_NUM_DOTS;
    }

    /**
     * flips the domino
     * ie it swaps the right and left side
     */
    public void flipDomino(){
        int temp=rightSide;
        rightSide=leftSide;
        leftSide=temp;
    }

    /**
     * returns a string representation of the domino
     * @return a string version of the current domino
     */
    public String toString() {
        return "[" + leftSide + "|" + rightSide + "]";
    }

}
