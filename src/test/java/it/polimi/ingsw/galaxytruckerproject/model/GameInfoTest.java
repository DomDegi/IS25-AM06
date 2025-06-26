package it.polimi.ingsw.galaxytruckerproject.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameInfoTest {

    private GameInfo gameInfo;
    @BeforeEach
    void setUp() {
       gameInfo = new GameInfo("game", GameMode.TRIAL, 2, 0);
    }

    @Test
    void getGameName() {
        assertEquals("game", gameInfo.getGameName());
    }

    @Test
    void getGameMode(){
        assertEquals(GameMode.TRIAL, gameInfo.getGameMode());
    }

    @Test
    void getMaxPlayerCount(){
        assertEquals(2, gameInfo.getMaxPlayerCount());
    }

    @Test
    void getCurrentPlayerCount(){
        assertEquals(0, gameInfo.getCurrentPlayerCount());
    }

    @Test
    void testToString_whenNotRestarted() {
        String expected = "game GameMode: TRIAL 0/2";
        assertEquals(expected, gameInfo.toString());
    }

    @Test
    void testToString_whenRestarted() {
        gameInfo.setRestarted();
        String expected = "game GameMode: TRIAL 0/2 re-starting game";
        assertEquals(expected, gameInfo.toString());
    }


}