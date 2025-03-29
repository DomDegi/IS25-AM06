package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.player.PlayersColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player_nome_ripetuto;
    private Player player_colore_ripetuto;

    @BeforeEach
    void setUp() {
        game = new Game(GameMode.LEVEL2);
        player1 = new Player("Andrea", PlayersColor.RED);
        player2 = new Player("Giacomo", PlayersColor.BLUE);
        player3 = new Player("Silvio", PlayersColor.GREEN);
        player4 = new Player("Pietro", PlayersColor.YELLOW);
        player_nome_ripetuto = new Player("andrea", PlayersColor.BLUE);
        player_colore_ripetuto = new Player("zoff", PlayersColor.RED);
    }

    @Test
    public void test_4_player_get_added () {
        game.setPlayerCount(4);
        game.addPlayer("Andrea", PlayersColor.RED);
        game.addPlayer("Giacomo", PlayersColor.BLUE);
        game.addPlayer("Silvio", PlayersColor.GREEN);
        game.addPlayer("Pietro", PlayersColor.YELLOW);
        assertEquals(4, game.getFlightBoard().getAllPlayers().size());
    }

    @Test
    public void test_4_player_get_added_but_one_has_the_same_name () {
        game.setPlayerCount(4);
        game.addPlayer("Andrea", PlayersColor.RED);
        game.addPlayer("Giacomo", PlayersColor.BLUE);
        game.addPlayer("Silvio", PlayersColor.GREEN);
        game.addPlayer("Andrea", PlayersColor.YELLOW);
        assertEquals(3, game.getFlightBoard().getAllPlayers().size());
    }

    @Test
    public void test_4_player_get_added_but_one_has_the_same_color () {
        game.setPlayerCount(4);
        game.addPlayer("Andrea", PlayersColor.RED);
        game.addPlayer("Giacomo", PlayersColor.BLUE);
        game.addPlayer("Silvio", PlayersColor.GREEN);
        game.addPlayer("Pietro", PlayersColor.RED);
        assertEquals(3, game.getFlightBoard().getAllPlayers().size());
    }


}