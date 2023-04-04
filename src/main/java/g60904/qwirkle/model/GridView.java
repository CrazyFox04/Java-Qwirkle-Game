package g60904.qwirkle.model;

public class GridView {
    private final Grid grid;

    public GridView(Grid grid) {
        this.grid = grid;
    }

    public Tile get(int row, int col) {
        return grid.get(row, col);
    }

    public boolean isEmpty() {
        return grid.isEmpty();
    }
}
