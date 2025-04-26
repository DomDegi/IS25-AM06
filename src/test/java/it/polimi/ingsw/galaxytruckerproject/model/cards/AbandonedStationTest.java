package it.polimi.ingsw.galaxytruckerproject.model.cards;

import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import it.polimi.ingsw.galaxytruckerproject.network.MockVirtualView;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbandonedStationTest {
    private AbandonedStation abandonedStation;
    private Game game;
    private Player player1;
    private Player player2;
    private Player player3;
    private final VirtualView mockView1 = new MockVirtualView();
    private final VirtualView mockView2 = new MockVirtualView();
    private final VirtualView mockView3 = new MockVirtualView();
    Map<String,VirtualView> viewMap = new HashMap<>();
    private FlightBoard flightBoard;

    @BeforeEach
    void setUp() {
        game = new Game(GameMode.LEVEL2, 3);
        flightBoard = game.getFlightBoard();
        player1 = new Player("MimmoPericoloso", PlayersColor.BLUE);
        player2 = new Player("FedeGalattico", PlayersColor.RED);
        player3 = new Player("EnnioVolante", PlayersColor.YELLOW);
        viewMap.put("MimmoPericoloso", mockView1);
        viewMap.put("FedeGalattico", mockView2);
        viewMap.put("EnnioVolante", mockView3);
        Goods good1=new Goods(BLUE);
        Goods good2=new Goods(RED);
        Goods good3=new Goods(GREEN);
        Goods good4=new Goods(YELLOW);
        ArrayList<Goods> goodsList = new ArrayList<>();
        goodsList.add(good1);
        goodsList.add(good2);
        goodsList.add(good3);
        goodsList.add(good4);
        goodsList.add(good1);
        goodsList.add(good1);
        abandonedStation = new AbandonedStation(1, 2, 3,goodsList );

        //shipboard 5 to player1
        ShipBoard shipBoard1 = new ShipBoard(player1);
        player1.setPlayerShip(shipBoard1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile1), new Coordinates(0,4));
        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile2), new Coordinates(1,1));
        tile2.setCrewType(CrewType.HUMAN);
        Tile tile3=new AlienLifeSupportsSystem( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),0,CrewType.BROWN);
        shipBoard1.positionTile(Optional.of(tile3), new Coordinates(1,2));
        Tile tile4=new CargoRed(3, new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile4), new Coordinates(1,3));
        Tile tile5=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile5), new Coordinates(1,4));
        Tile tile6=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),0);
        shipBoard1.positionTile(Optional.of(tile6), new Coordinates(1,5));
        Tile tile7=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile7), new Coordinates(2,0));
        Tile tile8=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile8), new Coordinates(2,1));
        tile8.setCrewType(CrewType.HUMAN);
        Tile tile9=new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile9), new Coordinates(2,2));
        tile9.setCrewType(CrewType.HUMAN);
        Tile tile10=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard1.positionTile(Optional.of(tile10), new Coordinates(2,4));
        Tile tile12=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard1.positionTile(Optional.of(tile12), new Coordinates(2,5));
        tile12.setCrewType(CrewType.HUMAN);
        Tile tile11=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard1.positionTile(Optional.of(tile11), new Coordinates(2,6));
        Tile tile13=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),0, 2);
        shipBoard1.positionTile(Optional.of(tile13), new Coordinates(3,0));
        Tile tile14=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile14), new Coordinates(3,2));
        tile14.setCrewType(CrewType.HUMAN);
        Tile tile15=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),0);
        shipBoard1.positionTile(Optional.of(tile15), new Coordinates(3,3));
        Tile tile16=new CargoRed(1, new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),0);
        shipBoard1.positionTile(Optional.of(tile16), new Coordinates(3,4));
        Tile tile17=new DoubleEngine( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),0);
        shipBoard1.positionTile(Optional.of(tile17), new Coordinates(3,5));
        Tile tile18=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),0, 2);
        shipBoard1.positionTile(Optional.of(tile18), new Coordinates(3,6));
        Tile tile19=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),0, 2);
        shipBoard1.positionTile(Optional.of(tile19), new Coordinates(4,0));
        Tile tile20=new SingleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile20), new Coordinates(4,1));
        Tile tile21=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        shipBoard1.positionTile(Optional.of(tile21), new Coordinates(4,2));
        Tile tile22=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),0);
        tile22.rotate();
        tile22.rotate();
        shipBoard1.positionTile(Optional.of(tile22), new Coordinates(4,4));
        shipBoard1.verifyCorrectness();

        //shipboard4 to player2
        ShipBoard shipBoard2 = new ShipBoard(player2);
        player2.setPlayerShip(shipBoard2);
        shipBoard2.initializeLevel2();
        Tile tile23=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),0);
        shipBoard2.positionTile(Optional.of(tile23), new Coordinates(1,3));
        Tile tile24=new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),0);
        shipBoard2.positionTile(Optional.of(tile24), new Coordinates(2,2));
        Tile tile25=new EquipCabin( new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),0);
        shipBoard2.positionTile(Optional.of(tile25), new Coordinates(2,4));
        tile25.setCrewType(CrewType.HUMAN);
        Tile tile26=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard2.positionTile(Optional.of(tile26), new Coordinates(2,5));
        Tile tile27=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),0,3);
        shipBoard2.positionTile(Optional.of(tile27), new Coordinates(3,2));
        Tile tile28=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),0,2);
        shipBoard2.positionTile(Optional.of(tile28), new Coordinates(3,3));
        Tile tile29=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        shipBoard2.positionTile(Optional.of(tile29), new Coordinates(3,4));
        shipBoard2.verifyCorrectness();

        //shipboard1 to player3
        ShipBoard shipBoard3 = new ShipBoard(player3);
        player3.setPlayerShip(shipBoard3);
        shipBoard3.initializeLevel2();
        Tile tile30=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),0);
        shipBoard3.positionTile(Optional.of(tile30), new Coordinates(1,3));
        Tile tile31=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),0);
        shipBoard3.positionTile(Optional.of(tile31), new Coordinates(1,4));
        Tile tile32=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),0);
        tile32.rotate();
        shipBoard3.positionTile(Optional.of(tile32), new Coordinates(2,5));
        Tile tile33=new Pipe( new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),0);
        shipBoard3.positionTile(Optional.of(tile33), new Coordinates(2,4));
        Tile tile34=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),0);
        tile34.rotate();
        tile34.rotate();
        tile34.rotate();
        shipBoard3.positionTile(Optional.of(tile34), new Coordinates(2,2));
        Tile tile35=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),0);
        shipBoard3.positionTile(Optional.of(tile35), new Coordinates(3,3));
        shipBoard3.verifyCorrectness();

        flightBoard.addPlayerToGame(player1);
        flightBoard.addPlayerToGame(player2);
        flightBoard.addPlayerToGame(player3);
        flightBoard.addToFlightBoard(player1, 2);
        flightBoard.addToFlightBoard(player2, 3);
        flightBoard.addToFlightBoard(player3, 1);
    }

    @Test
    void successfully_initialize_card () {
        game.setDrawnCard(abandonedStation);
        game.getDrawnCard().initializeCard(game,viewMap);
    }

    @Test
    void successfully_execute_card_and_refused () {
        successfully_initialize_card();
        abandonedStation.choice(player1.getPlayerName(), false);
        assert(player1.getShipBoard().getAllGoods().isEmpty());
    }

    @Test
    void successfully_execute_card_and_error () {
        successfully_initialize_card();
        abandonedStation.choice(player1.getPlayerName(), true);
        abandonedStation.manageGoods(player1.getPlayerName(), 200, new ArrayList<>());
        assert(player1.getShipBoard().getAllGoods().isEmpty());
    }

    @Test
    void successfully_execute_card_and_accepted () {
        successfully_initialize_card();
        abandonedStation.choice(player1.getPlayerName(), true);
        assertTrue(player1.getShipBoard().getAllGoods().isEmpty());
    }

    @Test
    void successfully_execute_card_accepted_landed_putted () {
        successfully_initialize_card();
        successfully_execute_card_and_accepted();
        ArrayList<CargoHold> modifiedTiles = new ArrayList<>();
        CargoHold newTile0 = new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        newTile0.setCoordinates(new Coordinates(1,4));
        newTile0.addGood(new Goods(BLUE));
        modifiedTiles.add(newTile0);
        abandonedStation.manageGoods(player1.getPlayerName(), 1, modifiedTiles);
        assertEquals(1,player1.getShipBoard().getAllGoods().size());
    }

    @Test
    void successfully_execute_card_accepted_landed_overloaded () {
        successfully_execute_card_and_accepted();
        ArrayList<CargoHold> modifiedTiles = new ArrayList<>();
        CargoHold newTile0=new CargoRed(3, new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),0);
        newTile0.setCoordinates(new Coordinates(1,3));
        newTile0.addGood(new Goods(BLUE));
        newTile0.addGood(new Goods(RED));
        newTile0.addGood(new Goods(RED));
        //Shouldn't get added
        newTile0.addGood(new Goods(BLUE));
        modifiedTiles.add(newTile0);
        abandonedStation.manageGoods(player1.getPlayerName(), 9, modifiedTiles);
        assertEquals(3,player1.getShipBoard().getAllGoods().size());
        assertEquals(9, player1.getShipBoard().convertGoodsToCredit());
    }

    @Test
    void successfully_execute_card_accepted_landed_not_hazard_cargo () {
        successfully_execute_card_and_accepted();
        ArrayList<CargoHold> modifiedTiles = new ArrayList<>();
        CargoHold newTile0 = new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        newTile0.setCoordinates(new Coordinates(1,4));
        newTile0.addGood(new Goods(RED));
        abandonedStation.manageGoods(player1.getPlayerName(), 4, modifiedTiles);
        assertEquals(0,player1.getShipBoard().getAllGoods().size());
    }

    @Test
    void wrong_type_of_cargo_hazard_for_notHazard_cargo () {
        successfully_execute_card_and_accepted();
        successfully_execute_card_and_accepted();
        ArrayList<CargoHold> modifiedTiles = new ArrayList<>();
        CargoHold newTile0=new CargoRed(3, new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),0);
        newTile0.setCoordinates(new Coordinates(1,4));
        newTile0.addGood(new Goods(RED));
        abandonedStation.manageGoods(player1.getPlayerName(), 4, modifiedTiles);
        assertEquals(0,player1.getShipBoard().getAllGoods().size());
    }

    @Test
    void wrong_type_of_cargo_different_spaces () {
        successfully_execute_card_and_accepted();
        ArrayList<CargoHold> modifiedTiles = new ArrayList<>();
        CargoHold newTile0 = new CargoBlue(1, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),0);
        newTile0.setCoordinates(new Coordinates(1,4));
        newTile0.addGood(new Goods(BLUE));
        abandonedStation.manageGoods(player1.getPlayerName(), 1, modifiedTiles);
        assertEquals(0,player1.getShipBoard().getAllGoods().size());
    }

    void refused_by_everyone() {
        successfully_initialize_card();
        abandonedStation.choice(player1.getPlayerName(), false);
        abandonedStation.choice(player2.getPlayerName(), false);
        abandonedStation.choice(player3.getPlayerName(), false);
        assertEquals(GameState.DRAW_CARD, game.getGameState());
    }

    void wrong_player_chooses() {
        successfully_initialize_card();
        abandonedStation.choice(player1.getPlayerName(), false);
        abandonedStation.choice(player3.getPlayerName(), false);
        abandonedStation.choice(player2.getPlayerName(), false);
        assertEquals(GameState.CARD_EVENT, game.getGameState());
    }
}