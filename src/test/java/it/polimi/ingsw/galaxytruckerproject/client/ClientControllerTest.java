package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.*;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import it.polimi.ingsw.galaxytruckerproject.network.MockVirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Optional;

class ClientControllerTest {

    VirtualController mockVirtualController=new MockVirtualController();

    ClientController clientController=new ClientController();

    @BeforeEach
    void setUp() {
        clientController.setVirtualController(mockVirtualController);
        ArrayList<Card> deck1 = new ArrayList<>();
        deck1.add(new Slavers(2,3,3,3,3));
        ArrayList<Card> deck2 = new ArrayList<>();
        ArrayList<Goods> goods = new ArrayList<>();
        goods.add(new Goods(GoodsColor.RED));
        goods.add(new Goods(GoodsColor.GREEN));
        goods.add(new Goods(GoodsColor.BLUE));
        deck2.add(new Smugglers(2,3,3,3,goods));
        ArrayList<Card> deck3 = new ArrayList<>();
        deck3.add(new StarDust(2));
        clientController.getDeck().put(1,deck1);
        clientController.getDeck().put(2,deck2);
        clientController.getDeck().put(3,deck3);
    }

    @Test
    void CHOOSE_UI_test() throws RemoteException {
        String input;
        input=" ";
        assertFalse(clientController.input(input));
        input="fui";
        assertFalse(clientController.input(input));
        input="       ";
        assertFalse(clientController.input(input));
        input="tui";
        assertTrue(clientController.input(input));
        assertEquals(TUI.class, clientController.getView().getDisplayedView().getClass());
    }

    @Test
    void CHOOSE_CONNECTION_test() throws RemoteException {
        CHOOSE_UI_test();
        String input="rmi";
        assertTrue(clientController.input(input));
    }

    @Test
    void LOGIN_test() throws RemoteException {
        CHOOSE_CONNECTION_test();
        String input="pippi";
        assertTrue(clientController.input(input));
        input = "redo";
        assertTrue(clientController.input(input));
        input = "pippo";
        assertTrue(clientController.input(input));
        input = "done";
        assertTrue(clientController.input(input));
        String outputCheck ="pippo";
        assertEquals(outputCheck, clientController.getName());
    }



    @Test
    void LOBBY_create_test() throws RemoteException {
        LOGIN_test();
        clientController.setState(ClientState.LOBBY);
        String input="creategame pluto 4 level2mode";
        assertTrue(clientController.input(input));
        assertEquals("pluto",mockVirtualController.getResults().get(0));
        assertEquals(4, mockVirtualController.getResults().get(1));
        assertEquals(GameMode.LEVEL2, mockVirtualController.getResults().get(2));
        assertEquals("pippo", clientController.getName());

        clientController.setGameMode(GameMode.LEVEL2);
    }

    @Test
    void LOBBY_join_test() throws RemoteException {
        LOGIN_test();
        clientController.setState(ClientState.LOBBY);
        String input="joingame pluto";
        assertFalse(clientController.input(input));
    }

    @Test
    void COLOR_CHOICE_test() throws RemoteException {
        LOBBY_create_test();
        clientController.setState(ClientState.COLOR_CHOICE);
        String input="red";
        assertTrue(clientController.input(input));
      //  verify(mockVirtualController,times(1)).chooseColor(playersColorArgumentCaptor.capture());
       // assertEquals(PlayersColor.RED,playersColorArgumentCaptor.getValue());
    }

    @Test
    void START_SHIP_CREATION_test() throws RemoteException {
        COLOR_CHOICE_test();
        assertEquals(GamePhases.LOGIN, clientController.getPhase());
        clientController.setState(ClientState.START_SHIP_CREATION);
        assertEquals(GamePhases.SHIPBOARD, clientController.getPhase());
        String input="STaRt  ";
        assertTrue(clientController.input(input));
        assertEquals(1, clientController.getHourglassTurns());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_done() throws RemoteException {
        START_SHIP_CREATION_test();
        clientController.setState(ClientState.S_END_DRAW_TILE_CARD);
        String input="done";
        assertTrue(clientController.input(input));
        assertEquals(ClientState.WAIT, clientController.getState());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_card() throws RemoteException {
        START_SHIP_CREATION_test();
        clientController.setState(ClientState.S_END_DRAW_TILE_CARD);
        ArrayList<Integer> notAvailableDeck = new ArrayList<>();
        notAvailableDeck.add(2);
        clientController.decksNotAvailable(notAvailableDeck);
        String input="draw card 2";
        assertFalse(clientController.input(input));
        input="draw card 5";
        assertFalse(clientController.input(input));
        input="draw card 1";
        assertTrue(clientController.input(input));
//        verify(mockVirtualController,times(1)).lookCardsRequest(choseCaptor.capture());
//        assertEquals(1,choseCaptor.getValue());
        assertEquals(Slavers.class, clientController.getDisplayedCard().getFirst().getClass());
        assertEquals(1, clientController.getIndexDeckInHandOrPlanet());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_new() throws RemoteException {
        START_SHIP_CREATION_test();
        clientController.setState(ClientState.S_END_DRAW_TILE_CARD);
        String input="draw tile new";
        assertTrue(clientController.input(input));
        clientController.setTileInHand(new CargoRed(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        clientController.setState(ClientState.S_MANAGE_DRAWN_TILE);
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_b1_butEmpty() throws RemoteException {
        START_SHIP_CREATION_test();
        clientController.setState(ClientState.S_END_DRAW_TILE_CARD);
        String input="draw tile b1";
        assertFalse(clientController.input(input));
    }

    @Test
    void S_MANAGE_DRAWN_TILE_test() throws RemoteException {
        S_END_DRAW_TILE_CARD_test_draw_tile_new();
        assertEquals(Connectors.SMOOTH, clientController.getTileInHand().getEast().getConnectorsType());
        assertEquals(Connectors.UNIVERSAL, clientController.getTileInHand().getWest().getConnectorsType());
        String input="rotate";
        assertTrue(clientController.input(input));
        assertEquals(Connectors.SMOOTH, clientController.getTileInHand().getSouth().getConnectorsType());
        input="book";
        assertTrue(clientController.input(input));
        assertEquals(clientController.getMe().getShipBoard().getBookedTiles().getFirst(), clientController.getTileInHand());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_b1() throws RemoteException {
        S_MANAGE_DRAWN_TILE_test();
        clientController.setState(ClientState.S_END_DRAW_TILE_CARD);
        clientController.setTileInHand(new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        String input="draw tile b1";
        assertTrue(clientController.input(input));
        assertEquals(CargoRed.class, clientController.getTileInHand().getClass());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_b2_butB2Empty_soB1() throws RemoteException {
        S_MANAGE_DRAWN_TILE_test();
        clientController.setState(ClientState.S_END_DRAW_TILE_CARD);
        clientController.setTileInHand(new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        String input="draw tile b2";
        assertTrue(clientController.input(input));
        assertEquals(CargoRed.class, clientController.getTileInHand().getClass());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_b1_refused() throws RemoteException {
        S_END_DRAW_TILE_CARD_test_draw_tile_b1();
        String input="refuse";
        assertTrue(clientController.input(input));
        assertEquals(clientController.getMe().getShipBoard().getBookedTiles().getFirst(), clientController.getTileInHand());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_new_book2() throws RemoteException {
        S_END_DRAW_TILE_CARD_test_draw_tile_b1_refused();
        String input="draw tile new";
        assertTrue(clientController.input(input));
        clientController.setTileInHand(new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        clientController.setState(ClientState.S_MANAGE_DRAWN_TILE);
        input="book";
        assertTrue(clientController.input(input));
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_new_book3_butFull() throws RemoteException {
        S_END_DRAW_TILE_CARD_test_draw_new_book2();
        String input="draw tile new";
        assertFalse(clientController.input(input));
        clientController.setState(ClientState.S_END_DRAW_TILE_CARD);
        input="draw tile new";
        assertTrue(clientController.input(input));
        clientController.setTileInHand(new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        clientController.setState(ClientState.S_MANAGE_DRAWN_TILE);
        input="book";
        assertFalse(clientController.input(input));
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_b2() throws RemoteException {
        S_END_DRAW_TILE_CARD_test_draw_new_book3_butFull();
        clientController.setState(ClientState.S_END_DRAW_TILE_CARD);
        clientController.setTileInHand(new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        String input="draw tile b2";
        assertTrue(clientController.input(input));
        assertEquals(CargoBlue.class, clientController.getTileInHand().getClass());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_fromRefused() throws RemoteException {
        START_SHIP_CREATION_test();
        clientController.setState(ClientState.S_END_DRAW_TILE_CARD);
        String input="draw tile 4";
        assertFalse(clientController.input(input));
        clientController.getTurnedTiles().put(4,new CargoRed(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        clientController.setState(ClientState.S_END_DRAW_TILE_CARD);
        assertEquals(1, clientController.getTurnedTiles().size());
        input="draw tile 4";
        assertTrue(clientController.input(input));
//        verify(mockVirtualController,times(1)).reqDrawTileFromTurned(choseCaptor.capture());
//        assertEquals(4, choseCaptor.getValue());
    }

    @Test
    void S_FINISHED_test() throws RemoteException {
        START_SHIP_CREATION_test();
        clientController.setState(ClientState.S_END_DRAW_TILE_CARD);
        assertEquals(1, clientController.getHourglassTurns());
        String input="done";
        assertTrue(clientController.input(input));
        assertEquals(GamePhases.SHIPBOARD, clientController.getPhase());
        assertEquals(ClientState.WAIT, clientController.getState());
        clientController.setState(ClientState.S_FINISHED);
        input="turn";
        assertTrue(clientController.input(input));
        assertEquals(2, clientController.getHourglassTurns());
        input="turn";
        assertTrue(clientController.input(input));
        assertEquals(3, clientController.getHourglassTurns());
        /*
        clientController.setState(ClientState.S_FINISHED);
        input="3";
        assertTrue(clientController.input(input));
        verify(mockVirtualController,times(1)).notifySetPosition(notNull(),choseCaptor.capture());
        assertEquals(3,choseCaptor.getValue());
        assertEquals(ClientState.WAIT, clientController.getState());*/

    }

    @Test
    void MANAGE_CABINS_test_butNull() throws RemoteException {
        S_FINISHED_test();
        clientController.setState(ClientState.MANAGE_CABINS);
        String input="humans";
        assertFalse(clientController.input(input));
        clientController.setState(ClientState.MANAGE_CABINS);
    }



    @Test
    void MANAGE_CABINS_test() throws RemoteException {
        S_FINISHED_test();
        LightShipBoard shipBoard1 = new LightShipBoard(clientController.getMe());
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
        shipBoard1.setGetStat();
        clientController.setState(ClientState.MANAGE_CABINS);
        String input="humans";
        assertTrue(clientController.input(input));
        input="humans";
        assertTrue(clientController.input(input));
        input="brownAlien";
        assertTrue(clientController.input(input));
        input="Alien";
        assertFalse(clientController.input(input));
        input="brownAlien";
        assertTrue(clientController.input(input));
        input="humans";
        assertTrue(clientController.input(input));
        input="humans";
        assertTrue(clientController.input(input));
//        verify(mockVirtualController,times(1)).notifyNewCrewArrangement(cabinCaptor.capture());
//        assertEquals(CrewType.HUMAN,cabinCaptor.getValue().get(0).getCrewType());
//        assertEquals(CrewType.HUMAN,cabinCaptor.getValue().get(1).getCrewType());
//        assertEquals(CrewType.BROWN,cabinCaptor.getValue().get(2).getCrewType());
//        assertEquals(CrewType.HUMAN,cabinCaptor.getValue().get(3).getCrewType());
//        assertEquals(CrewType.HUMAN,cabinCaptor.getValue().get(4).getCrewType());
    }

    @Test
    void MANAGE_GOODS_test() throws RemoteException {
        S_FINISHED_test();
        LightShipBoard shipBoard1 = new LightShipBoard(clientController.getMe());
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
        shipBoard1.setGetStat();
        ArrayList<Goods> goods=new ArrayList<>();
        goods.add(new Goods(GoodsColor.RED));
        goods.add(new Goods(GoodsColor.BLUE));
        goods.add(new Goods(GoodsColor.GREEN));
        goods.add(new Goods(GoodsColor.YELLOW));
        clientController.getGoodsList().addAll(goods);
        clientController.setState(ClientState.MANAGE_GOODS);
        String input="1 1 3";
        assertTrue(clientController.input(input));
        input="done";
        assertTrue(clientController.input(input));
//        verify(mockVirtualController,times(1)).notifyNewGoodsArrangement(valueCaptor.capture(),newTilesCaptor.capture());
//        assertEquals(GoodsColor.RED,newTilesCaptor.getValue().getFirst().getCargo().getFirst().getColor());
//        assertEquals(4,valueCaptor.getValue());
    }

    @Test
    void MANAGE_GOODS_test_pick() throws RemoteException{
        S_FINISHED_test();
        LightShipBoard shipBoard1 = new LightShipBoard(clientController.getMe());
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
        shipBoard1.setGetStat();
        ArrayList<Goods> goods=new ArrayList<>();
        goods.add(new Goods(GoodsColor.RED));
        goods.add(new Goods(GoodsColor.BLUE));
        goods.add(new Goods(GoodsColor.GREEN));
        goods.add(new Goods(GoodsColor.YELLOW));
        clientController.getGoodsList().addAll(goods);
        clientController.setState(ClientState.MANAGE_GOODS);
        String input="1 1 3";
        assertTrue(clientController.input(input));
        input= "pick 1 3";
        assertTrue(clientController.input(input));
        input="1";
        assertTrue(clientController.input(input));
        input="done";
        assertTrue(clientController.input(input));
//        verify(mockVirtualController,times(1)).notifyNewGoodsArrangement(valueCaptor.capture(),newTilesCaptor.capture());
//        assertTrue(newTilesCaptor.getValue().getFirst().getCargo().isEmpty());
//        assertEquals(0,valueCaptor.getValue());
    }

    @Test
    void SHIP_PRINT_test() throws RemoteException {
        LightShipBoard shipBoard1 = new LightShipBoard(new LightPlayer("pippo", PlayersColor.RED));
        shipBoard1.initializeLevel2();
        Tile tile1=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH));
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
        shipBoard1.setGetStat();
        System.out.println(shipBoard1.toString());
        clientController.getView().printShipboard(shipBoard1);
    }
}