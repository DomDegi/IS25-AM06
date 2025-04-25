package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.LargeCannonShot;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.LargeMeteor;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.SmallMeteor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ProjectilePenaltyTest {

    private Player player;
    private ProjectilePenalty penalty;
    private ArrayList<Projectile> listOfMeteors1;
    private ArrayList<Projectile> listOfMeteorsFull;
    private ArrayList<Projectile> listOfLargeCannonShot1;
    private ShipBoard shipBoard;
    private Game gameLvl2 = new Game(GameMode.LEVEL2, 4);
    private Game gameTrial = new Game(GameMode.TRIAL, 3);
    private ShipBoard shipBoard1;
    private Player player1;
    private VirtualView mockView = mock(VirtualView.class);

    @BeforeEach
    void setUp() {

        //setupShipBoard made of only single cannons
        player = new Player("dummy", PlayersColor.RED);
        shipBoard = new ShipBoard(player);
        player.setPlayerShip(shipBoard);
        shipBoard.initializeLevel2();
        SingleCannon singleCannonN = new SingleCannon(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.UNIVERSAL), new Link(Connectors.SMOOTH),0);
        SingleCannon singleCannonE = new SingleCannon(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.UNIVERSAL),0);
        SingleCannon singleCannonS = new SingleCannon(new Link(Connectors.UNIVERSAL), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH),0);
        SingleCannon singleCannonW = new SingleCannon(new Link(Connectors.SMOOTH), new Link(Connectors.UNIVERSAL), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH),0);

        shipBoard.positionTile(Optional.of(singleCannonN), new Coordinates(3, 1));
        shipBoard.positionTile(Optional.of(singleCannonE), new Coordinates(4, 2));
        shipBoard.positionTile(Optional.of(singleCannonS), new Coordinates(3, 3));
        shipBoard.positionTile(Optional.of(singleCannonW), new Coordinates(2, 2));



        player1= new  Player("MimmoPericoloso", PlayersColor.BLUE);
        shipBoard1=new ShipBoard(player1);
        shipBoard1.initializeLevel2();
        Tile tile1=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(1,3));
        Tile tile2=new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(2,2));
        Tile tile3=new EquipCabin( new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(2,4));
        tile3.setCrewType(CrewType.HUMAN);
        Tile tile4=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(2,5));
        Tile tile5=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),0,3);
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(3,2));
        Tile tile7=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),0,2);
        shipBoard1.positionTile(Optional.of(tile7), new Coordinates(3,3));
        Tile tile6=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(3,4));
        boolean result=shipBoard1.verifyCorrectness();

        listOfMeteors1 = new ArrayList<>(List.of(new LargeMeteor(Direction.NORTH)));

        listOfMeteorsFull = new ArrayList<>(Arrays.asList(
                new LargeMeteor(Direction.NORTH),
                new SmallMeteor(Direction.NORTH),
                new LargeMeteor(Direction.EAST),
                new SmallMeteor(Direction.EAST),
                new LargeMeteor(Direction.SOUTH),
                new SmallMeteor(Direction.SOUTH),
                new LargeMeteor(Direction.WEST),
                new SmallMeteor(Direction.WEST)
        ));

        listOfLargeCannonShot1 = new ArrayList<>(List.of(new LargeCannonShot(Direction.NORTH)));
    }

    @Test
    void large_meteor_from_north_on_single_cannon() {
        //System.out.println(shipBoard.toString());
        //System.out.println("Lancio meteore");
        String[] input = {};
        penalty = new ProjectilePenalty(listOfMeteors1);
        penalty.setDiceRoll(1);
        boolean returnValue = penalty.initializePenalty(gameLvl2, mockView, player);
        assertFalse(returnValue);
    }


    @Test
    void largeCannonShot_from_north_on_small_cannon() {
        System.out.println(shipBoard.toString());
        System.out.println("Lancio cannonShot");
        penalty = new ProjectilePenalty(listOfLargeCannonShot1);
        penalty.setDiceRoll(4);
        boolean returnValue = penalty.initializePenalty(gameLvl2, mockView, player);
        assertFalse(returnValue);
        System.out.println(shipBoard.toString());
    }

    @Test
    void ListOfMeteorsFull_test() {
        System.out.println(shipBoard.toString());
        System.out.println("Lancio cannonShot");
        penalty = new ProjectilePenalty(listOfMeteorsFull);
        boolean returnValue = penalty.initializePenalty(gameLvl2, mockView, player);
        penalty.randomRollForOne(mockView, player);
        System.out.println(shipBoard.toString());
        penalty.printInfoOnAllProjectiles();
    }

    @Test
    void large_meteor_from_north_on_double_cannon_activate_Battery() {
        //System.out.println(shipBoard.toString());
        //System.out.println("Lancio meteore");
        int initialMeteorsCount = penalty.getListOfProjectiles().size();
        ArrayList<Coordinates> coordinates=new ArrayList<>();
        coordinates.add(new Coordinates(3,3));
        penalty = new ProjectilePenalty(listOfMeteors1);
        penalty.setDiceRoll(7);
        //modified input method to use battery in this test
        boolean returnValue = penalty.initializePenalty(gameLvl2, mockView,player1);
        assertTrue(returnValue);
        penalty.playerUsesBatteryToDefend(player1,coordinates);
        assertEquals(1, shipBoard1.getTile(3,3).getNumBatteries());
        assertEquals(initialMeteorsCount - 1, penalty.getListOfProjectiles().size());
    }
    @Test
    void large_meteor_from_north_choose_shipboard_to_mantain() {
        //System.out.println(shipBoard.toString());
        //System.out.println("Lancio meteore");
        shipBoard1.destroyTile(new Coordinates(1,3));
        int initialMeteorsCount = penalty.getListOfProjectiles().size();
        ArrayList<Coordinates> coordinates=new ArrayList<>();
        coordinates.add(new Coordinates(3,3));
        penalty = new ProjectilePenalty(listOfMeteors1);
        penalty.setDiceRoll(8);
        //modified input method to use battery in this test
        boolean returnValue = penalty.initializePenalty(gameLvl2, mockView,player1);
        assertTrue(returnValue);
        penalty.chooseToMaintain(player1,coordinates);
        assertEquals(shipBoard1.getTilesTable()[2][4],Optional.empty());
        assertEquals(shipBoard1.getTilesTable()[2][5],Optional.empty());
        System.out.println(shipBoard1.toString());
    }



}