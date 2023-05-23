package g60904.qwirkle.model;

/**
 * An enumeration of shapes that can be used to represent tiles on a Qwirkle Game.
 *
 * @see Tile
 */
public enum Shape {
    CROSS, SQUARE, ROUND, STAR, PLUS, DIAMOND, TRIANGLE;
    /**
     * Returns a string representation of the shape.
     * The string representation corresponds to the symbol associated with the shape.
     *
     * @return the string representation of the shape
     */
    @Override
    public String toString() {
        return switch (this) {
            case CROSS -> "X";
            case SQUARE -> "■";
            case ROUND -> "●";
            case STAR -> "*";
            case PLUS -> "+";
            case DIAMOND -> "♦";
            case TRIANGLE -> "▲";
        };
    }
}
