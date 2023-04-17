package g60904.qwirkle.app;

import g60904.qwirkle.model.*;
import g60904.qwirkle.view.View;

import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner clavier = new Scanner(System.in);
    private static Game game;

    public static void main(String[] args) {
        View.displayWelcome();
        List<Player> playersList = View.askPlayerName();
        game = new Game(playersList);
        while (true) {
            for (Player player : playersList) {
                View.display(new GridView(game.getGrid()));
                player.refill();
                View.display(player);
                command(player);
            }
        }
    }

    private static void command(Player player) {
        boolean restart = true;
        while (restart) {
            String command = clavier.nextLine();
            command = command.toLowerCase();
            String[] parts = command.split(" ");
            switch (parts[0]) {
                case "h" -> {
                    displayHelp(player);
                }
                case "o" -> restart = placeOneTile(restart, command);
                case "l" -> restart = placeLineOfTiles(restart, command);
                case "m" -> restart = placeTiles(restart, command);
                case "f" -> restart = placeFirstTiles(restart, command);
                case "p" -> restart = pass();
                default -> View.displayError("This command, doesn't exist. Please try again.");
            }
        }
    }

    private static boolean pass() {
        game.pass();
        return false;
    }

    private static boolean placeFirstTiles(boolean restart, String command) {
        try {
            String[] parts = command.split(" ");
            var d = getDirection(parts[1]);
            var args = new int[parts.length - 2];
            for (int i = 2; i < parts.length; i++) {
                args[i - 2] = Integer.parseInt(parts[i]);
            }
            game.first(d, args);
            restart = false;
        } catch (QwirkleException e) {
            View.displayError(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            View.displayError("The number of parameters entered is incorrect. Please try again.");
        } catch (NumberFormatException e) {
            View.displayError("The parameters entered must be consistent integers. Please try again.");
        }
        return restart;
    }

    private static boolean placeTiles(boolean restart, String command) {
        try {
            String[] parts = command.split(" ");
            var args = new int[parts.length - 1];
            for (int i = 1; i < parts.length; i++) {
                args[i - 1] = Integer.parseInt(parts[i]);
            }
            game.play(args);
            restart = false;
        } catch (QwirkleException e) {
            View.displayError(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            View.displayError("The number of parameters entered is incorrect. Please try again.");
        } catch (NumberFormatException e) {
            View.displayError("The parameters entered must be consistent integers. Please try again.");
        }
        return restart;
    }

    private static boolean placeLineOfTiles(boolean restart, String command) {
        try {
            String[] parts = command.split(" ");
            var row = Integer.parseInt(parts[1]);
            var col = Integer.parseInt(parts[2]);
            var d = getDirection(parts[3]);
            var args = new int[parts.length - 4];
            for (int i = 4; i < parts.length; i++) {
                args[i - 4] = Integer.parseInt(parts[i]);
            }
            game.play(row, col, d, args);
            restart = false;
        } catch (QwirkleException e) {
            View.displayError(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            View.displayError("The number of parameters entered is incorrect. Please try again.");
        } catch (NumberFormatException e) {
            View.displayError("The parameters entered must be consistent integers. Please try again.");
        }
        return restart;
    }

    private static boolean placeOneTile(boolean restart, String command) {
        try {
            String[] parts = command.split(" ");
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            int tileIndex = Integer.parseInt(parts[3]);
            game.play(row, col, tileIndex);
            restart = false;
        } catch (QwirkleException e) {
            View.displayError(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            View.displayError("The number of parameters entered is incorrect. Please try again.");
        } catch (NumberFormatException e) {
            View.displayError("The parameters entered must be consistent integers. Please try again.");
        }
        return restart;
    }

    private static void displayHelp(Player player) {
        View.displayHelp();
        View.display(player);
    }

    private static Direction getDirection(String d) {
        return switch (d) {
            case "l" -> Direction.LEFT;
            case "r" -> Direction.RIGHT;
            case "u" -> Direction.UP;
            case "d" -> Direction.DOWN;
            default ->
                    throw new QwirkleException("The direction you entered does not match with any known direction. "
                            + "Please try again.");
        };
    }
}
