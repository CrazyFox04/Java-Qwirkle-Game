package g60904.qwirkle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@code Player} class represents a player in the Qwirkle game.
 */
public class Player implements Serializable {
    /**
     * The name of the player.
     */
    private final String name;
    /**
     * The tiles in the player's hand.
     */
    private final List<Tile> tiles;

    private int score;

    /**
     * Constructs a new player with the specified name.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.tiles = new ArrayList<>();
    }

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable view of the tiles in the player's hand.
     *
     * @return an unmodifiable view of the tiles in the player's hand
     */
    public List<Tile> getHand() {
        return Collections.unmodifiableList(tiles);
    }
    /**
     * Returns the score of the player.
     *
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Refills the player's hand by adding new random tiles from the bag.
     */
    public void refill() {
        var array = Bag.getInstance().getRandomTiles(-(tiles.size() - 7));
        for (Tile tile : array) {
            if (tile != null) {
                tiles.add(tile);
            }
        }
    }

    /**
     * Removes the specified tiles from the player's hand.
     *
     * @param ts the tiles to be removed from the player's hand
     */
    public void removeTilesFromHand(Tile... ts) {
        for (Tile t : ts) {
            tiles.remove(t);
        }
    }
    /**
     * Adds the specified value to the player's score.
     *
     * @param value the value to be added to the player's score
     */
    public void addScore(int value) {
        this.score += value;
    }
}