package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.*;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.Server.VirtualControllerRMI;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {
    MultiGameController multiGameController = new MultiGameController();
    VirtualController virtualController=new VirtualControllerRMI(multiGameController);
    ClientControllerTest() throws RemoteException {
    }

    @Mock
    VirtualController mockVirtualController;

    @InjectMocks
    ClientController controller;

    @BeforeEach
    void setUp() {
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
        controller.getDeck().put(1,deck1);
        controller.getDeck().put(2,deck2);
        controller.getDeck().put(3,deck3);
    }

    @Test
    void CHOOSE_UI_test() throws RemoteException {
        String input;
        input=" ";
        assertFalse(controller.input(input));
        input="fui";
        assertFalse(controller.input(input));
        input="       ";
        assertFalse(controller.input(input));
        input="tui";
        assertTrue(controller.input(input));
        assertEquals(TUI.class, controller.getView().getDisplayedView().getClass());
    }

    @Test
    void CHOOSE_CONNECTION_test() throws RemoteException {
        CHOOSE_UI_test();
        String input="rmi";
        assertTrue(controller.input(input));
    }

    @Test
    void LOGIN_test() throws RemoteException {
        CHOOSE_CONNECTION_test();
        String input="pippi";
        assertTrue(controller.input(input));
        input = "redo";
        assertTrue(controller.input(input));
        input = "pippo";
        assertTrue(controller.input(input));
        input = "done";
        assertTrue(controller.input(input));
        String outputCheck ="pippo";
        assertEquals(outputCheck,controller.getName());
    }

    @Captor
    ArgumentCaptor<GameMode> gameModeArgumentCaptor;

    @Captor
    ArgumentCaptor<String> gameNameArgumentCaptor;

    @Captor
    ArgumentCaptor<Integer> numberOfPlayersArgumentCaptor;

    @Test
    void LOBBY_create_test() throws RemoteException {
        LOGIN_test();
        controller.getView().setClientState(ClientState.LOBBY);
        String input="creategame pluto 4 level2mode";
        assertTrue(controller.input(input));
        verify(mockVirtualController,times(1)).createGame(gameNameArgumentCaptor.capture(),numberOfPlayersArgumentCaptor.capture(),gameModeArgumentCaptor.capture(),isNotNull());
        assertEquals(GameMode.LEVEL2, gameModeArgumentCaptor.getValue());
        assertEquals(4, numberOfPlayersArgumentCaptor.getValue());
        assertEquals("pluto", gameNameArgumentCaptor.getValue());
        assertEquals("pippo",controller.getName());

        controller.setGameMode(GameMode.LEVEL2);
    }

    @Test
    void LOBBY_join_test() throws RemoteException {
        LOGIN_test();
        controller.getView().setClientState(ClientState.LOBBY);
        String input="joingame pluto";
        assertTrue(controller.input(input));
        verify(mockVirtualController,times(1)).joinGame(gameNameArgumentCaptor.capture(),isNotNull());
        assertEquals("pluto", gameNameArgumentCaptor.getValue());
        assertEquals("pippo",controller.getName());
    }

    @Captor
    ArgumentCaptor<PlayersColor> playersColorArgumentCaptor;

    @Test
    void COLOR_CHOICE_test() throws RemoteException {
        LOBBY_create_test();
        controller.getView().setClientState(ClientState.COLOR_CHOICE);
        String input="red";
        assertTrue(controller.input(input));
        verify(mockVirtualController,times(1)).chooseColor(isNotNull(),playersColorArgumentCaptor.capture());
        assertEquals(PlayersColor.RED,playersColorArgumentCaptor.getValue());
    }

    @Test
    void START_SHIP_CREATION_test() throws RemoteException {
        COLOR_CHOICE_test();
        assertEquals(GamePhases.LOGIN,controller.getPhase());
        controller.getView().setClientState(ClientState.START_SHIP_CREATION);
        assertEquals(GamePhases.SHIPBOARD,controller.getPhase());
        String input="STaRt  ";
        assertTrue(controller.input(input));
        assertEquals(1,controller.getHourglassTurns());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_done() throws RemoteException {
        START_SHIP_CREATION_test();
        controller.getView().setClientState(ClientState.S_END_DRAW_TILE_CARD);
        String input="done";
        assertTrue(controller.input(input));
        assertEquals(ClientState.S_FINISHED,controller.getState());
    }

    @Captor
    ArgumentCaptor<Integer> choseCaptor;

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_card() throws RemoteException {
        START_SHIP_CREATION_test();
        controller.getView().setClientState(ClientState.S_END_DRAW_TILE_CARD);
        ArrayList<Integer> notAvailableDeck = new ArrayList<>();
        notAvailableDeck.add(2);
        controller.decksNotAvailable(notAvailableDeck);
        String input="draw card 2";
        assertFalse(controller.input(input));
        input="draw card 5";
        assertFalse(controller.input(input));
        input="draw card 1";
        assertTrue(controller.input(input));
        verify(mockVirtualController,times(1)).lookCardsRequest(notNull(),choseCaptor.capture());
        assertEquals(1,choseCaptor.getValue());
        assertEquals(Slavers.class,controller.getDisplayedCard().getClass());
        assertEquals(1,controller.getIndexDeckInHandOrPlanet());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_new() throws RemoteException {
        START_SHIP_CREATION_test();
        controller.getView().setClientState(ClientState.S_END_DRAW_TILE_CARD);
        String input="draw tile new";
        assertTrue(controller.input(input));
        controller.setTileInHand(new CargoRed(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        controller.getView().setClientState(ClientState.S_MANAGE_DRAWN_TILE);
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_b1_butEmpty() throws RemoteException {
        START_SHIP_CREATION_test();
        controller.getView().setClientState(ClientState.S_END_DRAW_TILE_CARD);
        String input="draw tile b1";
        assertFalse(controller.input(input));
    }

    @Test
    void S_MANAGE_DRAWN_TILE_test() throws RemoteException {
        S_END_DRAW_TILE_CARD_test_draw_tile_new();
        assertEquals(Connectors.SMOOTH, controller.getTileInHand().getEast().getConnectorsType());
        assertEquals(Connectors.UNIVERSAL, controller.getTileInHand().getWest().getConnectorsType());
        String input="rotate";
        assertTrue(controller.input(input));
        assertEquals(Connectors.SMOOTH, controller.getTileInHand().getSouth().getConnectorsType());
        input="book";
        assertTrue(controller.input(input));
        assertEquals(controller.getMe().getShipBoard().getBookedTiles().getFirst(),controller.getTileInHand());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_b1() throws RemoteException {
        S_MANAGE_DRAWN_TILE_test();
        controller.getView().setClientState(ClientState.S_END_DRAW_TILE_CARD);
        controller.setTileInHand(new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        String input="draw tile b1";
        assertTrue(controller.input(input));
        assertEquals(CargoRed.class, controller.getTileInHand().getClass());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_b2_butB2Empty_soB1() throws RemoteException {
        S_MANAGE_DRAWN_TILE_test();
        controller.getView().setClientState(ClientState.S_END_DRAW_TILE_CARD);
        controller.setTileInHand(new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        String input="draw tile b2";
        assertTrue(controller.input(input));
        assertEquals(CargoRed.class, controller.getTileInHand().getClass());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_b1_refused() throws RemoteException {
        S_END_DRAW_TILE_CARD_test_draw_tile_b1();
        String input="refuse";
        assertTrue(controller.input(input));
        assertEquals(controller.getMe().getShipBoard().getBookedTiles().getFirst(),controller.getTileInHand());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_new_book2() throws RemoteException {
        S_END_DRAW_TILE_CARD_test_draw_tile_b1_refused();
        String input="draw tile new";
        assertTrue(controller.input(input));
        controller.setTileInHand(new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        controller.getView().setClientState(ClientState.S_MANAGE_DRAWN_TILE);
        input="book";
        assertTrue(controller.input(input));
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_new_book3_butFull() throws RemoteException {
        S_END_DRAW_TILE_CARD_test_draw_new_book2();
        String input="draw tile new";
        assertFalse(controller.input(input));
        controller.getView().setClientState(ClientState.S_END_DRAW_TILE_CARD);
        input="draw tile new";
        assertTrue(controller.input(input));
        controller.setTileInHand(new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        controller.getView().setClientState(ClientState.S_MANAGE_DRAWN_TILE);
        input="book";
        assertFalse(controller.input(input));
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_b2() throws RemoteException {
        S_END_DRAW_TILE_CARD_test_draw_new_book3_butFull();
        controller.getView().setClientState(ClientState.S_END_DRAW_TILE_CARD);
        controller.setTileInHand(new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        String input="draw tile b2";
        assertTrue(controller.input(input));
        assertEquals(CargoBlue.class, controller.getTileInHand().getClass());
    }

    @Test
    void S_END_DRAW_TILE_CARD_test_draw_tile_fromRefused() throws RemoteException {
        START_SHIP_CREATION_test();
        controller.getView().setClientState(ClientState.S_END_DRAW_TILE_CARD);
        String input="draw tile 4";
        assertFalse(controller.input(input));
        controller.getTurnedTiles().put(4,new CargoRed(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL)));
        controller.getView().setClientState(ClientState.S_END_DRAW_TILE_CARD);
        assertEquals(1, controller.getTurnedTiles().size());
        input="draw tile 4";
        assertTrue(controller.input(input));
        verify(mockVirtualController,times(1)).reqDrawTileFromTurned(notNull(),choseCaptor.capture());
        assertEquals(4, choseCaptor.getValue());
    }

    @Test
    void S_FINISHED_test() throws RemoteException {
        START_SHIP_CREATION_test();
        controller.getView().setClientState(ClientState.S_END_DRAW_TILE_CARD);
        assertEquals(1,controller.getHourglassTurns());
        String input="done";
        assertTrue(controller.input(input));
        assertEquals(ClientState.S_FINISHED, controller.getState());
        input="turn";
        assertTrue(controller.input(input));
        assertEquals(2,controller.getHourglassTurns());
        input="turn";
        assertTrue(controller.input(input));
        assertEquals(3,controller.getHourglassTurns());
        //post collegamento a server
        /*
        input="3";
        assertTrue(controller.input(input));
        verify(mockVirtualController,times(1)).notifySetPosition(notNull(),choseCaptor.capture());
        assertEquals(3,choseCaptor.getValue());
        assertEquals(ClientState.WAIT_OTHER_PLAYER_ACTION, controller.getState());
        */
    }

    @Test
    void MANAGE_CABINS_test() throws RemoteException {
        S_FINISHED_test();
        controller.getView().setClientState(ClientState.MANAGE_CABINS);
        String input="humans";
        assertFalse(controller.input(input));
        controller.getView().setClientState(ClientState.MANAGE_CABINS);
    }

    @Captor
    ArgumentCaptor<ArrayList<CargoHold>> newTilesCaptor;

    @Captor
    ArgumentCaptor<Integer> valueCaptor;

    @Test
    void MANAGE_GOODS_test() throws RemoteException {
        LightShipBoard shipBoard1 = new LightShipBoard(controller.getMe());
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
        S_FINISHED_test();
        ArrayList<Goods> goods=new ArrayList<>();
        goods.add(new Goods(GoodsColor.RED));
        goods.add(new Goods(GoodsColor.BLUE));
        goods.add(new Goods(GoodsColor.GREEN));
        goods.add(new Goods(GoodsColor.YELLOW));
        controller.getGoodsList().addAll(goods);
        controller.getView().setClientState(ClientState.MANAGE_GOODS);
        String input="1 1 3";
        assertTrue(controller.input(input));
        input="done";
        assertTrue(controller.input(input));
        verify(mockVirtualController,times(1)).notifyNewGoodsArrangement(notNull(),valueCaptor.capture(),newTilesCaptor.capture());

    }
}