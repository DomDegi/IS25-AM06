package it.polimi.ingsw.galaxytruckerproject.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import org.junit.jupiter.api.Test;

class TileTest {
    Tile tile;
    Coordinates coordinates;
    @Test
    void testMustRotateCorrectly1(){
        tile= new DoubleEngine(new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        tile.rotate();
        if (!tile.getNorth().getConnectorsType().equals(Connectors.UNIVERSAL)) throw new AssertionError();
        return;
    }

    @Test
    void testMustRotateCorrectly2(){
        tile= new DoubleEngine(new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        tile.rotate();
        if (!tile.getSouth().getConnectorsType().equals(Connectors.SINGLE)) throw new AssertionError();
        return;
    }
    @Test
    void testMustRotateCorrectly3(){
        tile= new DoubleEngine(new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        tile.rotate();
        if (!tile.getEast().getConnectorsType().equals(Connectors.SMOOTH)) throw new AssertionError();
        return;
    }
    @Test
    void testMustRotateCorrectly4(){
        tile= new DoubleEngine(new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        tile.rotate();
        if (!tile.getWest().getConnectorsType().equals(Connectors.SMOOTH)) throw new AssertionError();
        return;
    }

}