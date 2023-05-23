package g60904.qwirkle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Grid class represents the game board of Qwirkle. It contains a 2D array of Tiles
 * which represents the board tiles and a boolean flag indicating if the grid is empty.
 * It contains an actualLimits attribute, which is an array of 4 integers representing
 * the size limits used in the game board. The first element (index 0) indicates the maximum row,
 * the second element (index 1) indicates the minimum col, the third element (index 2) indicates
 * the min row, and the fourth element (index 3) indicates the maximum column.
 */
public class Grid implements Serializable {
    private final Tile[][] tiles;
    private boolean isEmpty;
    private final int[] actualLimits;
    private final int GRID_SIZE = 91;

    /**
     * Constructs a new Grid instance with a 91x91 2D array of Tiles and initializes
     * the isEmpty flag to true.
     */
    public Grid() {
        tiles = new Tile[GRID_SIZE][GRID_SIZE];
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
    public int firstAdd(Direction d, Tile... line) throws QwirkleException {
        if (!isEmpty) {
            throw new QwirkleException("This method can only be called when the Grid is empty.");
        }
        if (line.length > 1) {
            addTiles(45, 45, d, line);
            if (!moveRespectRules(45, 45)) {
                removeTiles(45, 45, d, line.length);
                throw new QwirkleException("Tiles doesn't respect rules, no tile have been placed");
            }
        } else {
            addTile(45, 45, line[0]);
        }
        modifyLimits(d, line.length, 45, 45);
        isEmpty = false;
        return calculatePoint(45, 45);
    }
    /**
     * Checks if a Tile can be added to the Grid at the specified position.
     *
     * @param row The row index of the position.
     * @param col The column index of the position.
     * @return {@code true} if the Tile can be added at the specified position, {@code false} otherwise.
     */
    public boolean canAdd(int row, int col, Tile tile) {
        if (tiles[row][col] == null) {
            addTile(row, col, tile);
            if (moveRespectRules(row, col)) {
                removeTile(row, col);
                return true;
            } else {
                removeTile(row, col);
                return false;
            }
        } else {
            return false;
        }
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
    public int add(int row, int col, Tile tile) throws QwirkleException {
        if (isEmpty()) {
            throw new QwirkleException("You need to use first add to do the first move");
        }
        if (tiles[row][col] != null) {
            throw new QwirkleException("This position (" + row + ", " + col + ") already contain a tile");
        }
        addTile(row, col, tile);
        if (!moveRespectRules(row, col)) {
            removeTile(row, col);
            throw new QwirkleException("Tiles doesn't respect rules, no tile have been placed");
        }
        modifyLimits(row, col);
        return calculatePoint(row, col);
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
    public int add(int row, int col, Direction d, Tile... line) throws QwirkleException {
        if (isEmpty()) {
            throw new QwirkleException("You need to use first add to do the first move");
        }
        addTiles(row, col, d, line);
        if (!moveRespectRules(row, col, d, line.length)) {
            removeTiles(row, col, d, line.length);
            throw new QwirkleException("Tiles doesn't respect rules, no tile have been placed");
        }
        modifyLimits(d, line.length, row, col);
        return calculatedPoint(row, col, d, line.length);
    }

    /**
     * Adds a line of tiles to the game board, where each tile is specified by a TileAtPosition object.
     * The tiles are added in the order specified by the input array.
     *
     * @param line an array of TileAtPosition objects representing the line of tiles to be added.
     * @throws QwirkleException if a tile cannot be added to the specified position on the game board.
     */

    public int add(TileAtPosition... line) throws QwirkleException {
        if (isEmpty()) {
            throw new QwirkleException("You need to use first add to do the first move");
        }
        for (TileAtPosition tileAtPosition : line) {
            addTile(tileAtPosition.row(), tileAtPosition.col(), tileAtPosition.tile());
        }
        if (!moveRespectRulesTAP(line)) {
            for (TileAtPosition tileAtPosition : line) {
                removeTile(tileAtPosition.row(), tileAtPosition.col());
            }
            throw new QwirkleException("Tiles doesn't respect rules, no tile have been placed");
        }
        for (TileAtPosition tileAtPosition : line) {
            modifyLimits(tileAtPosition.row(), tileAtPosition.col());
        }
        return calculatedPoint(line);
    }

    /**
     * Returns the tile located at the specified row and column in the grid.
     *
     * @param row the row index of the tile
     * @param col the column index of the tile
     * @return the tile at the specified row and column
     */
    public Tile get(int row, int col) {
        if (row >= GRID_SIZE || col >= GRID_SIZE || row <= 0 || col <= 0) {
            return null;
        }
        return tiles[row][col];
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
     * the size limits used in the game board. The first element (index 0) indicates the maximum row,
     * the second element (index 1) indicates the minimum column, the third element (index 2) indicates
     * the min row, and the fourth element (index 3) indicates the maximum column.
     *
     * @return an int array containing the actualLimits of the grid.
     */
    public int[] getActualLimits() {
        return actualLimits;
    }

    public int getGRID_SIZE() {
        return GRID_SIZE;
    }

    /**
     * Adds a tile to the specified position in the game board.
     *
     * @param row  The row index of the position.
     * @param col  The column index of the position.
     * @param tile The tile to be added.
     * @throws QwirkleException if the position already contains a tile.
     */
    private void addTile(int row, int col, Tile tile) throws QwirkleException {
        if (tiles[row][col] != null) {
            throw new QwirkleException("The position (" + row + ", " + col + ") " + "already contain a tile");
        }

        tiles[row][col] = tile;

    }

    /**
     * Adds multiple tiles in a specified direction starting from the given position.
     *
     * @param row   The starting row index of the position.
     * @param col   The starting column index of the position.
     * @param d     The direction in which the tiles are added.
     * @param tiles The tiles to be added.
     * @throws QwirkleException if the position already contains a tile.
     */
    private void addTiles(int row, int col, Direction d, Tile... tiles) throws QwirkleException {
        var numberOfTilePlaced = 0;
        try {
            for (Tile tile : tiles) {
                addTile(row + numberOfTilePlaced * d.getDeltaRow(), col + numberOfTilePlaced * d.getDeltaCol(), tile);
                numberOfTilePlaced++;
            }
        } catch (QwirkleException e) {
            removeTiles(row, col, d, numberOfTilePlaced);
            throw new QwirkleException(e.getMessage());
        }
    }

    /**
     * Removes the tile at the specified position from the game board.
     *
     * @param row The row index of the position.
     * @param col The column index of the position.
     */
    private void removeTile(int row, int col) {
        tiles[row][col] = null;
    }

    /**
     * Removes multiple tiles in a specified direction starting from the given position.
     *
     * @param row            The starting row index of the position.
     * @param col            The starting column index of the position.
     * @param d              The direction in which the tiles are removed.
     * @param numberToRemove The number of tiles to be removed.
     */
    private void removeTiles(int row, int col, Direction d, int numberToRemove) {
        for (int i = 0; i < numberToRemove; i++) {
            removeTile(row + i * d.getDeltaRow(), col + i * d.getDeltaCol());
        }
    }

    /**
     * Checks if placing a tile at the specified position respects the game rules.
     *
     * @param row The row index of the position.
     * @param col The column index of the position.
     * @return {@code true} if the tile placement respects the rules, {@code false} otherwise.
     */
    private boolean moveRespectRules(int row, int col) {
        var rowOfTiles = getRowFromMove(row, col);
        var colOfTiles = getColFromMove(row, col);
        if (!notTheSameTile(rowOfTiles) || !notTheSameTile(colOfTiles)) {
            return false;
        }
        if (!shareACaract(rowOfTiles) || !shareACaract(colOfTiles)) {
            return false;
        }
        if (rowOfTiles.size() == 1 && colOfTiles.size() == 1) {
            return false;
        }
        return true;
    }

    /**
     * Checks if moving a tile to a specific position respects the rules.
     *
     * @param row           The row position to move the tile.
     * @param col           The column position to move the tile.
     * @param d             The direction in which the tile is being moved.
     * @param numberOfTiles The number of tiles being moved.
     * @return {@code true} if moving the tile respects the rules, {@code false} otherwise.
     */
    private boolean moveRespectRules(int row, int col, Direction d, int numberOfTiles) {
        return moveRespectRules(row, col) && isAttachedToExistingTile(row, col, d, numberOfTiles);
    }

    /**
     * Checks if a tile is attached to an existing tile in a specific direction.
     *
     * @param row           The row position of the tile.
     * @param col           The column position of the tile.
     * @param d             The direction in which to check attachment.
     * @param numberOfTiles The number of tiles to check attachment.
     * @return {@code true} if the tile is attached to an existing tile, {@code false} otherwise.
     */
    private boolean isAttachedToExistingTile(int row, int col, Direction d, int numberOfTiles) {
        var accoladeTiles = new HashSet<TileAtPosition>();
        for (int i = 0; i < numberOfTiles; i++) {
            for (Direction direction : Direction.values()) {
                accoladeTiles.addAll(getTilesAtPosInDirection(row, col, direction, 0));
            }
            row += d.getDeltaRow();
            col += d.getDeltaCol();
        }
        return accoladeTiles.size() != numberOfTiles;
    }

    /**
     * Checks if all the tiles in the given positions can be placed respecting the rules.
     *
     * @param tilesAtPos TilesAtPosition need to be checked.
     * @return {@code true} if all tiles can be placed respecting the rules, {@code false} otherwise.
     */
    private boolean moveRespectRulesTAP(TileAtPosition... tilesAtPos) {
        for (TileAtPosition tileAtPos : tilesAtPos) {
            if (!moveRespectRules(tileAtPos.row(), tileAtPos.col())) {
                return false;
            }
        }
        if (!isOnSameLine(tilesAtPos)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if all the tiles are on the same line.
     *
     * @param tilesAtPosition TilesAtPosition need to be checked.
     * @return {@code true} if all tiles are on the same line, {@code false} otherwise.
     */
    private boolean isOnSameLine(TileAtPosition... tilesAtPosition) {
        if (tilesAtPosition.length == 1) {
            return true;
        }
        var d = getDirOfTilesAtPos(tilesAtPosition);
        if (d == null) {
            return false;
        }
        var tiles = getTilesAtPosInDirection(tilesAtPosition[0].row(), tilesAtPosition[0].col(), d, 0);
        for (TileAtPosition tileAtPosition : tilesAtPosition) {
            if (!tiles.contains(tileAtPosition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the direction of the tiles at the given positions.
     *
     * @param tilesAtPosition An array of TileAtPosition objects representing the tiles and their positions.
     * @return The direction of the tiles, or {@code null} if the tiles are not in a straight line.
     */
    private Direction getDirOfTilesAtPos(TileAtPosition... tilesAtPosition) {
        var rowOfFirst = tilesAtPosition[0].row();
        var rowOfSecond = tilesAtPosition[1].row();
        var colOfFirst = tilesAtPosition[0].col();
        var colOfSecond = tilesAtPosition[1].col();
        if (rowOfFirst == rowOfSecond) {
            if (colOfFirst > colOfSecond) {
                return Direction.LEFT;
            } else {
                return Direction.RIGHT;
            }
        } else if (colOfFirst == colOfSecond) {
            if (rowOfFirst > rowOfSecond) {
                return Direction.UP;
            } else {
                return Direction.DOWN;
            }
        }
        return null;
    }

    /**
     * Retrieves the TileAtPosition object at the specified position.
     *
     * @param row The row position of the tile.
     * @param col The column position of the tile.
     * @return The TileAtPosition object at the specified position.
     */
    private TileAtPosition getTileAtPos(int row, int col) {
        return new TileAtPosition(row, col, get(row, col));
    }

    /**
     * Checks if the given list of tiles share a common shape or color.
     *
     * @param tiles The list of tiles to check.
     * @return {@code true} if the tiles share a common shape or color, {@code false} otherwise.
     */
    private boolean shareACaract(ArrayList<Tile> tiles) {
        if (tiles.size() == 1) {
            return true;
        }
        HashSet<Shape> shapes = (HashSet<Shape>) tiles.stream().map(Tile::shape).collect(Collectors.toSet());
        HashSet<Color> colors = (HashSet<Color>) tiles.stream().map(Tile::color).collect(Collectors.toSet());
        return shapes.size() == 1 ^ colors.size() == 1;
    }

    /**
     * Checks if the given list of tiles contains duplicates.
     *
     * @param tiles The list of tiles to check.
     * @return {@code true} if the list contains duplicates, {@code false} otherwise.
     */
    private boolean notTheSameTile(ArrayList<Tile> tiles) {
        var tilesHashSet = new HashSet<Tile>(tiles);
        return tilesHashSet.size() == tiles.size();
    }

    /**
     * Retrieves the column of tiles from the specified position.
     *
     * @param row The row position of the tile.
     * @param col The column position of the tile.
     * @return The list of tiles in the column.
     */
    private ArrayList<Tile> getColFromMove(int row, int col) {
        var listOfTiles = getTilesInDirection(row, col, Direction.UP, 1);
        listOfTiles.addAll(getTilesInDirection(row, col, Direction.DOWN, 1));
        listOfTiles.add(get(row, col));
        return listOfTiles;
    }

    /**
     * Retrieves the row of tiles from the specified position.
     *
     * @param row The row position of the tile.
     * @param col The column position of the tile.
     * @return The list of tiles in the row.
     */
    private ArrayList<Tile> getRowFromMove(int row, int col) {
        var listOfTiles = getTilesInDirection(row, col, Direction.LEFT, 1);
        listOfTiles.addAll(getTilesInDirection(row, col, Direction.RIGHT, 1));
        listOfTiles.add(get(row, col));
        return listOfTiles;
    }

    /**
     * Retrieves the tiles in the specified direction from the given position.
     *
     * @param row    The row position of the tile.
     * @param col    The column position of the tile.
     * @param d      The direction to retrieve the tiles.
     * @param offSet The offset from the initial position.
     * @return The list of tiles in the specified direction.
     */
    private ArrayList<Tile> getTilesInDirection(int row, int col, Direction d, int offSet) {
        var listOfTiles = new ArrayList<Tile>();
        Tile nextTile;
        var numberOfTileFound = offSet;
        do {
            nextTile = get(row + numberOfTileFound * d.getDeltaRow(), col + numberOfTileFound * d.getDeltaCol());
            if (nextTile != null) {
                listOfTiles.add(nextTile);
            }
            numberOfTileFound++;
        } while (nextTile != null);
        return listOfTiles;
    }

    /**
     * Retrieves the tiles at the specified position in the given direction.
     *
     * @param row    The row position of the tile.
     * @param col    The column position of the tile.
     * @param d      The direction to retrieve the tiles.
     * @param offSet The offset from the initial position.
     * @return The list of tiles at the specified position in the specified direction.
     */
    private ArrayList<TileAtPosition> getTilesAtPosInDirection(int row, int col, Direction d, int offSet) {
        var listOfTiles = new ArrayList<TileAtPosition>();
        TileAtPosition nextTile;
        var numberOfTileFound = offSet;
        do {
            nextTile = getTileAtPos(row + numberOfTileFound * d.getDeltaRow(),
                    col + numberOfTileFound * d.getDeltaCol());
            if (nextTile.tile() != null) {
                listOfTiles.add(nextTile);
            }
            numberOfTileFound++;
        } while (nextTile.tile() != null);
        return listOfTiles;
    }

    /**
     * Calculates the total points earned at the specified position.
     *
     * @param row The row position of the tile.
     * @param col The column position of the tile.
     * @return The total points earned at the specified position.
     */
    private int calculatePoint(int row, int col) {
        var accoladeTiles = new HashSet<TileAtPosition>();
        var QwirklePoints = 0;
        for (Direction direction : Direction.values()) {
            accoladeTiles.addAll(getTilesAtPosInDirection(row, col, direction, 0));
            if (getTilesInDirection(row, col, direction, 0).size() == 7) {
                QwirklePoints += 7;
            }
        }
        return accoladeTiles.size() + QwirklePoints + additionalPointsCausedByTile(row, col);
    }

    /**
     * Calculates the total points earned when placing tiles in a specific direction.
     *
     * @param row                 The row position of the first tile.
     * @param col                 The column position of the first tile.
     * @param d                   The direction in which the tiles are placed.
     * @param numberOfTilesPlaced The number of tiles placed.
     * @return The total points earned for the placement.
     */
    private int calculatedPoint(int row, int col, Direction d, int numberOfTilesPlaced) {
        var accoladeTiles = new HashSet<TileAtPosition>();
        var actualRow = row;
        var actualCol = col;
        var QwirklePoints = 0;
        for (int i = 0; i < numberOfTilesPlaced; i++) {
            for (Direction direction : Direction.values()) {
                accoladeTiles.addAll(getTilesAtPosInDirection(actualRow, actualCol, direction, 0));
            }
            actualRow += d.getDeltaRow();
            actualCol += d.getDeltaCol();
        }
        for (Direction direction : Direction.values()) {
            if (getTilesInDirection(row, col, direction, 0).size() == 7) {
                QwirklePoints += 7;
            }
        }
        return accoladeTiles.size() + QwirklePoints + additionalPointsCausedByTiles(row, col, d, numberOfTilesPlaced);
    }

    /**
     * Calculates the total points earned for a given set of tiles.
     *
     * @param tiles The tiles for which to calculate the points.
     * @return The total points earned for the tiles.
     */
    private int calculatedPoint(TileAtPosition... tiles) {
        var accoladeTiles = new HashSet<TileAtPosition>();
        var additionalPoints = 0;
        var QwirklePoints = 0;
        for (TileAtPosition tile : tiles) {
            for (Direction direction : Direction.values()) {
                accoladeTiles.addAll(getTilesAtPosInDirection(tile.row(), tile.col(), direction, 0));
            }
            additionalPoints += additionalPointsCausedByTile(tile.row(), tile.col());
            for (Direction direction : Direction.values()) {
                if (getTilesInDirection(tile.row(), tile.col(), direction, 0).size() == 7) {
                    QwirklePoints += 7;
                }
            }
        }
        return accoladeTiles.size() + QwirklePoints + additionalPoints;
    }

    /**
     * Calculates additional points caused by placing a tile at the specified position.
     *
     * @param row The row position of the tile.
     * @param col The column position of the tile.
     * @return The additional points caused by the tile placement.
     */
    private int additionalPointsCausedByTile(int row, int col) {
        var listOfDirectionsNearbyTile = getDirectionsNearbyATile(row, col);
        if ((listOfDirectionsNearbyTile.contains(Direction.UP) ||
                listOfDirectionsNearbyTile.contains(Direction.DOWN)) &&
                (listOfDirectionsNearbyTile.contains(Direction.LEFT) ||
                        listOfDirectionsNearbyTile.contains(Direction.RIGHT))) {
            return 1;
        }
        return 0;
    }

    /**
     * Calculates additional points caused by placing multiple tiles in a specific direction.
     *
     * @param row           The row position of the first tile.
     * @param col           The column position of the first tile.
     * @param d             The direction in which the tiles are placed.
     * @param numberOfTiles The number of tiles placed.
     * @return The additional points caused by the tile placements.
     */
    private int additionalPointsCausedByTiles(int row, int col, Direction d, int numberOfTiles) {
        var additionalPoints = 0;
        for (int i = 0; i < numberOfTiles; i++) {
            additionalPoints += numberOfNonOppositeDirectionsNearbyATile(row + i * d.getDeltaRow(), col + i * d.getDeltaCol());
        }
        return additionalPoints;
    }

    /**
     * Calculates the number of non-opposite directions nearby a tile at the specified position.
     *
     * @param row The row position of the tile.
     * @param col The column position of the tile.
     * @return The number of non-opposite directions nearby the tile.
     */
    private int numberOfNonOppositeDirectionsNearbyATile(int row, int col) {
        var listOfDirectionsNearbyTile = getDirectionsNearbyATile(row, col);
        if ((listOfDirectionsNearbyTile.contains(Direction.UP) ||
                listOfDirectionsNearbyTile.contains(Direction.DOWN)) &&
                (listOfDirectionsNearbyTile.contains(Direction.LEFT) ||
                        listOfDirectionsNearbyTile.contains(Direction.RIGHT))) {
            return 1;
        }
        return 0;
    }

    /**
     * Retrieves the list of directions of neighboring tiles near a specified position.
     *
     * @param row The row position of the tile.
     * @param col The column position of the tile.
     * @return The list of directions of neighboring tiles.
     */
    private List<Direction> getDirectionsNearbyATile(int row, int col) {
        var listOfDirections = new ArrayList<Direction>();
        for (Direction direction : Direction.values()) {
            if (get(row + direction.getDeltaRow(), col + direction.getDeltaCol()) != null) {
                listOfDirections.add(direction);
            }
        }
        return listOfDirections;
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
        modifyLimits(row, col);
        switch (d) {
            case UP -> actualLimits[2] = Math.min(actualLimits[2], row - numberPlaced);
            case DOWN -> actualLimits[0] = Math.max(actualLimits[0], row + numberPlaced);
            case LEFT -> actualLimits[1] = Math.min(actualLimits[1], col - numberPlaced);
            case RIGHT -> actualLimits[3] = Math.max(actualLimits[3], col + numberPlaced);
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
