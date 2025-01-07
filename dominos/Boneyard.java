package dominos;

import java.util.ArrayList;
import java.util.Random;

/**
 * The boneyard is a place in a game of dominos where dominos are drawn from
 * This class has all the methods required to draw from the boneyard
 * it also builds all the dominos for the game
 *
 * @author Logan Nunno
 */
public class Boneyard {
    private ArrayList<Domino> bones = new ArrayList<>();

    /**
     * The boneyard constructor
     * takes no parameters just makes all dominos for the game and saves them
     */
    public Boneyard() {
        makeDominoes();
    }

    /**
     * checks if the boneyard has dominos remaining
     * @return ture if the boneyard has any dominos left, false if not
     */
    public boolean hasBones() {
        return !bones.isEmpty();
    }

    /**
     * gets the number of dominos in the boneyard
     * @return the number of bones in the boneyard
     */
    public int getBonesCount() {
        return bones.size();
    }

    /**
     * Makes all the dominos based on the max number of dot per side gotten
     * from the domino class. THERE IS NO REPEAT DOMINOS
     * There is only one of each domino meaning there is not a domino for
     * [1,2] and [2,1] there is only one because it can be flipped to act like
     * the other domino. Default of 28 total dominos
     */
    private void makeDominoes() {
        for (int i = 0; i < Domino.getMaxNumDots() + 1; i++) {
            for (int j = i; j < Domino.getMaxNumDots() + 1; j++) {
                Domino newDomino = new Domino(i, j);
                bones.add(newDomino);
            }
        }
    }

    /**
     * If there is a domino then it draws a domino
     * If there is no domino to be drawn it build a bad domino.
     * That should never happen
     * @return a domino from the boneyard
     */
    public Domino drawDomino() {
        if (bones.size() != 0) {
            Random rand = new Random();
            int index = rand.nextInt(bones.size());
            Domino domino = bones.get(index);
            bones.remove(index);
            return domino;
        }
        return new Domino(-1, -1);
    }


}
