package g60904.qwirkle.model;

import java.util.Collections;
import java.util.List;

public class Player {
    private String name;

    private List<Tile> tiles;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Tile> getHand() {
        return Collections.unmodifiableList(tiles);
    }

    public void refill() {
        tiles.addAll(List.of(Bag.getInstance().getRandomTiles(tiles.size() - 6)));
    }

    public void remove(Tile... ts) {
        tiles.removeAll(List.of(ts));
    }
}