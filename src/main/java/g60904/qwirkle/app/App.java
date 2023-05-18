package g60904.qwirkle.app;

import g60904.qwirkle.model.*;
import g60904.qwirkle.view.View;

import java.util.List;
import java.util.Scanner;

public class App {
    private static Game game;

    public static void main(String[] args) {
        View.displayWelcome();
        if (!loadASavedGame()) {
            List<Player> playersList = View.askPlayerName(); //todo
            game = new Game(playersList);
        }
        do {
            View.display(new GridView(game.getGrid()));
            View.display(game.getCurrentPlayerName(), game.getCurrentPlayerHand(), game.getCurrentPlayerScore());
            askCommandFromCurrentPlayer();
        } while (!game.isOver());
        View.displayEnd(game.getPlayers());
    }

    private static void askCommandFromCurrentPlayer() {
        String command = View.getCommand();
        String[] parts = command.split(" ");
        switch (parts[0]) {
            case "h" -> View.displayHelp();
            case "o" -> placeOneTile(command);
            case "l" -> placeLineOfTiles(command);
            case "m" -> placeTilesAtPos(command);
            case "f" -> placeFirstTiles(command);
            case "p" -> pass();
            case "q" -> quit();
            default -> View.displayError("This command, doesn't exist. Please try again.");
        }
    }
    private static void quit() {
        boolean askQuestionAgain;
        do {
            if (View.playerWantToSaveAGame()) {
                askQuestionAgain = !game.write();
            } else {
                askQuestionAgain = false;
                View.displayEnd(game.getPlayers()); // todo
            }
        } while (askQuestionAgain);
        System.exit(0);
    }
    private static boolean loadASavedGame() {
        Game serializedGame = null;
        while (serializedGame == null) {
            if (View.playerWantToLoadAGame()) {
                serializedGame = Game.getFromFile();
            } else {
                return false;
            }
        }
        game = serializedGame;
        Game.setBagInstanceAfterSerialization(game);
        return true;
    }

    private static void pass() {
        game.pass();
        game.incNumberOfPassInARow();
    }

    private static void placeFirstTiles(String command) {
        String[] splitCommand = command.split(" ");
        if (splitCommand.length < 2 || (splitCommand.length == 2 && !splitCommand[1].matches("\\d"))) {
            View.displayError("The number of parameters entered is incorrect. Please try again.");
        } else if (splitCommand.length != 2 && !isOneOfTheDirection(splitCommand[1])) {
            View.displayError("The direction your entered is not recognised");
        } else if (!handPositionAreInHand(splitCommand, 2) ||
                (splitCommand.length == 2 && !handPositionAreInHand(splitCommand, 1))) {
            View.displayError("The position of the tile in the user's hand does not correspond to any known.");
        } else {
            try {
                Direction d;
                if (splitCommand.length == 2) {
                    game.first(Direction.RIGHT, Integer.parseInt(splitCommand[1]));
                } else {
                    d = getDirection(splitCommand[1]);
                    var args = new int[splitCommand.length - 2];
                    for (int i = 2; i < splitCommand.length; i++) {
                        args[i - 2] = Integer.parseInt(splitCommand[i]);
                    }
                    game.first(d, args);
                }
            } catch (QwirkleException e) {
                View.displayError(e.getMessage());
            }
        }
    }

    private static boolean handPositionAreInHandTAP(String[] command) {
        try {
            int sizeOfHand = game.getCurrentPlayerHand().size();
            for (int i = 3; i < command.length; i += 3) {
                var pos = Integer.parseInt(command[i]);
                if (pos > sizeOfHand || pos < 0) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            View.displayError("The parameters entered must be consistent integers. Please try again.");
            return false;
        }
    }

    private static boolean positionIsInGridTAP(String[] command) {
        try {
            for (int i = 1; i < command.length; i += 3) {
                if (!positionIsInGrid(Integer.parseInt(command[i]), Integer.parseInt(command[i + 1]))) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            View.displayError("The parameters entered must be consistent integers. Please try again.");
            return false;
        }
    }

    private static boolean handPositionAreInHand(String[] command, int firstPos) {
        try {
            int sizeOfHand = game.getCurrentPlayerHand().size();
            for (int i = firstPos; i < command.length; i++) {
                var pos = Integer.parseInt(command[i]);
                if (pos > sizeOfHand || pos < 0) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            View.displayError("The parameters entered must be consistent integers. Please try again.");
            return false;
        }
    }

    private static boolean positionIsInGrid(int row, int col) {
        var gridSize = game.getGrid().getGRID_SIZE();
        return row > 0 && row < gridSize && col > 0 && col < gridSize;
    }

    private static boolean positionIsCorrect(String[] command, int startPos) {
        var row = command[startPos];
        var col = command[startPos + 1];
        try {
            return positionIsInGrid(Integer.parseInt(row), Integer.parseInt(col));
        } catch (NumberFormatException e) {
            View.displayError("The parameters entered must be consistent integers. Please try again.");
            return false;
        }
    }

    private static void placeTilesAtPos(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 4) {
            View.displayError("The number of parameters entered is incorrect. Please try again.");
        } else if (!positionIsInGridTAP(parts)) {
            View.displayError("At least one of the position you entered doesn't correspond to correct position on the grid");
        } else if (!handPositionAreInHandTAP(parts)) {
            View.displayError("The position of the tile in the user's hand does not correspond to any known.");
        } else {
            try {
                var args = new int[parts.length - 1];
                for (int i = 1; i < parts.length; i++) {
                    args[i - 1] = Integer.parseInt(parts[i]);
                }
                game.play(args);
            } catch (QwirkleException e) {
                View.displayError(e.getMessage());
            }
        }
    }

    private static void placeLineOfTiles(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 5) {
            View.displayError("The number of parameters entered is incorrect. Please try again.");
        } else if (!positionIsCorrect(parts, 1)) {
            View.displayError("The position you entered doesn't correspond to a correct position on the grid");
        } else if (!isOneOfTheDirection(parts[3])) {
            View.displayError("The direction your entered is not recognised");
        } else if (!handPositionAreInHand(parts, 4)) {
            View.displayError("The position of the tile in the user's hand does not correspond to any known.");
        } else {
            try {
                var row = Integer.parseInt(parts[1]);
                var col = Integer.parseInt(parts[2]);
                var d = getDirection(parts[3]);
                var args = new int[parts.length - 4];
                for (int i = 4; i < parts.length; i++) {
                    args[i - 4] = Integer.parseInt(parts[i]);
                }
                game.play(row, col, d, args);
            } catch (QwirkleException e) {
                View.displayError(e.getMessage());
            }
        }
    }

    private static void placeOneTile(String command) {
        String[] parts = command.split(" ");
        if (parts.length != 4) {
            View.displayError("The number of parameters entered is incorrect. Please try again.");
        } else if (!positionIsCorrect(parts, 1)) {
            View.displayError("The position you entered doesn't correspond to a correct position on the grid");
        } else if (!handPositionAreInHand(parts, 3)) {
            View.displayError("The position of the tile in the user's hand does not correspond to any known.");
        } else {
            try {
                var row = Integer.parseInt(parts[1]);
                var col = Integer.parseInt(parts[2]);
                var tileIndex = Integer.parseInt(parts[3]);
                game.play(row, col, tileIndex);
            } catch (QwirkleException e) {
                View.displayError(e.getMessage());
            }
        }
    }

    private static Direction getDirection(String d) {
        return switch (d) {
            case "l" -> Direction.LEFT;
            case "r" -> Direction.RIGHT;
            case "u" -> Direction.UP;
            case "d" -> Direction.DOWN;
            default -> throw new QwirkleException("The direction you entered does not match with any known direction. "
                    + "Please try again.");
        };
    }

    private static boolean isOneOfTheDirection(String enteredDirectionNickname) {
        for (Direction direction : Direction.values()) {
            if (direction.getNickname() == enteredDirectionNickname.toCharArray()[0]) {
                return true;
            }
        }
        return false;
    }
}
