package g60904.qwirkle.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a bag of tiles used for a game. It is implemented as a Singleton and can only have one instance
 * created. The bag contains tiles with different colors and shapes. The tiles can be randomly drawn from the bag.
 */
public class Bag implements Serializable {
    private final List<Tile> tiles = new ArrayList<>();
    @Serial
    private static final long serialVersionUID = 362004;

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
     * Static inner class used to implement Singleton pattern.
     */
    private static class bagSingletonCreator {
        private static final Bag instance = new Bag();
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
    public Bag getInstance() {
        return bagSingletonCreator.instance;
    }

    /**
     * Ensures that the instance is the same after deserialization.
     *
     * @return the instance of the Bag class.
     */
    @Serial
    private Object readResolve() {
        return getInstance();
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
            int randomIndex = getRandomNumber(0, size());
            returnedTile[i] = tiles.get(randomIndex);
            tiles.remove(randomIndex);
        }
        return returnedTile;
    }

    /**
     * Returns a random integer between the specified minimum (inclusive) and maximum (inclusive) values.
     *
     * @param min the minimum value to be included in the range
     * @param max the maximum value to be included in the range
     * @return a random integer between the specified minimum (inclusive) and maximum (inclusive) values
     * @throws IllegalArgumentException if min is greater than max
     */
    private int getRandomNumber(int min, int max) throws RuntimeException {
        if (min > max) {
            throw new RuntimeException("The minimum value can't be greater than the maximum");
        }
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    /**
     * Returns the number of tiles in the bag.
     *
     * @return the number of tiles in the bag.
     */
    public int size() {
        return tiles.size();
    }

    /**
     * Main method used for testing the Bag class.
     * Is just an example of serialization. Will be removed in the future
     * TODO : remove it !!!
     *
     * @param args command-line arguments.
     * @throws FileNotFoundException  if the file cannot be found.
     * @throws IOException            if an I/O error occurs.
     * @throws ClassNotFoundException if the class cannot be found during deserialization.
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        var myFirstInstance = new Bag().getInstance();
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(
                "bagOfTile.ser"
        ));
        out.writeObject(myFirstInstance);
        System.out.println(myFirstInstance.tiles.toString());
        out.close();
        var aTest = new Bag().getInstance();
        // deserialization
        ObjectInput in = new ObjectInputStream(new FileInputStream(
                "bagOfTile.ser"
        ));
        var mySecondInstance = (Bag) in.readObject();
        mySecondInstance.tiles.remove(2);
        System.out.println(myFirstInstance.tiles.toString());
        in.close();

        System.out.println("myFirstInstance hashcode = " + myFirstInstance.hashCode());
        System.out.println("mySecondInstance hashcode = " + mySecondInstance.hashCode());
    }
}
