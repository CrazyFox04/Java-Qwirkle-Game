package g60904.qwirkle.app;

import g60904.qwirkle.model.*;
import g60904.qwirkle.view.View;

import java.util.List;
import java.util.Scanner;

public class App {
    private static Game game;
    /**
     * The main method of the Qwirkle application.
     * It handles the game flow by displaying the welcome message, loading a saved game if requested,
     * or creating a new game with player names if no saved game is available.
     * Then, it enters a loop where it displays the game state, asks for a command from the current player,
     * and continues until the game is over. Finally, it displays the end-of-game message with the final scores.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        View.displayWelcome();
        if (!loadASavedGame()) {
            List<String> playersNameList = View.askPlayerName();
            game = new Game(playersNameList);
        }
        do {
            View.display(new GridView(game.getGrid()));
            View.display(game.getCurrentPlayerName(), game.getCurrentPlayerHand(), game.getCurrentPlayerScore());
            askCommandFromCurrentPlayer();
        } while (!game.isOver());
        View.displayEnd(game.getPlayersName(), game.getPlayersScore());
    }
    /**
     * Asks the current player for a command and executes the corresponding action based on the command entered.
     */
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
    /**
     * Quits the game based on the player's decision.
     * If the player wants to save the game, it prompts for the save file name and attempts to write the game state.
     * If the saving process fails, it asks the question again.
     * If the player doesn't want to save the game, it displays the end-of-game message and terminates the application.
     */
    private static void quit() {
        boolean askQuestionAgain;
        do {
            if (View.playerWantToSaveAGame()) {
                askQuestionAgain = !game.write();
            } else {
                askQuestionAgain = false;
                View.displayEnd(game.getPlayersName(), game.getPlayersScore());
            }
        } while (askQuestionAgain);
        System.exit(0);
    }
    /**
     * Loads a saved game if the player chooses to do so.
     * It prompts the player to load a game and attempts to read the saved game file.
     * If the loading process fails, it asks the question again.
     *
     * @return true if a saved game was successfully loaded, false otherwise
     */
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
    /**
     * Passes the turn for the current player.
     */
    private static void pass() {
        game.pass();
    }
    /**
     * Places the first tiles on the grid based on the command entered.
     * It checks the command syntax and validity of positions and direction, then calls the corresponding game method.
     *
     * @param command the command entered by the player
     */
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
    /**
     * Checks if the hand positions provided in the command are valid.
     *
     * @param command the command entered by the player
     * @return true if the hand positions are valid, false otherwise
     */
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
    /**
     * Checks if the grid positions provided in the command are valid.
     *
     * @param command the command entered by the player
     * @return true if the grid positions are valid, false otherwise
     */
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
    /**
     * Checks if the hand positions provided in the command are valid.
     *
     * @param command   the command entered by the player
     * @param firstPos  the position in the command array where the hand positions start
     * @return true if the hand positions are valid, false otherwise
     */
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
    /**
     * Checks if the position provided is within the grid boundaries.
     *
     * @param row the row coordinate
     * @param col the column coordinate
     * @return true if the position is within the grid, false otherwise
     */
    private static boolean positionIsInGrid(int row, int col) {
        var gridSize = game.getGrid().getGRID_SIZE();
        return row > 0 && row < gridSize && col > 0 && col < gridSize;
    }
    /**
     * Checks if the position provided in the command is correct.
     *
     * @param command   the command entered by the player
     * @param startPos  the position in the command array where the position starts
     * @return true if the position is correct, false otherwise
     */
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
    /**
     * Places tiles at specific positions on the grid based on the command entered.
     * It checks the command syntax and validity of positions, then calls the corresponding game method.
     *
     * @param command the command entered by the player
     */
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
    /**
     * Places a line of tiles on the grid based on the command entered.
     * It checks the command syntax and validity of positions and direction, then calls the corresponding game method.
     *
     * @param command the command entered by the player
     */
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
    /**
     * Places one tile on the grid based on the command entered.
     * It checks the command syntax and validity of position, then calls the corresponding game method.
     *
     * @param command the command entered by the player
     */
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
    /**
     * Gets the direction enum value based on the direction string.
     *
     * @param d the direction string
     * @return the corresponding direction enum value
     */
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
    /**
     * Checks if the direction provided is one of the recognized directions.
     *
     * @param enteredDirectionNickname the direction string
     * @return true if the direction is recognized, false otherwise
     */
    private static boolean isOneOfTheDirection(String enteredDirectionNickname) {
        for (Direction direction : Direction.values()) {
            if (direction.getNickname() == enteredDirectionNickname.toCharArray()[0]) {
                return true;
            }
        }
        return false;
    }
}
