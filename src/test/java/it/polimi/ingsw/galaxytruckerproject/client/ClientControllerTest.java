package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.*;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoBlue;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoRed;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Connectors;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Link;
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
}