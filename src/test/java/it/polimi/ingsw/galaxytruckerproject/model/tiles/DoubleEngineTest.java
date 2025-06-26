package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DoubleEngineTest {
    @Test
    void ChooseToUseAndGetStrength() {
        Player player = new Player("franco", PlayersColor.BLUE);
        ShipBoard shipBoard = new ShipBoard(player);
        shipBoard.initializeLevel2();
        DoubleEngine doubleEngine = new DoubleEngine(new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL), new Link(Connectors.SMOOTH), new Link(Connectors.UNIVERSAL));
        shipBoard.positionTile(Optional.of(doubleEngine), new Coordinates (1,3));
        assertEquals(2, doubleEngine.getEngineStrength());
        assertTrue(doubleEngine.chooseToUse());
    }


}