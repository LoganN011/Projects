import java.util.List;

/**
 * The UnusIterator is what you use to find some index in the given List
 * with this list you can find the current index, the next index taking into accounting skips, or peek the next index
 * ignoring skips
 * This class takes into account skips and the direction that is curing in play for a game of UNUS
 *
 * @param <T> A type to be held in list that will be looped through
 * @author Logan Nunno
 */
public final class UnusIterator<T> {
    private final List<T> ls;
    private int curIndex;
    private final int len;

    private Direction dir = Direction.FORWARD;

    private final List<Integer> skips;

    /**
     * Enum for the direction of the game
     * can either be forward or backwards
     * has a parameters of
     * 1 for forwards
     * -1 for backwards
     * this number is used to see what direction should be the next index
     */
    private enum Direction {
        FORWARD(1), BACKWARD(-1);

        private final int adder;

        /**
         * This is the constructor for the saving the inside of the enum
         * saves the adder to the local variable of the same name
         *
         * @param adder the inside of the enum either 1 or -1
         */
        Direction(int adder) {
            this.adder = adder;
        }

        /**
         * The getter for getting what the adder is
         *
         * @return an int either 1 or -1 depending on direction
         */
        public int getAdder() {
            return adder;
        }

        /**
         * flips the direction from forwards to backwards and vice versa
         *
         * @return the new direction of the program
         */
        public Direction flip() {
            if (this == FORWARD) {
                return BACKWARD;
            } else {
                return FORWARD;
            }
        }
    }

    /**
     * The constructor for the UnusIterator
     * saves the list called ls that is passed in to a variable of the same name
     * also sets the current index to zero
     * sets the length to the size of the ls
     * It also makes a list called skips that is the length of the ls array and is filled with zeros
     * we fill this list with zeros by using the Utils.repeat method
     *
     * @param ls the provided this that iterator will use
     */
    public UnusIterator(List<T> ls) {
        this.ls = ls;
        this.curIndex = 0;
        this.len = ls.size();
        this.skips = Utils.repeat(this.len, 0);
    }

    /**
     * Find the next valid player index.
     * This is fairly complicated due to being able
     * to skip any player and being able to stack skips
     * on a given player. This function does the following:
     * - Loops until a valid next player is found
     * - Does not modify the curIndex
     * - Instead manipulate its own internal index
     * - This internal index is initially set to the curIndex + the directional adder
     * - It is then checked if it has gone out of bounds
     * - If it has gone negative then it should be set to the max value
     * - If it is above the max value then it should be set to 0
     * - This index is then looked up in the skips list
     * - If this value is anything but 0 then that means this index must be skipped.
     * - If decrement is true then the value in the skips list for this index should be decremented by 1
     * - If it is 0 then you have found the next index and the loop should be exited.
     * - The internal index should then be returned
     *
     * @return Index of next player in the order respecting skips and reverses
     */
    private int findNextIndex(boolean decrement) {
        int internalIndex = curIndex + dir.adder;
        boolean foundValid = false;
        while (!foundValid) {
            if (internalIndex < 0) {
                internalIndex = len - 1;
            } else if (internalIndex > len - 1) {
                internalIndex = 0;
            }
            if (skips.get(internalIndex) > 0) {
                if (decrement) {
                    skips.set(internalIndex, skips.get(internalIndex) - 1);
                    internalIndex = internalIndex + dir.adder;
                    foundValid = true;
                } else {
                    internalIndex = curIndex + dir.adder;
                    foundValid = true;
                }
                if (internalIndex < 0) {
                    internalIndex = len - 1;
                } else if (internalIndex > len - 1) {
                    internalIndex = 0;
                }
            } else {
                return internalIndex;
            }
        }
        return internalIndex;
    }

    /**
     * gets current object in the list at the current index
     *
     * @return the current object in the list
     */
    public T current() {
        return ls.get(curIndex);
    }

    /**
     * Gets the current index
     *
     * @return the current index
     */
    public int getCurIndex() {
        return curIndex;
    }

    /**
     * changes the current index to the next index taking into account the skips
     * will decrement the skips list
     */
    public void next() {
        curIndex = findNextIndex(true);
    }

    /**
     * Looks at the next object in the list without changing the skip list
     * it does take into account the skip list but will not decrement because it is set to false in the find next
     * index method
     *
     * @return will return the next object in the list
     */
    public T peekNext() {
        return ls.get(findNextIndex(false));
    }

    /**
     * This method is getting the direction adder
     *
     * @return will return either 1 or -1 depending on the direction
     */
    public int getDir() {
        return dir.getAdder();
    }

    /**
     * Simply calls flip on the dir member variable and
     * overwrites dir with this value
     */
    public void reverse() {
        dir = (dir.flip());
    }

    /**
     * Increments the nth element of skips by 1
     *
     * @param n Index in skips list to increment
     */
    public void skip(int n) {
        skips.set(n, skips.get(n) + 1);
    }
}
