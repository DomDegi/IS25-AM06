package it.polimi.ingsw.galaxytruckerproject.tiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {
    Tile tile;
    Coordinates coordinates;
    @Test
    void testMustRotateCorrectly(){
        tile= new DoubleEngine(new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        tile.rotate();
        if (!tile.getNorth().getConnectorsType().equals(Connectors.UNIVERSAL)) throw new AssertionError();
        return;
    }

}