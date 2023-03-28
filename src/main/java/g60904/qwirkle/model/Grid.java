package g60904.qwirkle.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
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
        if (checkNeighboringLines(tile, row, col)) {
            tiles[row][col] = tile;
        } else {
            throw new QwirkleException("Exception not handled - Grid:72");
        }
    }

    private boolean checkNeighboringLines(Tile tile, int row, int col) {
        var tileUp = get(row - 1, col);
        var tileRight = get(row, col + 1);
        var tileDown = get(row + 1, col);
        var tileLeft = get(row, col - 1);
        if (tileUp == null && tileRight == null && tileDown == null && tileLeft == null) {
            throw new QwirkleException("The Tile cannot be placed where there is none");
        } else return checkLineVert(tile, row, col) && checkLineHor(tile, row, col);
    }

    private List<Object> getLine(Function<Tile, Object> which, Direction direction, int row, int col) {
        var nextTile = tiles[row += direction.getDeltaRow()][col += direction.getDeltaCol()];
        ArrayList<Object> resultList = new ArrayList<>();
        if (nextTile == null) {
            return new ArrayList<>();
        }
        while (nextTile != null) {
            resultList.add(which.apply(nextTile));
            nextTile = tiles[row += direction.getDeltaRow()][col += direction.getDeltaCol()];
        }
        return resultList;
    }

    private static Color color(Tile tile) {
        return tile.color();
    }

    private List<Color> getLine_Color(Direction direction, int row, int col) {
        var nextTile = tiles[row += direction.getDeltaRow()][col += direction.getDeltaCol()];
        var resultList = new ArrayList<Color>();
        if (nextTile == null) {
            return new ArrayList<>();
        }
        while (nextTile != null) {
            resultList.add(nextTile.color());
            nextTile = tiles[row += direction.getDeltaRow()][col += direction.getDeltaCol()];
        }
        return resultList;
    }

    private List<Shape> getLine_Shape(Direction direction, int row, int col) {
        var nextTile = tiles[row += direction.getDeltaRow()][col += direction.getDeltaCol()];
        var resultList = new ArrayList<Shape>();
        if (nextTile == null) {
            return new ArrayList<>();
        }
        while (nextTile != null) {
            resultList.add(nextTile.shape());
            nextTile = tiles[row += direction.getDeltaRow()][col += direction.getDeltaCol()];
        }
        return resultList;
    }

    private boolean checkLineVert(Tile tile, int row, int col) {
        var tileUp = tiles[row + Direction.UP.getDeltaRow()][col + Direction.UP.getDeltaCol()];
        var tileDown = tiles[row + Direction.DOWN.getDeltaRow()][col + Direction.UP.getDeltaCol()];
        if (tileUp == null && tileDown == null) {
            return true;
        }
        if ((tileUp != null && tile.color() == tileUp.color()) ||
                (tileDown != null && tile.color() == tileDown.color())) {
            List<Object> shapeListUp = new ArrayList<>();
            List<Object> shapeListDown = new ArrayList<>();
            if (tileUp != null && tile.color() == tileUp.color()) {
                shapeListUp = getLine(Tile::shape, Direction.UP, row, col);
            }
            if (tileDown != null && tile.color() == tileDown.color()) {
                shapeListDown = getLine(Tile::shape, Direction.DOWN, row, col);
            }
            var colorList = Stream.concat(shapeListUp.stream(), shapeListDown.stream()).toList();
            var distinctSet = new HashSet<>(colorList);
            return colorList.size() == distinctSet.size();
        } else if ((tileUp != null && tile.shape() == tileUp.shape()) ||
                (tileDown != null && tile.shape() == tileDown.shape())) {
            List<Object> colorListUp = new ArrayList<>();
            List<Object> colorListDown = new ArrayList<>();
            if (tileUp != null && tile.shape() == tileUp.shape()) {
                colorListUp = getLine(Tile::color, Direction.UP, row, col);
            }
            if (tileDown != null && tile.color() == tileDown.color()) {
                colorListDown = getLine(Tile::color, Direction.DOWN, row, col);
            }
            var colorList = Stream.concat(colorListUp.stream(), colorListDown.stream()).toList();
            var distinctSet = new HashSet<>(colorList);
            return colorList.size() == distinctSet.size();
        } else {
            return false;
        }
    }

    private boolean checkLineHor(Tile tile, int row, int col) {
        var tileLeft = tiles[row + Direction.LEFT.getDeltaRow()][col + Direction.LEFT.getDeltaCol()];
        var tileRight = tiles[row + Direction.RIGHT.getDeltaRow()][col + Direction.RIGHT.getDeltaCol()];
        if (tileLeft == null && tileRight == null) {
            return true;
        }
        if ((tileLeft != null && tile.color() == tileLeft.color()) ||
                (tileRight != null && tile.color() == tileRight.color())) {
            List<Object> shapeListLeft = new ArrayList<>();
            List<Object> ShapeListRight = new ArrayList<>();
            if (tileLeft != null && tile.color() == tileLeft.color()) {
                shapeListLeft = getLine(Tile::shape, Direction.LEFT, row, col);
            }
            if (tileRight != null && tile.color() == tileRight.color()) {
                ShapeListRight = getLine(Tile::shape, Direction.RIGHT, row, col);
            }
            var colorList = Stream.concat(shapeListLeft.stream(), ShapeListRight.stream()).toList();
            var distinctSet = new HashSet<>(colorList);
            return colorList.size() == distinctSet.size();
        } else if ((tileLeft != null && tile.shape() == tileLeft.shape()) ||
                (tileRight != null && tile.shape() == tileRight.shape())) {
            List<Object> colorListLeft = new ArrayList<>();
            List<Object> colorListRight = new ArrayList<>();
            if (tileLeft != null && tile.shape() == tileLeft.shape()) {
                colorListLeft = getLine(Tile::color, Direction.LEFT, row, col);
            }
            if (tileRight != null && tile.color() == tileRight.color()) {
                colorListRight = getLine(Tile::color, Direction.RIGHT, row, col);
            }
            var colorList = Stream.concat(colorListLeft.stream(), colorListRight.stream()).toList();
            var distinctSet = new HashSet<>(colorList);
            return colorList.size() == distinctSet.size();
        } else {
            return false;
        }
    }

    private Color getColor(int row, int col) {
        try {
            return tiles[row][col].color();
        } catch (NullPointerException e) {
            return null;
        }
    }

    private Shape getShape(int row, int col) {
        try {
            return tiles[row][col].shape();
        } catch (NullPointerException e) {
            return null;
        }
    }

    private boolean checkShape(Shape shape, int row, int col) {
        return false;
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
