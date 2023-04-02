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
        assertThrows(QwirkleException.class, () -> myGrid.add(45, 42, new Tile(Color.GREEN, Shape.DIAMOND)));
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

    // Rules Test
    @Test
    void rules_Sonia_a() {
        var t1 = new Tile(Color.RED, Shape.ROUND);
        var t2 = new Tile(Color.RED, Shape.DIAMOND);
        var t3 = new Tile(Color.RED, Shape.PLUS);
        myGrid.firstAdd(Direction.UP, t1, t2, t3);
        assertEquals(t1, myGrid.get(45, 45));
        assertEquals(t2, myGrid.get(44, 45));
        assertEquals(t3, myGrid.get(43, 45));
    }

    @Test
    void rules_sonia_a_adapted_to_fail() {
        var t1 = new Tile(Color.RED, Shape.ROUND);
        var t2 = new Tile(Color.RED, Shape.DIAMOND);
        var t3 = new Tile(Color.RED, Shape.DIAMOND);
        assertThrows(QwirkleException.class, () -> {
            myGrid.firstAdd(Direction.UP, t1, t2, t3);
        });
        assertNull(myGrid.get(45, 45));
        assertNull(myGrid.get(44, 45));
        assertNull(myGrid.get(43, 45));
    }

    @Test
    void rules_Cedric_b() {
        rules_Sonia_a();
        var t1 = new Tile(Color.RED, Shape.SQUARE);
        var t2 = new Tile(Color.BLUE, Shape.SQUARE);
        var t3 = new Tile(Color.PURPLE, Shape.SQUARE);
        myGrid.add(46, 45, Direction.RIGHT, t1, t2, t3);
        assertEquals(t1, myGrid.get(46, 45));
        assertEquals(t2, myGrid.get(46, 46));
        assertEquals(t3, myGrid.get(46, 47));
    }

    @Test
    void rules_Cedric_b_adapted_to_fail() {
        rules_Sonia_a();
        var t1 = new Tile(Color.RED, Shape.SQUARE);
        var t2 = new Tile(Color.BLUE, Shape.CROSS);
        var t3 = new Tile(Color.PURPLE, Shape.SQUARE);
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(46, 45, Direction.RIGHT, t1, t2, t3);
        });
        assertNull(myGrid.get(46, 45));
        assertNull(myGrid.get(46, 46));
        assertNull(myGrid.get(46, 47));
    }

    @Test
    void rules_Elvire_c() {
        rules_Cedric_b();
        var t1 = new Tile(Color.BLUE, Shape.ROUND);
        myGrid.add(45, 46, t1);
        assertEquals(t1, myGrid.get(45, 46));
    }

    @Test
    void rules_Vincent_d() {
        rules_Elvire_c();
        var t1 = new Tile(Color.GREEN, Shape.PLUS);
        var t2 = new Tile(Color.GREEN, Shape.DIAMOND);
        myGrid.add(43, 44, Direction.DOWN, t1, t2);
        assertEquals(t1, myGrid.get(43, 44));
        assertEquals(t2, myGrid.get(44, 44));
    }

    @Test
    void rules_Sonia_e() {
        rules_Vincent_d();
        var t1 = new TileAtPosition(42, 44, new Tile(Color.GREEN, Shape.STAR));
        var t2 = new TileAtPosition(45, 44, new Tile(Color.GREEN, Shape.ROUND));
        myGrid.add(t1, t2);
        assertEquals(t1.tile(), myGrid.get(42, 44));
        assertEquals(t2.tile(), myGrid.get(45, 44));
    }

    @Test
    void rules_Cedric_f() {
        rules_Sonia_e();
        var t1 = new Tile(Color.ORANGE, Shape.SQUARE);
        var t2 = new Tile(Color.RED, Shape.SQUARE);
        myGrid.add(46, 48, Direction.DOWN, t1, t2);
        assertEquals(t1, myGrid.get(46, 48));
        assertEquals(t2, myGrid.get(47, 48));
    }

    @Test
    void rules_Elvire_g() {
        rules_Cedric_f();
        var t1 = new Tile(Color.YELLOW, Shape.STAR);
        var t2 = new Tile(Color.ORANGE, Shape.STAR);
        myGrid.add(42, 43, Direction.LEFT, t1, t2);
    }

    @Test
    void rules_Vincent_h() {
        rules_Elvire_g();
        var t1 = new Tile(Color.ORANGE, Shape.CROSS);
        var t2 = new Tile(Color.ORANGE, Shape.DIAMOND);
        myGrid.add(43, 42, Direction.DOWN, t1, t2);
        assertEquals(t1, myGrid.get(43, 42));
        assertEquals(t2, myGrid.get(44, 42));
    }

    @Test
    void rules_Sonia_i() {
        rules_Vincent_h();
        var t1 = new Tile(Color.YELLOW, Shape.DIAMOND);
        var t2 = new Tile(Color.YELLOW, Shape.ROUND);
        myGrid.add(44, 43, Direction.DOWN, t1, t2);
        assertEquals(t1, myGrid.get(44, 43));
        assertEquals(t2, myGrid.get(45, 43));
    }

    @Test
    void rules_Cedric_j() {
        rules_Sonia_i();
        var t1 = new Tile(Color.RED, Shape.STAR);
        myGrid.add(42, 45, t1);
        assertEquals(t1, myGrid.get(42, 45));
    }

    @Test
    void rules_Elvire_k() {
        rules_Cedric_j();
        var t1 = new Tile(Color.BLUE, Shape.CROSS);
        var t2 = new Tile(Color.RED, Shape.CROSS);
        var t3 = new Tile(Color.ORANGE, Shape.CROSS);
        myGrid.add(47, 46, Direction.LEFT, t1, t2, t3);
        assertEquals(t1, myGrid.get(47, 46));
        assertEquals(t2, myGrid.get(47, 45));
        assertEquals(t3, myGrid.get(47, 44));
    }

    @Test
    void rules_Vincent_l() {
        rules_Elvire_k();
        var t1 = new Tile(Color.YELLOW, Shape.SQUARE);
        var t2 = new Tile(Color.BLUE, Shape.SQUARE);
        myGrid.add(46, 49, Direction.DOWN, t1, t2);
        assertEquals(t1, myGrid.get(46, 49));
        assertEquals(t2, myGrid.get(47, 49));
    }
}