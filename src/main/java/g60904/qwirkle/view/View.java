package g60904.qwirkle.view;

import g60904.qwirkle.model.Game;
import g60904.qwirkle.model.GridView;
import g60904.qwirkle.model.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The View class provides static methods to display information related to the Qwirkle game.
 * It has methods to display the game board, player's hand, help menu, and error messages.
 * It depends on the GridView and Player classes from the model package to display the game state.
 */
public class View {
    private static final Scanner clavier = new Scanner(System.in);

    /**
     * Displays the game board represented by the given grid.
     *
     * @param grid the GridView object representing the game board
     */
    public static void display(GridView grid) {
        var actualLimits = grid.getGridLimits();
        for (int i = actualLimits[2]; i <= actualLimits[0]; i++) {
            System.out.printf("%2d | ", i);
            for (int j = actualLimits[1]; j <= actualLimits[3]; j++) {
                System.out.print(" " + getTileString(grid, i, j) + "  ");
            }
            System.out.println();
        }
        System.out.print("    ");
        for (int i = actualLimits[1]; i <= actualLimits[3]; i++) {
            System.out.printf(" %2d ", i);
        }
        System.out.println();
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
     * @param playerName the name of the player
     * @param playerHand the tiles in the player's hand
     * @param playerScore the score of the player
     */
    public static void display(String playerName, List<Tile> playerHand, int playerScore) {
        System.out.println(playerName + " it's your turn, " + "you have in your hand (0 - " + (playerHand.size() - 1) + ") : " + playerHand);
        System.out.println("You have " + playerScore + " points.");
    }

    /**
     * Displays the help menu for the Qwirkle game.
     */
    public static void displayHelp() {
        System.out.println(
                """
                                
                                ============================================================
                                                       QWIRKLE Help
                                ============================================================
                                Qwirkle command:
                                - play 1 tile : o <row> <col> <i>
                                - play line: l <row> <col> <direction> <i1> [<i2>]
                                - play plic-ploc : m <row1> <col1> <i1> [<row2> <col2> <i2>]
                                - play first : f <direction> <i1> [<i2>]
                                - pass : p
                                - quit : q
                                - display this help : h
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
    /**
     * Asks the players for their names and returns a list of player names.
     *
     * @return a list of player names
     */
    public static List<String> askPlayerName() {
        List<String> playerNameList = new ArrayList<>();
        System.out.print("How many players would like to play ? ");
        int n = 0;
        while (n < 1 || n > Game.getMaxNumberPlayers()) {
            n = lireEntier();
            if (n < 1) {
                System.out.print("Please enter an integer between 1 and " + Game.getMaxNumberPlayers());
            }
        }
        System.out.println();
        System.out.println("Enter the names of the players, press 'enter' to confirm the name of each player.");
        for (int i = 0; i < n; i++) {
            System.out.print("Player " + (i + 1) + " name : ");
            var playerName = clavier.nextLine();
            playerNameList.add(playerName);
        }
        System.out.println();
        return playerNameList;
    }
    /**
     * Reads an integer from the console input.
     *
     * @return the integer value read from the console
     */
    private static int lireEntier() {
        while (!clavier.hasNextInt()) {
            clavier.next();
            System.out.print("Please enter an integer greater than or equal to 1 : ");
        }
        var result = clavier.nextInt();
        clavier.nextLine();
        return result;
    }
    /**
     * Displays the welcome message for the Qwirkle game.
     */
    public static void displayWelcome() {
        System.out.println(
                """
                        ==================================================================================
                                                          QWIRKLE Game
                        ==================================================================================
                        """
        );
    }
    /**
     * Displays the end-of-game message with the final scores of each player.
     *
     * @param playersName  an array of player names
     * @param playersScore an array of player scores
     */
    public static void displayEnd(String[] playersName, int[] playersScore) {
        System.out.println("The game is finished !");
        for (int i = 0; i < playersName.length; i++) {
            System.out.println(playersName[i] + ", you finished the game with " + playersScore[i] + " points.");
        }
        System.out.println("==================================================================================");
    }
    /**
     * Asks the player if they want to load a previous game.
     *
     * @return true if the player wants to load a game, false otherwise
     */
    public static boolean playerWantToLoadAGame() {
        System.out.print("Do you want to restore a previous Game file ? (y/n) ");
        String answer = clavier.nextLine();
        answer = answer.toLowerCase();
        while (!answer.equals("y") && !answer.equals("n")) {
            System.out.print("Please enter 'y' or 'n' to answer the question : ");
            answer = clavier.nextLine();
            answer = answer.toLowerCase();
        }
        System.out.println();
        return answer.equals("y");
    }
    /**
     * Asks the player if they want to save the current game.
     *
     * @return true if the player wants to save the game, false otherwise
     */
    public static boolean playerWantToSaveAGame() {
        System.out.print("Do you want to save the game to restore it later ? (y/n) ");
        String answer = clavier.nextLine();
        answer = answer.toLowerCase();
        while (!answer.equals("y") && !answer.equals("n")) {
            System.out.print("Please enter 'y' or 'n' to answer the question : ");
            answer = clavier.nextLine();
            answer = answer.toLowerCase();
        }
        System.out.println();
        return answer.equals("y");
    }
    /**
     * Reads a command from the console input.
     *
     * @return the command entered by the player
     */
    public static String getCommand() {
        System.out.print("Command : ");
        String command = clavier.nextLine();
        command = command.toLowerCase();
        return command;
    }
}
