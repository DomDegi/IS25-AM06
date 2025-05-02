package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LightShipBoardTest2 {

    @Test
    void test() {
        LightPlayer lightPlayer = new LightPlayer("Per favore", PlayersColor.GREEN);
        LightShipBoard lightShipBoard = new LightShipBoard(lightPlayer);
        lightShipBoard.initializeLevel2();
    }

}