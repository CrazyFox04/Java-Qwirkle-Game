package g60904.qwirkle.model;

/**
 * The {@code GridView} class represents the view of a {@link Grid}.
 * It provides methods to access information about the tiles on the grid.
 */
public class GridView {
    /**
     * The grid represented by this view.
     */
    private final Grid grid;

    /**
     * Constructs a new {@code GridView} object with the given {@link Grid}.
     *
     * @param grid the grid to represent.
     */
    public GridView(Grid grid) {
        this.grid = grid;
    }

    /**
     * Gets the tile at the specified row and column on the grid.
     *
     * @param row the row index of the tile.
     * @param col the column index of the tile.
     * @return the tile at the specified row and column.
     */
    public Tile get(int row, int col) {
        return grid.get(row, col);
    }

    /**
     * Checks whether the grid is empty.
     *
     * @return true if the grid is empty, false otherwise.
     */
    public boolean isEmpty() {
        return grid.isEmpty();
    }

    public int[] getGridLimits() {
        return grid.getActualLimits();
    }
}
