package g60904.qwirkle.model;

import java.util.List;
import java.util.stream.Stream;

public class Game {
    private final Grid grid;
    private final Player[] players;

    private int currentPlayer;

    public Game(List<Player> listOfPlayers) {
        grid = new Grid();
        players = (Player[]) Stream.of(listOfPlayers).toArray();
        currentPlayer = 0;
    }

    public String getCurrentPlayerName() {
        return players[currentPlayer].getName();
    }

    public List<Tile> getCurrentPlayerHand() {
        return players[currentPlayer].getHand();
    }

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

    public void pass() {
        currentPlayer = ++currentPlayer % players.length;
    }

    private Tile[] getTileOfPlayer(int... is) {
        var tiles = new Tile[is.length];
        for (int i = 0; i < is.length; i++) {
            tiles[i] = players[currentPlayer].getHand().get(is[i]);
        }
        return tiles;
    }

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

    private int[] getTilesIndexes(int... is) {
        var tilesIndexes = new int[is.length / 3];
        for (int i = 0; i < is.length / 3; i++) {
            tilesIndexes[i] = is[i * 3 + 2];
        }
        return tilesIndexes;
    }

    private void removeTileOfPlayer(int... is) {
        players[currentPlayer].remove(getTileOfPlayer(is));
        players[currentPlayer].refill();
    }
}
