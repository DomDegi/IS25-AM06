package it.polimi.ingsw.galaxytruckerproject.tiles;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShipBoardTest {

    private ShipBoard shipBoard;


    @BeforeEach
    void setUp() {
        Player player = new Player("franco", PlayersColor.BLUE);
        shipBoard = new ShipBoard(player);
        shipBoard.initializeLevel2();
    }



    //a series of tests that initialize shipboard useful in testing"
    @Test
    void testShipBoard1() {
        Player player1 = new Player("fedeBulfariGalattico", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(1,3));
        Tile tile2=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,4));
        Tile tile3=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile3.rotate();
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(2,5));
        Tile tile4=new Pipe( new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(2,4));
        Tile tile5=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        tile5.rotate();
        tile5.rotate();
        tile5.rotate();
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(2,2));
        Tile tile6=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(3,3));
        boolean result=shipBoard1.verifyCorrectness();
        //verifyCorrectness is needed for the shipboard stats setting
        System.out.println(shipBoard1.toString());
        assertTrue(result);
    }

    @Test
    void testShipBoard2() {
        Player player1 = new Player("fedeBulfariGalattico", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        shipBoard1.initializeTestFlight();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(1,3));
        Tile tile2=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,4));
        Tile tile3=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile3.rotate();
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(2,5));
        Tile tile4=new Pipe( new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(2,4));
        Tile tile5=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        tile5.rotate();
        tile5.rotate();
        tile5.rotate();
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(2,2));
        Tile tile6=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(3,3));
        boolean result=shipBoard1.verifyCorrectness();
        //verifyCorrectness is needed for the shipboard stats setting
        System.out.println(shipBoard1.toString());
        assertTrue(result);
    }

    @Test
    void testShipBoard3() {
        Player player1 = new Player("MimmoPericoloso", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        shipBoard1.initializeLevel2();
        Tile tile1=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(1,3));
        Tile tile2=new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(2,2));
        Tile tile3=new EquipCabin( new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE));
        tile3.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(2,4));
        Tile tile4=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(2,5));
        Tile tile5=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),3);
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(3,2));
        Tile tile7=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),2);
        shipBoard1.positionTile(Optional.of(tile7), new Coordinates(3,3));
        Tile tile6=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(3,4));
        boolean result=shipBoard1.verifyCorrectness();
        //verifyCorrectness is needed for the shipboard stats setting
        System.out.println(shipBoard1.toString());
        assertTrue(result);
    }

    @Test
    void testShipBoard4() {
        Player player1 = new Player("MimmoPericoloso", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        shipBoard1.initializeTestFlight();
        Tile tile1=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(1,3));
        Tile tile2=new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(2,2));
        Tile tile3=new EquipCabin( new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE));
        tile3.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(2,4));
        Tile tile4=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(2,5));
        Tile tile5=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),3);
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(3,2));
        Tile tile7=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),2);
        shipBoard1.positionTile(Optional.of(tile7), new Coordinates(3,3));
        Tile tile6=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(3,4));
        boolean result=shipBoard1.verifyCorrectness();
        //verifyCorrectness is needed for the shipboard stats setting
        System.out.println(shipBoard1.toString());
        assertTrue(result);
    }

    @Test
    void testShipBoard5() {
        Player player1 = new Player("MimmoPericoloso", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(0,4));

        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE));
        tile2.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,1));
        Tile tile3=new AlienLifeSupportsSystem( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),CrewType.BROWN);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(1,2));
        Tile tile4=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(1,3));
        Tile tile5=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(1,4));
        Tile tile6=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(1,5));

        Tile tile7=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile7), new Coordinates(2,0));
        Tile tile8=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        tile8.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile8), new Coordinates(2,1));
        Tile tile9=new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        tile9.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile9), new Coordinates(2,2));
        Tile tile10=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile10), new Coordinates(2,4));
        Tile tile12=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        tile12.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile12), new Coordinates(2,5));
        Tile tile11=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile11), new Coordinates(2,6));

        Tile tile13=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE), 2);
        shipBoard1.positionTile(Optional.of(tile13), new Coordinates(3,0));
        Tile tile14=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        tile14.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile14), new Coordinates(3,2));
        Tile tile15=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile15), new Coordinates(3,3));
        Tile tile16=new CargoRed(1, new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE));
        shipBoard1.positionTile(Optional.of(tile16), new Coordinates(3,4));
        Tile tile17=new DoubleEngine( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE));
        shipBoard1.positionTile(Optional.of(tile17), new Coordinates(3,5));
        Tile tile18=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), 2);
        shipBoard1.positionTile(Optional.of(tile18), new Coordinates(3,6));

        Tile tile19=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), 2);
        shipBoard1.positionTile(Optional.of(tile19), new Coordinates(4,0));
        Tile tile20=new SingleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile20), new Coordinates(4,1));
        Tile tile21=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile21), new Coordinates(4,2));
        Tile tile22=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile22.rotate();
        tile22.rotate();
        shipBoard1.positionTile(Optional.of(tile22), new Coordinates(4,4));
        boolean result=shipBoard1.verifyCorrectness();
        //verifyCorrectness is needed for the shipboard stats setting
        System.out.println(shipBoard1.toString());
        assertTrue(result);
    }

    @Test
    void testOneTileCorrectShipboard() {
        Tile tile=new DoubleEngine(new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard.positionTile(Optional.of(tile), new Coordinates(2,3));
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
        Tile tile1=new DoubleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        tile1.rotate();
        shipBoard.positionTile(Optional.of(tile1), new Coordinates(3,3));
        //create e set tile 2
        assertFalse(shipBoard.verifyCorrectness());
    }

    @Test
    void testOneCannonStrength(){
        Tile tile1=new SingleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL));
        tile1.rotate();
        shipBoard.positionTile(Optional.of(tile1), new Coordinates(3,3));
        shipBoard.verifyCorrectness();
        assertTrue(shipBoard.getSingleCannonPower()==0.5);
    }

    @Test
    void testAddBreakSingleCannon(){
        shipBoard.addBreakSingleCannonPower(1);

        assertTrue(shipBoard.getSingleCannonPower()==1);
    }

    @Test
    void testOneBatteryComponent(){
        Tile tile1=new BatteryComponents(new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL), 2);
        tile1.rotate();
        shipBoard.positionTile(Optional.of(tile1), new Coordinates(3,3));
        shipBoard.verifyCorrectness();
        System.out.println(shipBoard.getNumBatteries());
        assertTrue(shipBoard.getNumBatteries()==2);
    }
    //getBatteryCoordinates
    //ChooseBatteryuse

    @Test
    void testOnePenaltyAdding(){
        Tile tile=new DoubleEngine(new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard.positionTile(Optional.of(tile), new Coordinates(3,3));
        tile.destroy();
        assertTrue(shipBoard.getPenalty()==1);
        return;
    }

    @Test
    void testOneTile4ExposedConnectors(){
        shipBoard.countExposedConnectors();
        assertTrue(shipBoard.getNumExposedConnectors()==4);
    }

    @Test
    void testShipboard1ExposedConnectors(){
        Player player1 = new Player("fedeBulfariGalattico", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(1,3));
        Tile tile2=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,4));
        Tile tile3=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile3.rotate();
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(2,5));
        Tile tile4=new Pipe( new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(2,4));
        Tile tile5=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        tile5.rotate();
        tile5.rotate();
        tile5.rotate();
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(2,2));
        Tile tile6=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(3,3));
        shipBoard1.countExposedConnectors();
        assertTrue(shipBoard1.getNumExposedConnectors()==4);

    }

    @Test
    void testShipboard3ExposedConnectors(){
        Player player1 = new Player("MimmoPericoloso", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        shipBoard1.initializeLevel2();
        Tile tile1=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(1,3));
        Tile tile2=new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(2,2));
        Tile tile3=new EquipCabin( new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE));
        tile3.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(2,4));
        Tile tile4=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(2,5));
        Tile tile5=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),3);
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(3,2));
        Tile tile7=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),2);
        shipBoard1.positionTile(Optional.of(tile7), new Coordinates(3,3));
        Tile tile6=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(3,4));
        shipBoard1.countExposedConnectors();
        assertTrue(shipBoard1.getNumExposedConnectors()==8);
    }

    @Test
    void testShipboard5ExposedConnectors(){
        Player player1 = new Player("MimmoPericoloso", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(0,4));

        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE));
        tile2.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,1));
        Tile tile3=new AlienLifeSupportsSystem( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),CrewType.BROWN);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(1,2));
        Tile tile4=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(1,3));
        Tile tile5=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(1,4));
        Tile tile6=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(1,5));

        Tile tile7=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile7), new Coordinates(2,0));
        Tile tile8=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        tile8.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile8), new Coordinates(2,1));
        Tile tile9=new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        tile9.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile9), new Coordinates(2,2));
        Tile tile10=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile10), new Coordinates(2,4));
        Tile tile12=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        tile12.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile12), new Coordinates(2,5));
        Tile tile11=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile11), new Coordinates(2,6));

        Tile tile13=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE), 2);
        shipBoard1.positionTile(Optional.of(tile13), new Coordinates(3,0));
        Tile tile14=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        tile14.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile14), new Coordinates(3,2));
        Tile tile15=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile15), new Coordinates(3,3));
        Tile tile16=new CargoRed(1, new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE));
        shipBoard1.positionTile(Optional.of(tile16), new Coordinates(3,4));
        Tile tile17=new DoubleEngine( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE));
        shipBoard1.positionTile(Optional.of(tile17), new Coordinates(3,5));
        Tile tile18=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), 2);
        shipBoard1.positionTile(Optional.of(tile18), new Coordinates(3,6));

        Tile tile19=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), 2);
        shipBoard1.positionTile(Optional.of(tile19), new Coordinates(4,0));
        Tile tile20=new SingleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile20), new Coordinates(4,1));
        Tile tile21=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile21), new Coordinates(4,2));
        Tile tile22=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile22.rotate();
        tile22.rotate();
        shipBoard1.positionTile(Optional.of(tile22), new Coordinates(4,4));
        shipBoard1.countExposedConnectors();
        assertTrue(shipBoard1.getNumExposedConnectors()==7);
    }


    @Test
    void ConnectedsetShipboard1(){
        Player player1 = new Player("fedeBulfariGalattico", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(1,3));
        Tile tile2=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,4));
        Tile tile3=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile3.rotate();
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(2,5));
        Tile tile4=new Pipe( new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(2,4));
        Tile tile5=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        tile5.rotate();
        tile5.rotate();
        tile5.rotate();
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(2,2));
        Tile tile6=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(3,3));
        Set<Coordinates> set = new HashSet<Coordinates>();
        set=shipBoard1.connectedSet( new Coordinates(2,3), set);
        //verifyCorrectness is needed for the shipboard stats setting

        assertTrue(set.size()==7);
    }

    @Test
    void DestroyAndSetNewShip_Shipboard1_ (){
            Player player1 = new Player("fedeBulfariGalattico", PlayersColor.BLUE);
            ShipBoard shipBoard1 = new ShipBoard(player1);
            shipBoard1.initializeLevel2();
            Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
            shipBoard1.positionTile(Optional.of(tile1), new Coordinates(1,3));
            Tile tile2=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
            shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,4));
            Tile tile3=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
            tile3.rotate();
            shipBoard1.positionTile(Optional.of(tile3), new Coordinates(2,5));
            Tile tile4=new Pipe( new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
            shipBoard1.positionTile(Optional.of(tile4), new Coordinates(2,4));
            Tile tile5=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
            tile5.rotate();
            tile5.rotate();
            tile5.rotate();
            shipBoard1.positionTile(Optional.of(tile5), new Coordinates(2,2));
            Tile tile6=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
            shipBoard1.positionTile(Optional.of(tile6), new Coordinates(3,3));
            boolean result=shipBoard1.verifyCorrectness();
            //verifyCorrectness is needed for the shipboard stats setting

            ArrayList<Set<Coordinates>> set = shipBoard1.destroyTile(new Coordinates(2, 4));
            assertTrue(set.size()==3);
        System.out.println(set.toString());

        shipBoard1.SetNewShip(set.get(1));
        System.out.println(shipBoard1.toString());

    }

    @Test
    void DestroyAndSetNewShip_Shipboard3_ (){
        Player player1 = new Player("MimmoPericoloso", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        shipBoard1.initializeLevel2();
        Tile tile1=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(1,3));
        Tile tile2=new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(2,2));
        Tile tile3=new EquipCabin( new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE));
        tile3.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(2,4));
        Tile tile4=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(2,5));
        Tile tile5=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),3);
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(3,2));
        Tile tile7=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),2);
        shipBoard1.positionTile(Optional.of(tile7), new Coordinates(3,3));
        Tile tile6=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(3,4));
        boolean result=shipBoard1.verifyCorrectness();
        //verifyCorrectness is needed for the shipboard stats setting

        ArrayList<Set<Coordinates>> set = shipBoard1.destroyTile(new Coordinates(3, 3));
        assertTrue(set.size()==1);
        System.out.println(set.toString());

    }

    @Test
    void pippo(){
        Player player1 = new Player("MimmoPericoloso", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        player1.setPlayerShip(shipBoard1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(0,4));
        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE));
        tile2.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,1));
        Tile tile3=new AlienLifeSupportsSystem( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),CrewType.BROWN);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(1,2));
        Tile tile4=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(1,3));
        Tile tile5=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(1,4));
        Tile tile6=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(1,5));
        Tile tile7=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile7), new Coordinates(2,0));
        Tile tile8=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        tile8.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile8), new Coordinates(2,1));
        Tile tile9=new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        tile9.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile9), new Coordinates(2,2));
        Tile tile10=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile10), new Coordinates(2,4));
        Tile tile12=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        tile12.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile12), new Coordinates(2,5));
        Tile tile11=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile11), new Coordinates(2,6));
        Tile tile13=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE), 2);
        shipBoard1.positionTile(Optional.of(tile13), new Coordinates(3,0));
        Tile tile14=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        tile14.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile14), new Coordinates(3,2));
        Tile tile15=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile15), new Coordinates(3,3));
        Tile tile16=new CargoRed(1, new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE));
        shipBoard1.positionTile(Optional.of(tile16), new Coordinates(3,4));
        Tile tile17=new DoubleEngine( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE));
        shipBoard1.positionTile(Optional.of(tile17), new Coordinates(3,5));
        Tile tile18=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), 2);
        shipBoard1.positionTile(Optional.of(tile18), new Coordinates(3,6));
        Tile tile19=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), 2);
        shipBoard1.positionTile(Optional.of(tile19), new Coordinates(4,0));
        Tile tile20=new SingleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile20), new Coordinates(4,1));
        Tile tile21=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile21), new Coordinates(4,2));
        Tile tile22=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile22.rotate();
        tile22.rotate();
        shipBoard1.positionTile(Optional.of(tile22), new Coordinates(4,4));
        shipBoard1.verifyCorrectness();

        shipBoard1.gainGoods(new Goods(GoodsColor.GREEN),new Coordinates(1,3));
        shipBoard1.gainGoods(new Goods(GoodsColor.BLUE),new Coordinates(1,3));
        shipBoard1.gainGoods(new Goods(GoodsColor.RED),new Coordinates(1,3));

        ArrayList<Goods> goods=new ArrayList<>();
        goods.add(new Goods(GoodsColor.GREEN));
        goods.add(new Goods(GoodsColor.BLUE));
        goods.add(new Goods(GoodsColor.RED));

        assertEquals(goods,shipBoard1.getSingleCargoGoods(new Coordinates(1,3)));
    }
    @Test
    void DestroyAndSetNewShip_Shipboard5_ (){
        Player player1 = new Player("MimmoPericoloso", PlayersColor.BLUE);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(0,4));

        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE));
        tile2.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,1));
        Tile tile3=new AlienLifeSupportsSystem( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),CrewType.BROWN);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(1,2));
        Tile tile4=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(1,3));
        Tile tile5=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(1,4));
        Tile tile6=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE));
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(1,5));

        Tile tile7=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile7), new Coordinates(2,0));
        Tile tile8=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        tile8.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile8), new Coordinates(2,1));
        Tile tile9=new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        tile9.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile9), new Coordinates(2,2));
        Tile tile10=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile10), new Coordinates(2,4));
        Tile tile12=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        tile12.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile12), new Coordinates(2,5));
        Tile tile11=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile11), new Coordinates(2,6));

        Tile tile13=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE), 2);
        shipBoard1.positionTile(Optional.of(tile13), new Coordinates(3,0));
        Tile tile14=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        tile14.setCrewType(CrewType.HUMAN);
        shipBoard1.positionTile(Optional.of(tile14), new Coordinates(3,2));
        Tile tile15=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile15), new Coordinates(3,3));
        Tile tile16=new CargoRed(1, new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE));
        shipBoard1.positionTile(Optional.of(tile16), new Coordinates(3,4));
        Tile tile17=new DoubleEngine( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE));
        shipBoard1.positionTile(Optional.of(tile17), new Coordinates(3,5));
        Tile tile18=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), 2);
        shipBoard1.positionTile(Optional.of(tile18), new Coordinates(3,6));

        Tile tile19=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), 2);
        shipBoard1.positionTile(Optional.of(tile19), new Coordinates(4,0));
        Tile tile20=new SingleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile20), new Coordinates(4,1));
        Tile tile21=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile21), new Coordinates(4,2));
        Tile tile22=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile22.rotate();
        tile22.rotate();
        shipBoard1.positionTile(Optional.of(tile22), new Coordinates(4,4));
        boolean result=shipBoard1.verifyCorrectness();
        //verifyCorrectness is needed for the shipboard stats setting
        ArrayList<Set<Coordinates>> set = shipBoard1.destroyTile(new Coordinates(2, 2));
        assertTrue(set.size()==2);
        System.out.println(set.toString());
    }

}
  
