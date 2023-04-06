package g60904.qwirkle.app;

import g60904.qwirkle.model.*;
import g60904.qwirkle.view.View;

import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner clavier = new Scanner(System.in);

    public static void main(String[] args) {
        View.displayWelcome();
        List<Player> playersList = View.askPlayerName();
        Game game = new Game(playersList);
        while (true) {
            for (Player player : playersList) {
                View.display(new GridView(game.getGrid()));
                player.refill();
                View.display(player);
                command(game);
            }
        }
    }

    private static void command(Game game) {
        boolean restart = true;
        while (restart) {
            String command = clavier.nextLine();
            command = command.toLowerCase();
            if (command.startsWith("h")) {
                View.displayHelp();
            } else if (command.startsWith("o")) {
                String[] parts = command.split(" ");
                int row = Integer.parseInt(parts[1]);
                int col = Integer.parseInt(parts[2]);
                int tileIndex = Integer.parseInt(parts[3]);
                try {
                    game.play(row, col, tileIndex);
                    restart = false;
                } catch (QwirkleException e) {
                    View.displayError(e.getMessage());
                }
            } else if (command.startsWith("l")) {
                String[] parts = command.split(" ");
                int row = Integer.parseInt(parts[1]);
                int col = Integer.parseInt(parts[2]);
                var d = getDirection(parts[3]);
                int tileIndex1 = Integer.parseInt(parts[4]);
                int tileIndex2 = -1;
                if (parts.length > 5) {
                    tileIndex2 = Integer.parseInt(parts[5]);
                }
                try {
                    game.play(row, col, d, tileIndex1, tileIndex2);
                    restart = false;
                } catch (QwirkleException e) {
                    View.displayError(e.getMessage());
                }
            } else if (command.startsWith("m")) {
                String[] parts = command.split(" ");
                var args = new int[parts.length - 1];
                try {
                    for (int i = 1; i < parts.length; i++) {
                        args[i - 1] = Integer.parseInt(parts[i]);
                    }
                    game.play(args);
                    restart = false;
                } catch (QwirkleException e) {
                    View.displayError(e.getMessage());
                }
            } else if (command.startsWith("f")) {
                String[] parts = command.split(" ");
                int tileIndex1 = Integer.parseInt(parts[1]);
                try {
                    game.first(Direction.DOWN, tileIndex1);
                    restart = false;
                } catch (QwirkleException e) {
                    View.displayError(e.getMessage());
                }
            } else if (command.startsWith("p")) {
                game.pass();
                restart = false;
            } else {
                View.displayError("This command, doesn't exist. Please try again.");
            }
        }
    }

    private static Direction getDirection(String d) {
        return switch (d) {
            case "l" -> Direction.LEFT;
            case "r" -> Direction.RIGHT;
            case "u" -> Direction.UP;
            case "d" -> Direction.DOWN;
            default -> throw new QwirkleException("error");
        };
    }
}
