package it.polimi.ingsw.galaxytruckerproject.model.player;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player1;
    private Player player2;
    private Player player3;
    private ShipBoard shipBoard1;
    private ShipBoard shipBoard2;
    private ShipBoard shipBoard3;

    @BeforeEach
    void setUp() {

        player1 = new Player("player 1", PlayersColor.BLUE);
        player2 = new Player("player 2", PlayersColor.RED);
        player3 = new Player("player 3", PlayersColor.YELLOW);

        //shipboard 5 to player1
        shipBoard1 = new ShipBoard(player1);
        player1.setPlayerShip(shipBoard1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(0,4));
        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,1));
        tile2.setCrewType(CrewType.HUMAN);
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
        shipBoard1.positionTile(Optional.of(tile8), new Coordinates(2,1));
        tile8.setCrewType(CrewType.HUMAN);
        Tile tile9=new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile9), new Coordinates(2,2));
        tile9.setCrewType(CrewType.HUMAN);
        Tile tile10=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile10), new Coordinates(2,4));
        Tile tile12=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile12), new Coordinates(2,5));
        tile12.setCrewType(CrewType.HUMAN);
        Tile tile11=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile11), new Coordinates(2,6));
        Tile tile13=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE), 2);
        shipBoard1.positionTile(Optional.of(tile13), new Coordinates(3,0));
        Tile tile14=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile14), new Coordinates(3,2));
        tile14.setCrewType(CrewType.HUMAN);
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
        Tile tile22=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile22.rotate();
        tile22.rotate();
        shipBoard1.positionTile(Optional.of(tile21), new Coordinates(4,2));
        shipBoard1.positionTile(Optional.of(tile22), new Coordinates(4,4));

        shipBoard1.verifyCorrectness();

        //shipboard4 to player2
        shipBoard2 = new ShipBoard(player2);
        player2.setPlayerShip(shipBoard2);
        shipBoard2.initializeLevel2();
        Tile tile23=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        shipBoard2.positionTile(Optional.of(tile23), new Coordinates(1,3));
        Tile tile24=new CargoRed(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        shipBoard2.positionTile(Optional.of(tile24), new Coordinates(2,2));
        Tile tile25=new EquipCabin( new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE));
        shipBoard2.positionTile(Optional.of(tile25), new Coordinates(2,4));
        tile25.setCrewType(CrewType.HUMAN);
        Tile tile27=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),3);
        shipBoard2.positionTile(Optional.of(tile27), new Coordinates(3,2));
        Tile tile28=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),2);
        shipBoard2.positionTile(Optional.of(tile28), new Coordinates(3,3));
        Tile tile29=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard2.positionTile(Optional.of(tile29), new Coordinates(3,4));
        shipBoard2.verifyCorrectness();

        //shipboard1 to player3
        shipBoard3 = new ShipBoard(player3);
        player3.setPlayerShip(shipBoard3);
        shipBoard3.initializeLevel2();
        Tile tile30=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard3.positionTile(Optional.of(tile30), new Coordinates(1,3));
        Tile tile31=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        shipBoard3.positionTile(Optional.of(tile31), new Coordinates(1,4));
        Tile tile32=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile32.rotate();
        shipBoard3.positionTile(Optional.of(tile32), new Coordinates(2,5));
        Tile tile33=new Pipe( new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard3.positionTile(Optional.of(tile33), new Coordinates(2,4));
        Tile tile34=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        tile34.rotate();
        tile34.rotate();
        tile34.rotate();
        shipBoard3.positionTile(Optional.of(tile34), new Coordinates(2,2));
        Tile tile35=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard3.positionTile(Optional.of(tile35), new Coordinates(3,3));
        shipBoard3.verifyCorrectness();
    }

    @Test
    void remove_crew_test() {
        int initial_crew = player1.getTotalCrew();
        player1.printCurrentInfoCabins();
        player1.removeCrew(player1.parseCoordinates(new String[]{"2", "1", "2", "1", "1", "1"}));
        player1.printCurrentInfoCabins();
        assertEquals(initial_crew - 3, player1.getTotalCrew());
    }

    //basic test
    @Test
    void use_double_cannon_test1() {
        float base_firepower = player1.useCannons(new ArrayList<>());
        assertEquals(base_firepower, 3.5);
        int initial_batteries = player1.getShipBoard().getNumBatteries();
        player1.useCannons(player1.parseCoordinates(new String []{"2", "0", "2", "6"}));
        float after_2_double_cannons = player1.useDoubleCannons(player1.parseCoordinates(new String[]{"3", "6", "3", "6"}));
        assertEquals(base_firepower + 4, after_2_double_cannons);
        assertEquals(initial_batteries - 2, player1.getShipBoard().getNumBatteries());
    }

    //basic test
    @Test
    void use_double_engine_test1() {
        float base_enginestrength = player1.useDoubleEngines(new ArrayList<>());
        int initial_batteries = player1.getShipBoard().getNumBatteries();
        assertEquals(base_enginestrength , 1);
        player1.useDoubleEngines(player1.parseCoordinates(new String []{"4", "2", "3", "5"}));
        player1.printCurrentInfoEngines();
        if (player1.getShipBoard().getDoubleEngine().isEmpty())
            System.out.println("is empty");
        int after_2_double_engines = player1.useDoubleEngines(player1.parseCoordinates(new String[]{"3", "0", "3", "0"}));
        assertEquals(base_enginestrength + 4, after_2_double_engines);
        assertEquals(initial_batteries - 2, player1.getShipBoard().getNumBatteries());

    }



}