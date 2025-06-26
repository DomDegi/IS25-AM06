package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
class BatteryComponentsTest {
    Player player;
    ShipBoard shipBoard;
    BatteryComponents battery;
    @BeforeEach
    void setUp() {
        player = new Player("franco", PlayersColor.BLUE);
        shipBoard = new ShipBoard(player);
        shipBoard.initializeLevel2();
        battery = new BatteryComponents(new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL),3);
        shipBoard.positionTile(Optional.of(battery), new Coordinates (1,3));
    }

    @Test
    void getBatteryCellsTest() {
        battery.consumeBattery();
        battery.addBattery();
        assertEquals(3, battery.getNumBatteries());
        battery.consumeBattery();
        battery.consumeBattery();
        battery.consumeBattery();
        assertFalse(battery.consumeBattery());
    }

    @Test
    void toString1Test(){
        battery.consumeBattery();
        battery.consumeBattery();
        battery.consumeBattery();
        String expected = battery.toString1();
        assertEquals(expected,"    3   ");
    }

    @Test
    void toString1Test2(){
        battery.consumeBattery();
        battery.consumeBattery();
        String expected = battery.toString1();
        assertEquals(expected," ▄  3   ");
    }

    @Test
    void toString3Test(){
        BatteryComponents battery1 = new BatteryComponents(new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL),null,0,82,3);
        String expected = battery1.toString3();
        assertEquals(expected," ▄ 3 82 ");
    }

    @Test
    void toString3Test2(){
        BatteryComponents battery2 = new BatteryComponents(new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL), new Link(Connectors.UNIVERSAL),null,0,99,2);
        String expected = battery2.toString3();
        assertEquals(expected,"   3 99 ");
    }

    @Test
    void testFromStringWithInvalidValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            GoodsColor.fromString("Z");
        });

        assertEquals("Unknown color code: Z", exception.getMessage());
    }






  
}