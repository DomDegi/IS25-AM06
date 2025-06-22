package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.MockVirtualView;
import it.polimi.ingsw.galaxytruckerproject.persistence.GameLoader;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.galaxytruckerproject.model.GameMode.LEVEL2;
import static it.polimi.ingsw.galaxytruckerproject.model.GameState.*;
import static org.junit.jupiter.api.Assertions.*;

class GameLoaderTest {

    String fileName = "f";
    GameController gameController;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    TUI shipPrinter = new TUI();
    MockVirtualView viewPluto = new MockVirtualView();
    MockVirtualView viewPippo = new MockVirtualView();
    MockVirtualView viewPaperino = new MockVirtualView();
    MockVirtualView viewTopolino = new MockVirtualView();


    @BeforeEach
    void setUp() {
        gameController = GameLoader.findSavedGame(fileName).get();

    }

    //This test works only if you run gameSaverTest not more than 2 hours ago
    @Test
    void load_Game() {
        String[] names = new String[]{"pippo","pluto","paperino","topolino"};
        List<String> playerNames = List.of(names);
        ArrayList<Player> players = gameController.getAllPlayers();
        for (Player player:  players) {
            assertTrue(playerNames.contains(player.getPlayerName()));
        }

        player1 = players.get(0);
        player2 = players.get(1);
        player3 = players.get(2);
        player4 = players.get(3);
        gameController.getActivePlayers().put("pluto", player2);
        gameController.getActivePlayers().put("pippo", player3);
        gameController.getActivePlayers().put("paperino", player4);
        gameController.getActivePlayers().put("topolino", player1);
        gameController.addToPlayersViewMap("pluto",viewPluto,false);
        gameController.addToPlayersViewMap("pippo",viewPippo,false);
        gameController.addToPlayersViewMap("paperino",viewPaperino,false);
        gameController.addToPlayersViewMap("topolino",viewTopolino,false);

        assertEquals("pluto R 7 2 25 L N 0 N N",player2.toStringData());
        assertEquals("topolino B 0 0 0 N N 0 N N",player1.toStringData());
        assertEquals("paperino Y 0 0 0 N N 0 C N",player4.toStringData());
        assertEquals("pippo G 0 0 0 N N 0 N N",player3.toStringData());

        assertEquals(0,gameController.getHourglassTurns());
        assertEquals("GameSaverTest",gameController.getGameName());

        assertEquals(LEVEL2,gameController.getGame().getMode());
        assertEquals(SHIPS_CREATION,gameController.getGameState());


        gameController.completed("pluto",viewPluto);
        gameController.setPosition("pluto",viewPluto,1);
        gameController.completed("pippo",viewPippo);
        gameController.setPosition("pippo",viewPippo,2);
        gameController.completed("paperino",viewPaperino);
        gameController.setPosition("paperino",viewPaperino,3);
        gameController.completed("topolino",viewTopolino);
        gameController.setPosition("topolino",viewTopolino,4);
    }

}