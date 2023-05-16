package g60904.qwirkle.model;

import java.io.Serializable;

/**
 * A record that represents a tile in a Qwirkle game, consisting of a color and a shape.
 * <p>
 * A tile is an immutable object that encapsulates a color and a shape in a Qwirkle game.
 * The color and shape are represented as instances of the {@link Color} and {@link Shape} enums,
 * respectively.
 * <p>
 * This class is a {@link Record}.
 *
 * @param color the color of the tile
 * @param shape the shape of the tile
 */
public record Tile(Color color, Shape shape) implements Serializable {
    @Override
    public String toString() {
        return color().toString() + shape + "\033[m";
    }
}