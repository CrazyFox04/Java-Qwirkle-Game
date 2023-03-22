package g60904.qwirkle.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {
    @Test
    void firstAddTest_oneTileOK() {
        Grid myGrid = new Grid();
        myGrid.firstAdd(
                Direction.LEFT,
                new Tile(Color.BLUE, Shape.PLUS)
        );
        assertEquals(myGrid.get(45, 45), new Tile(Color.BLUE, Shape.PLUS));
    }

    @Test
    void firstAddTest_MoreTilesOK() {
        Grid myGrid = new Grid();
        myGrid.firstAdd(
                Direction.LEFT,
                new Tile(Color.BLUE, Shape.PLUS),
                new Tile(Color.BLUE, Shape.CROSS),
                new Tile(Color.BLUE, Shape.DIAMOND)
        );
        assertEquals(myGrid.get(45, 45), new Tile(Color.BLUE, Shape.PLUS));
        assertEquals(myGrid.get(45, 44), new Tile(Color.BLUE, Shape.CROSS));
        assertEquals(myGrid.get(45, 43), new Tile(Color.BLUE, Shape.DIAMOND));
    }

    @Test
    void firstAddTest_DifferentColorException() {
        Grid myGrid = new Grid();
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
        Grid myGrid = new Grid();
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
        Grid myGrid = new Grid();
        assertThrows(
                QwirkleException.class, () -> myGrid.firstAdd(
                        Direction.LEFT,
                        new Tile(Color.GREEN, Shape.PLUS),
                        new Tile(Color.BLUE, Shape.DIAMOND),
                        new Tile(Color.BLUE, Shape.DIAMOND)
                )
        );
    }
}