package g60904.qwirkle.model;

/**
 * An enum representing the four cardinal directions: UP, DOWN, LEFT, RIGHT.
 * Each direction is associated with a deltaRow and deltaCol, which can be used to
 * represent the movement in the respective direction on a two-dimensional grid.
 * The columns decrease as you go down and the rows as you go to the left.
 */
public enum Direction {
    UP(-1, 0, 'u'), DOWN(1, 0, 'd'),
    LEFT(0, -1, 'l'), RIGHT(0, 1, 'r');
    private final int deltaRow;
    private final int deltaCol;
    private final char nickname;

    /**
     * Constructs a new Direction with the specified deltaRow, deltaCol, and nickname.
     * @param row the deltaRow associated with this Direction
     * @param col the deltaCol associated with this Direction
     * @param nickname the nickname associated with this Direction
     */
    Direction(int row, int col, char nickname) {
        this.deltaRow = row;
        this.deltaCol = col;
        this.nickname = nickname;
    }

    /**
     * Returns the change in row index associated with this Direction.
     * <p>
     * The change in row index, also known as the "delta row", specifies how much the row index
     * should be adjusted when moving in this direction. A negative delta row means moving up, while a
     * positive delta row means moving down.
     *
     * @return the delta row associated with this Direction
     */
    public int getDeltaRow() {
        return deltaRow;
    }

    /**
     * Returns the change in col index associated with this Direction.
     * <p>
     * The change in col index, also known as the "delta col", specifies how much the col index
     * should be adjusted when moving in this direction. A negative delta col means moving left, while a
     * positive delta row means moving right.
     *
     * @return the delta col associated with this Direction
     */
    public int getDeltaCol() {
        return deltaCol;
    }

    /**
     * Returns the nickname associated with this Direction.
     * @return the nickname associated with this Direction
     */
    public char getNickname() {
        return nickname;
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
