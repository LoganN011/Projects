package scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * A bag is a collection Tiles that is used to play the game of scrabble
 *
 * @author Logan Nunno
 */
public class Bag {
    private ArrayList<Tile> tiles = new ArrayList<>();

    /**
     * Constructor to build a bag of 100 tiles based on the
     * standard scrabble rules
     */
    public Bag() {
        buildBag();
    }

    /**
     * Builds the bag by reading in the file of scrabble tile occurrences
     * and building the number and tiles that the file requires
     */
    private void buildBag() {
        try {
            Scanner sc = new Scanner(new File("resources/scrabble_tiles.txt"));
            while (sc.hasNext()) {
                char letter = sc.next().charAt(0);
                sc.next();
                int count = Integer.parseInt(sc.next());
                for (int i = 0; i < count; i++) {
                    tiles.add(new Tile(letter));
                }

            }
            sc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * returns if the bag as tiles left
     * @return if the bag is empty
     */
    public boolean hasTilesLeft() {
        return !tiles.isEmpty();
    }

    /**
     * Draws a tile from the bag and deletes it from the bag
     * and returns the tile if it was a tile to be taken from the bag
     * @return the tile that is being drawn
     */
    public Tile drawTile() {
        //Random rand = new Random(1222);
        Random rand = new Random();
        if (hasTilesLeft()) {

            return tiles.remove(rand.nextInt(tiles.size()));
        } else {
            return new Tile(' ');
        }
    }
}
