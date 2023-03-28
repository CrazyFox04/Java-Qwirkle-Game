package g60904.qwirkle.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {
    private Grid myGrid;

    @BeforeEach
    void setUp() {
        myGrid = new Grid();
    }

    /**
     * Add some Tile for the first time in a game.
     * It adds a Blue Plus Tile at (45, 45),
     * a Blue Cross Tile at (45, 44),
     * a Blue Diamond Tile at (45, 43)
     */
    private void addSomeTiles_FirstAdd() {
        myGrid.firstAdd(
                Direction.LEFT,
                new Tile(Color.BLUE, Shape.PLUS),
                new Tile(Color.BLUE, Shape.CROSS),
                new Tile(Color.BLUE, Shape.DIAMOND)
        );
    }

    @Test
    void firstAddTest_oneTileOK() {
        myGrid.firstAdd(
                Direction.LEFT,
                new Tile(Color.BLUE, Shape.PLUS)
        );
        assertEquals(myGrid.get(45, 45), new Tile(Color.BLUE, Shape.PLUS));
    }

    @Test
    void firstAddTest_MoreTilesOK() {
        addSomeTiles_FirstAdd();
        assertEquals(myGrid.get(45, 45), new Tile(Color.BLUE, Shape.PLUS));
        assertEquals(myGrid.get(45, 44), new Tile(Color.BLUE, Shape.CROSS));
        assertEquals(myGrid.get(45, 43), new Tile(Color.BLUE, Shape.DIAMOND));
    }

    @Test
    void firstAddTest_DifferentColorException() {
        assertThrows(
                QwirkleException.class, () -> myGrid.firstAdd(
                        Direction.LEFT,
                        new Tile(Color.BLUE, Shape.PLUS),
                        new Tile(Color.GREEN, Shape.CROSS),
                        new Tile(Color.BLUE, Shape.DIAMOND)
                )
        );
    }

    @Test
    void firstAddTest_SameShapeException() {
        assertThrows(
                QwirkleException.class, () -> myGrid.firstAdd(
                        Direction.LEFT,
                        new Tile(Color.BLUE, Shape.PLUS),
                        new Tile(Color.BLUE, Shape.DIAMOND),
                        new Tile(Color.BLUE, Shape.DIAMOND)
                )
        );
    }

    @Test
    void firstAddTest_DifferentColorSameShapeException() {
        assertThrows(
                QwirkleException.class, () -> myGrid.firstAdd(
                        Direction.LEFT,
                        new Tile(Color.GREEN, Shape.PLUS),
                        new Tile(Color.BLUE, Shape.DIAMOND),
                        new Tile(Color.BLUE, Shape.DIAMOND)
                )
        );
    }

    @Test
    void addTest_DownATile_SameColor() {
        addSomeTiles_FirstAdd();
        myGrid.add(46, 44, new Tile(Color.BLUE, Shape.ROUND));
        assertEquals(myGrid.get(46, 44), new Tile(Color.BLUE, Shape.ROUND));
    }

    @Test
    void addTest_UpATile_SameColor() {
        addSomeTiles_FirstAdd();
        myGrid.add(44, 44, new Tile(Color.BLUE, Shape.ROUND));
        assertEquals(myGrid.get(44, 44), new Tile(Color.BLUE, Shape.ROUND));
    }

    @Test
    void addTest_LeftATile_SameColor() {
        addSomeTiles_FirstAdd();
        myGrid.add(45, 42, new Tile(Color.BLUE, Shape.ROUND));
        assertEquals(myGrid.get(45, 42), new Tile(Color.BLUE, Shape.ROUND));
    }

    @Test
    void addTest_RightATile_SameColor() {
        addSomeTiles_FirstAdd();
        myGrid.add(45, 46, new Tile(Color.BLUE, Shape.ROUND));
        assertEquals(myGrid.get(45, 46), new Tile(Color.BLUE, Shape.ROUND));
    }

    @Test
    void addTest_NoNeighboursTile() {
        assertThrows(QwirkleException.class, () -> myGrid.add(
                6, 8, new Tile(Color.ORANGE, Shape.STAR)
        ));
    }

    @Test
    void addTest_NotSameColorAndShape() {
        addSomeTiles_FirstAdd();
        assertThrows(QwirkleException.class, () -> myGrid.add(
                45, 42, new Tile(Color.GREEN, Shape.STAR)
        ));
    }

    @Test
    void addTest_UpATile_SameShape() {
        addSomeTiles_FirstAdd();
        myGrid.add(44, 45, new Tile(Color.GREEN, Shape.PLUS));
        assertEquals(myGrid.get(44, 45), new Tile(Color.GREEN, Shape.PLUS));
    }

    @Test
    void addTest_DownATile_SameShape() {
        addSomeTiles_FirstAdd();
        myGrid.add(46, 45, new Tile(Color.GREEN, Shape.PLUS));
        assertEquals(myGrid.get(46, 45), new Tile(Color.GREEN, Shape.PLUS));
    }

    @Test
    void addTest_LeftATile_SameShape() {
        addSomeTiles_FirstAdd();
        myGrid.add(45, 42, new Tile(Color.GREEN, Shape.DIAMOND));
        assertEquals(myGrid.get(45, 42), new Tile(Color.GREEN, Shape.DIAMOND));
    }

    @Test
    void addTest_RightATile_SameShape() {
        addSomeTiles_FirstAdd();
        assertThrows(QwirkleException.class, () -> myGrid.add(45, 46, new Tile(Color.GREEN, Shape.PLUS)));
    }

    @Test
    void addTest_TileBetweenTwoTiles_JoinTwoTiles() {
        addSomeTiles_FirstAdd();
        myGrid.add(46, 43, new Tile(Color.BLUE, Shape.STAR));
        myGrid.add(47, 43, new Tile(Color.BLUE, Shape.PLUS));
        myGrid.add(47, 44, new Tile(Color.BLUE, Shape.STAR));
        myGrid.add(47, 45, new Tile(Color.BLUE, Shape.ROUND));
        myGrid.add(46, 45, new Tile(Color.BLUE, Shape.SQUARE));
        assertEquals(myGrid.get(46, 45), new Tile(Color.BLUE, Shape.SQUARE));
    }

    @Test
    void addTest_TileBetweenTwoTiles_() {
        addSomeTiles_FirstAdd();
        myGrid.add(46, 43, new Tile(Color.BLUE, Shape.STAR));
        myGrid.add(47, 43, new Tile(Color.BLUE, Shape.CROSS));
        myGrid.add(48, 43, new Tile(Color.BLUE, Shape.SQUARE));
        myGrid.add(48, 44, new Tile(Color.BLUE, Shape.STAR));
        myGrid.add(48, 45, new Tile(Color.BLUE, Shape.ROUND));
        myGrid.add(49, 45, new Tile(Color.BLUE, Shape.PLUS));
        myGrid.add(47, 45, new Tile(Color.BLUE, Shape.SQUARE));

        assertThrows(QwirkleException.class, () -> myGrid.add(
                46, 45, new Tile(Color.BLUE, Shape.CROSS)
        ));
    }
}