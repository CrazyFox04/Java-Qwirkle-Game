package g60904.qwirkle.model;

/**
 * An enumeration of colors that can be used to represent tiles on a Qwirkle Game.
 *
 * @see Tile
 */
public enum Color {
    BLUE("\033[34m"),
    RED("\033[31m"),
    GREEN("\033[32m"),
    ORANGE("\u001B[38;2;255;157;10m"),
    YELLOW("\033[93m"),
    PURPLE("\033[35m"),
    WHITE("\033[97m");
    private final String code;

    /**
     * Constructs a Color object with the specified color code.
     * @param code the color code associated with the color.
     */
    Color(String code) {
        this.code = code;
    }

    /**
     * Returns the color code associated with the color.
     * @return the color code associated with the color.
     */
    @Override
    public String toString() {
        return this.code;
    }
}
