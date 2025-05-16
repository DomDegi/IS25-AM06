package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.galaxytruckerproject.model.GameMode.LEVEL2;
import static it.polimi.ingsw.galaxytruckerproject.model.GameState.*;
import static org.junit.jupiter.api.Assertions.*;

class GameLoaderTest {

    String fileName = "c";
    GameController gameController;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    TUI shipPrinter = new TUI();

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

        assertEquals("pluto R 7 2 25 L N 0 N",player2.toStringData());
        assertEquals("topolino B 0 0 0 N N 0 N",player1.toStringData());
        assertEquals("paperino Y 0 0 0 N N 0 N",player4.toStringData());
        assertEquals("pippo G 0 0 0 N N 0 N",player3.toStringData());

        assertEquals(0,gameController.getHourglassTurns());
        assertEquals("c",gameController.getGameName());

        assertEquals(LEVEL2,gameController.getGame().getMode());
        assertEquals(SHIPS_CREATION,gameController.getGameState());

        for (Player player:  players) {
            shipPrinter.printShipboard(new LightShipBoard(player.getShipBoard()));
            System.out.println("\n\n\n\n\n");
        }
    }

}