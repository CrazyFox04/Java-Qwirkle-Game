package g60904.qwirkle.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a bag of tiles used for a game. It is implemented as a Singleton and can only have one instance
 * created. The bag contains tiles with different colors and shapes. The tiles can be randomly drawn from the bag.
 */
public class Bag {
    private final List<Tile> tiles = new ArrayList<>();
    private static final Bag instance = new Bag();

    /**
     * Private constructor used to create the tiles in the bag.
     */
    private Bag() {
        for (Color color : Color.values()) {
            for (Shape shape : Shape.values()) {
                createNextThreeTiles(color, shape);
            }
        }
    }

    /**
     * Creates three tiles with the given color and shape and adds them to the bag.
     *
     * @param color the color of the tiles.
     * @param shape the shape of the tiles.
     */
    private void createNextThreeTiles(Color color, Shape shape) {
        for (int i = 0; i < 3; i++) {
            tiles.add(new Tile(color, shape));
        }
    }

    /**
     * Returns the singleton instance of the Bag class.
     *
     * @return the singleton instance of the Bag class.
     */
    public static Bag getInstance() {
        return instance;
    }

    /**
     * Returns an array of n random tiles drawn from the bag.
     * The tiles are removed from the bag.
     *
     * @param n the number of tiles to draw.
     * @return an array of n random tiles.
     */
    public Tile[] getRandomTiles(int n) {
        var returnedTile = new Tile[n];
        for (int i = 0; i < n; i++) {
            int randomIndex = getRandomNumber(size()-1);
            returnedTile[i] = tiles.get(randomIndex);
            tiles.remove(randomIndex);
        }
        return returnedTile;
    }

    /**
     * Returns a random integer between the specified minimum (inclusive) and maximum (inclusive) values.
     *
     * @param max the maximum value to be included in the range
     * @return a random integer between the specified minimum (inclusive) and maximum (inclusive) values
     */
    private int getRandomNumber(int max) {
        return (int) (Math.random() * (max + 1));
    }

    /**
     * Returns the number of tiles in the bag.
     *
     * @return the number of tiles in the bag.
     */
    public int size() {
        return tiles.size();
    }
}
