package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightBoardTest {
    private FlightBoard flightBoard;
    private final Player player1 = new Player("sam", PlayersColor.RED);
    private final Player player2 = new Player("bob", PlayersColor.BLUE);
    private final Player player3 = new Player("zig", PlayersColor.YELLOW);
    private final Player player4 = new Player("set", PlayersColor.GREEN);

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

    @Test
    void player1_set_to_last(){
        flightBoard.addToFlightBoard(player1, 1);
        flightBoard.addToFlightBoard(player2, 2);
        flightBoard.addToFlightBoard(player3, 3);
        flightBoard.addToFlightBoard(player4, 4);

        flightBoard.setPlayerToLast(player1);
        assertEquals(player1, flightBoard.getInGamePlayers().get(3));
    }

    @Test
    void player2_set_to_last(){
        flightBoard.addToFlightBoard(player1, 1);
        flightBoard.addToFlightBoard(player2, 2);
        flightBoard.addToFlightBoard(player3, 3);
        flightBoard.addToFlightBoard(player4, 4);

        flightBoard.setPlayerToLast(player1);
        flightBoard.setPlayerToLast(player2);
        assertEquals(player1, flightBoard.getInGamePlayers().get(2));
        assertEquals(player2, flightBoard.getInGamePlayers().get(3));
    }

    @Test
    void move_forward_successfully_and_add_jump_player2() {
        flightBoard.addToFlightBoard(player1, 1);
        flightBoard.addToFlightBoard(player2, 2);
        flightBoard.addToFlightBoard(player3, 3);
        flightBoard.addToFlightBoard(player4, 4);

        flightBoard.moveForward(player2,10);

        assertEquals(16, player2.getPlayerPosition());
    }

    @Test
    void move_backward_successfully_and_add_jump_player2() {
        flightBoard.addToFlightBoard(player1, 1);
        flightBoard.addToFlightBoard(player2, 2);
        flightBoard.addToFlightBoard(player3, 3);
        flightBoard.addToFlightBoard(player4, 4);

        flightBoard.moveBackward(player2,10);
        assertEquals(-7, player2.getPlayerPosition());
    }

    @Test
    void multiple_moving_players(){
        flightBoard.addToFlightBoard(player1, 1);
        flightBoard.addToFlightBoard(player2, 2);
        flightBoard.addToFlightBoard(player3, 3);
        flightBoard.addToFlightBoard(player4, 4);

        flightBoard.moveForward(player1,5);
        flightBoard.moveForward(player2,10);
        flightBoard.moveForward(player3,6);
        flightBoard.moveForward(player4,9);
        flightBoard.rearrange();

        assertEquals(14, player1.getPlayerPosition());
        assertEquals(16, player2.getPlayerPosition());
        assertEquals(8, player3.getPlayerPosition());
        assertEquals(10, player4.getPlayerPosition());

        assertEquals(1, player2.getPlayerRanking());
        assertEquals(2, player1.getPlayerRanking());
        assertEquals(3, player4.getPlayerRanking());
        assertEquals(4, player3.getPlayerRanking());

        flightBoard.moveBackward(player3,6);
        flightBoard.moveBackward(player4,1);
        flightBoard.moveBackward(player1,9);
        flightBoard.moveBackward(player2,10);
        flightBoard.rearrange();

        assertEquals(4, player1.getPlayerPosition());
        assertEquals(5, player2.getPlayerPosition());
        assertEquals(2, player3.getPlayerPosition());
        assertEquals(9, player4.getPlayerPosition());

        assertEquals(2, player2.getPlayerRanking());
        assertEquals(3, player1.getPlayerRanking());
        assertEquals(1, player4.getPlayerRanking());
        assertEquals(4, player3.getPlayerRanking());
    }

    @Test
    void successfully_early_landed(){
        flightBoard.addToFlightBoard(player1, 1);
        flightBoard.addToFlightBoard(player2, 2);
        flightBoard.addToFlightBoard(player3, 3);
        flightBoard.addToFlightBoard(player4, 4);

        flightBoard.earlyLanding(player1);
        flightBoard.earlyLanding(player3);
        flightBoard.earlyLanding(player4);
        flightBoard.earlyLanding(player2);

        assertEquals(player1, flightBoard.getAllPlayers().get(3));
        assertEquals(player2, flightBoard.getAllPlayers().get(0));
        assertEquals(player3, flightBoard.getAllPlayers().get(2));
        assertEquals(player4, flightBoard.getAllPlayers().get(1));

    }

    @Test
    void full_moving_players(){
        flightBoard.addToFlightBoard(player1, 1);
        flightBoard.addToFlightBoard(player2, 2);
        flightBoard.addToFlightBoard(player3, 3);
        flightBoard.addToFlightBoard(player4, 4);
        player1.getShipBoard().setHumanCrew(2);
        player2.getShipBoard().setHumanCrew(2);
        player3.getShipBoard().setHumanCrew(2);
        player4.getShipBoard().setHumanCrew(2);

        flightBoard.moveForward(player1,5);
        flightBoard.moveForward(player2,10);
        flightBoard.moveForward(player3,6);
        flightBoard.moveForward(player4,9);
        flightBoard.concludeMovement();

        assertEquals(14, player1.getPlayerPosition());
        assertEquals(16, player2.getPlayerPosition());
        assertEquals(8, player3.getPlayerPosition());
        assertEquals(10, player4.getPlayerPosition());

        assertEquals(1, player2.getPlayerRanking());
        assertEquals(2, player1.getPlayerRanking());
        assertEquals(3, player4.getPlayerRanking());
        assertEquals(4, player3.getPlayerRanking());

        flightBoard.moveBackward(player3,6);
        flightBoard.moveBackward(player4,1);
        flightBoard.moveBackward(player1,9);
        flightBoard.moveBackward(player2,40);
        flightBoard.concludeMovement();

        assertEquals(4, player1.getPlayerPosition());
        assertEquals(-27, player2.getPlayerPosition());
        assertEquals(2, player3.getPlayerPosition());
        assertEquals(9, player4.getPlayerPosition());

        assertEquals(4, player2.getPlayerRanking());
        assertEquals(2, player1.getPlayerRanking());
        assertEquals(1, player4.getPlayerRanking());
        assertEquals(3, player3.getPlayerRanking());

        assertEquals(player2, flightBoard.getAllPlayers().get(3));
        assertFalse(flightBoard.getInGamePlayers().contains(player2));
    }

    @Test
    void full_moving_players_with_early_landed_and_human(){
        flightBoard.addToFlightBoard(player1, 1);
        flightBoard.addToFlightBoard(player2, 2);
        flightBoard.addToFlightBoard(player3, 3);
        flightBoard.addToFlightBoard(player4, 4);
        player1.getShipBoard().setHumanCrew(2);
        player2.getShipBoard().setHumanCrew(2);
        player3.getShipBoard().setHumanCrew(2);
        player4.getShipBoard().setHumanCrew(0);

        flightBoard.moveForward(player1,5);
        flightBoard.moveForward(player2,10);
        flightBoard.moveForward(player3,6);
        flightBoard.moveForward(player4,9);
        flightBoard.concludeMovement();

        assertEquals(14, player1.getPlayerPosition());
        assertEquals(16, player2.getPlayerPosition());
        assertEquals(8, player3.getPlayerPosition());
        assertEquals(10, player4.getPlayerPosition());

        assertEquals(1, player2.getPlayerRanking());
        assertEquals(2, player1.getPlayerRanking());
        assertEquals(4, player4.getPlayerRanking());
        assertEquals(3, player3.getPlayerRanking());

        flightBoard.moveBackward(player3,6);
        flightBoard.moveBackward(player4,1);
        flightBoard.moveBackward(player1,9);
        flightBoard.moveBackward(player2,40);
        flightBoard.concludeMovement();

        assertEquals(5, player1.getPlayerPosition());
        assertEquals(-26, player2.getPlayerPosition());
        assertEquals(2, player3.getPlayerPosition());
        assertEquals(10, player4.getPlayerPosition());

        assertEquals(3, player2.getPlayerRanking());
        assertEquals(1, player1.getPlayerRanking());
        assertEquals(4, player4.getPlayerRanking());
        assertEquals(2, player3.getPlayerRanking());

        assertEquals(player2, flightBoard.getAllPlayers().get(2));
        assertFalse(flightBoard.getInGamePlayers().contains(player2));
        assertEquals(player4, flightBoard.getAllPlayers().get(3));
        assertFalse(flightBoard.getInGamePlayers().contains(player4));
    }
    @Test
    void full_moving_players_with_early_landed(){
        flightBoard.addToFlightBoard(player1, 1);
        flightBoard.addToFlightBoard(player2, 2);
        flightBoard.addToFlightBoard(player3, 3);
        flightBoard.addToFlightBoard(player4, 4);
        player1.getShipBoard().setHumanCrew(2);
        player2.getShipBoard().setHumanCrew(2);
        player3.getShipBoard().setHumanCrew(2);
        player4.getShipBoard().setHumanCrew(2);

        flightBoard.moveForward(player1,5);
        flightBoard.moveForward(player2,10);
        flightBoard.moveForward(player3,6);
        flightBoard.moveForward(player4,9);
        assertEquals(14, player1.getPlayerPosition());
        assertEquals(16, player2.getPlayerPosition());
        assertEquals(8, player3.getPlayerPosition());
        assertEquals(10, player4.getPlayerPosition());

        assertEquals(1, player2.getPlayerRanking());
        assertEquals(2, player1.getPlayerRanking());
        assertEquals(3, player4.getPlayerRanking());
        assertEquals(4, player3.getPlayerRanking());
        flightBoard.concludeMovement();

        assertEquals(14, player1.getPlayerPosition());
        assertEquals(16, player2.getPlayerPosition());
        assertEquals(8, player3.getPlayerPosition());
        assertEquals(10, player4.getPlayerPosition());

        assertEquals(1, player2.getPlayerRanking());
        assertEquals(2, player1.getPlayerRanking());
        assertEquals(3, player4.getPlayerRanking());
        assertEquals(4, player3.getPlayerRanking());

        flightBoard.moveBackward(player3,6);
        flightBoard.moveBackward(player4,1);
        flightBoard.moveBackward(player1,9);
        flightBoard.moveBackward(player2,40);
        assertEquals(4, player1.getPlayerPosition());
        assertEquals(-27, player2.getPlayerPosition());
        assertEquals(2, player3.getPlayerPosition());
        assertEquals(9, player4.getPlayerPosition());

        assertEquals(4, player2.getPlayerRanking());
        assertEquals(2, player1.getPlayerRanking());
        assertEquals(1, player4.getPlayerRanking());
        assertEquals(3, player3.getPlayerRanking());
        flightBoard.concludeMovement();

        assertEquals(4, player1.getPlayerPosition());
        assertEquals(-27, player2.getPlayerPosition());
        assertEquals(2, player3.getPlayerPosition());
        assertEquals(9, player4.getPlayerPosition());

        assertEquals(4, player2.getPlayerRanking());
        assertEquals(2, player1.getPlayerRanking());
        assertEquals(1, player4.getPlayerRanking());
        assertEquals(3, player3.getPlayerRanking());

        assertEquals(player2, flightBoard.getAllPlayers().get(3));
        assertFalse(flightBoard.getInGamePlayers().contains(player2));

    }
}