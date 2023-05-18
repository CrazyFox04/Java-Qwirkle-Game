package g60904.qwirkle.model;
/**
 * A record that represents a tile placed at a specific position on a grid.
 * <p>
 * The `TileAtPosition` record encapsulates the row and column indices of the position,
 * as well as the tile that is placed at that position on the grid.
 *
 * @param row  the row index of the position
 * @param col  the column index of the position
 * @param tile the tile placed at the position
 */
public record TileAtPosition(int row, int col, Tile tile) {
}
