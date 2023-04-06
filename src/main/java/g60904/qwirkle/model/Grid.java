package g60904.qwirkle.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The Grid class represents the game board of Qwirkle. It contains a 2D array of Tiles
 * which represents the board tiles and a boolean flag indicating if the grid is empty.
 * It contains an actualLimits attribute, which is an array of 4 integers representing
 * the size limits used in the game board. The first element (index 0) indicates the minimum row,
 * the second element (index 1) indicates the minimum column, the third element (index 2) indicates
 * the maximum row, and the fourth element (index 3) indicates the maximum column.
 */
public class Grid {
    private final Tile[][] tiles;
    private boolean isEmpty;
    private int[] actualLimits;

    /**
     * Constructs a new Grid instance with a 91x91 2D array of Tiles and initializes
     * the isEmpty flag to true.
     */
    public Grid() {
        tiles = new Tile[91][91];
        isEmpty = true;
        actualLimits = new int[]{46, 44, 44, 46};
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
            int rowToAddTile = 45 + addCounter * d.getDeltaRow();
            int colToAddTile = 45 + addCounter * d.getDeltaCol();
            tiles[rowToAddTile][colToAddTile] = tile;
            addCounter++;
        }
        modifyLimits(d, line.length, 45, 45);
        isEmpty = false;
    }

    /**
     * Adds a tile to the specified position on the game board.
     *
     * @param row  The row index of the position where the tile is to be added.
     * @param col  The column index of the position where the tile is to be added.
     * @param tile The tile to be added to the game board.
     * @throws QwirkleException if the position already contains a tile or if the specified
     *                          position cannot accept the tile due to the neighboring tiles'
     *                          attributes not matching with the given tile.
     */
    public void add(int row, int col, Tile tile) throws QwirkleException {
        if (tiles[row][col] != null) {
            throw new QwirkleException("This position (" + row + ", " + col + ") already contain a tile");
        }
        if (checkNearbyLines(tile, row, col)) {
            tiles[row][col] = tile;
            modifyLimits(row, col);
        } else {
            throw new QwirkleException("The position (" + row + ", " + col + ") " +
                    "cannot accept the Tile (" + tile.shape() + " " + tile.color() + ").");
        }
    }

    /**
     * Adds a sequence of tiles in a line on the game board starting from a given position in a given direction.
     *
     * @param row  the row index of the starting position
     * @param col  the column index of the starting position
     * @param d    the direction in which to place the tiles
     * @param line an array of tiles to place in the line
     * @throws QwirkleException if any of the tiles cannot be placed on the board or
     *                          if a position already contains a tile
     */
    public void add(int row, int col, Direction d, Tile... line) {
        var numberOfTilesPlaced = 0;
        try {
            for (Tile tile : line) {
                add(row + numberOfTilesPlaced * d.getDeltaRow(),
                        col + numberOfTilesPlaced * d.getDeltaCol(), tile);
                numberOfTilesPlaced++;
            }
            modifyLimits(d, line.length, row, col);
        } catch (QwirkleException e) {
            remove(row, col, d, numberOfTilesPlaced);
            String lineType = (d == Direction.LEFT || d == Direction.RIGHT) ? "row" : "column";
            throw new QwirkleException("The position (" + (row + (numberOfTilesPlaced) * d.getDeltaRow())
                    + ", " + (col + (numberOfTilesPlaced) * d.getDeltaCol()) +
                    ") cannot accept the Tile (" + line[numberOfTilesPlaced].shape() + " "
                    + line[numberOfTilesPlaced].color() + ").\n All previously placed tiles in this " +
                    lineType + " have been removed.");
        }
    }


    /**
     * Adds a line of tiles to the game board, where each tile is specified by a TileAtPosition object.
     * The tiles are added in the order specified by the input array.
     *
     * @param line an array of TileAtPosition objects representing the line of tiles to be added.
     * @throws QwirkleException if a tile cannot be added to the specified position on the game board.
     */

    public void add(TileAtPosition... line) {
        int[] copyOfLimits = Arrays.copyOf(actualLimits, actualLimits.length);
        try {
            for (TileAtPosition tile : line) {
                add(tile.row(), tile.col(), tile.tile());
                modifyLimits(tile.row(), tile.col());
            }
        } catch (QwirkleException e) {
            removeTilesDueToException(line);
            actualLimits = copyOfLimits;
            throw new QwirkleException(e.getMessage());
        }
    }

    private void removeTilesDueToException(TileAtPosition[] tile) {
        for (TileAtPosition tileAtPosition : tile) {
            tiles[tileAtPosition.row()][tileAtPosition.col()] = null;
        }
    }

    /**
     * Checks if the neighboring tiles of the specified position match the attributes of the given tile
     * in all four directions: up, down, left, and right.
     *
     * @param tile The tile to be checked for attribute matching.
     * @param row  The row index of the position whose neighboring tiles are to be checked.
     * @param col  The column index of the position whose neighboring tiles are to be checked.
     * @return {@code true} if the neighboring tiles' attributes match with the given tile in all
     * four directions; {@code false} otherwise.
     * @throws QwirkleException if the position does not have any neighboring tiles.
     */
    private boolean checkNearbyLines(Tile tile, int row, int col) {
        if (surroundingsAreNull(row, col)) {
            throw new QwirkleException("The Tile (" + row + ", " + col + ") cannot be placed " +
                    "where there is no Tile yet");
        } else return
                checkRedundantTiles(
                        checkLineInDirection(tile, row, col, Direction.UP),
                        checkLineInDirection(tile, row, col, Direction.DOWN),
                        tile
                ) && checkRedundantTiles(
                        checkLineInDirection(tile, row, col, Direction.LEFT),
                        checkLineInDirection(tile, row, col, Direction.RIGHT),
                        tile
                );
    }

    /**
     * Returns a list of objects that are obtained by applying the specified function to the
     * tiles in a line in a particular direction starting from the specified position on the
     * game board.
     *
     * @param which The function that is applied to each tile in the line to obtain an object.
     * @param d     The direction in which the line is traversed.
     * @param row   The row index of the starting position.
     * @param col   The column index of the starting position.
     * @return A list of objects that are obtained by applying the specified function to the
     * tiles in a line in the specified direction starting from the specified position.
     */
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

    /**
     * Checks the tiles in a line in the specified direction for a match with the specified tile.
     * Returns a list of objects, which can either be the shapes or colors of the tiles in the line.
     *
     * @param tile the tile to match against
     * @param row  the row of the tile to match
     * @param col  the column of the tile to match
     * @param d    the direction in which to search for a matching tile
     * @return a list of objects representing either the shapes or colors of the tiles in the line
     * @throws QwirkleException if there is no matching tile in the line
     */
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
            throw new QwirkleException("In the direction '" + d + "' the Tile " +
                    "(" + tile + ") doesn't have a matching color or shape");
        }
        return list;
    }

    /**
     * Checks if there are any redundant tiles in two lists of tiles obtained by checking the tiles
     * in two directions for a match with a specified tile.
     *
     * @param tilesInDir1 the list of tiles obtained by checking in one direction
     * @param tilesInDir2 the list of tiles obtained by checking in another direction
     * @return {@code true} if there are no redundant tiles, {@code false} otherwise
     */
    private boolean checkRedundantTiles(List<Object> tilesInDir1, List<Object> tilesInDir2, Tile tile) {
        if (!tilesInDir1.isEmpty() && tilesInDir1.get(0) instanceof Shape) {
            tilesInDir1.add(tile.shape());
        } else {
            tilesInDir1.add(tile.color());
        }
        var list = Stream.concat(tilesInDir1.stream(), tilesInDir2.stream()).toList();
        var distinctSet = new HashSet<>(list);
        return list.size() == distinctSet.size();
    }

    /**
     * Checks if all the surrounding positions of a given position are null.
     *
     * @param row the row index of the position to check
     * @param col the column index of the position to check
     * @return {@code true} if all surrounding positions are null, {@code false} otherwise
     */
    private boolean surroundingsAreNull(int row, int col) {
        for (Direction d : Direction.values()) {
            if (get(row + d.getDeltaRow(), col + d.getDeltaCol()) != null) {
                return false;
            }
        }
        return true;
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

    private void remove(int row, int col, Direction d, int numberOfTilesPlaced) {
        for (int i = numberOfTilesPlaced - 1; i >= 0; i--) {
            tiles[row + i * d.getDeltaRow()][col + i * d.getDeltaCol()] = null;
        }
    }


    /**
     * Returns a boolean value indicating whether the Grid is empty or not.
     *
     * @return {@code true} if the game board is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Returns the actualLimits attribute, which is an array of 4 integers representing
     * the size limits used in the game board. The first element (index 0) indicates the minimum row,
     * the second element (index 1) indicates the minimum column, the third element (index 2) indicates
     * the maximum row, and the fourth element (index 3) indicates the maximum column.
     *
     * @return an int array containing the actualLimits of the grid.
     */
    public int[] getActualLimits() {
        return actualLimits;
    }

    /**
     * Updates the actualLimits attribute based on the placement of a Tile in a certain direction.
     * The direction can be UP, DOWN, LEFT, or RIGHT, and the numberPlaced parameter indicates
     * the number of Tiles placed in that direction. The row and col parameters indicate the
     * position of the first Tile placed in that direction.
     *
     * @param d            the direction of the placement (UP, DOWN, LEFT, or RIGHT).
     * @param numberPlaced the number of Tiles placed in the specified direction.
     * @param row          the row of the first Tile placed in the specified direction.
     * @param col          the column of the first Tile placed in the specified direction.
     */
    private void modifyLimits(Direction d, int numberPlaced, int row, int col) {
        switch (d) {
            case UP -> actualLimits[2] = Math.min(actualLimits[2], row - numberPlaced);
            case DOWN -> actualLimits[0] = Math.max(actualLimits[0], row + numberPlaced);
            case LEFT -> actualLimits[1] = Math.min(actualLimits[1], row - numberPlaced);
            case RIGHT -> actualLimits[3] = Math.max(actualLimits[0], row + numberPlaced);
        }
    }

    /**
     * Updates the actualLimits attribute based on the placement of a Tile at a specific position.
     * The row and col parameters indicate the position of the Tile placed.
     *
     * @param row the row of the Tile placed.
     * @param col the column of the Tile placed.
     */
    private void modifyLimits(int row, int col) {
        if (row >= actualLimits[0]) {
            actualLimits[0]++;
        } else if (row <= actualLimits[2]) {
            actualLimits[2]--;
        }
        if (col <= actualLimits[1]) {
            actualLimits[1]--;
        } else if (col >= actualLimits[3]) {
            actualLimits[3]++;
        }
    }
}
