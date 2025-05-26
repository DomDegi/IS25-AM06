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
import static it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor.YELLOW;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SmugglersTest {
    private Smugglers smugglers;
    private Game game;
    private Player player1;
    private Player player2;
    private Player player3;
    private FlightBoard flightBoard;
    private final VirtualView mockView1 = new MockVirtualView();
    private final VirtualView mockView2 = new MockVirtualView();
    private final VirtualView mockView3 = new MockVirtualView();
    Map<String,VirtualView> viewMap = new HashMap<>();

    @BeforeEach
    void setUp() {
        game = new Game(GameMode.LEVEL2, 3);
        flightBoard = game.getFlightBoard();
        player1 = new Player("MimmoPericoloso", PlayersColor.BLUE);
        player2 = new Player("FedeGalattico", PlayersColor.RED);
        player3 = new Player("EnnioVolante", PlayersColor.YELLOW);
        Goods good1=new Goods(BLUE);
        Goods good2=new Goods(RED);
        Goods good3=new Goods(GREEN);
        Goods good4=new Goods(YELLOW);
        ArrayList<Goods> rewardGoods =new ArrayList<>();

        viewMap.put("MimmoPericoloso", mockView1);
        viewMap.put("FedeGalattico", mockView2);
        viewMap.put("EnnioVolante", mockView3);

        rewardGoods.add(good2);
        rewardGoods.add(good3);
        rewardGoods.add(good4);
        rewardGoods.add(good1);
        rewardGoods.add(good1);

        smugglers = new Smugglers(1, 2, 2,2,rewardGoods);

        //shipboard 5 to player1
        ShipBoard shipBoard1 = new ShipBoard(player1);
        player1.setPlayerShip(shipBoard1);
        shipBoard1.initializeLevel2();
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
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
        shipBoard1.verifyCorrectness();

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
        shipBoard2.verifyCorrectness();

        //shipboard1 to player3
        ShipBoard shipBoard3 = new ShipBoard(player3);
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

        player1.setAllCrewToHuman();
        player2.setAllCrewToHuman();
        player3.setAllCrewToHuman();

        flightBoard.addPlayerToGame(player1);
        flightBoard.addPlayerToGame(player2);
        flightBoard.addPlayerToGame(player3);
        flightBoard.addToFlightBoard(player1, 2);
        flightBoard.addToFlightBoard(player2, 3);
        flightBoard.addToFlightBoard(player3, 1);
    }

    @Test
    void successfully_initialize_card () {
        game.setDrawnCard(smugglers);
        game.getDrawnCard().initializeCard(game, viewMap);
    }

    @Test
    void successfully_initialize_all_lost () {
        Goods good1=new Goods(BLUE);
        Goods good2=new Goods(RED);
        Goods good3=new Goods(GREEN);
        Goods good4=new Goods(YELLOW);
        ArrayList<Goods> rewardGoods =new ArrayList<>();

        rewardGoods.add(good2);
        rewardGoods.add(good3);
        rewardGoods.add(good4);
        rewardGoods.add(good1);
        rewardGoods.add(good1);
        smugglers = new Smugglers(1, 2, 10,2,rewardGoods);

        game.setDrawnCard(smugglers);
        game.getDrawnCard().initializeCard(game, viewMap);

        game.getDrawnCard().cannonChoice("EnnioVolante", 0, new ArrayList<>());

        int batteries=player1.getShipBoard().getNumBatteries();
        game.getDrawnCard().cannonChoice("MimmoPericoloso",  0, new ArrayList<>());
        ArrayList<Coordinates> toRemove =new ArrayList<>();
        toRemove.add(new Coordinates(3,0)); toRemove.add(new Coordinates(3,0));
        game.getDrawnCard().removeGoods("MimmoPericoloso",toRemove);

        assertEquals(batteries-2,player1.getShipBoard().getNumBatteries());

        batteries=player2.getShipBoard().getNumBatteries();
        game.getDrawnCard().cannonChoice("FedeGalattico", 0, new ArrayList<>());
        toRemove =new ArrayList<>();
        toRemove.add(new Coordinates(3,3));  toRemove.add(new Coordinates(3,3));
        game.getDrawnCard().removeGoods("FedeGalattico", toRemove);
        assertEquals(batteries-2,player2.getShipBoard().getNumBatteries());
    }

    @Test
    void successfully_initialize_player3_won_refused(){
        successfully_initialize_card();
        game.getDrawnCard().cannonChoice(player3.getPlayerName(), 0, new ArrayList<>());
        assertEquals(0,player3.getShipBoard().getAllGoods().size());
    }

    @Test
    void successfully_initialize_player3_won_accepted(){
        successfully_initialize_card();
        game.getDrawnCard().cannonChoice("EnnioVolante",0,new ArrayList<>());
        game.getDrawnCard().choice("EnnioVolante",true);
        assertEquals(0,player3.getShipBoard().getAllGoods().size());
    }

    @Test
    void successfully_initialize_player3_won_accepted_managed(){
        successfully_initialize_player3_won_accepted();
        CargoHold cargo1 = new CargoRed(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE));
        cargo1.setCoordinates(new Coordinates(3,3));
        cargo1.addGood(new Goods(RED));
        ArrayList<CargoHold> cargoHolds=new ArrayList<>(); cargoHolds.add(cargo1);
        game.getDrawnCard().manageGoods("EnnioVolante", 4, cargoHolds);
        assertEquals(0,player3.getShipBoard().getAllGoods().size());
    }

    @Test
    void successfully_initialize_player1_usedDC_won_withErrors(){
        flightBoard.moveBackward(player3,10);
        successfully_initialize_card();
        int batteries=player1.getShipBoard().getNumBatteries();
        ArrayList<Coordinates> toUse = new ArrayList<>();
        toUse.add(new Coordinates(1,4));
        game.getDrawnCard().cannonChoice("MimmoPericoloso", 2, toUse); //not a battery
        toUse = new ArrayList<>();
        toUse.add(new Coordinates(3,0));
        game.getDrawnCard().cannonChoice("MimmoPericoloso", 2, toUse);
        assertEquals(batteries-1,player1.getShipBoard().getNumBatteries());
    }

    @Test
    void successfully_initialize_player1_usedDC_won_multiple(){
        flightBoard.moveBackward(player3,10);
        successfully_initialize_card();
        int batteries=player1.getShipBoard().getNumBatteries();
        ArrayList<Coordinates> toUse = new ArrayList<>();
        toUse.add(new Coordinates(3,0)); toUse.add(new Coordinates(3,0));
        game.getDrawnCard().cannonChoice("MimmoPericoloso",4,toUse);
        game.getDrawnCard().choice("MimmoPericoloso",false);
        assertEquals(batteries-2,player1.getShipBoard().getNumBatteries());
    }

    @Test
    void successfully_initialize_player1_tie(){
        Goods good1=new Goods(BLUE);
        Goods good2=new Goods(RED);
        Goods good3=new Goods(GREEN);
        Goods good4=new Goods(YELLOW);
        ArrayList<Goods> rewardGoods =new ArrayList<>();
        int batteries=player1.getShipBoard().getNumBatteries();

        rewardGoods.add(good2);
        rewardGoods.add(good3);
        rewardGoods.add(good4);
        rewardGoods.add(good1);
        rewardGoods.add(good1);
        smugglers = new Smugglers(1, 2, 3,2,rewardGoods);
        flightBoard.moveBackward(player3,10);

        game.setDrawnCard(smugglers);
        game.getDrawnCard().initializeCard(game, viewMap);

        game.getDrawnCard().cannonChoice("MimmoPericoloso",0,new ArrayList<>());
        assertEquals(0,player2.getShipBoard().getAllGoods().size());
        assertEquals(batteries,player1.getShipBoard().getNumBatteries());
    }

    @Test
    void successfully_initialize_player1_usedDB_tie(){
        Goods good1=new Goods(BLUE);
        Goods good2=new Goods(RED);
        Goods good3=new Goods(GREEN);
        Goods good4=new Goods(YELLOW);
        ArrayList<Goods> rewardGoods =new ArrayList<>();
        int batteries=player1.getShipBoard().getNumBatteries();

        rewardGoods.add(good2);
        rewardGoods.add(good3);
        rewardGoods.add(good4);
        rewardGoods.add(good1);
        rewardGoods.add(good1);
        smugglers = new Smugglers(1, 2, 7,2,rewardGoods);
        flightBoard.moveBackward(player3,10);

        game.setDrawnCard(smugglers);
        game.getDrawnCard().initializeCard(game, viewMap);

        ArrayList<Coordinates> toUse = new ArrayList<>();
        toUse.add(new Coordinates(3,0)); toUse.add(new Coordinates(3,0));

        game.getDrawnCard().cannonChoice("MimmoPericoloso", 4,toUse);
        assertEquals(0,player2.getShipBoard().getAllGoods().size());
        assertEquals(batteries-2,player1.getShipBoard().getNumBatteries());
    }

    @Test
    void successfully_initialize_player1_usedDB_loose(){
        Goods good1=new Goods(BLUE);
        Goods good2=new Goods(RED);
        Goods good3=new Goods(GREEN);
        Goods good4=new Goods(YELLOW);
        ArrayList<Goods> rewardGoods =new ArrayList<>();
        int batteries=player1.getShipBoard().getNumBatteries();

        rewardGoods.add(good2);
        rewardGoods.add(good3);
        rewardGoods.add(good4);
        rewardGoods.add(good1);
        rewardGoods.add(good1);
        smugglers = new Smugglers(1, 2, 7,2,rewardGoods);
        flightBoard.moveBackward(player3,10);

        game.setDrawnCard(smugglers);
        game.getDrawnCard().initializeCard(game, viewMap);

        ArrayList<Coordinates> toUse = new ArrayList<>();
        toUse.add(new Coordinates(3,0));
        game.getDrawnCard().cannonChoice("MimmoPericoloso",2,toUse);
        assertEquals(0,player2.getShipBoard().getAllGoods().size());
        assertEquals(batteries-1,player1.getShipBoard().getNumBatteries());
    }

    @Test
    void successfully_initialize_disconnected_player_loses(){
        Goods good1=new Goods(BLUE);
        Goods good2=new Goods(RED);
        Goods good3=new Goods(GREEN);
        Goods good4=new Goods(YELLOW);
        ArrayList<Goods> rewardGoods =new ArrayList<>();
        player1.getShipBoard().gainGoods(good2,new Coordinates(1,3));
        player1.playerDisconnects();
        int batteries=player1.getShipBoard().getNumBatteries();
        int goods = player1.getShipBoard().getAllGoods().size();

        rewardGoods.add(good2);
        rewardGoods.add(good3);
        rewardGoods.add(good4);
        rewardGoods.add(good1);
        rewardGoods.add(good1);
        smugglers = new Smugglers(1, 2, 15,2,rewardGoods);
        flightBoard.moveBackward(player3,10);

        game.setDrawnCard(smugglers);
        game.getDrawnCard().initializeCard(game, viewMap);
        assertEquals(goods - 1,player1.getShipBoard().getAllGoods().size());
        assertEquals(batteries-1,player1.getShipBoard().getNumBatteries());
    }

    @Test
    void successfully_initialize_disconnected_player_loses_multiple_goods(){
        Goods good1=new Goods(BLUE);
        Goods good2=new Goods(RED);
        Goods good3=new Goods(GREEN);
        Goods good4=new Goods(YELLOW);
        ArrayList<Goods> rewardGoods =new ArrayList<>();
        player1.getShipBoard().gainGoods(good2,new Coordinates(1,3));
        player1.getShipBoard().gainGoods(good4,new Coordinates(1,3));
        player1.getShipBoard().gainGoods(good1,new Coordinates(1,4));
        player1.playerDisconnects();
        int batteries=player1.getShipBoard().getNumBatteries();
        int goods = player1.getShipBoard().getAllGoods().size();

        rewardGoods.add(good2);
        rewardGoods.add(good3);
        rewardGoods.add(good4);
        rewardGoods.add(good1);
        rewardGoods.add(good1);
        smugglers = new Smugglers(1, 2, 15,2,rewardGoods);
        flightBoard.moveBackward(player3,10);

        game.setDrawnCard(smugglers);
        game.getDrawnCard().initializeCard(game, viewMap);
        assertEquals(goods - 2,player1.getShipBoard().getAllGoods().size());
        assertEquals(1,player1.getShipBoard().convertGoodsToCredit());
    }

    @Test
    void player_disconnects_after_choosing_to_get_reward() {
        flightBoard.moveBackward(player3,10);
        successfully_initialize_card();
        int batteries=player1.getShipBoard().getNumBatteries();
        ArrayList<Coordinates> toUse = new ArrayList<>();
        toUse.add(new Coordinates(3,0)); toUse.add(new Coordinates(3,0));
        game.getDrawnCard().cannonChoice("MimmoPericoloso",4,toUse);
        game.getDrawnCard().choice("MimmoPericoloso",true);
        assertEquals(batteries-2,player1.getShipBoard().getNumBatteries());
        int player1_initialcargo = player1.getShipBoard().getAllGoods().size();
        player1.setDisconnected(true);
        game.getDrawnCard().playerDisconnected(player1.getPlayerName());
        assertEquals(player1_initialcargo + smugglers.getGoodsList(player1.getPlayerName()).size(),player1.getShipBoard().getAllGoods().size());
        assertEquals(GameState.DRAW_CARD,game.getGameState());
    }

    @Test
    void player_disconnects_before_choosing_to_get_reward() {
        flightBoard.moveBackward(player3,10);
        successfully_initialize_card();
        int batteries=player1.getShipBoard().getNumBatteries();
        ArrayList<Coordinates> toUse = new ArrayList<>();
        toUse.add(new Coordinates(3,0)); toUse.add(new Coordinates(3,0));
        game.getDrawnCard().cannonChoice("MimmoPericoloso",4,toUse);
        assertEquals(batteries-2,player1.getShipBoard().getNumBatteries());
        int player1_initialcargo = player1.getShipBoard().getAllGoods().size();
        player1.setDisconnected(true);
        game.getDrawnCard().playerDisconnected(player1.getPlayerName());
        assertEquals(player1_initialcargo,player1.getShipBoard().getAllGoods().size());
        assertEquals(GameState.DRAW_CARD,game.getGameState());
    }

    @Test
    void player_disconnects_after_losing() {
        Goods good1=new Goods(BLUE);
        Goods good2=new Goods(RED);
        Goods good3=new Goods(GREEN);
        Goods good4=new Goods(YELLOW);
        ArrayList<Goods> rewardGoods =new ArrayList<>();
        int batteries=player1.getShipBoard().getNumBatteries();
        rewardGoods.add(good2);
        rewardGoods.add(good3);
        rewardGoods.add(good4);
        rewardGoods.add(good1);
        rewardGoods.add(good1);
        player1.getShipBoard().gainGoods(good2,new Coordinates(1,3));
        player1.getShipBoard().gainGoods(good4,new Coordinates(1,3));
        player1.getShipBoard().gainGoods(good1,new Coordinates(1,4));
        int player1_initialcargo = player1.getShipBoard().getAllGoods().size();
        smugglers = new Smugglers(1, 2, 7,2,rewardGoods);
        flightBoard.moveBackward(player3,10);

        game.setDrawnCard(smugglers);
        game.getDrawnCard().initializeCard(game, viewMap);

        ArrayList<Coordinates> toUse = new ArrayList<>();
        toUse.add(new Coordinates(3,0));
        game.getDrawnCard().cannonChoice("MimmoPericoloso",2,toUse);
        assertEquals(0,player2.getShipBoard().getAllGoods().size());
        assertEquals(batteries-1,player1.getShipBoard().getNumBatteries());
        player1.setDisconnected(true);
        game.getDrawnCard().playerDisconnected(player1.getPlayerName());
        assertEquals(player1_initialcargo - smugglers.getGoodsPenalty(),player1.getShipBoard().getAllGoods().size());
    }

}