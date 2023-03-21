package g60904.qwirkle.model;

import java.io.Serializable;

/**
 * Tile represent a tile in the game
 *
 * @param color color of the tile
 * @param shape shape of the tile
 */
public record Tile(Color color, Shape shape) implements Serializable {
}