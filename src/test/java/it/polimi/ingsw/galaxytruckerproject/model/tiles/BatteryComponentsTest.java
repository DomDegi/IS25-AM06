package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
class BatteryComponentsTest {
    @Test
    void getBatteryCells() {
        Player player = new Player("franco", PlayersColor.BLUE);
        ShipBoard shipBoard = new ShipBoard(player);
        shipBoard.initializeLevel2();
        BatteryComponents battery = new BatteryComponents(new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL),3);
        shipBoard.positionTile(Optional.of(battery), new Coordinates (1,3));
        battery.consumeBattery();
        battery.addBattery();
        assertEquals(3, battery.getNumBatteries());
    }
  
}