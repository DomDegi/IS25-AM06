package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.MockVirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class GameControllerTest {
    GameController gameController;
    Game game;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    MockVirtualView view1 = new MockVirtualView();
    MockVirtualView view2 = new MockVirtualView();
    MockVirtualView view3 = new MockVirtualView();
    MockVirtualView view4 = new MockVirtualView();

    @BeforeEach
    void setUp() {
        game = new Game(GameMode.LEVEL2, 4);
        this.gameController = new GameController(game,"test");
        player1 = new Player("p1", RED);
        player2 = new Player("p2", YELLOW);
        player3 = new Player("p3", GREEN);
        player4 = new Player("p4", BLUE);
    }

    @Test
    void player_addition_test() {
        gameController.addToPlayersViewMap(player1.getPlayerName(), view1, false);
        assertTrue(gameController.checkColorAvailable(player1.getPlayerName(),view1,RED));
        gameController.playerAddition("p1", RED);
        assertEquals(1, gameController.getPlayers().size());
        gameController.addToPlayersViewMap(player2.getPlayerName(), view2, false);
        assertTrue(gameController.checkColorAvailable(player2.getPlayerName(),view2,YELLOW));
        gameController.playerAddition("p2", YELLOW);
        assertEquals(2, gameController.getPlayers().size());
        gameController.addToPlayersViewMap(player3.getPlayerName(), view3, false);
        assertTrue(gameController.checkColorAvailable(player3.getPlayerName(),view3,GREEN));
        gameController.playerAddition("p3", GREEN);
        assertEquals(3, gameController.getPlayers().size());
        gameController.addToPlayersViewMap(player4.getPlayerName(), view4, false);
        assertTrue(gameController.checkColorAvailable(player4.getPlayerName(),view4,BLUE));
        gameController.playerAddition("p4", BLUE);
        assertEquals(4, gameController.getPlayers().size());
    }
}