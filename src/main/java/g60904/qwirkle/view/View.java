package g60904.qwirkle.view;

import g60904.qwirkle.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class View {
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

    private static String getTileString(GridView grid, int row, int col) {
        if (grid.get(row, col) == null) {
            return " ";
        } else {
            return grid.get(row, col).toString();
        }
    }

    public static void display(Player player) {
        System.out.println(player.getName() + " à votre tour, " + "vous avez dans votre main : " + player.getHand());
    }

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

    public static void displayError(String message) {
        System.err.println(message);
    }
}
