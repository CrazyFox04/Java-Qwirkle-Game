package g60904.qwirkle.view;

import g60904.qwirkle.model.*;

/**
 * The View class provides static methods to display information related to the Qwirkle game.
 * It has methods to display the game board, player's hand, help menu, and error messages.
 * It depends on the GridView and Player classes from the model package to display the game state.
 */
public class View {
    /**
     * Displays the game board represented by the given grid.
     *
     * @param grid the GridView object representing the game board
     */
    public static void display(GridView grid) {
        var actualLimits = grid.getGridLimits();
        for (int i = actualLimits[2]; i <= actualLimits[0]; i++) {
            System.out.printf("%2d | ", i);
            for (int j = actualLimits[3]; j >= actualLimits[1]; j--) {
                System.out.print(" " + getTileString(grid, i, j) + "  ");
            }
            System.out.println();
        }
        System.out.print("    ");
        for (int i = actualLimits[2]; i <= actualLimits[0]; i++) {
            System.out.printf(" %2d ", i);
        }
    }

    /**
     * Returns the string representation of the tile at the specified row and column of the grid.
     *
     * @param grid the GridView object representing the game board
     * @param row  the row of the tile
     * @param col  the column of the tile
     * @return the string representation of the tile at the specified row and column of the grid
     */
    private static String getTileString(GridView grid, int row, int col) {
        if (grid.get(row, col) == null) {
            return " ";
        } else {
            return grid.get(row, col).toString();
        }
    }

    /**
     * Displays the name of the player and the tiles in their hand.
     *
     * @param player the Player object whose hand is to be displayed
     */
    public static void display(Player player) {
        System.out.println(player.getName() + " à votre tour, " + "vous avez dans votre main : " + player.getHand());
    }

    /**
     * Displays the help menu for the Qwirkle game.
     */
    public static void displayHelp() {
        System.out.println(
                """
                                QWIRKLE
                                Qwirkle command:
                                - play 1 tile : o <row> <col> <i>
                                - play line: l <row> <col> <direction> <i1> [<i2>]
                                - play plic-ploc : m <row1> <col1> <i1> [<row2> <col2> <i2>] - play first : f <i1> [<i2>]
                                - pass : p
                                - quit : q
                                i : index in list of tiles
                                d : direction in l (left), r (right), u (up), d(down)
                        """);
    }

    /**
     * Displays the error message.
     *
     * @param message the error message to be displayed
     */
    public static void displayError(String message) {
        System.err.println(message);
    }
}
