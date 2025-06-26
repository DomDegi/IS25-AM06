package it.polimi.ingsw.galaxytruckerproject.model.player;

import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
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
        Tile tile9=new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile9), new Coordinates(2,2));
        Tile tile10=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile10), new Coordinates(2,4));
        Tile tile12=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile12), new Coordinates(2,5));
        Tile tile11=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        shipBoard1.positionTile(Optional.of(tile11), new Coordinates(2,6));
        Tile tile13=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE), 2);
        shipBoard1.positionTile(Optional.of(tile13), new Coordinates(3,0));
        Tile tile14=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
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
        player1.setAllCrewToHuman();
        int initial_crew = player1.getTotalCrew();
        player1.printCurrentInfoCabins();
        ArrayList<Coordinates> coord = new ArrayList<>();
        coord.add(new Coordinates(2,1));
        coord.add(new Coordinates(2,1));
        coord.add(new Coordinates(1,1));
        player1.removeCrew(coord);
        player1.printCurrentInfoCabins();
        assertEquals(initial_crew - 3, player1.getTotalCrew());
    }

    //basic test
    @Test
    void use_double_cannon_test1() {
        Map<Float,ArrayList<Tile>> returnedMap = player1.useCannons(0F, new ArrayList<>());
        float base_firepower = returnedMap.keySet().iterator().next();
        ArrayList<Tile> tiles = returnedMap.get(base_firepower);
        assertEquals(3.5, base_firepower);
        int initial_batteries = player1.getShipBoard().getNumBatteries();
        ArrayList<Coordinates> coord = new ArrayList<>(); coord.add(new Coordinates(3,0)); coord.add(new Coordinates(4,0));
        Map<Float,ArrayList<Tile>> returnedMap2 = player1.useCannons(4F, coord);
        float after_2_doubleCannons =  returnedMap2.keySet().iterator().next();
        ArrayList<Tile> tiles2 = returnedMap2.get(after_2_doubleCannons);
        assertEquals(base_firepower + 4, after_2_doubleCannons);
        assertEquals(initial_batteries - 2, player1.getShipBoard().getNumBatteries());
        assertEquals(tiles2.get(0).getCoordinates(), coord.get(0));
        assertEquals(tiles2.get(1).getCoordinates(), coord.get(1));
    }

    //basic test
    @Test
    void use_double_engine_test1() {
        Map<Integer,ArrayList<Tile>> returned0 = player1.useEngines(0, new ArrayList<>());
        int base_enginestrength = returned0.keySet().iterator().next();
        int initial_batteries = player1.getShipBoard().getNumBatteries();
        assertEquals(1, base_enginestrength);
        ArrayList<Coordinates>  coord = new ArrayList<>(); coord.add(new Coordinates(3,0)); coord.add(new Coordinates(3,0));
        Map<Integer,ArrayList<Tile>> returned1 = player1.useEngines(2, coord);
        player1.printCurrentInfoEngines();
        if (player1.getShipBoard().getDoubleEngine().isEmpty())
            System.out.println("is empty");
        int after_2_double_engines = returned1.keySet().iterator().next();
        ArrayList<Tile> tiles2 = returned1.get(after_2_double_engines);
        assertEquals(base_enginestrength + 4, after_2_double_engines);
        assertEquals(initial_batteries - 2, player1.getShipBoard().getNumBatteries());
        assertEquals(tiles2.get(0).getCoordinates(), coord.get(0));
        assertEquals(tiles2.get(1).getCoordinates(), coord.get(1));
    }

    @Test
    void automatic_crew_choice() {
        assertEquals(2, player1.getTotalCrew());
        player1.setAllCrewToHuman();
        assertEquals(12, player1.getTotalCrew());
    }

    @Test
    void crew_choice_one_is_brown_alien() {
        assertEquals(2, player1.getTotalCrew());
        ArrayList<Tile> tiles = new ArrayList<>();
        EquipCabin newCabin0 = new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE));
        newCabin0.setCoordinates(new Coordinates(1,1));
        newCabin0.setCrewTypeOfTestTile(CrewType.BROWN);
        EquipCabin newCabin1 =new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        newCabin1.setCoordinates(new Coordinates(2,1));
        newCabin1.setCrewTypeOfTestTile(CrewType.HUMAN);
        EquipCabin newCabin2 = new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
        newCabin2.setCoordinates(new Coordinates(2,2));
        newCabin2.setCrewTypeOfTestTile(CrewType.BROWN);
        tiles.add(newCabin0); tiles.add(newCabin1); tiles.add(newCabin2);
        assertTrue(player1.verifyAndSetupCrew(tiles));
        assertEquals(CrewType.BROWN, player1.getShipBoard().getTile(new Coordinates(1,1)).getCrewType());
        assertEquals(CrewType.BROWN, player1.getShipBoard().getTile(new Coordinates(2,2)).getCrewType());
        assertEquals(CrewType.HUMAN, player1.getShipBoard().getTile(new Coordinates(2,1)).getCrewType());
        assertEquals(6, player1.getTotalCrew());
        assertEquals(2, player1.getShipBoard().getNumBrownAliens());
        assertEquals(4, player1.getShipBoard().getNumHumanCrew());
    }

    @Test
    void removeGoods_test() {
        player1.getShipBoard().gainGoods(new Goods(GoodsColor.RED), new Coordinates(3,4));
        player1.getShipBoard().gainGoods(new Goods(GoodsColor.YELLOW), new Coordinates(1,3));
        player1.getShipBoard().gainGoods(new Goods(GoodsColor.BLUE), new Coordinates(1,3));
        player1.getShipBoard().gainGoods(new Goods(GoodsColor.GREEN), new Coordinates(1,3));
        player1.getShipBoard().gainGoods(new Goods(GoodsColor.YELLOW), new Coordinates(1,4));
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        coordinates.add(new  Coordinates(3,4));
        coordinates.add(new  Coordinates(1,3));
        coordinates.add(new Coordinates(1,4));
        coordinates.add(new Coordinates(1,3));
        player1.removeGoods(coordinates);
        assertEquals(1,player1.getShipBoard().getAllGoods().size());
        assertTrue(player1.getShipBoard().getTile(new Coordinates(3,4)).getCargo().isEmpty());
        assertEquals(GoodsColor.BLUE, player1.getShipBoard().getAllGoods().getFirst().getColor());
    }

    @Test
    void addCredit(){
        player1.addCredit(10);
        assertEquals(10, player1.getCredit());
        player1.removeCredit(5);
        assertEquals(5, player1.getCredit());
    }

    @Test
    void playerReconnects() {
        player1.playerReconnects();
        assertEquals(false,player1.isDisconnected());
    }
}