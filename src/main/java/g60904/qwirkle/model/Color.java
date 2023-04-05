package g60904.qwirkle.model;

/**
 * An enumeration of colors that can be used to represent tiles on a Qwirkle Game.
 *
 * @see Tile
 */
public enum Color {
    BLUE, RED, GREEN, ORANGE, YELLOW, PURPLE;

    @Override
    public String toString() {
        return switch (this) {
            case BLUE -> "\033[34m";
            case RED -> "\033[31m";
            case GREEN -> "\033[32m";
            case ORANGE -> "\u001B[38;2;255;157;10m";
            case YELLOW -> "\033[93m";
            case PURPLE -> "\033[35m";
        };
    }
}
