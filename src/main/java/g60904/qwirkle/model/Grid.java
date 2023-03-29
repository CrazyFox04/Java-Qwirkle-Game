package g60904.qwirkle.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Grid class represents the game board of Qwirkle. It contains a 2D array of Tiles
 * which represents the board tiles and a boolean flag indicating if the grid is empty.
 */
public class Grid {
    private final Tile[][] tiles;
    private boolean isEmpty;

    /**
     * Constructs a new Grid instance with a 91x91 2D array of Tiles and initializes
     * the isEmpty flag to true.
     */
    public Grid() {
        tiles = new Tile[91][91];
        isEmpty = true;
    }

    /**
     * Adds a line of Tiles to the Grid in a given direction (horizontal or vertical)
     * starting from the center of the grid. Throws a QwirkleException if the grid is not
     * empty or if the added Tiles do not comply with the Qwirkle game rules.
     *
     * @param d    The direction in which the line of Tiles is added (horizontal or vertical).
     * @param line An array of Tiles representing the line of Tiles to add to the Grid.
     * @throws QwirkleException if the grid is not empty or if the added
     *                          Tiles do not comply with the Qwirkle game rules.
     */
    public void firstAdd(Direction d, Tile... line) {
        if (!isEmpty) {
            throw new QwirkleException("This method can only be called when the Grid is empty.");
        }
        Color savedColor = line[0].color();
        var savedShapes = new ArrayList<Shape>();
        for (Tile tile : line) {
            if (tile.color() != savedColor) {
                throw new QwirkleException("You can't play these Tiles, the color isn't the same : " + savedColor);
            }
            if (savedShapes.contains(tile.shape())) {
                throw new QwirkleException("You can't play these Tiles, there is more than one piece " +
                        "with the same shape");
            }
            savedShapes.add(tile.shape());
        }
        int addCounter = 0;
        for (Tile tile : line) {
            int rowToAddTile = 45 + addCounter * d.deltaRow;
            int colToAddTile = 45 + addCounter * d.deltaCol;
            tiles[rowToAddTile][colToAddTile] = tile;
            addCounter++;
        }
        isEmpty = false;

    }

    public void add(int row, int col, Tile tile) throws QwirkleException {
        if (tiles[row][col] != null) {
            throw new QwirkleException("This position (" + row + ", " + col + ") already contain a tile");
        }
        if (checkNearbyLines(tile, row, col)) {
            tiles[row][col] = tile;
        } else {
            throw new QwirkleException("Exception not handled - Grid:72");
        }
    }

    private boolean checkNearbyLines(Tile tile, int row, int col) {
        if (surroundingsAreNull(row, col)) {
            throw new QwirkleException("The Tile cannot be placed where there is none");
        } else return
                checkRedundantTiles(
                        checkLineInDirection(tile, row, col, Direction.UP),
                        checkLineInDirection(tile, row, col, Direction.DOWN)
                ) && checkRedundantTiles(
                        checkLineInDirection(tile, row, col, Direction.LEFT),
                        checkLineInDirection(tile, row, col, Direction.RIGHT)
                );
    }

    private List<Object> getLineInDirection(Function<Tile, Object> which, Direction d, int row, int col) {
        var nextTile = tiles[row += d.getDeltaRow()][col += d.getDeltaCol()];
        ArrayList<Object> resultList = new ArrayList<>();
        if (nextTile == null) {
            return new ArrayList<>();
        }
        while (nextTile != null) {
            resultList.add(which.apply(nextTile));
            nextTile = tiles[row += d.getDeltaRow()][col += d.getDeltaCol()];
        }
        return resultList;
    }

    private List<Object> checkLineInDirection(Tile tile, int row, int col, Direction d) {
        List<Object> list = new ArrayList<>();
        var tileInD = tiles[row + d.getDeltaRow()][col + d.getDeltaCol()];
        if (tileInD != null && tile.color() == tileInD.color()) {
            list.addAll(getLineInDirection(Tile::shape, d, row, col));
        } else if (tileInD != null && tile.shape() == tileInD.shape()) {
            list.addAll(getLineInDirection(Tile::color, d, row, col));
        } else if (tileInD == null) {
            return list;
        } else {
            throw new QwirkleException("Tile can't be placed");
        }
        return list;
    }

    private boolean checkRedundantTiles(List<Object> tilesInDir1, List<Object> tilesInDir2) {
        var list = Stream.concat(tilesInDir1.stream(), tilesInDir2.stream()).toList();
        var distinctSet = new HashSet<>(list);
        return list.size() == distinctSet.size();
    }

    private boolean surroundingsAreNull(int row, int col) {
        return get(row - 1, col) == null &&
                get(row, col + 1) == null &&
                get(row + 1, col) == null &&
                get(row, col - 1) == null;
    }

    /**
     * Returns the tile located at the specified row and column in the grid.
     *
     * @param row the row index of the tile
     * @param col the column index of the tile
     * @return the tile at the specified row and column
     */
    public Tile get(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Returns a boolean value indicating whether the Grid is empty or not.
     *
     * @return true if the game board is empty; false otherwise
     */
    public boolean isEmpty() {
        return isEmpty;
    }
}
