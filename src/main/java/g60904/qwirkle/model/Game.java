package g60904.qwirkle.model;

import java.util.List;
import java.util.stream.Stream;

/**
 * This class represents a Qwirkle game. It contains a grid where tiles can be placed and an array of players.
 * The current player is also tracked by the game.
 */
public class Game {
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

    /**
     * Constructs a new Qwirkle game with the specified list of players.
     *
     * @param listOfPlayers the list of players to participate in the game
     */
    public Game(List<Player> listOfPlayers) {
        grid = new Grid();
        players = new Player[listOfPlayers.size()];
        for (int i = 0; i < listOfPlayers.size(); i++) {
            players[i] = listOfPlayers.get(i);
        }
        currentPlayer = 0;
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
     * Adds the specified tiles to the grid as the first play of the game.
     *
     * @param d  the direction in which the tiles will be played
     * @param is the indices of the tiles to play from the current player's hand
     */
    public void first(Direction d, int... is) {
        var restart = true;
        while (restart) {
            try {
                grid.firstAdd(d, getTileOfPlayer(is));
                removeTileOfPlayer(is);
                pass();
                restart = false;
            } catch (QwirkleException e) {
                System.out.println("Vous ne pouvez pas jouer ces tuiles car " + e.getMessage() +
                        ".\n Veillez réessayer");
            }
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
        var restart = true;
        while (restart) {
            try {
                grid.add(row, col, players[currentPlayer].getHand().get(index));
                removeTileOfPlayer(index);
                pass();
                restart = false;
            } catch (QwirkleException e) {
                System.out.println("Vous ne pouvez pas jouer ces tuiles car " + e.getMessage() +
                        ".\n Veillez réessayer");
            }
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
        var restart = true;
        while (restart) {
            try {
                grid.add(row, col, d, getTileOfPlayer(indexes));
                removeTileOfPlayer(indexes);
                pass();
                restart = false;
            } catch (QwirkleException e) {
                System.out.println("Vous ne pouvez pas jouer ces tuiles car " + e.getMessage() +
                        ".\n Veillez réessayer");
            }
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
    public void play(int... is) {
        var restart = true;
        while (restart) {
            try {
                grid.add(getTileAtPosOfPlayer(is));
                removeTileOfPlayer(getTilesIndexes(is));
                pass();
                restart = false;
            } catch (QwirkleException e) {
                System.out.println("Vous ne pouvez pas jouer ces tuiles car " + e.getMessage() +
                        ".\n Veillez réessayer");
            }
        }
    }

    /**
     * This method allows the current player to pass their turn.
     * After calling this method, the next player will become the current player.
     */
    public void pass() {
        currentPlayer = ++currentPlayer % players.length;
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
        players[currentPlayer].remove(getTileOfPlayer(is));
        players[currentPlayer].refill();
    }
}
