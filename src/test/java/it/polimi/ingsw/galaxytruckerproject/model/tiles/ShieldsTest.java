package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShieldsTest {
    @Test
    void testShieldRotation(){
        Shields shield = new Shields(new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        assertEquals(shield.getCoveredArea(),Coverage.NORTH_EAST);
        shield.rotate();
        assertEquals(shield.getCoveredArea(),Coverage.SOUTH_EAST);
        shield.rotate();
        assertEquals(shield.getCoveredArea(),Coverage.SOUTH_WEST);
        shield.rotate();
        assertEquals(shield.getCoveredArea(),Coverage.NORTH_WEST);
        shield.rotate();
        assertEquals(shield.getCoveredArea(),Coverage.NORTH_EAST);
    }

}