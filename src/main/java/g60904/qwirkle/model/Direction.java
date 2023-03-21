package g60904.qwirkle.model;

/**
 * An enum representing the four cardinal directions: UP, DOWN, LEFT, RIGHT.
 * Each direction is associated with a deltaRow and deltaCol, which can be used to
 * represent the movement in the respective direction on a two-dimensional grid.
 */
public enum Direction {
    /**
     * The UP direction, associated with a deltaRow of -1 and a deltaCol of 0.
     */
    UP(-1, 0),
    /**
     * The DOWN direction, associated with a deltaRow of 1 and a deltaCol of 0.
     */
    DOWN(1, 0),
    /**
     * The LEFT direction, associated with a deltaRow of 0 and a deltaCol of -1.
     */
    LEFT(0, -1),
    /**
     * The RIGHT direction, associated with a deltaRow of 0 and a deltaCol of 1.
     */
    RIGHT(0, +1);
    /**
     * The deltaRow associated with this direction.
     */
    final int deltaRow;
    /**
     * The deltaCol associated with this direction.
     */
    final int deltaCol;

    /**
     * Constructs a new Direction with the specified deltaRow and deltaCol.
     *
     * @param row the deltaRow associated with this Direction
     * @param col the deltaCol associated with this Direction
     */
    private Direction(int row, int col) {
        deltaRow = row;
        deltaCol = col;
    }

    /**
     * Returns the deltaRow associated with this Direction.
     *
     * @return the deltaRow associated with this Direction
     */
    public int getDeltaRow() {
        return deltaRow;
    }

    /**
     * Returns the deltaCol associated with this Direction.
     *
     * @return the deltaCol associated with this Direction
     */
    public int getDeltaCol() {
        return deltaCol;
    }

    /**
     * Returns the opposite direction of the current direction.
     *
     * @return the opposite direction of the current direction
     */
    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
