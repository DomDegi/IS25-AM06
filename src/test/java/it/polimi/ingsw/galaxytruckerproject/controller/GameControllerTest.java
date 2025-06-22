package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import it.polimi.ingsw.galaxytruckerproject.network.MockVirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor.*;
import static org.junit.jupiter.api.Assertions.*;


class GameControllerTest {
    MultiGameController newMultiGameController = new MultiGameController();
    GameController gameController;
    GameController trialController;
    Game game;
    Game trialGame;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    MockVirtualView view1 = new MockVirtualView();
    MockVirtualView view2 = new MockVirtualView();
    MockVirtualView view3 = new MockVirtualView();
    MockVirtualView view4 = new MockVirtualView();
    Controller controller1;
    Controller controller2;
    Controller controller3;
    Controller controller4;

    @BeforeEach
    void setUp() {
        game = new Game(GameMode.LEVEL2, 4);
        trialGame = new Game(GameMode.TRIAL, 4);
        this.gameController = new GameController(game,"test");
        this.trialController = new GameController(trialGame,"trialTest");
        controller1 = new Controller("p1",gameController,view1);
        controller2 = new Controller("p2",gameController,view2);
        controller3 = new Controller("p3",gameController,view3);
        controller4 = new Controller("p4",gameController,view4);
        player1 = new Player("p1", RED);
        player2 = new Player("p2", YELLOW);
        player3 = new Player("p3", GREEN);
        player4 = new Player("p4", BLUE);
    }

    @Test
    void player_addition_test_trial() {
        trialController.addToPlayersViewMap(player1.getPlayerName(), view1, false);
        assertTrue(trialController.checkColorAvailable(player1.getPlayerName(),view1,RED));
        trialController.playerAddition("p1", RED);
        assertEquals(1, trialController.getActivePlayers().size());
        trialController.addToPlayersViewMap(player2.getPlayerName(), view2, false);
        assertTrue(trialController.checkColorAvailable(player2.getPlayerName(),view2,YELLOW));
        trialController.playerAddition("p2", YELLOW);
        assertEquals(2, trialController.getActivePlayers().size());
        trialController.addToPlayersViewMap(player3.getPlayerName(), view3, false);
        assertTrue(trialController.checkColorAvailable(player3.getPlayerName(),view3,GREEN));
        trialController.playerAddition("p3", GREEN);
        assertEquals(3, trialController.getActivePlayers().size());
        trialController.addToPlayersViewMap(player4.getPlayerName(), view4, false);
        assertTrue(trialController.checkColorAvailable(player4.getPlayerName(),view4,BLUE));
        trialController.playerAddition("p4", BLUE);
        assertEquals(4, trialController.getActivePlayers().size());
    }

    @Test
    void setupShips() {
        set_player_pointers();
        ShipBoard shipBoard1 = new ShipBoard(player1);
        player1.setPlayerShip(shipBoard1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH));
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(0,4));
        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE));
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,1));
        tile2.setCrewType(CrewType.HUMAN);
        Tile tile3=new AlienLifeSupportsSystem( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),CrewType.BROWN);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(1,2));
        Tile tile4=new CargoRed(3, new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
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
        shipBoard1.positionTile(Optional.of(tile21), new Coordinates(4,2));
        Tile tile22=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE));
        tile22.rotate();
        tile22.rotate();
        shipBoard1.positionTile(Optional.of(tile22), new Coordinates(4,4));

        //shipboard4 to player2
        ShipBoard shipBoard2 = new ShipBoard(player2);
        player2.setPlayerShip(shipBoard2);
        shipBoard2.initializeLevel2();
        Tile tile23=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH));
        shipBoard2.positionTile(Optional.of(tile23), new Coordinates(1,3));
        Tile tile24=new CargoRed(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        shipBoard2.positionTile(Optional.of(tile24), new Coordinates(2,2));
        Tile tile25=new EquipCabin( new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE));
        shipBoard2.positionTile(Optional.of(tile25), new Coordinates(2,4));
        tile25.setCrewType(CrewType.HUMAN);
        Tile tile26=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL));
        shipBoard2.positionTile(Optional.of(tile26), new Coordinates(2,5));
        Tile tile27=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),3);
        shipBoard2.positionTile(Optional.of(tile27), new Coordinates(3,2));
        Tile tile28=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),2);
        shipBoard2.positionTile(Optional.of(tile28), new Coordinates(3,3));
        Tile tile29=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE));
        shipBoard2.positionTile(Optional.of(tile29), new Coordinates(3,4));

        //shipboard1 to player3
        ShipBoard shipBoard3 = new ShipBoard(player3);
        player3.setPlayerShip(shipBoard3);
        shipBoard3.initializeLevel2();
        Tile tile30=new CargoBlue(3, new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
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
    }

    @Test
    void set_player_pointers_trial() {
        player_addition_test_trial();
        player1 = trialController.getActivePlayers().get("p1");
        player2 = trialController.getActivePlayers().get("p2");
        player3 = trialController.getActivePlayers().get("p3");
        player4 = trialController.getActivePlayers().get("p4");
    }

    @Test
    void player_addition_test() {
        gameController.addToPlayersViewMap(player1.getPlayerName(), view1, false);
        assertTrue(gameController.checkColorAvailable(player1.getPlayerName(),view1,RED));
        controller1.chooseColor(RED);
        assertEquals(1, gameController.getActivePlayers().size());
        gameController.addToPlayersViewMap(player2.getPlayerName(), view2, false);
        assertTrue(gameController.checkColorAvailable(player2.getPlayerName(),view2,YELLOW));
        controller2.chooseColor(YELLOW);
        assertEquals(2, gameController.getActivePlayers().size());
        gameController.addToPlayersViewMap(player3.getPlayerName(), view3, false);
        assertTrue(gameController.checkColorAvailable(player3.getPlayerName(),view3,GREEN));
        controller3.chooseColor(GREEN);
        assertEquals(3, gameController.getActivePlayers().size());
        gameController.addToPlayersViewMap(player4.getPlayerName(), view4, false);
        assertTrue(gameController.checkColorAvailable(player4.getPlayerName(),view4,BLUE));
        controller4.chooseColor(BLUE);
        assertEquals(4, gameController.getActivePlayers().size());
    }

    @Test
    void set_player_pointers() {
        player_addition_test();
        player1 = gameController.getActivePlayers().get("p1");
        player2 = gameController.getActivePlayers().get("p2");
        player3 = gameController.getActivePlayers().get("p3");
        player4 = gameController.getActivePlayers().get("p4");
    }

    @Test
    void player_draws_tile_test() {
        set_player_pointers();
        controller1.turnHourglass();
        controller1.drawTileFromStack();
        Tile drawnBy1 = player1.getDrawnTile();
        assertNotNull(drawnBy1);
        controller1.refuseTile();
        assertFalse(game.getTurnedTiles().isEmpty());
        assertNotNull(game.identifyPlayerByName(player2.getPlayerName()));
        controller2.drawTileFromTurned(drawnBy1.getKey());
        assertNotNull(player2.getDrawnTile());
    }

    @Test
    void player_draws_cards_ship() {
        set_player_pointers();
        controller1.turnHourglass();
        controller1.lookGameCards(1);
        assertEquals(1, gameController.getLockedSmallDecks().size());
        controller2.lookGameCards(1);
        assertEquals(1,gameController.getLockedSmallDecks().size());
        controller1.stopLookingAtCards();
        assertEquals(0,gameController.getLockedSmallDecks().size());
    }

    @Test
    void player_positions_tile() {
        player_draws_tile_test();
        Tile toSet = player2.getDrawnTile();
        toSet.setCoordinates(new Coordinates(1,3));
        controller2.setTile(toSet);
        assertEquals(player2.getShipBoard().getTile(new Coordinates(1,3)),toSet);
    }

    @Test
    void booked_test() {
        player_draws_tile_test();
        Tile tile = player2.getDrawnTile();
        controller2.bookTile();
        tile.setCoordinates(new Coordinates(1,3));
        controller2.setTile(tile);
        assertEquals(player2.getShipBoard().getTile(new Coordinates(1,3)),tile);
        assertTrue(player2.getShipBoard().getBookedTiles().isEmpty());
    }

    @Test
    void completed_ship_test_trial() {
        set_player_pointers_trial();
        trialController.turnHourglass(player1.getPlayerName());
        trialController.completed(player1.getPlayerName(), view1);
        trialController.completed(player2.getPlayerName(), view2);
        trialController.completed(player3.getPlayerName(), view3);
        trialController.completed(player4.getPlayerName(), view4);
        //Goes in draw card because ships don't have errors
        assertEquals(GameState.DRAW_CARD, trialController.getGameState());
    }

    @Test
    void completed_ship_test_level2_and_position() {
        setupShips();
        controller1.turnHourglass();
        controller1.completedShip();
        controller1.setPosition(1);
        controller2.completedShip();
        controller2.setPosition(2);
        controller3.completedShip();
        controller3.setPosition(3);
        controller4.completedShip();
        controller4.setPosition(4);
        //Goes in draw card because ships don't have errors
        assertEquals(4, game.getFlightBoard().getInGamePlayers().size());
    }



    @Test
    void drawCard() {
        completed_ship_test_level2_and_position();
        controller1.drawCard();
        assertEquals(GameState.CARD_EVENT, gameController.getGameState());
    }

    // Ship Error Management with just player 1 with errors
    @Test
    void shipErrorManagement_just_one_tile() {
        completed_ship_test_level2_and_position();
        ArrayList<Coordinates> toRemove = new ArrayList<>();
        toRemove.add(new Coordinates(0,4));
        controller1.shipErrorManagement(toRemove);
        assertEquals(GameState.VERIFY_SHIP_CORRECTNESS, gameController.getGameState());
        for (Player player: gameController.getGame().getListOfInFlightPlayers()) {
            assertFalse(player.getShipBoard().isCompleted());
        }
    }

    @Test
    void playersPickCrewMembers() {
        shipErrorManagement_just_one_tile();
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
        EquipCabin newCabin3 = new EquipCabin(new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH));
        newCabin3.setCoordinates(new Coordinates(3,2));
        newCabin3.setCrewTypeOfTestTile(CrewType.HUMAN);
        EquipCabin newCabin4 =new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL));
        newCabin4.setCoordinates(new Coordinates(2,5));
        newCabin4.setCrewTypeOfTestTile(CrewType.HUMAN);
        tiles.add(newCabin0); tiles.add(newCabin1); tiles.add(newCabin2);  tiles.add(newCabin3); tiles.add(newCabin4);
        controller1.pickCrewMembers(tiles);
        tiles.clear();
        EquipCabin newCabin5 = new EquipCabin( new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE));
        newCabin5.setCoordinates(new Coordinates(2,4));
        newCabin5.setCrewTypeOfTestTile(CrewType.HUMAN);
        tiles.add(newCabin5);
        controller2.pickCrewMembers(tiles);
        controller3.pickCrewMembers(new ArrayList<>());
        controller4.pickCrewMembers(new ArrayList<>());
        for (Player player: gameController.getGame().getListOfInFlightPlayers()) {
            if (!player.getShipBoard().isCompleted()) {
                System.out.println(player.getShipBoard());
            }
        }
        assertEquals(gameController.getGameState(),GameState.DRAW_CARD);
    }


}