package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Connectors;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Link;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkTest {
    @Test
    void testCorrectLink1(){
        Link i= new Link(Connectors.SMOOTH);
        Link j= new Link(Connectors.SMOOTH);
        assertTrue(i.isConnected(j));
    }
    @Test
    void testCorrectLink2(){
        Link i= new Link(Connectors.UNIVERSAL);
        Link j= new Link(Connectors.SMOOTH);
        assertFalse(i.isConnected(j));
    }
    @Test
    void testCorrectLink3(){
        Link i= new Link(Connectors.UNIVERSAL);
        Link j= new Link(Connectors.UNIVERSAL);
        assertTrue(i.isConnected(j));
    }
    @Test
    void testCorrectLink4(){
        Link i= new Link(Connectors.UNIVERSAL);
        Link j= new Link(Connectors.SINGLE);
        assertTrue(i.isConnected(j));
    }
    @Test
    void testCorrectLink5(){
        Link i= new Link(Connectors.SMOOTH);
        Link j= new Link(Connectors.UNIVERSAL);
        assertFalse(i.isConnected(j));
    }



}