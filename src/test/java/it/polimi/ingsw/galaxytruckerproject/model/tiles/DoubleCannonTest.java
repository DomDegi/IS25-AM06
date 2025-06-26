package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleCannonTest {

    DoubleCannon cannon = new DoubleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL));
    @Test
    void ChooseToUse() {
        assertTrue(cannon.chooseToUse());
    }

    @Test
    void toString1(){
        String expected = " ↑  0   ";
        assertEquals(expected, cannon.toString1());
    }



}