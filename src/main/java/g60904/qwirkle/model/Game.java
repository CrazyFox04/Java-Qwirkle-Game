package g60904.qwirkle.model;

import javax.swing.*;
import java.io.*;
import java.util.List;

/**
 * This class represents a Qwirkle game. It contains a grid where tiles can be placed and an array of players.
 * The current player is also tracked by the game.
 */
public class Game implements Serializable {
    /**
     * The grid where tiles are placed.
     */
    private final Grid grid;
    /**
     * The array of players.
     */
    private final Player[] players;

    /**
     * The index of the current player.
     */
    private int currentPlayer;
    private final Bag bag = Bag.getInstance();
    private static final JFileChooser fc = new JFileChooser();
    private static final int MAX_NUMBER_PLAYERS = 5;
    /**
     * Constructs a new Qwirkle game with the specified list of players.
     * @param playersNameList the list of players to participate in the game
     * @throws QwirkleException if the number of players exceeds the maximum limit
     */
    public Game(List<String> playersNameList) throws QwirkleException {
        if (playersNameList.size()>MAX_NUMBER_PLAYERS) {
            throw new QwirkleException("You can't be more than " + MAX_NUMBER_PLAYERS + " players.");
        }
        players = new Player[playersNameList.size()];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(playersNameList.get(i));
        }
        grid = new Grid();
        currentPlayer = 0;
        players[currentPlayer].refill();
    }

    /**
     * Writes the current game state to a file using serialization.
     * @return true if the write operation is successful, false otherwise
     * @throws QwirkleException if there is an error while writing the file
     */
    public boolean write() throws QwirkleException {
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try (var fileOutputStream = new FileOutputStream(file)) {
                var outputStream = new ObjectOutputStream(fileOutputStream);
                outputStream.writeObject(this);
                return true;
            } catch (IOException e) {
                throw new QwirkleException("Error while writing the file, double-checked that you have " +
                        "sufficient rights to perform this action.");
            }
        }
        return false;
    }

    /**
     * Reads a game state from a file using deserialization.
     * @return the Game object read from the file
     * @throws QwirkleException if there is an error while reading the file
     */
    public static Game getFromFile() throws QwirkleException {
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try (var fileInputStream = new FileInputStream(file)) {
                ObjectInput in = new ObjectInputStream(
                        fileInputStream
                );
                return (Game) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new QwirkleException("Error while reading the file, it may be corrupted or simply doesn't exist.");
            }
        }
        return null;
    }

    /**
     * Returns the names of all players participating in the game.
     * @return an array of player names
     */
    public String[] getPlayersName() {
        var playersName = new String[players.length];
        for (int i = 0; i < players.length; i++) {
            playersName[i] = players[i].getName();
        }
        return playersName;
    }

    /**
     * Returns the scores of all players participating in the game.
     * @return an array of player scores
     */
    public int[] getPlayersScore() {
        var playersScore = new int[players.length];
        for (int i = 0; i < players.length; i++) {
            playersScore[i] = players[i].getScore();
        }
        return playersScore;
    }
    /**
     * Returns the maximum number of players allowed in the game.
     * @return the maximum number of players
     */
    public static int getMaxNumberPlayers() {
        return MAX_NUMBER_PLAYERS;
    }
    /**
     * Sets the bag instance after deserialization.
     */
    public static void setBagInstanceAfterSerialization(Game game) {
        Bag.setInstance(game.bag);
    }

    /**
     * Returns the name of the current player.
     *
     * @return the name of the current player
     */
    public String getCurrentPlayerName() {
        return players[currentPlayer].getName();
    }

    /**
     * Returns the hand of the current player.
     *
     * @return the hand of the current player
     */
    public List<Tile> getCurrentPlayerHand() {
        return players[currentPlayer].getHand();
    }

    /**
     * Returns the score of the current player.
     * @return the score of the current player
     */
    public int getCurrentPlayerScore() {
        return players[currentPlayer].getScore();
    }

    /**
     * Adds the specified tiles to the grid as the first play of the game.
     *
     * @param d  the direction in which the tiles will be played
     * @param is the indices of the tiles to play from the current player's hand
     */
    public void first(Direction d, int... is) {
        try {
            players[currentPlayer].addScore(grid.firstAdd(d, getTileOfPlayer(is)));
            removeTileOfPlayer(is);
            pass();
        } catch (QwirkleException e) {
            throw new QwirkleException(e.getMessage());
        }
    }

    /**
     * Adds the specified tile to the grid at the specified position.
     *
     * @param row   the row where the tile will be placed
     * @param col   the column where the tile will be placed
     * @param index the index of the tile to play from the current player's hand
     */
    public void play(int row, int col, int index) {
        try {
            players[currentPlayer].addScore(grid.add(row, col, players[currentPlayer].getHand().get(index)));
            removeTileOfPlayer(index);
            pass();
        } catch (QwirkleException e) {
            throw new QwirkleException(e.getMessage());
        }
    }

    /**
     * Adds the specified tiles to the grid in the specified direction starting at the specified position.
     *
     * @param row     the row where the first tile will be placed
     * @param col     the column where the first tile will be placed
     * @param d       the direction in which the tiles will be played
     * @param indexes the indices of the tiles to play from the current player's hand
     */
    public void play(int row, int col, Direction d, int... indexes) {
        try {
            players[currentPlayer].addScore(grid.add(row, col, d, getTileOfPlayer(indexes)));
            removeTileOfPlayer(indexes);
            pass();
        } catch (QwirkleException e) {
            throw new QwirkleException(e.getMessage());
        }
    }

    /**
     * This method allows the current player to play a set of tiles onto the game grid.
     *
     * @param is The indices of the tiles in the current player's hand to play.
     *           The indices must be provided in groups of three:
     *           the first two indices represent the row and column indices
     *           on the game grid where the tile is to be placed,
     *           and the third index represents the index of the tile in the current player's hand to play.
     * @throws QwirkleException if the tiles cannot be played because they do not form a valid qwirkle
     *                          or do not match the tiles already on the grid.
     */
    public void play(int... is) throws QwirkleException {
        try {
            players[currentPlayer].addScore(grid.add(getTileAtPosOfPlayer(is)));
            removeTileOfPlayer(getTilesIndexes(is));
            pass();
        } catch (QwirkleException e) {
            throw new QwirkleException(e.getMessage());
        }
    }

    /**
     * This method allows the current player to pass their turn.
     * After calling this method, the next player will become the current player.
     */
    public void pass() {
        currentPlayer = ++currentPlayer % players.length;
        players[currentPlayer].refill();
    }

    /**
     * Checks if the game is over.
     * @return true if the game is over, false otherwise
     */
    public boolean isOver() {
        if (players[getPreviousPlayer()].getHand().isEmpty() && isBagEmpty()) {
            players[getPreviousPlayer()].addScore(6);
            return true;
        } else return !atLeastOnePlayerCanPlay() && isBagEmpty();
    }
    /**
     * Checks if at least one player cannot play any tiles.
     * @return true if at least one player cannot play any tiles, false otherwise
     */
    private boolean atLeastOnePlayerCanPlay() {
        for (Player player : players) {
            for (Tile tile : player.getHand()) {
                for (int row = 0; row < grid.getGRID_SIZE(); row++) {
                    for (int col = 0; col < grid.getGRID_SIZE(); col++) {
                        if (grid.canAdd(row, col, tile)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     * Returns the index of the previous player.
     *
     * @return the index of the previous player
     */
    private int getPreviousPlayer() {
        return currentPlayer - 1 > 0 ? currentPlayer - 1 : players.length - 1;
    }
    /**
     * Checks if the bag is empty.
     * @return true if the bag is empty, false otherwise
     */
    private boolean isBagEmpty() {
        return bag.size()==0;
    }

    /**
     * Returns the grid of the Qwirkle game.
     *
     * @return the grid of the Qwirkle game
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Returns an array of the tiles at the given indexes in the current player's hand.
     *
     * @param is The indexes of the desired tiles in the current player's hand.
     * @return An array of the Tile objects at the specified indexes in the current player's hand.
     */
    private Tile[] getTileOfPlayer(int... is) {
        var tiles = new Tile[is.length];
        for (int i = 0; i < is.length; i++) {
            tiles[i] = players[currentPlayer].getHand().get(is[i]);
        }
        return tiles;
    }

    /**
     * Returns an array of TileAtPosition objects representing the tiles to be played by the current player,
     * as specified by their indexes in the player's hand and the positions on the board where they should be placed.
     *
     * @param is The indexes of the tiles to be played in the current player's hand,
     *           followed by their x and y coordinates on the board.
     * @return An array of TileAtPosition objects representing the tiles to be played by the current player.
     */
    private TileAtPosition[] getTileAtPosOfPlayer(int... is) {
        if (is.length % 3 != 0) {
            throw new QwirkleException("The number of arguments is incorrect. Please try again.");
        }
        var tilesAtPos = new TileAtPosition[is.length / 3];
        for (int i = 0; i < is.length / 3; i++) {
            tilesAtPos[i] = new TileAtPosition(
                    is[i * 3],
                    is[i * 3 + 1],
                    players[currentPlayer].getHand().get(is[i * 3 + 2])
            );
        }
        return tilesAtPos;
    }

    /**
     * Returns an array of the indexes of the tiles to be played in the current player's hand.
     *
     * @param is The indexes of the tiles to be played in the current player's hand,
     *           followed by their x and y coordinates on the board.
     * @return An array of the indexes of the tiles to be played in the current player's hand.
     */
    private int[] getTilesIndexes(int... is) {
        var tilesIndexes = new int[is.length / 3];
        for (int i = 0; i < is.length / 3; i++) {
            tilesIndexes[i] = is[i * 3 + 2];
        }
        return tilesIndexes;
    }

    /**
     * Removes the tiles at the specified indexes from the current player's hand,
     * and refills the hand with new tiles from the bag if necessary.
     *
     * @param is The indexes of the tiles to be removed from the current player's hand.
     */
    private void removeTileOfPlayer(int... is) {
        players[currentPlayer].removeTilesFromHand(getTileOfPlayer(is));
        players[currentPlayer].refill();
    }
}
