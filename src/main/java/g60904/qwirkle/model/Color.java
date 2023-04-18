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
    PURPLE("\033[35m");
    private final String code;

    Color(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
