package g60904.qwirkle.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
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
    private void addSomeTiles_FirstAdd_SameColor() {
        myGrid.firstAdd(
                Direction.LEFT,
                new Tile(Color.BLUE, Shape.PLUS),
                new Tile(Color.BLUE, Shape.CROSS),
                new Tile(Color.BLUE, Shape.DIAMOND)
        );
    }

    /**
     * Add some Tile for the first time in a game.
     * It adds a Blue Plus Tile at (45, 45),
     * a Red Plus Tile at (45, 44),
     * a Green Plus Tile at (45, 43)
     */
    private void addSomeTiles_FirstAdd_SameShape() {
        myGrid.firstAdd(
                Direction.LEFT,
                new Tile(Color.BLUE, Shape.PLUS),
                new Tile(Color.RED, Shape.PLUS),
                new Tile(Color.GREEN, Shape.PLUS)
        );
    }

    // Test for constructor
    @Test
    @Tag("constructor")
    @DisplayName("Test constructor")
    void constructorTest() {
        assertTrue(myGrid.isEmpty());
        assertArrayEquals(new int[]{46, 44, 44, 46}, myGrid.getActualLimits());
        assertNull(myGrid.get(45, 45));
    }

    // Tests for firstAdd
    @Test
    @Tag("firstAdd")
    @DisplayName("Add tiles using firstAdd when the game is not empty")
    void firstAdd_NotEmpty() {
        addSomeTiles_FirstAdd_SameColor();
        assertThrows(QwirkleException.class, () ->
                myGrid.firstAdd(Direction.DOWN, new Tile(Color.BLUE, Shape.SQUARE))
        );
    }

    @Test
    @Tag("firstAdd")
    @DisplayName("Add tiles using first add and check if the limits are modified")
    void firstAdd_modifyLimits() {
        addSomeTiles_FirstAdd_SameColor();
        assertArrayEquals(new int[]{46, 42, 44, 46}, myGrid.getActualLimits());
    }

    @Test
    @Tag("firstAdd")
    @DisplayName("Add tiles using first add that cannot be placed and check if they aren't placed")
    void firstAdd_removeTile() {
        assertThrows(QwirkleException.class, () ->
                myGrid.firstAdd(
                        Direction.DOWN,
                        new Tile(Color.RED, Shape.DIAMOND),
                        new Tile(Color.BLUE, Shape.DIAMOND),
                        new Tile(Color.RED, Shape.DIAMOND)
                )
        );
        assertNull(myGrid.get(45, 45));
        assertNull(myGrid.get(46, 45));
        assertNull(myGrid.get(47, 45));
    }

    // Tests for add a tile method

    @Test
    @Tag("addATile")
    @DisplayName("Add a tile using add(r,c,T) where it contains already one")
    void add_alreadyContainsTile() {
        addSomeTiles_FirstAdd_SameColor();
        assertThrows(QwirkleException.class, () ->
                myGrid.add(45, 44, new Tile(Color.BLUE, Shape.SQUARE))
        );
    }

    @Test
    @Tag("addATile")
    @DisplayName("Add a tile using add(r,c,T) where it cannot be placed")
    void add_cannotBePlaced() {
        addSomeTiles_FirstAdd_SameColor();
        assertThrows(QwirkleException.class, () ->
                myGrid.add(30, 30, new Tile(Color.BLUE, Shape.CROSS))
        );
        assertNull(myGrid.get(30, 30));
    }

    @Test
    @Tag("addATile")
    @DisplayName("Add a tile and check if its placed")
    void add_oneTile() {
        addSomeTiles_FirstAdd_SameColor();
        var myTile = new Tile(Color.BLUE, Shape.SQUARE);
        myGrid.add(45, 46, myTile);
        assertEquals(myTile, myGrid.get(45, 46));
    }

    @Test
    @Tag("addATile")
    @DisplayName("Add a tile and check if limits are updated")
    void add_checkLimits() {
        addSomeTiles_FirstAdd_SameColor();
        myGrid.add(45, 46, new Tile(Color.BLUE, Shape.SQUARE));
        assertArrayEquals(new int[]{46, 42, 44, 47}, myGrid.getActualLimits());
    }

    // Tests for add some tiles

    @Test
    @Tag("addSomeTiles")
    @DisplayName("Add some tiles that cannot be placed")
    void addSomeTiles_isRemove_isThrown() {
        addSomeTiles_FirstAdd_SameColor();
        assertThrows(QwirkleException.class, () ->
                myGrid.add(45, 46, Direction.DOWN,
                        new Tile(Color.BLUE, Shape.SQUARE),
                        new Tile(Color.YELLOW, Shape.SQUARE),
                        new Tile(Color.PURPLE, Shape.SQUARE),
                        new Tile(Color.RED, Shape.DIAMOND)
                )
        );
        assertNull(myGrid.get(45, 46));
        assertNull(myGrid.get(46, 46));
        assertNull(myGrid.get(47, 46));
        assertNull(myGrid.get(48, 46));
    }

    @Test
    @Tag("addSomeTiles")
    @DisplayName("Add some tiles and check if limits are updated")
    void addSomeTiles_checkLimits() {
        addSomeTiles_FirstAdd_SameColor();
        myGrid.add(45, 46, Direction.UP,
                new Tile(Color.BLUE, Shape.SQUARE),
                new Tile(Color.YELLOW, Shape.SQUARE),
                new Tile(Color.PURPLE, Shape.SQUARE),
                new Tile(Color.RED, Shape.SQUARE)
        );
        assertArrayEquals(new int[]{46, 42, 41, 47}, myGrid.getActualLimits());
    }

    // Tests for add Tiles at pos

    @Test
    @Tag("addTilesAtPos")
    @DisplayName("Add tiles at pos and check if limits are updated")
    void addTilesAtPos_checkLimits() {
        addSomeTiles_FirstAdd_SameColor();
        myGrid.add(
                new TileAtPosition(45, 46, new Tile(Color.BLUE, Shape.SQUARE)),
                new TileAtPosition(45, 42, new Tile(Color.BLUE, Shape.STAR))
        );
        assertArrayEquals(new int[]{46, 41, 44, 47}, myGrid.getActualLimits());
    }

    @Test
    @Tag("addTilesAtPos")
    @DisplayName("Add tiles at pos that cannot be placed")
    void addTilesAtPos_isRemove_isThrown() {
        addSomeTiles_FirstAdd_SameColor();
        assertThrows(QwirkleException.class, () ->
                myGrid.add(
                        new TileAtPosition(45, 46, new Tile(Color.BLUE, Shape.STAR)),
                        new TileAtPosition(44, 44, new Tile(Color.RED, Shape.CROSS))
                )
        );
        assertNull(myGrid.get(45, 46));
        assertNull(myGrid.get(44, 44));
    }

    @Test
    @Tag("removeTileAtPos")
    @DisplayName("Check that tiles are removed")
    void removeTilesDueToException_isRemove() {
        addSomeTiles_FirstAdd_SameColor();

    }

    // Test getter

    @Test
    @Tag("getter")
    @DisplayName("Check getter for limits")
    void getActualLimits() {
        assertArrayEquals(new int[]{46, 44, 44, 46}, myGrid.getActualLimits());
    }

    // Tests checking nearby lines

    @Test
    @Tag("checkNearbyLines")
    @DisplayName("Check nearby lines for alone Tile")
    void checkLineInDirection_alone() {
        addSomeTiles_FirstAdd_SameColor();
        assertThrows(QwirkleException.class, () ->
                myGrid.add(21, 23, new Tile(Color.RED, Shape.DIAMOND))
        );
    }

    @Test
    @Tag("checkNearbyLines")
    @DisplayName("Check line for up a tile")
    void checkLineInDirection_up() {
        addSomeTiles_FirstAdd_SameColor();
        assertDoesNotThrow(() ->
                myGrid.add(44, 44, Direction.UP,
                        new Tile(Color.BLUE, Shape.ROUND),
                        new Tile(Color.BLUE, Shape.SQUARE)
                )
        );
    }

    @Test
    @Tag("checkNearbyLines")
    @DisplayName("Check line for left a tile")
    void checkLineInDirection_left() {
        addSomeTiles_FirstAdd_SameColor();
        assertDoesNotThrow(() ->
                myGrid.add(45, 42, Direction.LEFT,
                        new Tile(Color.BLUE, Shape.ROUND),
                        new Tile(Color.BLUE, Shape.STAR)
                )
        );
    }

    @Test
    @Tag("checkNearbyLines")
    @DisplayName("Check line for down a tile")
    void checkLineInDirection_down() {
        addSomeTiles_FirstAdd_SameColor();
        assertDoesNotThrow(() ->
                myGrid.add(46, 44, new Tile(Color.BLUE, Shape.ROUND))
        );
    }

    @Test
    @Tag("checkNearbyLines")
    @DisplayName("Check line for right a tile")
    void checkLineInDirection_right() {
        addSomeTiles_FirstAdd_SameColor();
        assertDoesNotThrow(() ->
                myGrid.add(45, 46, Direction.RIGHT,
                        new Tile(Color.BLUE, Shape.ROUND),
                        new Tile(Color.BLUE, Shape.STAR)
                )
        );
    }

    // Given tests

    @Test
    void firstAdd_cannot_be_called_twice() {
        Tile redcross = new Tile(Color.RED, Shape.CROSS);
        Tile reddiamond = new Tile(Color.RED, Shape.DIAMOND);
        myGrid.firstAdd(Direction.UP, redcross, reddiamond);
        assertThrows(QwirkleException.class, () -> myGrid.firstAdd(Direction.DOWN, redcross, reddiamond));
    }

    @Test
    void firstAdd_must_be_called_first_simple() {
        Tile redcross = new Tile(Color.RED, Shape.CROSS);
        assertThrows(QwirkleException.class, () -> myGrid.add(0, 0, redcross));
    }

    @Test
    @DisplayName("get outside the grid should return null, not throw")
    void can_get_tile_outside_virtual_grid() {
        assertDoesNotThrow(() -> myGrid.get(-250, 500));
        assertNull(myGrid.get(-250, 500));
    }

    // Rule based tests

    @Test
    @Tag("rules")
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
    @Tag("rules")
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
    @Tag("rules")
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
    @Tag("rules")
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
    @Tag("rules")
    void rules_Elvire_c() {
        rules_Cedric_b();
        var t1 = new Tile(Color.BLUE, Shape.ROUND);
        myGrid.add(45, 46, t1);
        assertEquals(t1, myGrid.get(45, 46));
    }

    @Test
    @Tag("rules")
    void rules_Elvire_c_adapted_to_fail() {
        rules_Cedric_b();
        var t1 = new Tile(Color.BLUE, Shape.STAR);
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(45, 46, t1);
        });
        assertNull(myGrid.get(45, 46));
    }

    @Test
    @Tag("rules")
    void rules_Vincent_d() {
        rules_Elvire_c();
        var t1 = new Tile(Color.GREEN, Shape.PLUS);
        var t2 = new Tile(Color.GREEN, Shape.DIAMOND);
        myGrid.add(43, 44, Direction.DOWN, t1, t2);
        assertEquals(t1, myGrid.get(43, 44));
        assertEquals(t2, myGrid.get(44, 44));
    }

    @Test
    @Tag("rules")
    void rules_Vincent_d_adapted_to_fail() {
        rules_Elvire_c();
        var t1 = new Tile(Color.GREEN, Shape.PLUS);
        var t2 = new Tile(Color.BLUE, Shape.DIAMOND);
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(43, 44, Direction.DOWN, t1, t2);
        });
        assertNull(myGrid.get(43, 44));
        assertNull(myGrid.get(44, 44));
    }

    @Test
    @Tag("rules")
    void rules_Sonia_e() {
        rules_Vincent_d();
        var t1 = new TileAtPosition(42, 44, new Tile(Color.GREEN, Shape.STAR));
        var t2 = new TileAtPosition(45, 44, new Tile(Color.GREEN, Shape.ROUND));
        myGrid.add(t1, t2);
        assertEquals(t1.tile(), myGrid.get(42, 44));
        assertEquals(t2.tile(), myGrid.get(45, 44));
    }

    @Test
    @Tag("rules")
    void rules_Sonia_e_adapted_to_fail() {
        rules_Vincent_d();
        var t1 = new TileAtPosition(42, 44, new Tile(Color.GREEN, Shape.STAR));
        var t2 = new TileAtPosition(45, 44, new Tile(Color.BLUE, Shape.ROUND));
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(t1, t2);
        });
        assertNull(myGrid.get(42, 44));
        assertNull(myGrid.get(45, 44));
    }

    @Test
    @Tag("rules")
    void rules_Cedric_f() {
        rules_Sonia_e();
        var t1 = new Tile(Color.ORANGE, Shape.SQUARE);
        var t2 = new Tile(Color.RED, Shape.SQUARE);
        myGrid.add(46, 48, Direction.DOWN, t1, t2);
        assertEquals(t1, myGrid.get(46, 48));
        assertEquals(t2, myGrid.get(47, 48));
    }

    @Test
    @Tag("rules")
    void rules_Cedric_f_adapted_to_fail() {
        rules_Sonia_e();
        var t1 = new Tile(Color.ORANGE, Shape.SQUARE);
        var t2 = new Tile(Color.RED, Shape.SQUARE);
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(46, 48, Direction.DOWN, t2, t1);
        });
        assertNull(myGrid.get(46, 48));
        assertNull(myGrid.get(47, 48));
    }

    @Test
    @Tag("rules")
    void rules_Elvire_g() {
        rules_Cedric_f();
        var t1 = new Tile(Color.YELLOW, Shape.STAR);
        var t2 = new Tile(Color.ORANGE, Shape.STAR);
        myGrid.add(42, 43, Direction.LEFT, t1, t2);
    }

    @Test
    @Tag("rules")
    void rules_Elvire_g_adapted_to_fail() {
        rules_Cedric_f();
        var t1 = new Tile(Color.YELLOW, Shape.STAR);
        var t2 = new Tile(Color.GREEN, Shape.STAR);
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(42, 43, Direction.LEFT, t1, t2);
        });
        assertNull(myGrid.get(42, 43));
        assertNull(myGrid.get(42, 42));
    }

    @Test
    @Tag("rules")
    void rules_Vincent_h() {
        rules_Elvire_g();
        var t1 = new Tile(Color.ORANGE, Shape.CROSS);
        var t2 = new Tile(Color.ORANGE, Shape.DIAMOND);
        myGrid.add(43, 42, Direction.DOWN, t1, t2);
        assertEquals(t1, myGrid.get(43, 42));
        assertEquals(t2, myGrid.get(44, 42));
    }

    @Test
    @Tag("rules")
    void rules_Vincent_h_adapted_to_fail() {
        rules_Elvire_g();
        var t1 = new Tile(Color.ORANGE, Shape.CROSS);
        var t2 = new Tile(Color.GREEN, Shape.DIAMOND);
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(43, 42, Direction.DOWN, t1, t2);
        });
        assertNull(myGrid.get(43, 42));
        assertNull(myGrid.get(44, 42));
    }

    @Test
    @Tag("rules")
    void rules_Sonia_i() {
        rules_Vincent_h();
        var t1 = new Tile(Color.YELLOW, Shape.DIAMOND);
        var t2 = new Tile(Color.YELLOW, Shape.ROUND);
        myGrid.add(44, 43, Direction.DOWN, t1, t2);
        assertEquals(t1, myGrid.get(44, 43));
        assertEquals(t2, myGrid.get(45, 43));
    }

    @Test
    @Tag("rules")
    void rules_Sonia_i_adapted_to_fail() {
        rules_Vincent_h();
        var t1 = new Tile(Color.YELLOW, Shape.DIAMOND);
        var t2 = new Tile(Color.BLUE, Shape.ROUND);
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(44, 43, Direction.DOWN, t1, t2);
        });
        assertNull(myGrid.get(44, 43));
        assertNull(myGrid.get(45, 43));
    }

    @Test
    @Tag("rules")
    void rules_Cedric_j() {
        rules_Sonia_i();
        var t1 = new Tile(Color.RED, Shape.STAR);
        myGrid.add(42, 45, t1);
        assertEquals(t1, myGrid.get(42, 45));
    }

    @Test
    @Tag("rules")
    void rules_Cedric_j_adapted_to_fail() {
        rules_Sonia_i();
        var t1 = new Tile(Color.GREEN, Shape.PLUS);
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(42, 45, t1);
        });
        assertNull(myGrid.get(42, 45));
    }

    @Test
    @Tag("rules")
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
    @Tag("rules")
    void rules_Elvire_k_adapted_to_fail() {
        rules_Cedric_j();
        var t1 = new Tile(Color.BLUE, Shape.CROSS);
        var t2 = new Tile(Color.RED, Shape.CROSS);
        var t3 = new Tile(Color.BLUE, Shape.CROSS);
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(47, 46, Direction.LEFT, t1, t2, t3);
        });
        assertNull(myGrid.get(47, 46));
        assertNull(myGrid.get(47, 45));
        assertNull(myGrid.get(47, 44));
    }

    @Test
    @Tag("rules")
    void rules_Vincent_l() {
        rules_Elvire_k();
        var t1 = new Tile(Color.YELLOW, Shape.SQUARE);
        var t2 = new Tile(Color.BLUE, Shape.SQUARE);
        myGrid.add(46, 49, Direction.DOWN, t1, t2);
        assertEquals(t1, myGrid.get(46, 49));
        assertEquals(t2, myGrid.get(47, 49));
    }

    @Test
    @Tag("rules")
    void rules_Vincent_l_adapted_to_fail() {
        rules_Elvire_k();
        var t1 = new Tile(Color.YELLOW, Shape.SQUARE);
        var t2 = new Tile(Color.BLUE, Shape.SQUARE);
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(46, 49, Direction.DOWN, t2, t1);
        });
        assertNull(myGrid.get(46, 49));
        assertNull(myGrid.get(47, 49));
    }

    // Case tests

    @Test
    @Tag("cases")
    @DisplayName("FirstAdd - Add one tile")
    void cases_firstAdd_oneTile() {
        var tile = new Tile(Color.BLUE, Shape.STAR);
        assertDoesNotThrow(() ->
                myGrid.firstAdd(Direction.DOWN, tile)
        );
        assertEquals(tile, myGrid.get(45, 45));
    }

    @Test
    @Tag("cases")
    @DisplayName("FirstAdd - Add tiles - Same color, Same shape")
    void cases_firstAdd_tiles_SameShapeSameColor() {
        var tile1 = new Tile(Color.BLUE, Shape.STAR);
        var tile2 = new Tile(Color.BLUE, Shape.STAR);
        for (Direction direction : Direction.values()) {
            myGrid = new Grid();
            assertThrows(QwirkleException.class, () ->
                    myGrid.firstAdd(direction, tile1, tile2)
            );
            assertNull(myGrid.get(45, 45));
            assertNull(myGrid.get(45 + direction.getDeltaRow(), 45 + direction.getDeltaCol()));
        }
    }

    @Test
    @Tag("cases")
    @DisplayName("FirstAdd - Add tiles - Same color, Diff shape")
    void cases_firstAdd_tiles_DiffShapeSameColor() {
        var tile1 = new Tile(Color.BLUE, Shape.STAR);
        var tile2 = new Tile(Color.BLUE, Shape.SQUARE);
        for (Direction direction : Direction.values()) {
            myGrid = new Grid();
            assertDoesNotThrow(() ->
                    myGrid.firstAdd(direction, tile1, tile2)
            );
            assertEquals(tile1, myGrid.get(45, 45));
            assertEquals(tile2, myGrid.get(45 + direction.getDeltaRow(), 45 + direction.getDeltaCol()));
        }
    }

    @Test
    @Tag("cases")
    @DisplayName("FirstAdd - Add tiles - Diff color, Same shape")
    void cases_firstAdd_tiles_SameShapeDiffColor() {
        var tile1 = new Tile(Color.BLUE, Shape.STAR);
        var tile2 = new Tile(Color.GREEN, Shape.STAR);
        for (Direction direction : Direction.values()) {
            myGrid = new Grid();
            assertDoesNotThrow(() ->
                    myGrid.firstAdd(direction, tile1, tile2)
            );
            assertEquals(tile1, myGrid.get(45, 45));
            assertEquals(tile2, myGrid.get(45 + direction.getDeltaRow(), 45 + direction.getDeltaCol()));
        }
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add one tile - Complete line same color, diff shape")
    void cases_add_tile_SameColorDiffShape() {
        addSomeTiles_FirstAdd_SameColor();
        var tile = new Tile(Color.BLUE, Shape.SQUARE);
        var tile2 = new Tile(Color.BLUE, Shape.STAR);
        assertDoesNotThrow(() -> {
            myGrid.add(44, 43, tile);
            myGrid.add(45, 42, tile);
            myGrid.add(46, 43, tile2);
        });
        assertEquals(tile, myGrid.get(44, 43));
        assertEquals(tile, myGrid.get(45, 42));
        assertEquals(tile2, myGrid.get(46, 43));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add one tile - Complete line diff color, same shape")
    void cases_add_tile_DiffColorSameShape() {
        addSomeTiles_FirstAdd_SameColor();
        var tile = new Tile(Color.RED, Shape.DIAMOND);
        var tile2 = new Tile(Color.GREEN, Shape.CROSS);
        var tile3 = new Tile(Color.YELLOW, Shape.PLUS);
        assertDoesNotThrow(() -> {
            myGrid.add(46, 45, tile3);
            myGrid.add(44, 44, tile2);
            myGrid.add(46, 43, tile);
        });
        assertEquals(tile, myGrid.get(46, 43));
        assertEquals(tile2, myGrid.get(44, 44));
        assertEquals(tile3, myGrid.get(46, 45));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add one tile - Complete line diff color, diff shape")
    void cases_add_tile_DiffColorDiffShape() {
        addSomeTiles_FirstAdd_SameColor();
        var tile = new Tile(Color.RED, Shape.DIAMOND);
        var tile2 = new Tile(Color.GREEN, Shape.CROSS);
        var tile3 = new Tile(Color.YELLOW, Shape.SQUARE);
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(45, 42, tile);
        });
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(44, 43, tile2);
        });
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(46, 45, tile3);
        });
        assertNull(myGrid.get(45, 42));
        assertNull(myGrid.get(44, 43));
        assertNull(myGrid.get(46, 45));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add one tile - Complete line same color, same shape")
    void cases_add_tile_SameColorSameShape() {
        addSomeTiles_FirstAdd_SameColor();
        var tile = new Tile(Color.BLUE, Shape.DIAMOND);
        var tile2 = new Tile(Color.BLUE, Shape.CROSS);
        var tile3 = new Tile(Color.BLUE, Shape.PLUS);
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(44, 43, tile);
        });
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(46, 44, tile2);
        });
        assertThrows(QwirkleException.class, () -> {
            myGrid.add(46, 45, tile3);
        });
        assertNull(myGrid.get(44, 43));
        assertNull(myGrid.get(46, 44));
        assertNull(myGrid.get(46, 45));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add one tile - Pos already used")
    void cases_add_tile_posAlreadyUsed() {
        addSomeTiles_FirstAdd_SameShape();
        var tile = new Tile(Color.RED, Shape.STAR);
        var savedTile = myGrid.get(45, 45);
        assertThrows(QwirkleException.class, () ->
                myGrid.add(45, 45, tile)
        );
        assertEquals(savedTile, myGrid.get(45, 45));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add tiles - Add a line diff shape, same color")
    void cases_add_tiles_DiffShapeSameColor() {
        addSomeTiles_FirstAdd_SameColor();
        var tile1 = new Tile(Color.GREEN, Shape.PLUS);
        var tile2 = new Tile(Color.GREEN, Shape.CROSS);
        assertDoesNotThrow(() ->
                myGrid.add(44, 45, Direction.LEFT, tile1, tile2)
        );
        assertEquals(tile1, myGrid.get(44, 45));
        assertEquals(tile2, myGrid.get(44, 44));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add tiles - Add a line same shape, diff color")
    void cases_add_tiles_SameShapeDiffColor() {
        addSomeTiles_FirstAdd_SameColor();
        var tile1 = new Tile(Color.GREEN, Shape.PLUS);
        var tile2 = new Tile(Color.RED, Shape.PLUS);
        assertDoesNotThrow(() ->
                myGrid.add(44, 45, Direction.UP, tile1, tile2)
        );
        assertEquals(tile1, myGrid.get(44, 45));
        assertEquals(tile2, myGrid.get(43, 45));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add tiles - Add a line same shape, same color")
    void cases_add_tiles_SameShapeSameColor() {
        addSomeTiles_FirstAdd_SameColor();
        var tile1 = new Tile(Color.GREEN, Shape.PLUS);
        var tile2 = new Tile(Color.BLUE, Shape.PLUS);
        assertThrows(QwirkleException.class, () ->
                myGrid.add(44, 45, Direction.UP, tile1, tile2)
        );
        assertNull(myGrid.get(44, 45));
        assertNull(myGrid.get(43, 45));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add tiles - Pos already used")
    void cases_add_tiles_posAlreadyUsed() {
        addSomeTiles_FirstAdd_SameShape();
        var tile1 = new Tile(Color.GREEN, Shape.PLUS);
        var tile2 = new Tile(Color.BLUE, Shape.PLUS);
        var savedTile = myGrid.get(45, 45);
        assertThrows(QwirkleException.class, () ->
                myGrid.add(45, 45, Direction.RIGHT, tile1, tile2)
        );
        assertEquals(savedTile, myGrid.get(45, 45));
        assertNull(myGrid.get(45, 46));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add tiles at pos - Same Color")
    void cases_add_tilesAtPos_sameColor() {
        addSomeTiles_FirstAdd_SameColor();
        var tile1 = new Tile(Color.BLUE, Shape.STAR);
        var tile2 = new Tile(Color.BLUE, Shape.ROUND);
        var tileAtPos1 = new TileAtPosition(45, 46, tile1);
        var tileAtPos2 = new TileAtPosition(45, 42, tile2);
        assertDoesNotThrow(() ->
                myGrid.add(tileAtPos1, tileAtPos2)
        );
        assertEquals(tile1, myGrid.get(45, 46));
        assertEquals(tile2, myGrid.get(45, 42));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add tiles at pos - Same Shape")
    void cases_add_tilesAtPos_sameShape() {
        addSomeTiles_FirstAdd_SameShape();
        var tile1 = new Tile(Color.YELLOW, Shape.PLUS);
        var tile2 = new Tile(Color.PURPLE, Shape.PLUS);
        var tileAtPos1 = new TileAtPosition(45, 46, tile1);
        var tileAtPos2 = new TileAtPosition(45, 42, tile2);
        assertDoesNotThrow(() ->
                myGrid.add(tileAtPos1, tileAtPos2)
        );
        assertEquals(tile1, myGrid.get(45, 46));
        assertEquals(tile2, myGrid.get(45, 42));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add tiles at pos - Diff Shape, Diff Color")
    void cases_add_tilesAtPos_diffShapeDiffColor() {
        addSomeTiles_FirstAdd_SameShape();
        var tile1 = new Tile(Color.YELLOW, Shape.SQUARE);
        var tile2 = new Tile(Color.PURPLE, Shape.DIAMOND);
        var tileAtPos1 = new TileAtPosition(45, 46, tile1);
        var tileAtPos2 = new TileAtPosition(45, 42, tile2);
        assertThrows(QwirkleException.class, () ->
                myGrid.add(tileAtPos1, tileAtPos2)
        );
        assertNull(myGrid.get(45, 46));
        assertNull(myGrid.get(45, 42));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add tiles at pos - Pos already used")
    void cases_add_tilesAtPos_posAlreadyUsed() {
        addSomeTiles_FirstAdd_SameShape();
        var tile1 = new Tile(Color.YELLOW, Shape.SQUARE);
        var tile2 = new Tile(Color.PURPLE, Shape.DIAMOND);
        var tileAtPos1 = new TileAtPosition(45, 45, tile1);
        var tileAtPos2 = new TileAtPosition(45, 43, tile2);
        var savedShape1 = myGrid.get(45, 45);
        var savedShape2 = myGrid.get(45, 43);
        assertThrows(QwirkleException.class, () ->
                myGrid.add(tileAtPos1, tileAtPos2)
        );
        assertEquals(savedShape1, myGrid.get(45, 45));
        assertEquals(savedShape2, myGrid.get(45, 43));
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add tile - Join two lines - Same color")
    void cases_add_tile_joinTwoLine_sameColor() {
        addSomeTiles_FirstAdd_SameColor();
        myGrid.add(46, 43, new Tile(Color.BLUE, Shape.STAR));
        myGrid.add(47, 43, new Tile(Color.BLUE, Shape.PLUS));
        myGrid.add(47, 44, new Tile(Color.BLUE, Shape.STAR));
        myGrid.add(47, 45, new Tile(Color.BLUE, Shape.ROUND));
        var tile = new Tile(Color.BLUE, Shape.SQUARE);
        assertDoesNotThrow(() ->
                myGrid.add(46, 45, tile)
        );
        assertEquals(myGrid.get(46, 45), tile);
    }

    @Test
    @Tag("cases")
    @DisplayName("Add - Add tile - Join two lines - Tile Already in a line")
    void cases_add_tile_joinTwoLine_tileAlreadyInALine() {
        addSomeTiles_FirstAdd_SameColor();
        myGrid.add(46, 43, new Tile(Color.BLUE, Shape.STAR));
        myGrid.add(47, 43, new Tile(Color.BLUE, Shape.CROSS));
        myGrid.add(48, 43, new Tile(Color.BLUE, Shape.SQUARE));
        myGrid.add(48, 44, new Tile(Color.BLUE, Shape.STAR));
        myGrid.add(48, 45, new Tile(Color.BLUE, Shape.ROUND));
        myGrid.add(49, 45, new Tile(Color.BLUE, Shape.PLUS));
        myGrid.add(47, 45, new Tile(Color.BLUE, Shape.SQUARE));
        var tile = new Tile(Color.BLUE, Shape.CROSS);
        assertThrows(QwirkleException.class, () ->
                myGrid.add(46, 45, tile)
        );
        assertNull(myGrid.get(46, 45));
    }

    // Point based tests

    @Test
    @Tag("points")
    @DisplayName("Points - firstAdd - Add tile")
    void points_firstAdd_addTile() {
        assertEquals(1, myGrid.firstAdd(
                Direction.DOWN, new Tile(Color.BLUE, Shape.ROUND)
        ));
    }

    @Test
    @Tag("points")
    @DisplayName("Points - firstAdd - Add tiles")
    void points_firstAdd_addTiles() {
        assertEquals(2, myGrid.firstAdd(
                Direction.UP,
                new Tile(Color.GREEN, Shape.PLUS), new Tile(Color.ORANGE, Shape.PLUS)
        ));
    }

    @Test
    @Tag("points")
    @DisplayName("Points - firstAdd - Add tiles - Remove, no points")
    void points_firstAdd_addTiles_remove_noPoints() {
        assertThrows(QwirkleException.class, () -> myGrid.firstAdd(
                Direction.UP,
                new Tile(Color.GREEN, Shape.PLUS), new Tile(Color.ORANGE, Shape.SQUARE)
        ));
    }

    @Test
    @Tag("points")
    @DisplayName("Points - add - Add tile - Adjacent Two lines")
    void points_add_addTile_adjacentTwoLines() {
        addSomeTiles_FirstAdd_SameColor();
        myGrid.add(44, 45, new Tile(Color.GREEN, Shape.PLUS));
        assertEquals(4, myGrid.add(44, 44, new Tile(Color.GREEN, Shape.CROSS)));
    }

    @Test
    @Tag("points")
    @DisplayName("Points - add - Add tile - Complete a line")
    void points_add_addTile_completeALine() {
        addSomeTiles_FirstAdd_SameColor();
        assertEquals(4, myGrid.add(45, 42, new Tile(Color.BLUE, Shape.STAR)));
    }

    @Test
    @Tag("points")
    @DisplayName("Points - add - Add tile - Complete a line - QWIRKLE")
    void points_add_addTile_completeALine_qwirkle() {
        addSomeTiles_FirstAdd_SameColor();
        myGrid.add(45, 42, new Tile(Color.BLUE, Shape.STAR));
        myGrid.add(45, 41, new Tile(Color.BLUE, Shape.ROUND));
        assertEquals(12, myGrid.add(45, 40, new Tile(Color.BLUE, Shape.SQUARE)));
    }

    @Test
    @Tag("points")
    @DisplayName("Points - add - Add tile - Between two lines")
    void points_add_addTile_betweenTwoLines() {
        addSomeTiles_FirstAdd_SameColor();
        myGrid.add(46, 45, new Tile(Color.RED, Shape.PLUS));
        myGrid.add(47, 45, new Tile(Color.YELLOW, Shape.PLUS));
        myGrid.add(48, 45, new Tile(Color.PURPLE, Shape.PLUS));
        myGrid.add(48, 44, new Tile(Color.ORANGE, Shape.PLUS));
        myGrid.add(48, 43, new Tile(Color.BLUE, Shape.PLUS));
        myGrid.add(47, 43, new Tile(Color.BLUE, Shape.STAR));
        assertEquals(4, myGrid.add(46, 43, new Tile(Color.BLUE, Shape.CROSS)));
    }

    @Test
    @Tag("points")
    @DisplayName("Points - add - Add tile - Between three lines")
    void points_add_addTile_betweenThreeLines() {
        myGrid.firstAdd(Direction.DOWN, new Tile(Color.BLUE, Shape.STAR),
                new Tile(Color.BLUE, Shape.CROSS),
                new Tile(Color.BLUE, Shape.SQUARE));
        myGrid.add(47, 44, Direction.LEFT, new Tile(Color.BLUE, Shape.PLUS),
                new Tile(Color.BLUE, Shape.STAR));
        myGrid.add(46, 43, new Tile(Color.BLUE, Shape.DIAMOND));
        myGrid.add(46, 42, new Tile(Color.BLUE, Shape.ROUND));
        myGrid.add(46, 46, new Tile(Color.BLUE, Shape.STAR));
        assertEquals(7, myGrid.add(46, 44, new Tile(Color.BLUE, Shape.SQUARE)));
    }

    @Test
    @Tag("points")
    @DisplayName("Points - add - Add tile - Between four lines")
    void points_add_addTile_betweenFourLines() {
        myGrid.firstAdd(Direction.DOWN, new Tile(Color.BLUE, Shape.STAR),
                new Tile(Color.BLUE, Shape.CROSS),
                new Tile(Color.BLUE, Shape.SQUARE));
        myGrid.add(47, 44, Direction.LEFT, new Tile(Color.BLUE, Shape.PLUS),
                new Tile(Color.BLUE, Shape.STAR));
        myGrid.add(46, 43, Direction.UP, new Tile(Color.BLUE, Shape.DIAMOND),
                new Tile(Color.BLUE, Shape.CROSS));
        myGrid.add(45, 44, new Tile(Color.BLUE, Shape.ROUND));
        myGrid.add(46, 42, new Tile(Color.BLUE,Shape.ROUND));
        myGrid.add(46, 46, new Tile(Color.BLUE, Shape.STAR));
        assertEquals(8, myGrid.add(46, 44, new Tile(Color.BLUE, Shape.SQUARE)));
    }

    @Test
    @Tag("points")
    @DisplayName("Points - add - Add tiles - Complete a line")
    void points_add_addTiles_completeALine() {
        addSomeTiles_FirstAdd_SameColor();
        assertEquals(5,
                myGrid.add(45, 42, Direction.LEFT,
                        new Tile(Color.BLUE, Shape.STAR), new Tile(Color.BLUE, Shape.SQUARE)));
    }

    @Test
    @Tag("points")
    @DisplayName("Points - add - Add tiles - Line Collate to another")
    void points_add_addTiles_addALine_collateToAnother() {
        addSomeTiles_FirstAdd_SameShape();
        myGrid.add(45, 42, new Tile(Color.PURPLE, Shape.PLUS));
        assertEquals(12,
                myGrid.add(44, 45, Direction.LEFT,
                        new Tile(Color.RED, Shape.PLUS), new Tile(Color.GREEN, Shape.PLUS),
                        new Tile(Color.BLUE, Shape.PLUS), new Tile(Color.ORANGE, Shape.PLUS)));
    }

    @Test
    @Tag("points")
    @DisplayName("Points - addTileAtPosition - Add tiles - Before and after line")
    void points_addTileAtPos_addTwoTiles_beforeAndAfterLine() {
        addSomeTiles_FirstAdd_SameColor();
        assertEquals(5, myGrid.add(
                new TileAtPosition(45,46, new Tile(Color.BLUE, Shape.SQUARE)),
                new TileAtPosition(45, 42, new Tile(Color.BLUE, Shape.ROUND))
                )
        );
    }

    @Test
    @Tag("points")
    @DisplayName("Points - addTileAtPosition - Add tiles - Next a line ")
    void points_addTileAtPos_addTwoTiles_nextALine() {
        addSomeTiles_FirstAdd_SameColor();
        assertEquals(5, myGrid.add(
                        new TileAtPosition(45,42, new Tile(Color.BLUE, Shape.SQUARE)),
                        new TileAtPosition(45, 41, new Tile(Color.BLUE, Shape.ROUND))
                )
        );
    }

    @Test
    @Tag("points")
    @DisplayName("Points - addTileAtPosition - Add tile - Between two lines")
    void points_addTileAtPos_AddATile_betweenTwoLines(){
        addSomeTiles_FirstAdd_SameColor();
        myGrid.add(46, 45, new Tile(Color.RED, Shape.PLUS));
        myGrid.add(47, 45, new Tile(Color.YELLOW, Shape.PLUS));
        myGrid.add(48, 45, new Tile(Color.PURPLE, Shape.PLUS));
        myGrid.add(48, 44, new Tile(Color.ORANGE, Shape.PLUS));
        myGrid.add(48, 43, new Tile(Color.BLUE, Shape.PLUS));
        myGrid.add(47, 43, new Tile(Color.BLUE, Shape.STAR));
        assertEquals(4, myGrid.add(new TileAtPosition(46,43, new Tile(Color.BLUE, Shape.CROSS))));
    }
}