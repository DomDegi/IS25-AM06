package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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
        game = new Game(GameMode.LEVEL2, 4);
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

    @Test
    void test_drawTile(){
        game.setPlayerCount(4);
        ShipBoard shipBoard1 = new ShipBoard(player1);
        player1.setPlayerShip(shipBoard1);
        game.addPlayer("Andrea", PlayersColor.RED);
        game.addPlayer("Giacomo", PlayersColor.BLUE);
        game.addPlayer("Silvio", PlayersColor.GREEN);
        game.addPlayer("Pietro", PlayersColor.YELLOW);
        Tile tile=game.drawTile("Andrea");
        game.getListOfAllPlayer().getFirst().hasDrawnTile(tile);
        game.playerBookTile("Andrea");
        assertEquals(tile,game.getListOfAllPlayer().getFirst().getShipBoard().getBookedTiles().getFirst());

    }

    @Test
    void getPlayerShipBoard() {
        game.setPlayerCount(4);
        game.addPlayer("Andrea", PlayersColor.RED);
        game.addPlayer("Giacomo", PlayersColor.BLUE);
        game.addPlayer("Silvio", PlayersColor.GREEN);
        game.addPlayer("Pietro", PlayersColor.YELLOW);

        // Recupero il vero player dentro al Game
        Player playerInGame = game.identifyPlayerByName("Andrea");
        ShipBoard shipBoard = playerInGame.getShipBoard();

        // Posiziono qualche tile a caso
        Tile tile1 = new SingleCannon(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH),
                new Link(Connectors.DOUBLE), new Link(Connectors.SMOOTH));
        shipBoard.positionTile(Optional.of(tile1), new Coordinates(0, 4));

        Tile tile2 = new EquipCabin(new Link(Connectors.SMOOTH), new Link(Connectors.DOUBLE),
                new Link(Connectors.UNIVERSAL), new Link(Connectors.SINGLE));
        shipBoard.positionTile(Optional.of(tile2), new Coordinates(1, 1));
        tile2.setCrewType(CrewType.HUMAN);

        shipBoard.verifyCorrectness();


        ShipBoard returned = game.getPlayerShipBoard("Andrea");
        assertEquals(shipBoard, returned);
    }



}