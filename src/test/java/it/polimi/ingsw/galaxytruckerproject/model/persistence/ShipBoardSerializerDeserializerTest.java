package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShipBoardSerializerDeserializerTest {

    String fileName = "serializerTest";
    Player player = new Player("Giorgio", PlayersColor.RED);
    ShipBoardSerializerDeserializer shipBoardSerializer;
    Player playerCopy = new Player("Copy", PlayersColor.RED);
    TUI shipPrinter = new TUI();

    @BeforeEach
    void setUp() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            // This will clear the file content by opening it in overwrite mode
            writer.write("");  // Optional: explicitly write to ensure file is cleared.
        } catch (IOException e) {
            System.out.println("Error while clearing file: " + e.getMessage());
        }

        Goods red = new Goods(GoodsColor.RED);
        Goods yellow = new Goods(GoodsColor.YELLOW);
        Goods green = new Goods(GoodsColor.GREEN);
        Goods blue = new Goods(GoodsColor.BLUE);

        ShipBoard shipBoard1 = player.getShipBoard();
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
        tile2.setCrewType(CrewType.BROWN);
        tile9.setCrewType(CrewType.BROWN);
        tile12.setCrewType(CrewType.HUMAN);
        tile8.setCrewType(CrewType.HUMAN);
        tile12.removeCrew();
        shipBoard1.getTile(new Coordinates(2,3)).removeCrew();

        tile4.addGood(blue); tile4.addGood(green); tile4.addGood(yellow);
        tile16.addGood(red);
    }

    @Test
    void save_test() {
        shipBoardSerializer = new ShipBoardSerializerDeserializer(fileName);
        shipBoardSerializer.save(player.getShipBoard());
    }

    @Test
    void load_test() {
        save_test();
        int expectedEndOfLine = 35;
        int trueEndOfLine = shipBoardSerializer.load(playerCopy, 0);
        assertEquals(expectedEndOfLine, trueEndOfLine);
    }

    @Test
    void print_test() {
        load_test();
        shipPrinter.printShipboard(new LightShipBoard(player.getShipBoard()));
        shipPrinter.printShipboard(new LightShipBoard(playerCopy.getShipBoard()));
        System.out.println("actual " + player.getTotalCrew());
        System.out.println("copy " + playerCopy.getTotalCrew());
    }

    @Test
    void crew_test() {
        load_test();
        assertEquals(player.getShipBoard().getNumBrownAliens(), playerCopy.getShipBoard().getNumBrownAliens());
        assertEquals(player.getShipBoard().getNumPurpleAliens(), playerCopy.getShipBoard().getNumPurpleAliens());
        assertEquals(player.getShipBoard().getNumHumanCrew(), playerCopy.getShipBoard().getNumHumanCrew());
        assertEquals(player.getTotalCrew(),playerCopy.getTotalCrew());
    }

    @Test
    void print_stuff_to_check() {
        load_test();
        player.printCurrentInfoCabins();
        playerCopy.printCurrentInfoCabins();

        player.printCurrentInfoEngines();
        playerCopy.printCurrentInfoEngines();

        player.printCurrentInfoCannons();
        playerCopy.printCurrentInfoCannons();

        player.printCurrentInfoCargoHolds();
        playerCopy.printCurrentInfoCargoHolds();

        player.printCurrentInfoBatteries();
        playerCopy.printCurrentInfoBatteries();
    }

}