package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.player.PlayersColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightBoardTest {
    private FlightBoard flightBoard;
    private Player player1 = new Player("sam", PlayersColor.RED);
    private Player player2 = new Player("bob", PlayersColor.BLUE);
    private Player player3 = new Player("zig", PlayersColor.YELLOW);
    private Player player4 = new Player("set", PlayersColor.GREEN);

    @BeforeEach
    void setUp() {
        flightBoard = new FlightBoard(GameMode.LEVEL2);
        flightBoard.addPlayerToGame(player1);
        flightBoard.addPlayerToGame(player2);
        flightBoard.addPlayerToGame(player3);
        flightBoard.addPlayerToGame(player4);
    }

    @Test
    void player_added_successfully() {
        assertEquals(player1, flightBoard.getAllPlayers().get(0));
        assertEquals(player2, flightBoard.getAllPlayers().get(1));
        assertEquals(player3, flightBoard.getAllPlayers().get(2));
        assertEquals(player4, flightBoard.getAllPlayers().get(3));
    }

    @Test
    void podium_players_added_to_flightboard() {
        Player giocondo = new Player("Giocondo", PlayersColor.RED);
        flightBoard.addToFlightBoard(giocondo, 1); //player not present on podium shouldn't be added
        assertTrue(flightBoard.getInGamePlayers().isEmpty());

        flightBoard.addToFlightBoard(player1, 2);
        flightBoard.addToFlightBoard(player2, 1);
        flightBoard.addToFlightBoard(player3, 4);
        flightBoard.addToFlightBoard(player4, 3);
        assertEquals(player1, flightBoard.getInGamePlayers().get(1));
        assertEquals(player2, flightBoard.getInGamePlayers().get(0));
        assertEquals(player3, flightBoard.getInGamePlayers().get(3));
        assertEquals(player4, flightBoard.getInGamePlayers().get(2));
    }
}