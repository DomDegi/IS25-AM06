package it.polimi.ingsw.galaxytruckerproject.tiles;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.player.PlayersColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShipBoardTest {

    private ShipBoard shipBoard;


    @BeforeEach
    void setUp() {
        Player player = new Player("franco", PlayersColor.BLUE);
        shipBoard = new ShipBoard(player);
        shipBoard.initializeLevel2();
    }

    @Test
    void testOneTileCorrectShipboard() {
        Tile tile=new DoubleEngine(new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard.positionTile(Optional.of(tile), new Coordinates(3,3));
        assertTrue(shipBoard.verifyCorrectness());
    }

    @Test
    void testTwoTilesConnectorsShipboardWithRotation() {
        //create e set tile 1
        Tile tile1=new DoubleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL));
        tile1.rotate();
        shipBoard.positionTile(Optional.of(tile1), new Coordinates(3,3));
        //create e set tile 2
        Tile tile2=new Pipe(new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard.positionTile(Optional.of(tile2), new Coordinates(2,3));
        assertTrue(shipBoard.verifyCorrectness());
    }

    @Test
    void testTwoTilesWrongConnectorsShipboardWithRotation() {
        //create e set tile 1
        Tile tile1=new DoubleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL));
        tile1.rotate();
        shipBoard.positionTile(Optional.of(tile1), new Coordinates(3,3));
        //create e set tile 2
        Tile tile2=new Pipe(new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard.positionTile(Optional.of(tile2), new Coordinates(2,3));
        assertFalse(shipBoard.verifyCorrectness());
    }

    @Test
    void testOneCannonStrength(){
        Tile tile1=new SingleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL));
        shipBoard.positionTile(Optional.of(tile1), new Coordinates(3,3));
        shipBoard.verifyCorrectness();
        assertTrue(shipBoard.getSingleCannonPower()==1);
    }

    @Test
    void testAddBreakSingleCannon(){
        shipBoard.addBreakSingleCannonPower(1);

        assertTrue(shipBoard.getSingleCannonPower()==1);
    }

    @Test
    void testOneBatteryComponent(){
        Tile tile1=new BatteryComponents(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL), 2);
        shipBoard.positionTile(Optional.of(tile1), new Coordinates(3,3));
        shipBoard.verifyCorrectness();
        assertTrue(shipBoard.getBatteryCoordinates().contains(tile1.getCoordinates()));
    }

    @Test
    void testOnePenaltyAdding(){
        Tile tile=new DoubleEngine(new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard.positionTile(Optional.of(tile), new Coordinates(3,3));
        tile.destroy();
        assertTrue(shipBoard.getPenalty()==1);
    }

    @Test
    void testOneTileTwoExposedConnectors(){
        Tile tile=new DoubleEngine(new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard.positionTile(Optional.of(tile), new Coordinates(3,3));
        shipBoard.countExposedConnectors();
        assertTrue(shipBoard.getNumExposedConnectors()==2);

    }


}
  
